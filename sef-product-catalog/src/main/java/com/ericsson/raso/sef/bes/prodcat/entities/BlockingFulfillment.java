package com.ericsson.raso.sef.bes.prodcat.entities;

import java.util.Map;

public abstract class BlockingFulfillment<E> extends FulfillmentProfile<E, Map<String, String>> {
		private static final long serialVersionUID = -4516816984715067475L;

		protected BlockingFulfillment(String name) {
			super(name);
		}


}
