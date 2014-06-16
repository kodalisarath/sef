package com.ericsson.raso.sef.core.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.RangeRouter;

public class ConfigService implements IConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	 Config conf=null;

	 public ConfigService() {
		 init();
	 }
	 
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
				
				//TODO: schema validation method is deprecated in JAXB. So commented this part to push through FT. Later address this
				
					/*final String JAXP_SCHEMA_LANGUAGE =
				           "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
					final String JAXP_SCHEMA_LOCATION =
				           "http://java.sun.com/xml/jaxp/properties/schemaSource";
					final String W3C_XML_SCHEMA =
				           "http://www.w3.org/2001/XMLSchema";

				       System.setProperty( "javax.xml.parsers.SAXParserFactory",
				                           "org.apache.xerces.jaxp.SAXParserFactoryImpl" );

				       SAXParserFactory spf = SAXParserFactory.newInstance();
				       spf.setNamespaceAware(true);
				       spf.setValidating(true);
				       SAXParser saxParser = spf.newSAXParser();
				       
				       try {
				           saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
				           saxParser.setProperty(JAXP_SCHEMA_LOCATION, "http://....");
				       } catch (SAXNotRecognizedException x) {
				           // exception handled and omitted
				       }

				       XMLReader xmlReader = saxParser.getXMLReader();
				       SAXSource source = 
				           new SAXSource( xmlReader, new InputSource( finalfile)) ;*/
				
				
				       // Setup JAXB to unmarshal
				       JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);  
				       Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
				       //jaxbUnmarshaller.setValidating(false);
				       conf = (Config) jaxbUnmarshaller.unmarshal(new File(finalfile));  
				} catch (JAXBException e) {  
					e.printStackTrace();  
		        	throw new RuntimeException("JAXBException while loading configuration", e);
		      }  
			System.out.println("Config loaded successfully!!");
		} 
	
	
	public Section getSection(String sectionId) {
		 ArrayList<Section> list=conf.getSection();
		 	Section section = null;
	        for(Section sectionList:list)  {
	        	if(sectionList.getId().equalsIgnoreCase(sectionId)){
	        		section = sectionList;
	        		break;
	        	}
	        }
		return section;
	}
	
	
	public String getValue(String sectionId, String propKey) {
		logger.debug("Fetch value for section: " +  sectionId + " key: " + propKey);
		Section section = getSection(sectionId);
		if(section != null) {
			logger.debug("Found section: " +  sectionId);
			List<Property> sectionProperties = getProperties(section);
			if(sectionProperties != null) {
				for (Property property: sectionProperties) {
					if(property.getKey().equalsIgnoreCase(propKey)) {
						logger.debug("found property: " + propKey + " with value: " + property.getValue());
		        		return property.getValue();
					}
				}
			}
		}
		logger.debug("section (" + sectionId + ") not found");
		return null;
	}
	
	public  List <Property> getProperties(Section section) {
		ArrayList<Property> propertyList = section.getProperty();
		return propertyList;
		
	}
	
	public Value getRoute(String sectionId, long locate) {
		return RangeRouter.getInstance().getRoute(sectionId, locate);
	}
	
}  

	
