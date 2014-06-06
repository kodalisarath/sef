package com.ericsson.raso.sef.bes.prodcat.tasks;


public final class Persistence<Object> extends TransactionTask {
	private static final long serialVersionUID = -4255775283459415035L;

	private PersistenceMode mode = null;

	private Object toSave = null;
	private String subscriberId = null;
	
	public Persistence(PersistenceMode mode, Object entity, String subscriberId) {
		super(TaskType.PERSIST);
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




	public Object getToSave() {
		return toSave;
	}




	public void setToSave(Object toSave) {
		this.toSave = toSave;
	}




	public String getSubscriberId() {
		return subscriberId;
	}



	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}




	@Override
	public String toString() {
		return "Persistence [mode=" + mode + ", toSave=" + toSave + ", subscriberId=" + subscriberId + "]";
	}

	
	
}
