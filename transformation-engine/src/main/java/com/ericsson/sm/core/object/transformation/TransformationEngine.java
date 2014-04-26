package com.ericsson.sm.core.object.transformation;

import java.util.List;

import org.dozer.DozerBeanMapper;

public class TransformationEngine {
	
	private DozerBeanMapper mapper = new DozerBeanMapper();
	private static TransformationEngine instance = null;
	
	//TODO: Add logger once logger service is ready

	private TransformationEngine() {
		
	}
	
	//TODO: Remove if singleton is implemented through spring IOC
	public static synchronized TransformationEngine getInstance() {
		if (instance == null) {
			instance = new TransformationEngine();
		}
		
		return instance;
	}
	
	public void setMappings(List<String> mappingFiles) {
		mapper.setMappingFiles(mappingFiles);
	}
	
	public Object transform(Object input, Class<?> clazz) {
		Object output = mapper.map(input, clazz);
		return output;
	}
	

	public void enrich(Object input, Object output) {
		mapper.map(input, output);
	}
	
}
