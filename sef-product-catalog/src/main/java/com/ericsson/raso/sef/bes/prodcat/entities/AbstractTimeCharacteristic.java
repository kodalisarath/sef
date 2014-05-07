package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;

public abstract class AbstractTimeCharacteristic implements Serializable {
	private static final long serialVersionUID = 4051790660183459432L;
	protected static final long DISCOVERY_MODE = -1L;

	private Type type = null;
	private long activationTime = DISCOVERY_MODE;

	public AbstractTimeCharacteristic(Type type) {
		this.type = type;
	}

	/**
	 * Returns the expiry date & time in milliseconds since epoch (Java epoch is
	 * January 1, 1970, 00:00:00 GMT).
	 * 
	 * Expected behaviour of ths method is based on the
	 * <code>{@link Type}</code>... 
	 * 
	 * <li>INFINITE - will return -1 to indicate there
	 * is no expiry
	 *  
	 * <li>HOURS, DAYS, DATE - will return the expiry in millis after
	 * flattening the date parts, when in discovery mode 
	 * 
	 * <li>HOURS, DAYS, DATE -
	 * will return the absolute timestamp of expiry in millis after flattening
	 * the date parts, when in lifecycle mode
	 * 
	 * 
	 * @return
	 */

	public abstract long getExpiryTimeInMillis();

	public Type getType() {
		return type;
	}
	
	
	public long getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(long activationTime) {
		this.activationTime = activationTime;
	}



	enum Type {
		INFINITE, HOURS, DAYS, DATE,
	}

}
