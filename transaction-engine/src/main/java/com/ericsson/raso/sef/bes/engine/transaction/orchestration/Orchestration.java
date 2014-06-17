package com.ericsson.raso.sef.bes.engine.transaction.orchestration;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.bes.engine.transaction.CloneHelper;
import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.Phase;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.AbstractResponse;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.raso.sef.bes.prodcat.tasks.Charging;
import com.ericsson.raso.sef.bes.prodcat.tasks.ChargingMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.Fulfillment;
import com.ericsson.raso.sef.bes.prodcat.tasks.FulfillmentMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.Future;
import com.ericsson.raso.sef.bes.prodcat.tasks.FutureMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.Notification;
import com.ericsson.raso.sef.bes.prodcat.tasks.NotificationMode;
import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.UniqueIdGenerator;

public class Orchestration implements Serializable, Callable<AbstractResponse> {
	private static final long		serialVersionUID		= -8295152874923894285L;

	private static final Logger logger = LoggerFactory.getLogger(Orchestration.class);
	
	private String							northBoundCorrelator	= null;
	private List<ChargingStep>				charges					= null;
	private List			prepareFulfillment		= null;
	private List			fulfillment				= null;
	private List			reverseFulfillment		= null;
	private List<NotificationStep>			notification			= null;
	private List<SchedulingStep>			schedules				= null;
	private List<PersistenceStep>			persistence				= null;

	private Status							status					= Status.WAITING;
	private Mode							mode					= Mode.FORWARD;
	private Map<Phase, Status>				phasingProgress			= null;
	private TransactionException			executionFault			= null;
	private Map<String, String>	metas = null;

	private Semaphore criticalPath = new Semaphore(1);

	
	

