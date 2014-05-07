package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.ericsson.raso.sef.core.FrameworkException;

public abstract class ChargingTask implements Callable<Boolean>, Serializable {
	private static final long serialVersionUID = -7774372771842184423L;

	private Mode mode = null;
	private State state = State.WAITING;
	

	/**
	 * Performs a charging activity before service order is fulfilled.
	 * 
	 * @return - true if successful; false if unsuccessful but gracefully handled.
	 * @throws FrameworkException relevant to Fulfillment Engine.
	 */
	public abstract boolean charge() throws FrameworkException;

	/**
	 * Performs a reversal of previous charging activity, when associated fulfillment activities have failed beyond recovery.
	 * 
	 * @return - true if successful; false if unsuccessful but gracefully handled.
	 * @throws FrameworkException relevant to Fulfillment Engine.
	 */
	public abstract boolean reverse() throws FrameworkException;

	@Override
	public Boolean call() throws Exception {
		this.state = State.PROCESSING;
		
		boolean result;
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
