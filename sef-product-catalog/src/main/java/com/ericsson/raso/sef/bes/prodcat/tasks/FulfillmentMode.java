package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.io.Serializable;

public enum FulfillmentMode implements Serializable {
	PREPARE,
	FULFILL,
	REVERSE,
	CANCEL, 
	QUERY;
}

