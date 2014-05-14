package com.ericsson.raso.sef.bes.prodcat.tasks;

import java.io.Serializable;

public enum PersistenceMode implements Serializable {
	QUERY,
	SAVE,
	REMOVE;
}
