package com.ericsson.raso.sef.core.config;

import java.util.List;  
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  
import javax.xml.bind.annotation.XmlAccessorType; 
import javax.xml.bind.annotation.XmlAccessType; 


@XmlRootElement(name="config")
public class Config {
	
	private ArrayList<Section>section = new ArrayList<Section>() ;  
	public Config() {}  
	public Config(ArrayList<Section> section) {  
	    super();  
	    this.section = section;  
	} 
	
	
	public ArrayList<Section> getSection() {
		System.out.println("sdffsdf");
	    return section;  
	} 
		
	@XmlElement(name="section")
	public void setSection(ArrayList<Section> section) {  
	System.out.println("sdffsdf set");
		
	    this.section = section;  
	}  
	  

}
