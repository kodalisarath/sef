package com.ericsson.raso.sef.notification.workflows.promo;

 
public class Promo {

	private String id;
	private CallingCircleType type;
	private String associatedPromo;
	private boolean openCloudRegistration;
	private String ucip;
	private String successEventId;
	private Integer smRelationShip;
	private Integer mmRelationShip;
	private Integer msRelationShip;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CallingCircleType getType() {
		return type;
	}
	public void setType(CallingCircleType type) {
		this.type = type;
	}
	public String getAssociatedPromo() {
		return associatedPromo;
	}
	public void setAssociatedPromo(String associatedPromo) {
		this.associatedPromo = associatedPromo;
	}
	public boolean isOpenCloudRegistration() {
		return openCloudRegistration;
	}
	public void setOpenCloudRegistration(boolean openCloudRegistration) {
		this.openCloudRegistration = openCloudRegistration;
	}
	public String getUcip() {
		return ucip;
	}
	public void setUcip(String ucip) {
		this.ucip = ucip;
	}
	public String getSuccessEventId() {
		return successEventId;
	}
	public void setSuccessEventId(String successEventId) {
		this.successEventId = successEventId;
	}
	public Integer getSmRelationShip() {
		return smRelationShip;
	}
	public void setSmRelationShip(Integer smRelationShip) {
		this.smRelationShip = smRelationShip;
	}
	public Integer getMmRelationShip() {
		return mmRelationShip;
	}
	public void setMmRelationShip(Integer mmRelationShip) {
		this.mmRelationShip = mmRelationShip;
	}
	public Integer getMsRelationShip() {
		return msRelationShip;
	}
	public void setMsRelationShip(Integer msRelationShip) {
		this.msRelationShip = msRelationShip;
	}
	
	
}
