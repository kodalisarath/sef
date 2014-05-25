package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import org.springframework.dao.PessimisticLockingFailureException;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.Phase;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.commands.AbstractTransaction;
import com.ericsson.raso.sef.bes.engine.transaction.entities.AbstractResponse;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.AtomicProduct;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.raso.sef.bes.prodcat.tasks.*;
import com.ericsson.raso.sef.core.CloneHelper;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.UniqueIdGenerator;

public class Orchestration implements Serializable, Callable<AbstractResponse> {
	private static final long		serialVersionUID		= -8295152874923894285L;

	private String							northBoundCorrelator	= null;
	private List<ChargingStep>				charges					= null;
	private List<FulfillmentStep>			prepareFulfillment		= null;
	private List<FulfillmentStep>			fulfillment				= null;
	private List<FulfillmentStep>			reverseFulfillment		= null;
	private List<NotificationStep>			notification			= null;
	private List<SchedulingStep>			schedules				= null;
	private List<PersistenceStep>			persistence				= null;

	private Status							status					= Status.WAITING;
	private Mode							mode					= Mode.FORWARD;
	private Map<Phase, Status>				phasingProgress			= null;
	private TransactionException			executionFault			= null;

	private Map<String, Orchestration> orchestrationTaskMapper = SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.ORCHESTRATION_TASK_MAPPER.name());
	
	private Map<String, AbstractStepResult> sbRequestResultMapper = SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.TRANSACTION_STEP_STATUS.name());
	
	private Map<String, Step> sbRequestStepMapper = SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.TRANSACTION_STEPS.name());
	
	

	
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
		switch (this.status) {
			case WAITING:
				try {
					this.initiateExecution();
				} catch (Exception e) {
					this.status = Status.DONE_FAULT;
					this.executionFault = new TransactionException(northBoundCorrelator, "CHARGING FAIED", e);
				}
				break;
			case PROCESSING:
				if (this.phasingProgress.get(Phase.TX_PHASE_PREP_FULFILLMENT) == Status.PROCESSING) {
					if (this.isPhaseComplete(Phase.TX_PHASE_PREP_FULFILLMENT) && this.phasingProgress.get(Phase.TX_PHASE_PREP_FULFILLMENT) == Status.DONE_SUCCESS) 
						this.promote2Fulfill();
					else {
						this.status = Status.DONE_FAULT;
						this.executionFault = new TransactionException(northBoundCorrelator, "FULFILLMENT PREPARATION FAILED");
					}
				} else if (this.phasingProgress.get(Phase.TX_PHASE_FULFILLMENT) == Status.PROCESSING) {
					if (this.isPhaseComplete(Phase.TX_PHASE_FULFILLMENT) && this.phasingProgress.get(Phase.TX_PHASE_FULFILLMENT) == Status.DONE_SUCCESS) {
						this.processNotification();
						this.promote2Schedule();
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

				// proceed to scheduling...
				if (this.phasingProgress.get(Phase.TX_PHASE_SCHEDULE) == Status.PROCESSING) {
					if (this.isPhaseComplete(Phase.TX_PHASE_SCHEDULE) && this.phasingProgress.get(Phase.TX_PHASE_SCHEDULE) == Status.DONE_SUCCESS) 
						this.promote2Persist();
					else {
						this.status = Status.DONE_FAULT;
						this.executionFault = new TransactionException(northBoundCorrelator, "FUTURE EVENT SCHEDULING FAILED");
					}
				} 
				
				// proceed to persistence...
				if (this.phasingProgress.get(Phase.TX_PHASE_PERSISTENCE) == Status.PROCESSING) {
					if (this.isPhaseComplete(Phase.TX_PHASE_PERSISTENCE) && this.phasingProgress.get(Phase.TX_PHASE_PERSISTENCE) == Status.DONE_SUCCESS) 
						this.status = Status.DONE_SUCCESS;
					else {
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
			this.processNotification();
			OrchestrationManager.getInstance().sendResponse(northBoundCorrelator, this);
		}
		
		return null;
	}

	
	private void promote2Fulfill() {
		boolean isAllStepsCompleted = true;
		boolean isCurrentParallelAllCompleted = true;
		boolean isSequenceCompleted = true;
		boolean isSerialModeNow = false;
		boolean anyFailure = false;
		boolean anyFault = false;
		
		for (Object next: this.fulfillment) {
			Step step = null;
			if (next instanceof FulfillmentStep) {
				step = (FulfillmentStep) next;
			
				if (this.sbRequestStepMapper.get(step.getStepCorrelator()) != null) {
					// this means the task had been submitted earlier...
					AbstractStepResult result = this.sbRequestResultMapper.get(step.getStepCorrelator());
					if (result != null) {
						// this means the task has been executed
						if (result.getResultantFault() != null) {
							step.setFault(result.getResultantFault());
							anyFault = true;	
						}
						if (((FulfillmentStepResult)result).getFulfillmentResult() == null || ((FulfillmentStepResult)result).getFulfillmentResult().isEmpty())
							anyFailure = true;
						step.setResult(result);
						
						if (anyFault || anyFailure) {
							this.status = Status.DONE_FAULT;
							break;
						}
					} else {
						// this means the task is not yet completed its execution in fulfillment engine....
						isAllStepsCompleted = false;
						isCurrentParallelAllCompleted = false; // allow the task to complete & check on the next one....
					}
				} else {
					isAllStepsCompleted = false;
					// this means the task is not yet submitted...
					if (!isCurrentParallelAllCompleted) {
						// can submit all subsequent tasks  until hitting a serial mode...
						if (!isSerialModeNow) {
							this.orchestrationTaskMapper.put(step.getStepCorrelator(), this);
							this.sbRequestStepMapper.put(step.getStepCorrelator(), step);
							this.sbRequestResultMapper.put(step.getStepCorrelator(), null);
							OrchestrationManager.getInstance().getGrinder().submit(step);
						}
					}
				}
			} else if (next instanceof SequentialExecution) {
				isSerialModeNow = true;
				
				SequentialExecution sequencedSteps = (SequentialExecution) next;
				Iterator<FulfillmentStep> fulfillments = sequencedSteps.iterator();
				while (fulfillments.hasNext()) {
					Step fulfillment = fulfillments.next();
					
					if (this.sbRequestStepMapper.get(fulfillment.getStepCorrelator()) != null) {
						// this means the task had been submitted earlier...
						AbstractStepResult result = this.sbRequestResultMapper.get(fulfillment.getStepCorrelator());
						if (result != null) {
							// this means the task has been executed
							if (result.getResultantFault() != null) {
								fulfillment.setFault(result.getResultantFault());
								anyFault = true;
							}
							if (((FulfillmentStepResult)result).getFulfillmentResult() == null ||  ((FulfillmentStepResult)result).getFulfillmentResult().isEmpty())
								anyFailure = true;
							fulfillment.setResult(result);
							
							if (anyFault || anyFailure) {
								this.status = Status.DONE_FAULT;
								break;
							}
						} else {
							// this means the task is not yet completed its execution in fulfillment engine....
							isSequenceCompleted = false; // allow the task to complete & check on the next one....
							isAllStepsCompleted = false;
						}
					} else {
						isSequenceCompleted = false;
						isAllStepsCompleted = false;
						// this means the task is not yet submitted...
						this.orchestrationTaskMapper.put(fulfillment.getStepCorrelator(), this);
						this.sbRequestStepMapper.put(fulfillment.getStepCorrelator(), fulfillment);
						this.sbRequestResultMapper.put(fulfillment.getStepCorrelator(), null);
						OrchestrationManager.getInstance().getGrinder().submit(fulfillment);
						break;
					}
				}
			}
		}
		
		if (isAllStepsCompleted) {
			this.phasingProgress.put(Phase.TX_PHASE_FULFILLMENT, Status.DONE_SUCCESS);
			this.phasingProgress.put(Phase.TX_PHASE_SCHEDULE, Status.PROCESSING);
		}
		
	}

	private void promote2Persist() {
		this.phasingProgress.put(Phase.TX_PHASE_PERSISTENCE, Status.PROCESSING);
		
		boolean isAllPersistenceComplete = true;
		for (PersistenceStep persistence: this.persistence) {
			PersistenceStepResult result = null;
			
			try {
				result = persistence.call();
				persistence.setResult(result);
				this.sbRequestResultMapper.put(persistence.getStepCorrelator(), result);
			} catch (Exception e) {
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
		
		for (Step<FulfillmentStepResult> cleanupKey: this.prepareFulfillment) { 
			this.sbRequestResultMapper.remove(cleanupKey);
			this.sbRequestStepMapper.remove(cleanupKey.getStepCorrelator());
			this.orchestrationTaskMapper.remove(cleanupKey.getStepCorrelator());
		}
		
		for (Step<FulfillmentStepResult> cleanupKey: this.fulfillment) { 
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
	}
	
	
	private void initiateExecution() throws TransactionException {
		this.status = Status.PROCESSING;
		this.phasingProgress.put(Phase.TX_PHASE_CHARGING, Status.PROCESSING);
		
		// first... finish the charging phase....
		boolean isAllChargingComplete = true;
		for (ChargingStep charging: this.charges) {
			ChargingStepResult result = null;
			try {
				result = charging.call();
				charging.setResult(result);
				this.sbRequestResultMapper.put(charging.getStepCorrelator(), result);
				
			} catch (Exception e) {
				result = new ChargingStepResult(new StepExecutionException("Charging Step Failed", e), null);
				this.sbRequestResultMapper.put(charging.getStepCorrelator(), result);
				isAllChargingComplete = false;
				this.phasingProgress.put(Phase.TX_PHASE_CHARGING, Status.DONE_FAULT);
				break;
			}
		}
		if (isAllChargingComplete)
			this.phasingProgress.put(Phase.TX_PHASE_CHARGING, Status.DONE_SUCCESS);
		else {
			this.status = Status.DONE_FAULT;
			throw new TransactionException(northBoundCorrelator, "Charging Failed... Cannot proceed!!");
		}	
		
		// then... get into fulfillment phase & fire out prepare queries....
		this.phasingProgress.put(Phase.TX_PHASE_PREP_FULFILLMENT, Status.PROCESSING);
		for (Step prepare: this.prepareFulfillment) {
			this.orchestrationTaskMapper.put(prepare.getStepCorrelator(), this);
			this.sbRequestStepMapper.put(prepare.getStepCorrelator(), prepare);
			this.sbRequestResultMapper.put(prepare.getStepCorrelator(), null);
			OrchestrationManager.getInstance().getGrinder().submit(prepare);
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
		return charges;
	}

	private List<FulfillmentStep> extractFulfillmentSteps(List<TransactionTask> tasks) throws TransactionException {
		Queue<Fulfillment> toProcess = new LinkedList<Fulfillment>();
		for (TransactionTask task: tasks) {
			if (task instanceof Fulfillment) {
				toProcess.offer((Fulfillment)task);
			}
		}
		
		this.prepareFulfillment = this.packPreparation(toProcess);
		return this.packOrchestration(toProcess);
	}
	
	private ParallelExecution packPreparation(Queue<Fulfillment> toProcess) {
		ParallelExecution prepFulfill = new ParallelExecution();
		
		for (TransactionTask task: toProcess) {
			if (task instanceof Fulfillment) {
				String southBoundRequestId = UniqueIdGenerator.generateId();
				Fulfillment clonedTask = CloneHelper.deepClone((Fulfillment)task);
				clonedTask.setMode(FulfillmentMode.PREPARE);
				prepFulfill.add(new FulfillmentStep(southBoundRequestId, (Fulfillment)clonedTask));
			}
		}
		return prepFulfill;
	}

	private List<FulfillmentStep> packOrchestration(Queue<Fulfillment> toProcess) {
		ParallelExecution parallel = new ParallelExecution();
		SequentialExecution sequence = new SequentialExecution();
		
		IServiceRegistry resources = ServiceResolver.getServiceRegistry();
		while (!toProcess.isEmpty()) {
			Fulfillment fulfillment = toProcess.peek();
			try {
				Resource current = resources.readResource(fulfillment.getAtomicProduct().getResource().getName());
				if (current.getDependantOnOthers() != null) {
					
					for (Resource dependancy: current.getDependantOnOthers()) {
						if (this.isPresentIn(toProcess, dependancy)) {
							String southBoundRequestId = UniqueIdGenerator.generateId();
							sequence.add(new FulfillmentStep(southBoundRequestId, this.getGetDependantFulfillment(toProcess, dependancy)));
						}
					}
					String southBoundRequestId = UniqueIdGenerator.generateId();
					sequence.add(new FulfillmentStep(southBoundRequestId, fulfillment));
				} else {
					String southBoundRequestId = UniqueIdGenerator.generateId();
					parallel.add(new FulfillmentStep(southBoundRequestId, fulfillment));
				}
				
			} catch (CatalogException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (!parallel.isEmpty()) {
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
		
		for (FulfillmentStep prepared: this.prepareFulfillment) {
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
		return persistence;
	}

	
	public Map<Step, AbstractStepResult> getAtomicStepResults() {
		Map<Step, AbstractStepResult> results = new TreeMap<Step, AbstractStepResult>();
		
		for (Step step: this.charges)
			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		
		for (Step step: this.prepareFulfillment)
			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		
		for (Step step: this.fulfillment)
			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		
		for (Step step: this.reverseFulfillment)
			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		
		for (Step step: this.schedules)
			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		
		for (Step step: this.persistence)
			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		
		for (Step step: this.notification)
			results.put(step, this.sbRequestResultMapper.get(step.getStepCorrelator()));
		
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
		
		
		boolean isAllStepsComplete = true;
		boolean anyFailure = false;
		boolean anyFault = false;
		
		for (Object next: toCheck) {
			Step step = null;
			if (next instanceof Step) {
				step = (Step) next;

				AbstractStepResult result = this.sbRequestResultMapper.get(step.getResult());
				if (result == null) {
					return false;
				} else {
					step.setResult(result);
					if (result.getResultantFault() != null) {
						anyFault = true;
						step.setFault(result.getResultantFault());
					}
					// check for types of steps....
					if (step instanceof FulfillmentStep) {
						if (((FulfillmentStepResult) result).getFulfillmentResult() == null || ((FulfillmentStepResult) result).getFulfillmentResult().isEmpty())
							anyFailure = true;
						
					} else if (step instanceof PersistenceStep) {
						if (((PersistenceStepResult) result).getPersistenceResult() == null)
							anyFailure = true;
						
					} else if (step instanceof SchedulingStep) {
						if (((SchedulingStepResult) result).getNotificationResult() == null || ((SchedulingStepResult) result).getNotificationResult() == false)
							anyFailure = true;
					}
				}
			}
		}
		if (isAllStepsComplete && !anyFailure && !anyFault)
			this.phasingProgress.put(Phase.TX_PHASE_PREP_FULFILLMENT, Status.DONE_SUCCESS);
		if (isAllStepsComplete && anyFailure && !anyFault)
			this.phasingProgress.put(Phase.TX_PHASE_PREP_FULFILLMENT, Status.DONE_FAILED);
		else if (isAllStepsComplete && anyFault)
			this.phasingProgress.put(Phase.TX_PHASE_PREP_FULFILLMENT, status.DONE_FAULT);

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


	enum Mode {
		FORWARD,
		ROLLBACK;
	}
	
	
}