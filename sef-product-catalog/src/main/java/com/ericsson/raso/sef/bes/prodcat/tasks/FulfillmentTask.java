package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.ericsson.raso.sef.core.FrameworkException;

public abstract class FulfillmentTask<E> implements Callable<E>, Serializable {
	private static final long serialVersionUID = -2151895776584893865L;

	private Mode mode = null;
	private State state = State.WAITING;

	/**
	 * READ - Command Pattern representing a CRUD model for the backend objects about to be impacted.
	 * 
	 * This method must be used for reading any values from subscriber profile, in preparation to reset back to old state when a certain
	 * part of the transaction fails from overall perspective. Preparation carried out here mandatory to assure consistency when rollback is
	 * invoked.
	 * 
	 * @return - true if successful; false if unsuccessful but gracefully handled.
	 * @throws FrameworkException
	 *             relevant to Fulfillment Engine.
	 */
	public abstract E prepareTransaction() throws FrameworkException;

	/**
	 * DO - Command Pattern representing the requisite fulfillment.
	 * 
	 * This method must be used for the actual fulfillment activity of the profile.
	 * 
	 * @return - true if successful; false if unsuccessful but gracefully handled.
	 * @throws FrameworkException
	 *             relevant to Fulfillment Engine.
	 */
	public abstract E fulfill() throws FrameworkException;

	/**
	 * UNDO - Command Pattern representing the reverse activity to DO Pattern.
	 * 
	 * This method must be used for rollback of the previous fulfillment activity. state info saved from preparation phase must be available
	 * and hence the invoker must
	 * 
	 * @return - true if successful; false if unsuccessful but gracefully handled.
	 * @throws FrameworkException
	 *             relevant to Fulfillment Engine.
	 */
	public abstract E revert() throws FrameworkException;

	@Override
	public E call() throws Exception {
		this.state = State.PROCESSING;

		E result;
		switch (mode) {
			case FULFILL:
				result = this.fulfill();
				break;
			case PREPARE:
				result = this.prepareTransaction();
				break;
			case REVERSE:
				result = this.revert();
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
		PREPARE,
		FULFILL,
		REVERSE;
	}

	enum State {
		WAITING,
		PROCESSING,
		DONE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (obj instanceof FulfillmentTask) {
			FulfillmentTask<E> other = (FulfillmentTask<E>) obj;
			if (mode != other.mode)
				return false;

			if (state != other.state)
				return false;
		} else {
			return false;
		}

		return true;
	}

}
