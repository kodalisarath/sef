package com.ericsson.raso.sef.bes.prodcat.entities;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public final class PhysicalAsset extends AssetResource {
		private static final long serialVersionUID = -1493873269032558907L;

		public PhysicalAsset(String name) {
			super(name);
		}

		@Override
		public boolean addFulfillmentProfile(FulfillmentProfile<?> profile) throws CatalogException {
			if (profile instanceof AsynchronousFulfillment)
				return super.addFulfillmentProfile(profile);
			
			throw new CatalogException("Physical Asset Resource cannot be fulfilled thru BlockingFulfillment");
		}
		
		
	
}
