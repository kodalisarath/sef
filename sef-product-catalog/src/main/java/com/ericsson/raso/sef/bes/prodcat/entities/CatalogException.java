package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.core.FrameworkException;

public final class CatalogException extends FrameworkException {
	private static final long serialVersionUID = 9155910233608721116L;

	public CatalogException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public CatalogException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CatalogException(String arg0) {
		super(arg0);
	}

	public CatalogException(Throwable arg0) {
		super(arg0);
	}

}
