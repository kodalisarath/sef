package com.ericsson.raso.sef.bes.prodcat.entities;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ericsson.raso.sef.bes.prodcat.tasks.Charging;
import com.ericsson.raso.sef.bes.prodcat.tasks.Fulfillment;
import com.ericsson.raso.sef.bes.prodcat.tasks.Notification;
import com.ericsson.raso.sef.core.Task;

public final class EventProfile implements Serializable {
	private static final long serialVersionUID = 3520970207251399960L;

	private TreeMap<Integer, Object> profile = null;
	private int currentSequence = 0;
	private AtomicInteger sequence = new AtomicInteger(0);

	/**
	 * This method is defined to be generic to accept both passive and active events...
	 * 
	 * <strong>Passive</strong events can be simple Strings, Numbers or complex Pojos which can be used by default handlers in the BES framework.
	 * 
	 * <strong>Active</strong> events can be a {@link Task} that can be executed when this event needs to be handled. It is possible to extend/ define custom
	 * tasks and present as a handler to this event.
	 * 
	 * Internally, a sequence is managed to assign the handler. the sequence of the profile is persisted to the susbcriber profile and hence
	 * internal to offer execution, the subscription will be able to execute the next event automatically. It is preferred to limit the
	 * argument to known Pojos that Offering Catalog can talk to Transaction Engine such as {@link Charging}, {@link Fulfillment},
	 * {@link Notification} types only.
	 * 
	 * @param event
	 */
	public <T> void addEvent(T event) {
		if (this.profile == null) {
			this.profile = new TreeMap<Integer, Object>();
		} 
		this.profile.put(sequence.incrementAndGet(), event);
	}
	
	/**
	 * This method is defined to be generic to accept both passive and active events...
	 * 
	 * <strong>Passive</strong events can be simple Strings, Numbers or complex Pojos which can be used by default handlers in the BES framework.
	 * 
	 * <strong>Active</strong> events can be a {@link Task} that can be executed when this event needs to be handled. It is possible to extend/ define custom
	 * tasks and present as a handler to this event.
	 * 
	 * Internally, a sequence is managed to assign the handler. the sequence of the profile is persisted to the susbcriber profile and hence
	 * internal to offer execution, the subscription will be able to execute the next event automatically. It is preferred to limit the
	 * argument to known Pojos that Offering Catalog can talk to Transaction Engine such as {@link Charging}, {@link Fulfillment},
	 * {@link Notification} types only.
	 * 
	 * @param event
	 * @see #addEvent(Object)
	 */
	public <T> void replaceEvent(int sequence, T event) {
		if (this.profile != null)
			if (this.profile.containsKey(sequence))
				this.profile.put(sequence, event);
	}
	
	public void removeEvent(int sequence) {
		if (this.profile != null)
			if (this.profile.containsKey(sequence))
				this.profile.remove(sequence);
	}
	
	
	public Object getEvent(int sequence) {
		if (this.profile != null)
			if (this.profile.containsKey(sequence))
				return this.profile.get(sequence);
		
		return null;
	}

}
