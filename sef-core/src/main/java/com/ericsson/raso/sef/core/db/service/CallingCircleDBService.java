package com.ericsson.raso.sef.core.db.service;

import java.util.Collection;

import com.ericsson.raso.sef.core.db.model.CallingCircle;
import com.ericsson.raso.sef.core.db.model.SmSequence;

public interface CallingCircleDBService {

	SmSequence callingCircleSequence(String rand);

	void createCallingCircle(CallingCircle callingCircle);

	Collection<CallingCircle> findIdenticalCircles(CallingCircle callingCircle);
	
	Collection<CallingCircle> findAllMemberMemberCircles(CallingCircle callingCircle);
	
	void updateCallingCircle(CallingCircle callingCircle);

	void removeCallingCircle(long id);
}