	private static final Map<String, Orchestration> orchestrationTaskMapper = SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.ORCHESTRATION_TASK_MAPPER.name());
	
	private static final Map<String, AbstractStepResult> sbRequestResultMapper = SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.TRANSACTION_STEP_STATUS.name());
	
	private static final Map<String, Step> sbRequestStepMapper = SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.TRANSACTION_STEPS.name());
	
	private static final Map<String, Status> sbExecutionStatus = SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.TRANSACTION_STATUS.name());
	

	
	private Orchestration() {
		this.initPhasingProgress();
	}

	public Orchestration(String requestId, List<TransactionTask> tasks) throws TransactionException {
		this.northBoundCorrelator = requestId;
		
		this.charges = this.extractChargingSteps(tasks);
		this.fulfillment = this.extractFulfillmentSteps(tasks);
		this.notification = this.extractNotificationSteps(tasks);
		this.schedules = this.extractSchedulingSteps(tasks);
		this.persistence = this.extractPersistenceSteps(tasks);
		
		this.initPhasingProgress();
	}
	
	public Orchestration getRollbackProfile() {
		Orchestration rollback = new Orchestration();
		rollback.mode = Mode.ROLLBACK;
		
		
		//TODO: implement the logic to reverse the orchestration
		// --- revert the polarity of charges
		rollback.charges = new LinkedList<ChargingStep>();
		for (ChargingStep step: this.charges) {
			String southBoundRequestId = UniqueIdGenerator.generateId();
			ChargingStep rollbackStep = CloneHelper.deepClone(step);
			((Charging)rollbackStep.getExecutionInputs()).setMode(ChargingMode.REVERSE);
			rollback.charges.add(rollbackStep);
		}
		
		// --- set the processed fulfillment alone and reset polarity to reverse
		rollback.fulfillment = this.prepareRollbackFulfillment();
		
		// -- set the schedules to cancel
		rollback.schedules = new LinkedList<SchedulingStep>();
		for (SchedulingStep step: this.schedules) {
			String southBoundRequestId = UniqueIdGenerator.generateId();
			SchedulingStep rollbackStep = CloneHelper.deepClone(step);
			((Future)rollbackStep.getExecutionInputs()).setMode(FutureMode.CANCEL);
			rollback.schedules.add(rollbackStep);
		}
		
		// --- need to send a notification for rollback as well
		rollback.notification = new LinkedList<NotificationStep>();
		for (NotificationStep step: this.notification) {
			String southBoundRequestId = UniqueIdGenerator.generateId();
			NotificationStep rollbackStep = CloneHelper.deepClone(step);
			((Notification)rollbackStep.getExecutionInputs()).setMode(NotificationMode.NOTIFY_USER);
			rollback.notification.add(rollbackStep);
		}
		
		
		
		return rollback;
	}
	

	@Override
	public AbstractResponse call() throws Exception {
		logger.info("Orchestration execution Status: " + this.status + " Phasing progress: " + printPhasingProgress());
		try {
		switch (this.status) {
			case WAITING:
				logger.debug("Entering waiting phase");
				try {
					this.initiateExecution();
				} catch (Exception e) {
					logger.error("Exception while preparation, Exception:" + e);
					this.status = Status.DONE_FAULT;
					this.executionFault = new TransactionException(northBoundCorrelator, "CHARGING FAIED", e);
				}
				break;
			case PROCESSING:
				
				if (this.phasingProgress.get(Phase.TX_PHASE_PREP_FULFILLMENT) == Status.PROCESSING) {
					logger.debug("Verifying " + Phase.TX_PHASE_PREP_FULFILLMENT.name());
					if (this.isPhaseComplete(Phase.TX_PHASE_PREP_FULFILLMENT) && this.phasingProgress.get(Phase.TX_PHASE_PREP_FULFILLMENT) == Status.DONE_SUCCESS) { 
						this.promote2Fulfill();
					}
					else {
						this.status = Status.DONE_FAULT;
						this.executionFault = new TransactionException(northBoundCorrelator, "FULFILLMENT PREPARATION FAILED");
					}
				} else  if (this.phasingProgress.get(Phase.TX_PHASE_FULFILLMENT) == Status.PROCESSING) {
					logger.debug("Entering " + Phase.TX_PHASE_FULFILLMENT.name());
					if (this.isPhaseComplete(Phase.TX_PHASE_FULFILLMENT) && this.phasingProgress.get(Phase.TX_PHASE_FULFILLMENT) == Status.DONE_SUCCESS) {
//						this.processNotification();
//						this.promote2Schedule();
						//TODO: remove this when uncomment the above two tasks
						this.promote2Persist();
						this.status = Status.DONE_SUCCESS; 

					} else {
						if (this.phasingProgress.get(Phase.TX_PHASE_FULFILLMENT) == Status.DONE_FAULT || this.phasingProgress.get(Phase.TX_PHASE_FULFILLMENT) == Status.DONE_FAILED) {
							this.status = Status.DONE_FAULT;
							this.executionFault = new TransactionException(northBoundCorrelator, "FULFILLMENT FAILED");
						}
						
						if (this.phasingProgress.get(Phase.TX_PHASE_FULFILLMENT) == Status.PROCESSING) {
							this.promote2Fulfill();
						}
					}
				} 
				logger.debug("Seems to be out fulfillment...are we going to schedule?\n State: " + this.printPhasingProgress());
//				// proceed to scheduling...
//				if (this.phasingProgress.get(Phase.TX_PHASE_SCHEDULE) == Status.PROCESSING) {
//					if (this.isPhaseComplete(Phase.TX_PHASE_SCHEDULE) && this.phasingProgress.get(Phase.TX_PHASE_SCHEDULE) == Status.DONE_SUCCESS) {
//						logger.debug("Schedule tasks are completed. Promoting to persistence tasks");
//						this.promote2Persist();
//					}	else {
//						this.status = Status.DONE_FAULT;
//						this.executionFault = new TransactionException(northBoundCorrelator, "FUTURE EVENT SCHEDULING FAILED");
//					}
//				} 
//				
				logger.debug("Seems to be crossing schedule.... are we going to persistence?\n State: " + this.printPhasingProgress());
				// proceed to persistence...
				if (this.phasingProgress.get(Phase.TX_PHASE_PERSISTENCE) == Status.PROCESSING) {
					logger.debug("Yes.. we will do persistence...");
					if (this.isPhaseComplete(Phase.TX_PHASE_PERSISTENCE) && this.phasingProgress.get(Phase.TX_PHASE_PERSISTENCE) == Status.DONE_SUCCESS) {
						this.status = Status.DONE_SUCCESS;
						logger.debug("Persistence tasks are completed. Use case respnose processing will start now");
					}	else {
						this.status = Status.DONE_FAULT;
						this.executionFault = new TransactionException(northBoundCorrelator, "PERSISTENCE OF THIS TRANSACTION FAILED");
					}
				} 
				break;
			case DONE_FAILED:
				//nothing to do here... most promotion methods would have preempted this piece of code and hence need not be triggered by external events...
				break;
			case DONE_FAULT:
				//nothing to do here... most promotion methods would have preempted this piece of code and hence need not be triggered by external events...
				break;
			case DONE_SUCCESS:
				//nothing to do here... most promotion methods would have preempted this piece of code and hence need not be triggered by external events...
				break;
		}
		
		if (this.status.name().startsWith("DONE_")) {
			logger.debug("Orchestration completed with status: " + this.status.name());
			//this.processNotification();
			OrchestrationManager.getInstance().sendResponse(northBoundCorrelator, this);
		}
		
		return null;
		} catch(Exception e) {
			logger.error("Exception in orchestration. Exception Message: " + e.getMessage() + "Exception: " + e, e);
			this.status = Status.DONE_FAULT;
			this.executionFault = new TransactionException(northBoundCorrelator, "Fault occured during orchestration");
			OrchestrationManager.getInstance().sendResponse(northBoundCorrelator, this);
			throw e;
		}
	}

	
	private void promote2Fulfill() {
		logger.debug("Entering promote2Fulfill()....");
		
		boolean isAllStepsCompleted = true;
		boolean isSerialModeNow = false;
		boolean anyFailure = false;
		boolean anyFault = false;
		
		this.status = Status.PROCESSING;
		this.phasingProgress.put(Phase.TX_PHASE_FULFILLMENT, Status.PROCESSING);
		
		logger.debug("Fulfilment steps size: " + this.fulfillment.size());
		logger.debug("Visualize Fulfillment Steps (temp) in profile: " + this.fulfillment);
		
		for (Object next: this.fulfillment) {
			logger.debug("Quick Type Check: fulfillment.next() ==> " + next.getClass().getCanonicalName());
			
			isSerialModeNow = (this.fulfillment instanceof SequentialExecution);
			logger.debug("Execution Mode Serial? " + isSerialModeNow);
			
			Step step = null;
			if (next instanceof FulfillmentStep) {
				step = (FulfillmentStep) next;
				Status executionStatus = this.sbExecutionStatus.get(step.stepCorrelator);
				logger.debug("Execution Status <" + step.stepCorrelator + ", " + executionStatus + ">");

				if (executionStatus == null || executionStatus == Status.WAITING) {
					logger.debug("Attemptin to safely submit to execution for: " + step.stepCorrelator);

					// start of critical path
					try {
						this.criticalPath.acquire();
					} catch (InterruptedException e) {
						logger.error("Interrupted while attempting a critical path. Consistency guarantee compromised. Halting the Orchestration!!");
						this.status = Status.DONE_FAULT;
						break;
					}
					logger.debug("promote2Fulfill(): found the fulfillment step is yet to be submitted!! will submit them now");
					isAllStepsCompleted = false;
					// this means the task is not yet submitted...
					// can submit all subsequent tasks until hitting a serial
					// mode...
					logger.debug("Submitting next task: " + step.stepCorrelator + ": " + step);

					this.orchestrationTaskMapper.put(step.getStepCorrelator(),this);
					this.sbRequestStepMapper.put(step.getStepCorrelator(),step);
					this.sbRequestResultMapper.put(step.getStepCorrelator(),new FulfillmentStepResult(null, null));
					this.sbExecutionStatus.put(step.stepCorrelator, Status.PROCESSING);
					OrchestrationManager.getInstance().getGrinder().submit(step);
					logger.debug("Submission successful for: " + step.stepCorrelator);
					if (!isSerialModeNow)
						break;

					this.criticalPath.release();
					// end of critical path
				} else if (executionStatus.name().startsWith("DONE_")) {
					logger.debug("promote2Fulfill(): found the fulfilment step pending result in requestStep mapper store");
					AbstractStepResult result = this.sbRequestResultMapper.get(step.getStepCorrelator());
					
					logger.debug("Confirming the state of completed step: " 
								+ step.stepCorrelator + " = " + this.sbExecutionStatus.get(step.stepCorrelator) 
								+ "Fault: " + result.getResultantFault());

				}
				
				
			} else if (next instanceof SequentialExecution) {
				logger.debug("promote2Fulfill(): found the fulfillment step is sequential will execute now");
				isSerialModeNow = true;
				
				SequentialExecution sequencedSteps = (SequentialExecution) next;
				Iterator<FulfillmentStep> fulfillments = sequencedSteps.iterator();
				while (fulfillments.hasNext()) {
					Step fulfillmentStep = fulfillments.next();
					Status executionStatus = this.sbExecutionStatus.get(step.getStepCorrelator());
					
					if (executionStatus == null || executionStatus == Status.WAITING) {
						logger.info("Attemptin to safely submit to execution for: " + step.stepCorrelator);

						// start of critical path
						try {
							this.criticalPath.acquire();
						} catch (InterruptedException e) {
							logger.error("Interrupted while attempting a critical path. Consistency guarantee compromised. Halting the Orchestration!!");
							this.status = Status.DONE_FAULT;
							break;
						}
						isAllStepsCompleted = false;
						
						// this means the task is not yet submitted...
						logger.debug("Submitting sequential task: " + fulfillmentStep.stepCorrelator + ": " + fulfillmentStep);
						this.orchestrationTaskMapper.put(fulfillmentStep.getStepCorrelator(), this);
						this.sbRequestStepMapper.put(fulfillmentStep.getStepCorrelator(), fulfillmentStep);
						this.sbRequestResultMapper.put(fulfillmentStep.getStepCorrelator(), null);
						this.sbExecutionStatus.put(step.stepCorrelator, Status.PROCESSING);
						OrchestrationManager.getInstance().getGrinder().submit(fulfillmentStep);
						logger.debug("Submission successful for: " + step.stepCorrelator);

						this.criticalPath.release();
						// end of critical path

						break;

					} else if (executionStatus == status.DONE_FAULT || executionStatus == status.DONE_SUCCESS) {
						logger.debug("promote2Fulfill(): found the fulfilment step pending result in requestStep mapper store");
						AbstractStepResult result = this.sbRequestResultMapper.get(fulfillmentStep.getStepCorrelator());
						if (executionStatus == Status.DONE_FAULT)
							anyFault = true;
						logger.debug("Confirming the state of completed step: " + fulfillmentStep.stepCorrelator + " = " + this.sbExecutionStatus.put(fulfillmentStep.stepCorrelator, Status.DONE_FAILED));
					}
					
					
					
				}
			}
		}
		
		logger.debug("isAllStepsCompleted?" + isAllStepsCompleted);
		
		if (isAllStepsCompleted) {
			if (!anyFault)
				this.phasingProgress.put(Phase.TX_PHASE_FULFILLMENT, Status.DONE_SUCCESS);
			else 
				this.phasingProgress.put(Phase.TX_PHASE_FULFILLMENT, Status.DONE_FAULT);
				
			//this.phasingProgress.put(Phase.TX_PHASE_SCHEDULE, Status.PROCESSING);          //TODO: revert back to scheduler
			this.phasingProgress.put(Phase.TX_PHASE_PERSISTENCE, Status.PROCESSING);  
			logger.debug("Orchestration Phase promoted to : " + this.phasingProgress);
		}
		logger.debug("exiting promote2Fulfill()");
	}

	private void promote2Persist() {
		this.phasingProgress.put(Phase.TX_PHASE_PERSISTENCE, Status.PROCESSING);
		logger.debug("Already in persistence...");
		
		boolean isAllPersistenceComplete = true;
		for (PersistenceStep persistence: this.persistence) {
			PersistenceStepResult result = null;
			
			try {
				logger.debug("Executing persistence for:" + persistence.getClass().getCanonicalName() + ":= " + persistence);
				result = persistence.call();
				persistence.setResult(result);
				this.sbRequestResultMapper.put(persistence.getStepCorrelator(), result);
				logger.debug("Execution graceful...");
			} catch (Exception e) {
				logger.debug("Execution blew up on the face!!!", e);
				StepExecutionException fault = new StepExecutionException("Persistence Step Failed", e);
				result = new PersistenceStepResult(fault, null);
				result.setResultantFault(fault);
				this.sbRequestResultMapper.put(persistence.getStepCorrelator(), result);
				isAllPersistenceComplete = false;
				this.phasingProgress.put(Phase.TX_PHASE_PERSISTENCE, Status.DONE_FAULT);
				break;
			}
		}
		
		if (isAllPersistenceComplete) {
			this.phasingProgress.put(Phase.TX_PHASE_PERSISTENCE, Status.DONE_SUCCESS);
			this.status = Status.DONE_SUCCESS;
		} else {
			this.status = Status.DONE_FAULT;
		}
		logger.debug("Final status: " + this.status + "State Machine: " + this.printPhasingProgress());
	}

	private void promote2Schedule() {
		this.phasingProgress.put(Phase.TX_PHASE_SCHEDULE, Status.PROCESSING);
		
		boolean isAllScheduleComplete = true;
		for (SchedulingStep schedule: this.schedules) {
			SchedulingStepResult result = null;
			
			try {
				result = schedule.call();
				schedule.setResult(result);
				this.sbRequestResultMapper.put(schedule.getStepCorrelator(), result);
			} catch (Exception e) {
				StepExecutionException fault = new StepExecutionException("Scheduling Failed", e);
				result = new SchedulingStepResult(fault, null);
				result.setResultantFault(fault);
				this.sbRequestResultMapper.put(schedule.getStepCorrelator(), result);
				isAllScheduleComplete = false;
				this.phasingProgress.put(Phase.TX_PHASE_SCHEDULE, Status.DONE_FAULT);
				break;
			}
		}
		
		if (isAllScheduleComplete) {
			this.phasingProgress.put(Phase.TX_PHASE_SCHEDULE, Status.DONE_SUCCESS);
		} else {
			this.status = Status.DONE_FAULT;
		}
	}

	private void processNotification() {
		for (NotificationStep notification: this.notification) {
			NotificationStepResult result = null;
			try {
				result = notification.call();
				notification.setResult(result);
				this.sbRequestResultMapper.put(notification.getStepCorrelator(), result);

			} catch (Exception e) {
				result = new NotificationStepResult(null, false);
				this.sbRequestResultMapper.put(notification.getStepCorrelator(), result);
			}
		}
	}

	public void cleanupTransaction() {
		for (Step<ChargingStepResult> cleanupKey: this.charges) {
			this.sbRequestResultMapper.remove(cleanupKey);
			this.sbRequestStepMapper.remove(cleanupKey.getStepCorrelator());
			this.orchestrationTaskMapper.remove(cleanupKey.getStepCorrelator());
		}
		
		Iterator  iterator1 = this.prepareFulfillment.iterator();
		while(iterator1.hasNext()) {
			Step<FulfillmentStepResult> cleanupKey = (Step<FulfillmentStepResult>) iterator1.next();
			this.sbRequestResultMapper.remove(cleanupKey);
			this.sbRequestStepMapper.remove(cleanupKey.getStepCorrelator());
			this.orchestrationTaskMapper.remove(cleanupKey.getStepCorrelator());
		}
		
		Iterator  iterator2 = this.fulfillment.iterator();
		while(iterator2.hasNext()) {
			Step<FulfillmentStepResult> cleanupKey = (Step<FulfillmentStepResult>) iterator2.next();
			this.sbRequestResultMapper.remove(cleanupKey);
			this.sbRequestStepMapper.remove(cleanupKey.getStepCorrelator());
			this.orchestrationTaskMapper.remove(cleanupKey.getStepCorrelator());
		}

		for (Step<NotificationStepResult> cleanupKey: this.notification) { 
			this.sbRequestResultMapper.remove(cleanupKey);
			this.sbRequestStepMapper.remove(cleanupKey.getStepCorrelator());
			this.orchestrationTaskMapper.remove(cleanupKey.getStepCorrelator());
		}
		
		for (Step<SchedulingStepResult> cleanupKey: this.schedules) { 
			this.sbRequestResultMapper.remove(cleanupKey);
			this.sbRequestStepMapper.remove(cleanupKey.getStepCorrelator());
			this.orchestrationTaskMapper.remove(cleanupKey.getStepCorrelator());
		}
		
		for (Step<PersistenceStepResult> cleanupKey: this.persistence) { 
			this.sbRequestResultMapper.remove(cleanupKey);
			this.sbRequestStepMapper.remove(cleanupKey.getStepCorrelator());
			this.orchestrationTaskMapper.remove(cleanupKey.getStepCorrelator());
		}
		
	}

	private void initPhasingProgress() {
		if (this.phasingProgress == null) 
			this.phasingProgress = new TreeMap<Phase, Status>();
		
		this.phasingProgress.put(Phase.TX_PHASE_CHARGING, Status.WAITING);
		this.phasingProgress.put(Phase.TX_PHASE_PREP_FULFILLMENT, Status.WAITING);
		this.phasingProgress.put(Phase.TX_PHASE_FULFILLMENT, Status.WAITING);
		this.phasingProgress.put(Phase.TX_PHASE_SCHEDULE, Status.WAITING);
		this.phasingProgress.put(Phase.TX_PHASE_PERSISTENCE, Status.WAITING);
		logger.debug("init phasing progress set to WAITING!!");
	}
	
	
	private void initiateExecution() throws TransactionException {
		this.status = Status.PROCESSING;
		this.phasingProgress.put(Phase.TX_PHASE_CHARGING, Status.PROCESSING);
		
		// first... finish the charging phase....
		logger.debug("Charging tasks to be executed now!!! Total: " + this.charges.size());
		boolean isAllChargingComplete = true;
		for (ChargingStep charging: this.charges) {
			ChargingStepResult result = null;
			try {
				result = charging.call();
				charging.setResult(result);
				this.sbRequestResultMapper.put(charging.getStepCorrelator(), result);
				logger.debug("Charging tasks completed");
			} catch (Exception e) {
				result = new ChargingStepResult(new StepExecutionException("Charging Step Failed", e), null);
				this.sbRequestResultMapper.put(charging.getStepCorrelator(), result);
				isAllChargingComplete = false;
				this.phasingProgress.put(Phase.TX_PHASE_CHARGING, Status.DONE_FAULT);
				break;
			}
		}
		if (isAllChargingComplete) {
			this.phasingProgress.put(Phase.TX_PHASE_CHARGING, Status.DONE_SUCCESS);
			logger.debug("Charging phase complete");
		} else {
			this.status = Status.DONE_FAULT;
			throw new TransactionException(northBoundCorrelator, "Charging Failed... Cannot proceed!!");
		}	
		
		
		// then... get into fulfillment phase & fire out prepare queries....
		this.phasingProgress.put(Phase.TX_PHASE_PREP_FULFILLMENT, Status.PROCESSING);
		logger.debug("Prepare Fulfillment tasks to be executed now!!!. Total: " + this.prepareFulfillment.size()
				+ "Status: " + this.status.name() + "Phasing: " + printPhasingProgress());
		Iterator iterator = this.prepareFulfillment.iterator();
		while(iterator.hasNext()) {
			Step prepare = (Step) iterator.next();
			logger.debug("Prepare Fulfillment task correlationId: " + prepare.getStepCorrelator());
			this.orchestrationTaskMapper.put(prepare.getStepCorrelator(), this);
			this.sbRequestStepMapper.put(prepare.getStepCorrelator(), prepare);
			this.sbRequestResultMapper.put(prepare.getStepCorrelator(), new FulfillmentStepResult(null, null));
			this.sbExecutionStatus.put(prepare.stepCorrelator, Status.PROCESSING);
			OrchestrationManager.getInstance().getGrinder().submit(prepare);
		}

		logger.debug("Prepare fulfilment initiated...");
		
		if(this.prepareFulfillment.size() < 1) {
			promote2Fulfill();
		}
	}

 	private List<ChargingStep> extractChargingSteps(List<TransactionTask> tasks) {
		List<ChargingStep> charges = new LinkedList<ChargingStep>();
		
		for (TransactionTask task: tasks) {
			if (task instanceof Charging) {
				String southBoundRequestId = UniqueIdGenerator.generateId();
				charges.add(new ChargingStep(southBoundRequestId, (Charging)task));
			}
		}
		logger.debug("Extracted charging steps. Total: " + charges.size());
		return charges;
	}

	private List<FulfillmentStep> extractFulfillmentSteps(List<TransactionTask> tasks) throws TransactionException {
		Queue<Fulfillment> toProcess = new LinkedList<Fulfillment>();
		for (TransactionTask task: tasks) {
			if (task instanceof Fulfillment) {
				toProcess.offer((Fulfillment)task);
			}
		}
		
		logger.debug("Going to create prepare fulfillment tasks");
		this.prepareFulfillment = this.packPreparation(toProcess);
		return this.packOrchestration(toProcess);
	}
	
	private ParallelExecution packPreparation(Queue<Fulfillment> toProcess) {
		ParallelExecution prepFulfill = new ParallelExecution();
		
		//2do: uncomment this when rollback is ready!!!
//		for (TransactionTask task: toProcess) {
//			if (task instanceof Fulfillment && ((Fulfillment)task).getMode() == FulfillmentMode.FULFILL) {
//				String southBoundRequestId = UniqueIdGenerator.generateId();
//				Fulfillment clonedTask = CloneHelper.deepClone((Fulfillment)task);
//				clonedTask.setMode(FulfillmentMode.PREPARE);
//				prepFulfill.add(new FulfillmentStep(southBoundRequestId, (Fulfillment)clonedTask));
//			}
//		}
		logger.debug("Extracted prepare fulfilment tasks. Total: " + prepFulfill.size());
		return prepFulfill;
	}

	private List packOrchestration(Queue<Fulfillment> toProcess) {
		ParallelExecution parallel = new ParallelExecution();
		SequentialExecution sequence = new SequentialExecution();
		logger.debug("Packaging the orchestration!!!");
		IServiceRegistry resources = ServiceResolver.getServiceRegistry();
		while (!toProcess.isEmpty()) {
			Fulfillment fulfillment = toProcess.peek();
			logger.debug("Adding Fulfillment Profile: " + fulfillment.getAtomicProduct().getName());
			
			try {
				logger.debug("Retrieve resource from registry: " + fulfillment.getAtomicProduct().getResource().getName());
//				Resource current = resources.readResource(fulfillment.getAtomicProduct().getResource().getName());
//				logger.debug("Just to show Noli is being an ass... Resource current is: " + current);
//				if (current.getDependantOnOthers() != null) {
//					logger.debug("Found dependency for the resouce. preparing sequential orchestration");
//					for (Resource dependancy: current.getDependantOnOthers()) {
//						if (this.isPresentIn(toProcess, dependancy)) {
//							String southBoundRequestId = UniqueIdGenerator.generateId();
//							sequence.add(new FulfillmentStep(southBoundRequestId, this.getGetDependantFulfillment(toProcess, dependancy)));
//						}
//					}
//					String southBoundRequestId = UniqueIdGenerator.generateId();
//					sequence.add(new FulfillmentStep(southBoundRequestId, fulfillment));
//				} else {
					String southBoundRequestId = UniqueIdGenerator.generateId();
					parallel.add(new FulfillmentStep(southBoundRequestId, fulfillment));
//				}
				toProcess.poll();
			} catch (Exception e) {
				logger.error(this.northBoundCorrelator, "Unable to pack orchestration profile for fulfillment... Service Registry Failure", e);
				return new ArrayList();
			}
		}
		
		logger.debug("Fulfillment tasks packed. Total parallel tasks: " + parallel.size());
		logger.debug("Fulfillment tasks packed. Total Sequential tasks: " + sequence.size());
		if (!parallel.isEmpty()) {
		     if (!sequence.isEmpty())
		    	 parallel.add(sequence);
		     return parallel;
		} else
		     return sequence;
	}
	
	
	private List<FulfillmentStep> prepareRollbackFulfillment() {
		List<FulfillmentStep> rollbackActions = new LinkedList<>();
		
		for (Object nextElement: this.fulfillment) {
			if (nextElement instanceof FulfillmentStep) {
				FulfillmentStep step = (FulfillmentStep) nextElement;
				
				if (this.sbRequestResultMapper.get(step.getStepCorrelator()) != null) {
					// this means that this task has executed during the forward transaction & must be rolledback...
					String southBoundRequestId = UniqueIdGenerator.generateId();
					FulfillmentStep rollbackStep = CloneHelper.deepClone((FulfillmentStep)nextElement);
					((Fulfillment)rollbackStep.getExecutionInputs()).setMode(FulfillmentMode.REVERSE);
					((Fulfillment)rollbackStep.getExecutionInputs()).setAdditionalInputs(this.getPreparedInfo(step));
					
					rollbackActions.add(rollbackStep);
				}
			} else if (nextElement instanceof SequentialExecution) {
				SequentialExecution sequencedSteps = (SequentialExecution) nextElement;
				Iterator<FulfillmentStep> fulfillments = sequencedSteps.iterator();
				while (fulfillments.hasNext()) {
					FulfillmentStep step = fulfillments.next();
					if (this.sbRequestResultMapper.get(step.getStepCorrelator()) != null) {
						// this means that this task has executed during the forward transaction & must be rolledback...
						String southBoundRequestId = UniqueIdGenerator.generateId();
						FulfillmentStep rollbackStep = CloneHelper.deepClone((FulfillmentStep)nextElement);
						((Fulfillment)rollbackStep.getExecutionInputs()).setMode(FulfillmentMode.REVERSE);
						((Fulfillment)rollbackStep.getExecutionInputs()).setAdditionalInputs(this.getPreparedInfo(step));

						rollbackActions.add(rollbackStep);
					}	
				}
			}
		}
		
		
		return rollbackActions;
	}
	
	private Map<String, Object> getPreparedInfo(FulfillmentStep originalFulfillment) {
		Map<String, Object> preparedInfo = new TreeMap<String, Object>();
		
		Iterator iterator = this.prepareFulfillment.iterator();
		while(iterator.hasNext()) {
			FulfillmentStep prepared = (FulfillmentStep) iterator.next();
			String resourceFromOriginalFulfillment = ((Fulfillment)originalFulfillment.getExecutionInputs()).getAtomicProduct().getResource().getName();
			String resourceFromPreparedFulfillment = ((Fulfillment)prepared.getExecutionInputs()).getAtomicProduct().getResource().getName(); 
			if (resourceFromOriginalFulfillment.equals(resourceFromPreparedFulfillment)) {
				preparedInfo.put(resourceFromPreparedFulfillment, prepared.getResult().getFulfillmentResult());
			}
		}
						
		return preparedInfo;
	}

	private List<NotificationStep> extractNotificationSteps(List<TransactionTask> tasks) {
		List<NotificationStep> notifications = new LinkedList<NotificationStep>();
		
		for (TransactionTask task: tasks) {
			if (task instanceof Notification) {
				String southBoundRequestId = UniqueIdGenerator.generateId();
				notifications.add(new NotificationStep(southBoundRequestId, (Notification)task));
			}
		}
		logger.debug("Extracted notification tasks. Total: " + notifications.size());
		return notifications;
	}

	
	private List<SchedulingStep> extractSchedulingSteps(List<TransactionTask> tasks) {
		List<SchedulingStep> schedules = new LinkedList<>();
		
		for (TransactionTask task: tasks) {
			if (task instanceof Future) {
				String southBoundRequestId = UniqueIdGenerator.generateId();
				schedules.add(new SchedulingStep(southBoundRequestId, (Future)task));
			}
		}
		logger.debug("Extracted schedule tasks. Total: " + schedules.size());
		return schedules;
	}

	private List<PersistenceStep> extractPersistenceSteps(List<TransactionTask> tasks) {
		List<PersistenceStep> persistence = new LinkedList<>();
		
		for (TransactionTask task: tasks) {
			if (task instanceof Persistence<?>) {
				String southBoundRequestId = UniqueIdGenerator.generateId();
				persistence.add(new PersistenceStep(southBoundRequestId, (Persistence<?>)task));
			}
		}
		logger.debug("Extracted persistence tasks. Total: " + persistence.size());
		return persistence;
	}

	
	public Map<Step, AbstractStepResult> getAtomicStepResults() {
		Map<Step, AbstractStepResult> results = new TreeMap<Step, AbstractStepResult>();
		
		for (Step step: this.charges)
			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		
		Iterator itr1 = this.prepareFulfillment.iterator();
		while(itr1.hasNext()) {
			Step step = (Step) itr1.next();
			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		}
		
		Iterator itr2 = this.fulfillment.iterator();
		while(itr2.hasNext()) {
			
			Object oNext = itr2.next();
			if(oNext instanceof Step) {
				Step step = (Step) oNext;
				results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
			} else if(oNext instanceof SequentialExecution) {
				SequentialExecution seqSteps = (SequentialExecution) oNext; 
				Iterator it1 = seqSteps.iterator();
				while(it1.hasNext()) {
					Step step = (Step) it1.next();
					results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
				}
					
			}
			
		}
		
//		Iterator itr3 = this.reverseFulfillment.iterator();
//		while(itr3.hasNext()) {
//			Step step = (Step) itr3.next();
//			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
//		}
//			
//		
//		for (Step step: this.schedules)
//			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
//		
//		for (Step step: this.persistence)
//			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
//		
//		for (Step step: this.notification)
//			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		
		return results;
	}


	
	
	private Fulfillment getGetDependantFulfillment(Queue<Fulfillment> toProcess, Resource dependancy) {
		for (Fulfillment fulfillment: toProcess) {
			if (fulfillment.getAtomicProduct().getResource().getName().equals(dependancy.getName())) {
				toProcess.remove(fulfillment);
				return fulfillment;
			}
		}
		return null;
	}

	private boolean isPresentIn(Queue<Fulfillment> toProcess, Resource dependancy) {
		for (Fulfillment fulfillment: toProcess) {
			if (dependancy.getName().equals(fulfillment.getAtomicProduct().getResource().getName()))
				return true;
		}
		return false;
	}
	
	private boolean isPhaseComplete(Phase phase) {
		List<? extends Step> toCheck = null;
		logger.debug("Checking if phase is complete. Phase: " + phase.name());
		switch (phase) {
			case TX_PHASE_CHARGING:
				// this is impossible, since charging is handled synchronous execution... but hey!! what the hell?!
				toCheck = this.charges;
				break;
			case TX_PHASE_PREP_FULFILLMENT:
				toCheck = this.prepareFulfillment;
				break;
			case TX_PHASE_FULFILLMENT:
				toCheck = this.fulfillment;
				break;
			case TX_PHASE_PERSISTENCE:
				toCheck = this.persistence;
				break;
			case TX_PHASE_SCHEDULE:
				toCheck = this.schedules;
				break;
		}
		
		
		boolean isAllStepsComplete = false;
		boolean anyFault = false;
		
		int completion = 0;
		for (Object next: toCheck) {
			Step step = null;
			if (next instanceof Step) {
				step = (Step) next;
				
				AbstractStepResult result = this.sbRequestResultMapper.get(step.getStepCorrelator());
				Status executionStatus = this.sbExecutionStatus.get(step.stepCorrelator);
				logger.debug("Checking " + step.getStepCorrelator() + "Step: " + step.toString() + 
							", Result: " + result + 
							", ExecutionStatus: " + executionStatus);

				if (executionStatus != null && executionStatus.name().startsWith("DONE_")) {
					completion++;
					logger.debug("Step:" + step.stepCorrelator + " is complete with " + executionStatus);
					if (executionStatus == Status.DONE_FAULT)
						anyFault = true;
				} //else {
				//	anyFault = true;
				//	step.setFault(result.getResultantFault());
				//	completion++;
				//	logger.debug("Step:" + step.stepCorrelator + " is complete with failure!!");
				//}
			}
		}
		
		logger.info("Total Steps in this phase: " + toCheck.size() + "  & completion is: " + completion);
		
		isAllStepsComplete = (toCheck.size() == completion);
		
		if (!isAllStepsComplete) {
			logger.debug("May be we wait for some more time for all to complete");
			return false;
		}
		
		if (isAllStepsComplete && !anyFault) {
			logger.debug("All steps complete. Moving: " + phase.name() + " -> Status: " + Status.DONE_SUCCESS.name());
			this.phasingProgress.put(phase, Status.DONE_SUCCESS);
		} else if (isAllStepsComplete && anyFault) {
			logger.debug("All steps complete. Moving: " + phase.name() + " -> Status: " + Status.DONE_FAULT.name());
			this.phasingProgress.put(phase, status.DONE_FAULT);
		} else {
			logger.debug("Seems like all steps are not completed yet???");
		}

		return true;
	}

	
	public String getNorthBoundCorrelator() {
		return northBoundCorrelator;
	}

	public Status getStatus() {
		return status;
	}


	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public String printPhasingProgress() {
		if(this.phasingProgress != null) {
			return this.phasingProgress.toString();
		}
		return null;
	}

	enum Mode implements Serializable {
		FORWARD,
		ROLLBACK;
	}

	public void setMetas(Map<String, String> metas) {
		this.metas = metas;
		
	}

	public Map<String, String> getMetas() {
		return this.metas;
	}

	public void addMetas(Map<String, String> metas) {
		if (this.metas == null)
			this.metas = new HashMap<String, String>();
		
		this.metas.putAll(metas);
	}
	
	
}
