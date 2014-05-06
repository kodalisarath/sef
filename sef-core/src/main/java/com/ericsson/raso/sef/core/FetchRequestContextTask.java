package com.ericsson.raso.sef.core;

import java.io.Serializable;

import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;


public final class FetchRequestContextTask extends ExternDataUnitTask<ReqContext> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String requestId;
	
	private ReqContext requestContext;
	
	public FetchRequestContextTask() {
	}
	
    public FetchRequestContextTask(String requestId) {
		this.requestId = requestId;
	}

	@Override
    public ReqContext execute() throws FrameworkException {
    	//TODO: update when Distributed Cache Services is available...... requestContext = new DistributedRequestContext(requestId);
        return requestContext;
    }

	@Override
	public int hashCode() {
		return -591365283;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		if (this.hashCode() != obj.hashCode())
			return false;

		return true;
	}
	
	@Override
	public String toString() {
		//TODO: Cleanup SMCORE project first...... return "FetchRequestContextTask [requestId=" + requestContext.get(SmConstants.REQUEST_ID) + "]";
		return null;
	}
}
