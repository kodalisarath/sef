package com.ericsson.raso.sef.bes.prodcat.entities;

public final class PhysicalAsset extends AssetResource {
		private static final long serialVersionUID = -1493873269032558907L;

		public PhysicalAsset(String name) {
			super(name);
		}

//		public boolean addFulfillmentProfile(String profile) throws CatalogException {
//			if (profile instanceof AsynchronousFulfillment)
//				return super.addFulfillmentProfile(profile);
//			
//			throw new CatalogException("Physical Asset Resource cannot be fulfilled thru BlockingFulfillment");
//		}
		
		
	
}
