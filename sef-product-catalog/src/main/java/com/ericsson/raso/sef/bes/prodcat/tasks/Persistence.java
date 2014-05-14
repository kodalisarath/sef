package com.ericsson.raso.sef.bes.prodcat.tasks;


public final class Persistence<T> extends TransactionTask {
	private static final long serialVersionUID = -4255775283459415035L;

	private PersistenceMode mode = null;

	private T toSave = null;
	private String subscriberId = null;
	
	public Persistence(PersistenceMode mode, T entity, String subscriberId) {
		super(Type.CHARGING);
		this.mode = mode;
		this.toSave = entity;
		this.subscriberId = subscriberId;
	}
	

	
	
	public PersistenceMode getMode() {
		return mode;
	}




	public void setMode(PersistenceMode mode) {
		this.mode = mode;
	}




	public T getToSave() {
		return toSave;
	}




	public void setToSave(T toSave) {
		this.toSave = toSave;
	}




	public String getSubscriberId() {
		return subscriberId;
	}



	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	
	
}
