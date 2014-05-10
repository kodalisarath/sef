package com.ericsson.raso.sef.bes.prodcat.entities;

public class Cost extends MonetaryUnit {
		private static final long serialVersionUID = 5063537173639921252L;

		public Cost(String iso4217CurrencyCode, long amount) {
			super(iso4217CurrencyCode, amount);
		}


		
}
