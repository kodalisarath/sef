package com.ericsson.raso.sef.core.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ConfigService {
	
	 Config conf=null;

	public  void init() {
		try {  
			String filename = "config.xml";
			String finalfile = "";
			String workingDir = System.getenv("CONFIG_HOME");
			String your_os = System.getProperty("os.name").toLowerCase();
			if(your_os.indexOf("win") >= 0){
				finalfile = workingDir + "\\" + filename;
			}else if(your_os.indexOf( "nix") >=0 || your_os.indexOf( "nux") >=0){
				finalfile = workingDir + "/" + filename;
			}else{
				finalfile = workingDir + "{others}" + filename;
			}
						
	        File file = new File(finalfile);  
			JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);  
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			conf = (Config) jaxbUnmarshaller.unmarshal(file);  
			} catch (JAXBException e) {  
	        e.printStackTrace();  
	      }  
	} 
	
	
	public Section getSection(String sectionId) {
		 ArrayList<Section> list=conf.getSection();
		 	Section section = null;
	        for(Section sectionList:list)  {
	        	if(sectionList.getId() == sectionId){
	        		break;
	        	}
	        }
		return section;
	}
	
	
	public String getValue(String sectionId, String propKey) {
	
		Section section = getSection(sectionId);
		if(section != null) {
			List<Property> sectionProperties = getProperties(section);
			if(sectionProperties != null) {
				for (Property property: sectionProperties) {
					if(property.getKey().equalsIgnoreCase(propKey))
		        		return property.getValue();
				}
			}
		}
		return null;
	}
	
	public  ArrayList <Property> getProperties(Section section) {
		ArrayList<Property> propertyList = section.getProperty();
		return propertyList;
		
	}
	
}  

	
