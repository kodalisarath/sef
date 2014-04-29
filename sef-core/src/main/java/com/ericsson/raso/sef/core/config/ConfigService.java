package com.ericsson.raso.sef.core.config;

import java.io.File;  
import java.util.List;  
import java.util.ArrayList;  

import javax.xml.bind.JAXBContext;  
import javax.xml.bind.JAXBException;  
import javax.xml.bind.Unmarshaller;   
import java.util.List;  
  
import javax.xml.bind.JAXBContext;  
import javax.xml.bind.JAXBException;  
import javax.xml.bind.Unmarshaller;  

public class ConfigService {
	
	 Config conf=null;

	public  void init() {
		// TODO Auto-generated method stub
		
		try {  
			String filename = "config.xml";
			String finalfile = "";
			String workingDir = System.getProperty("user.dir");
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
			Config conf = (Config) jaxbUnmarshaller.unmarshal(file);  
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
	
	public  ArrayList <Property> getProperty(Section section) {
		ArrayList<Property> propertyList = section.getProperty();
		return propertyList;
		
	}
	
	public String getValue(ArrayList sectionList,String sectionId,String propKey){
		Section section = null;
		for (int i=0; i < sectionList.size(); i++)
			{	if(((Section)sectionList.get(i)).getId()== sectionId)
					section = (Section)sectionList.get(i);
					break;
				  
			}
		String propertyValue="";
		//System.out.println(section);  
		ArrayList<Property> property = getProperty(section);
		//System.out.println("propertylist"+property); 
		//System.out.println("propKey"+propKey);  		
		for(Property propertyList:property)  {
				//System.out.println("propKey1"+propertyList.getKey());  		
        	if(propertyList.getKey().equalsIgnoreCase(propKey))
        		propertyValue = propertyList.getValue();
        		break;
        }
		return propertyValue;
	}	
}  

	
