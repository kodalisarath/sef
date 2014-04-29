package com.ericsson.raso.sef.core.config;

import java.util.List;

public interface IConfig {

	public  void init();
	public Section getSection(String sectionId);
	public String getValue(String sectionId, String propKey);
	public  List<Property> getProperties(Section section);
}
