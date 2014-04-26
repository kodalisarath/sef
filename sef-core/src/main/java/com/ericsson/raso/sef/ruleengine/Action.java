package com.ericsson.raso.sef.ruleengine;

/**
 * @author esatnar
 * 
 *         This is a specific interface for the rule engine semantics. The
 *         custom rule engine is designed over ECA (Event-Condition-Action)
 *         Architectural Pattern.
 * 
 */

public interface Action {

	public boolean execute();

}
