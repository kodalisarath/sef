package com.ericsson.raso.sef.bes.prodcat.tasks;

import com.ericsson.raso.sef.bes.prodcat.entities.MonetaryUnit;
import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.ruleengine.ExternDataUnitTask;

public class CostInputTask extends ExternDataUnitTask<MonetaryUnit> {
	private static final long serialVersionUID = -1303009231551678538L;

	private MonetaryUnit cost = null;
	
	public CostInputTask(MonetaryUnit cost) {
		this.cost = cost;
	}

	@Override
	public MonetaryUnit execute() throws FrameworkException {
		return this.cost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;
		
		if (!(obj instanceof CostInputTask))
			return false;
		
		CostInputTask other = (CostInputTask) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "<CostInputTask cost='" + cost + "' />";
	}
	
	

	
}
