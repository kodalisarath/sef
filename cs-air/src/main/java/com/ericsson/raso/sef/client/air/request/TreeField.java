package com.ericsson.raso.sef.client.air.request;

public class TreeField extends NativeAirRequest{

	private String treeDefinedFieldName;
	private String treeDefinedFieldType;
	private String treeDefinedFieldValue;
	
	public String getTreeDefinedFieldName() {
		return treeDefinedFieldName;
	}
	public void setTreeDefinedFieldName(String treeDefinedFieldName) {
		this.treeDefinedFieldName = treeDefinedFieldName;
		addParam("treeDefinedFieldName",this.treeDefinedFieldName);
	}
	public String getTreeDefinedFieldType() {
		return treeDefinedFieldType;
	}
	public void setTreeDefinedFieldType(String treeDefinedFieldType) {
		this.treeDefinedFieldType = treeDefinedFieldType;
		addParam("treeDefinedFieldType",this.treeDefinedFieldType);
	}
	public String getTreeDefinedFieldValue() {
		return treeDefinedFieldValue;
	}
	public void setTreeDefinedFieldValue(String treeDefinedFieldValue) {
		this.treeDefinedFieldValue = treeDefinedFieldValue;
		addParam("treeDefinedFieldValue",this.treeDefinedFieldValue);
	}
	
}
