
package com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutStructParamId complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutStructParamId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="SimpleParam" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}OutSimpleParamId"/>
 *         &lt;element name="StructParam" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}OutStructParamId"/>
 *       &lt;/choice>
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="index" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}Index" />
 *       &lt;attribute name="formatString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutStructParamId", propOrder = {
    "simpleParamOrStructParam"
})
public class OutStructParamId {

    @XmlElements({
        @XmlElement(name = "StructParam", type = OutStructParamId.class),
        @XmlElement(name = "SimpleParam", type = OutSimpleParamId.class)
    })
    protected List<Object> simpleParamOrStructParam;
    @XmlAttribute(name = "namespace")
    protected String namespace;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "index")
    protected Long index;
    @XmlAttribute(name = "formatString")
    protected String formatString;

    /**
     * Gets the value of the simpleParamOrStructParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the simpleParamOrStructParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSimpleParamOrStructParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OutStructParamId }
     * {@link OutSimpleParamId }
     * 
     * 
     */
    public List<Object> getSimpleParamOrStructParam() {
        if (simpleParamOrStructParam == null) {
            simpleParamOrStructParam = new ArrayList<Object>();
        }
        return this.simpleParamOrStructParam;
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
     * Gets the value of the index property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIndex(Long value) {
        this.index = value;
    }

    /**
     * Gets the value of the formatString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatString() {
        return formatString;
    }

    /**
     * Sets the value of the formatString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatString(String value) {
        this.formatString = value;
    }

}
