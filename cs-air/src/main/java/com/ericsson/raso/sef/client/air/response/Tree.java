package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class Tree extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public Tree(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private String treeDefinedFieldName;
	private String treeDefinedFieldType;
	private String treeDefinedFieldValue;

	public String getTreeDefinedFieldName() {
		if (treeDefinedFieldName == null) {
			treeDefinedFieldName = getParam("treeDefinedFieldName", String.class);
		}
		return treeDefinedFieldName;
	}

	public String getTreeDefinedFieldType() {
		if (treeDefinedFieldType == null) {
			treeDefinedFieldType = getParam("treeDefinedFieldType", String.class);
		}
		return treeDefinedFieldType;
	}

	public String getTreeDefinedFieldValue() {
		if (treeDefinedFieldValue == null) {
			treeDefinedFieldValue = getParam("treeDefinedFieldValue", String.class);
		}
		return treeDefinedFieldValue;
	}

}
