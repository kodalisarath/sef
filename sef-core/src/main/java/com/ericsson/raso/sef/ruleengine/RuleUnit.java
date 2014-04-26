package com.ericsson.raso.sef.ruleengine;

import java.io.Serializable;


public class RuleUnit implements Serializable {
	
	private static final long serialVersionUID = -5113378282713251141L;

	//TODO: private static transient Logger logger = LoggerFactory.getLogger(RuleUnit.class);

	private String ruleUnitName = null;
	private ExternDataUnitTask<?> loadDataUnit = null;
	private Condition condition = null;
	private String baseObjectName;
	private String ruleVariable;
	private String schemaName;
	//private RuleAttribute ruleAttribute;

	public RuleUnit(String name) {
		ruleUnitName = name;
	}

	public RuleUnit(String name, ExternDataUnitTask<?> loadDataUnit, Condition evaluate) {
		if (!(name != null && name.length() > 0)) {
			throw new IllegalArgumentException("Found 'null' arguments for ruleName");
		}

		if (loadDataUnit == null || evaluate == null) {
			throw new IllegalArgumentException("Found 'null' arguments for loadUnit or condition");
		}

		this.ruleUnitName = name;
		this.loadDataUnit = loadDataUnit;
		this.condition = evaluate;
	}

	public RuleUnit(String ruleUnitName, ExternDataUnitTask<?> loadDataUnit, Condition condition,
			String baseObjectName, String ruleVariable) {
		this.ruleUnitName = ruleUnitName;
		this.loadDataUnit = loadDataUnit;
		this.condition = condition;
		this.baseObjectName = baseObjectName;
		this.ruleVariable = ruleVariable;
	}

	public boolean execute() {
		Object operand = null;
		try {
			//logger.debug("Executing rule Unit: " + this);
			operand = this.loadDataUnit.execute();
			//logger.debug("Operand value : " + operand);
		} catch (Exception e) {
			//logger.debug("Unable to fetch the data load unit for Rule. Cause: " + e.toString());
			return false;
		}

		try {
			//logger.debug("Evaluating condition");
			boolean result = this.condition.evaluate(operand);
			//logger.debug("Rule unit execution Result: " + result);
			return result;
		} catch (RuleFailedException e) {
			//logger.debug(this.toString() + " failed. Cause: " + e.toString());
			return false;
		}
	}

	public Condition getEvaluate() {
		return condition;
	}

	public void setEvaluate(Condition evaluate) {
		this.condition = evaluate;
	}

	public String getRuleName() {
		return ruleUnitName;
	}

	public void setRuleName(String ruleName) {
		this.ruleUnitName = ruleName;
	}

	public String getBaseObjectName() {
		return baseObjectName;
	}

	public void setBaseObjectName(String baseObjectName) {
		this.baseObjectName = baseObjectName;
	}

	public String getRuleVariable() {
		return ruleVariable;
	}

	public void setRuleVariable(String ruleVariable) {
		this.ruleVariable = ruleVariable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseObjectName == null) ? 0 : baseObjectName.hashCode());
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((loadDataUnit == null) ? 0 : loadDataUnit.hashCode());
		result = prime * result + ((ruleUnitName == null) ? 0 : ruleUnitName.hashCode());
		result = prime * result + ((ruleVariable == null) ? 0 : ruleVariable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleUnit other = (RuleUnit) obj;
		if (baseObjectName == null) {
			if (other.baseObjectName != null)
				return false;
		} else if (!baseObjectName.equals(other.baseObjectName))
			return false;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (loadDataUnit == null) {
			if (other.loadDataUnit != null)
				return false;
		} else if (!loadDataUnit.equals(other.loadDataUnit))
			return false;
		if (ruleUnitName == null) {
			if (other.ruleUnitName != null)
				return false;
		} else if (!ruleUnitName.equals(other.ruleUnitName))
			return false;
		if (ruleVariable == null) {
			if (other.ruleVariable != null)
				return false;
		} else if (!ruleVariable.equals(other.ruleVariable))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RuleUnit: " + this.ruleUnitName + " [" + baseObjectName + "." + ruleVariable + " " + condition + "]";
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public ExternDataUnitTask<?> getLoadDataUnit() {
		return loadDataUnit;
	}

	public void setLoadDataUnit(ExternDataUnitTask<?> loadDataUnit) {
		this.loadDataUnit = loadDataUnit;
	}

	
}
