package com.ericsson.raso.sef.core.db.service.internal;

import java.util.Collection;

import com.ericsson.raso.sef.core.db.mapper.CallingCircleMapper;
import com.ericsson.raso.sef.core.db.model.CallingCircle;
import com.ericsson.raso.sef.core.db.model.SmSequence;
import com.ericsson.raso.sef.core.db.service.CallingCircleDBService;

public class CallingCircleServiceImpl implements CallingCircleDBService {

	private CallingCircleMapper callingCircleMapper;
	
	public void setCallingCircleMapper(CallingCircleMapper callingCircleMapper) {
		this.callingCircleMapper = callingCircleMapper;
	}

	@Override
	public SmSequence callingCircleSequence(String rand) {

		return callingCircleMapper.callingCircleSequence(rand);
	}

	@Override
	public void createCallingCircle(CallingCircle callingCircle) {
		callingCircleMapper.createCallingCircle(callingCircle);
	}

	@Override
	public Collection<CallingCircle> findIdenticalCircles(CallingCircle callingCircle) {
		return callingCircleMapper.findIdenticalCircles(callingCircle);
	}

	@Override
	public Collection<CallingCircle> findAllMemberMemberCircles(CallingCircle callingCircle) {
		return callingCircleMapper.findAllMemberMemberCircles(callingCircle);
	}

	@Override
	public void updateCallingCircle(CallingCircle callingCircle) {
		callingCircleMapper.updateCallingCircle(callingCircle);		
	}

	@Override
	public void removeCallingCircle(long id) {
		callingCircleMapper.removeCallingCircle(id);
	}
}
