package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.ericsson.raso.sef.core.FrameworkException;

public abstract class NotificationTask<E> implements Callable<E>, Serializable, TransactionTask {
	private static final long serialVersionUID = -7189717345996246019L;

	private State state = State.WAITING;
	

	/**
	 * Performs a notification activity while a purchase and/or service order is being fulfilled.
	 * 
	 * @return - true if successful; false if unsuccessful but gracefully handled.
	 * @throws FrameworkException relevant to Fulfillment Engine.
	 */
	public abstract E sendNotification() throws FrameworkException;

	@Override
	public E call() throws Exception {
		this.state = State.PROCESSING;
		
		E result = this.sendNotification();
		
		this.state = State.DONE;
		return result;
		
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	enum State {
		WAITING,
		PROCESSING,
		DONE;
	}

}
