package com.ericsson.raso.sef.core.config;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="section")
public class Section {
	
	private String id;  
	private String description;  
	private ArrayList<Property> property = new ArrayList<Property>() ; 
	private Router router;
	
	public Section() {}  
	
	public Section(String id, String description, ArrayList<Property> property) {  
	    super();  
	    this.id = id;  
	    this.description = description;  
	    this.property = property;    
	}  
	
	public String getId() {  
	    return id;  
	}  
	
	@XmlAttribute
	public void setId(String id) {  
	    this.id = id;  
	}
	
	public String getDescription() {  
	    return description;  
	}  
	
	@XmlAttribute
	public void setDescription(String description) {  
	    this.description = description;  
	}  
	
	
	public ArrayList<Property> getProperty() {  
	    return property;  
	}  
	
	@XmlElement(name="property")
	public void setProperty(ArrayList<Property> property) {  
	    this.property = property;  
	}

	public Router getRouter() {
		return router;
	}
	
	@XmlElement(name="router")
	public void setRouter(Router router) {
		this.router = router;
	}  

}
