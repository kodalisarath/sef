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

	private TaskType type = null;

	public TransactionTask(TaskType type) {
		this.type = type;
	}

	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
	}



	



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TransactionTask))
			return false;
		TransactionTask other = (TransactionTask) obj;
		if (type != other.type)
			return false;
		return true;
	}

	
	
	

}
