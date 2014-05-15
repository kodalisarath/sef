package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.ServiceResolver;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.entities.AbstractResponse;
import com.ericsson.raso.sef.bes.prodcat.CatalogException;
import com.ericsson.raso.sef.bes.prodcat.entities.MonetaryUnit;
import com.ericsson.raso.sef.bes.prodcat.entities.Resource;
import com.ericsson.raso.sef.bes.prodcat.service.IServiceRegistry;
import com.ericsson.raso.sef.bes.prodcat.tasks.Charging;
import com.ericsson.raso.sef.bes.prodcat.tasks.Fulfillment;
import com.ericsson.raso.sef.bes.prodcat.tasks.Future;
import com.ericsson.raso.sef.bes.prodcat.tasks.Notification;
import com.ericsson.raso.sef.bes.prodcat.tasks.Persistence;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.UniqueIdGenerator;

public class Orchestration implements Serializable, Callable<AbstractResponse> {
	private static final long	serialVersionUID	= -8295152874923894285L;

	private String		northBoundCorrelator	= null;
	private List<ChargingStep>	charges					= null;
	private List<FulfillmentStep>	fulfillment				= null;
	private List<Step>	notification			= null;
	private List<Step>	schedules				= null;
	private List<Step>	persistence				= null;
	
	private Status status = Status.WAITING;
	private Map<Constants, Status> phasingProgress = null;

	private Orchestration() {
		this.initPhasingProgress();
	}

	public Orchestration(String requestId, List<TransactionTask> tasks) throws TransactionException {
		this.northBoundCorrelator = requestId;
		
		this.charges = this.getChargingSteps(tasks);
		this.fulfillment = this.getFulfillmentSteps(tasks);
		this.notification = this.getNotificationSteps(tasks);
		this.schedules = this.getSchedulingSteps(tasks);
		this.persistence = this.getPersistenceSteps(tasks);
		
		this.initPhasingProgress();
	}
	
	public Orchestration getRollbackProfile() {
		Orchestration rollback = new Orchestration();
		//TODO: implement the logic to reverse the orchestration
		return rollback;
	}
	

	@Override
	public AbstractResponse call() throws Exception {
		// TODO Auto-generated method stub
		
		switch (this.status) {
			case WAITING:
				//TODO:
				this.initiateExecution();
				break;
			case PROCESSING:
				//TODO:
				
				break;
			case ROLLING_BACK:
				//TODO: 
				break;
			case DONE_FAILED:
				//TODO:
				
				break;
			case DONE_FAULT:
				//TODO:
				
				break;
			case DONE_SUCCESS:
				//TODO:
				break;
		}
		
		
		
		return null;
	}


	private void initPhasingProgress() {
		if (this.phasingProgress == null) 
			this.phasingProgress = new TreeMap<Constants, Status>();
		
		this.phasingProgress.put(Constants.TX_PHASE_CHARGING, Status.WAITING);
		this.phasingProgress.put(Constants.TX_PHASE_FULFILLMENT, Status.WAITING);
		this.phasingProgress.put(Constants.TX_PHASE_SCHEDULE, Status.WAITING);
		this.phasingProgress.put(Constants.TX_PHASE_PERSISTENCE, Status.WAITING);
	}
	
	
	private void initiateExecution() {
		this.status = Status.PROCESSING;
		this.phasingProgress.put(Constants.TX_PHASE_CHARGING, Status.PROCESSING);
		
		boolean isAllChargingComplete = true;
		for (ChargingStep charging: this.charges) {
			ChargingStepResult result = null;
			try {
				result = charging.call();
			} catch (Exception e) {
				result = new ChargingStepResult(new StepExecutionException("Charging Step Failed", e), null);
				isAllChargingComplete = false;
				break;
			}
		}
		
	}
	
	

 	private List<ChargingStep> getChargingSteps(List<TransactionTask> tasks) {
		List<ChargingStep> charges = new SequentialExecution();
		
		for (TransactionTask task: tasks) {
			if (task instanceof Charging) {
				String southBoundRequestId = UniqueIdGenerator.generateId();
				charges.add(new ChargingStep(southBoundRequestId, (Charging)task));
			}
		}
		return charges;
	}

	private List<FulfillmentStep> getFulfillmentSteps(List<TransactionTask> tasks) throws TransactionException {
		Queue<Fulfillment> toProcess = new LinkedList<Fulfillment>();
		for (TransactionTask task: tasks) {
			if (task instanceof Fulfillment) {
				toProcess.offer((Fulfillment)task);
			}
		}
		
		return this.packOrchestration(toProcess);
	}

	private List<NotificationStep> getNotificationSteps(List<TransactionTask> tasks) {
		List<NotificationStep> notifications = new SequentialExecution();
		
		for (TransactionTask task: tasks) {
			if (task instanceof Notification) {
				String southBoundRequestId = UniqueIdGenerator.generateId();
				notifications.add(new NotificationStep(southBoundRequestId, (Notification)task));
			}
		}
		return notifications;
	}

	
	private List<Step> getSchedulingSteps(List<TransactionTask> tasks) {
		List<Step> schedules = new SequentialExecution();
		
		for (TransactionTask task: tasks) {
			if (task instanceof Future) {
				String southBoundRequestId = UniqueIdGenerator.generateId();
				schedules.add(new Step(southBoundRequestId, task));
			}
		}
		return schedules;
	}

	private List<Step> getPersistenceSteps(List<TransactionTask> tasks) {
		List<Step> persistence = new SequentialExecution();
		
		for (TransactionTask task: tasks) {
			if (task instanceof Persistence<?>) {
				String southBoundRequestId = UniqueIdGenerator.generateId();
				persistence.add(new Step(southBoundRequestId, task));
			}
		}
		return persistence;
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
		parallel.add(sequence);
		return parallel;
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

	private void removeFulfillment(Queue<Fulfillment> toProcess, Resource dependancy) {
		for (Fulfillment fulfillment: toProcess) {
			if (fulfillment.getAtomicProduct().getResource().getName().equals(dependancy.getName())) {
				toProcess.remove(fulfillment);
				return;
			}
		}
	}

	private boolean isPresentIn(Queue<Fulfillment> toProcess, Resource dependancy) {
		for (Fulfillment fulfillment: toProcess) {
			if (dependancy.getName().equals(fulfillment.getAtomicProduct().getResource().getName()))
				return true;
		}
		return false;
	}

}
