
package com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Operation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Operation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ParameterList" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}ParameterList" minOccurs="0"/>
 *         &lt;element name="OutputSpecification" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}OutputSpecification" minOccurs="0"/>
 *         &lt;element name="WarningInfo" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}WarningInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="modifier" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Operation", propOrder = {
    "parameterList",
    "outputSpecification",
    "warningInfo"
})
public class Operation {

    @XmlElement(name = "ParameterList")
    protected ParameterList parameterList;
    @XmlElement(name = "OutputSpecification")
    protected OutputSpecification outputSpecification;
    @XmlElement(name = "WarningInfo")
    protected List<WarningInfo> warningInfo;
    @XmlAttribute(name = "namespace")
    protected String namespace;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "modifier")
    protected String modifier;

    /**
     * Gets the value of the parameterList property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterList }
     *     
     */
    public ParameterList getParameterList() {
    	if(parameterList == null) {
    		parameterList = new ParameterList();
    	}
        return parameterList;
    }

    /**
     * Sets the value of the parameterList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterList }
     *     
     */
    public void setParameterList(ParameterList value) {
        this.parameterList = value;
    }

    /**
     * Gets the value of the outputSpecification property.
     * 
     * @return
     *     possible object is
     *     {@link OutputSpecification }
     *     
     */
    public OutputSpecification getOutputSpecification() {
        return outputSpecification;
    }

    /**
     * Sets the value of the outputSpecification property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutputSpecification }
     *     
     */
    public void setOutputSpecification(OutputSpecification value) {
        this.outputSpecification = value;
    }

    /**
     * Gets the value of the warningInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the warningInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWarningInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WarningInfo }
     * 
     * 
     */
    public List<WarningInfo> getWarningInfo() {
        if (warningInfo == null) {
            warningInfo = new ArrayList<WarningInfo>();
        }
        return this.warningInfo;
    }

    /**
     * Gets the value of the namespace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Sets the value of the namespace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNamespace(String value) {
        this.namespace = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the modifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * Sets the value of the modifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifier(String value) {
        this.modifier = value;
    }
    
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	if(namespace != null) {
    		builder.append(namespace + ":");
    	}
    	
    	if(name != null) {
    		builder.append(name + ":");
    	}
    	
    	if(modifier != null) {
    		builder.append(modifier);
    	}
    	
    	String opName = builder.toString();
    	if(opName.endsWith(":")) {
    		opName = opName.substring(0, opName.length() - 1);
    	}
    	
    	return opName;
    }

}
