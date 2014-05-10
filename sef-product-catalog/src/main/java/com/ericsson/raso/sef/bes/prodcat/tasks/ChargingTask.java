package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.ericsson.raso.sef.core.FrameworkException;

public abstract class ChargingTask<E> implements Callable<E>, Serializable, TransactionTask {
	private static final long serialVersionUID = -7774372771842184423L;

	private Mode mode = null;
	private State state = State.WAITING;
	

	/**
	 * Performs a charging activity before service order is fulfilled.
	 * 
	 * @return - true if successful; false if unsuccessful but gracefully handled.
	 * @throws FrameworkException relevant to Fulfillment Engine.
	 */
	public abstract E charge() throws FrameworkException;

	/**
	 * Performs a reversal of previous charging activity, when associated fulfillment activities have failed beyond recovery.
	 * 
	 * @return - true if successful; false if unsuccessful but gracefully handled.
	 * @throws FrameworkException relevant to Fulfillment Engine.
	 */
	public abstract E reverse() throws FrameworkException;

	@Override
	public E call() throws Exception {
		this.state = State.PROCESSING;
		
		E result;
		switch (mode) {
			case CHARGE:
				result = this.charge();
				break;
			case REVERSE:
				result = this.reverse();
				break;
			default:
				return null;
		}
		
		this.state = State.DONE;
		return result;
		
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Mode getMode() {
		return this.mode;
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}



	enum Mode {
		CHARGE,
		REVERSE;
	}
	
	enum State {
		WAITING,
		PROCESSING,
		DONE;
	}

}
