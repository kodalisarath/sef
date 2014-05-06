package com.ericsson.raso.sef.ruleengine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rule implements Action, Serializable {
	private static final long serialVersionUID = -6748802525923878678L;

	private String name = null;
	private LogicType logicGate = LogicType.AND;
	private List<Action> ruleset = new ArrayList<Action>();

	public Rule() { }

	public Rule(String name) {
		this.name = name;
	}

	public Rule(String name, ArrayList<Action> ruleset) {
		this.name = name;
		this.ruleset = ruleset;
	}

	public Rule(String name, LogicType logicGate, List<Action> ruleset) {
		this.name = name;
		this.logicGate = logicGate;
		this.ruleset = ruleset;
	}

	@Override
	public boolean execute() {
		switch (this.logicGate) {
		case AND:
			for (Action ruleUnit : ruleset) {
				if (!ruleUnit.execute()) {
					return false;
				}
			}
			return true;
		case NAND:
			for (Action ruleUnit : ruleset) {
				if (!ruleUnit.execute()) {
					return true;
				}
			}
			return false;
		case OR:
			for (Action ruleUnit : ruleset) {
				if (ruleUnit.execute()) {
					return true;
				}
			}
			return false;
		case NOR:
			for (Action ruleUnit : ruleset) {
				if (ruleUnit.execute()) {
					return false;
				}
			}
			return true;
		default:
			return false;
		}
	}

	public List<Action> getRuleset() {
		return ruleset;
	}

	public void setRuleset(List<Action> ruleset) {
		this.ruleset = ruleset;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LogicType getLogicGate() {
		return logicGate;
	}

	public void setLogicGate(LogicType logicGate) {
		this.logicGate = logicGate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ruleset == null) ? 0 : ruleset.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (this.getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (ruleset == null) {
			if (other.ruleset != null)
				return false;
		} else if (!ruleset.equals(other.ruleset))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String toString = "Rule [name=" + name + ", rules=(";
		Iterator<Action> rules = this.ruleset.iterator();
		while (rules.hasNext()) {
			toString += rules.next().toString();
			if (rules.hasNext())
				toString += " " + this.logicGate + " ";
		}
		
		toString += ")]";
		
		return toString;
	}

}
