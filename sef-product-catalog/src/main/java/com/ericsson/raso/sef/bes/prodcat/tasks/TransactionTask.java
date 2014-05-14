package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.io.Serializable;

/**
 * Base class for all tasks that execute in Transaction Engine to signify interaction and integration between Transaction Engine and Offer Catalog..
 * 
 * @author esatnar
 *
 */
public abstract class TransactionTask implements Serializable {
	private static final long serialVersionUID = 82627494140719220L;

	private Status status = Status.WAITING;
	private Type type = null;

	public TransactionTask(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}



	enum Status implements Serializable {
		WAITING,
		PROCESSING,
		DONE;
	}
	
	enum Type {
		CHARGING,
		FULFILLMENT,
		NOTIFICATION,
		FUTURE,
		PERSIST;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof TransactionTask))
			return false;
		
		TransactionTask other = (TransactionTask) obj;
		if (status != other.status)
			return false;
		
		return true;
	}
	
	

}
