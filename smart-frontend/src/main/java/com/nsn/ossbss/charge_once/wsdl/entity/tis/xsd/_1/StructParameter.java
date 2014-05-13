
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
 * <p>Java class for StructParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StructParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="Parameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}Parameter"/>
 *         &lt;element name="BooleanParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}BooleanParameter"/>
 *         &lt;element name="ByteParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}ByteParameter"/>
 *         &lt;element name="ShortParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}ShortParameter"/>
 *         &lt;element name="IntParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}IntParameter"/>
 *         &lt;element name="LongParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}LongParameter"/>
 *         &lt;element name="FloatParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}FloatParameter"/>
 *         &lt;element name="DoubleParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}DoubleParameter"/>
 *         &lt;element name="DateParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}DateParameter"/>
 *         &lt;element name="DateTimeParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}DateTimeParameter"/>
 *         &lt;element name="DurationParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}DurationParameter"/>
 *         &lt;element name="StringParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}StringParameter"/>
 *         &lt;element name="HexBinaryParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}HexBinaryParameter"/>
 *         &lt;element name="Base64BinaryParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}Base64BinaryParameter"/>
 *         &lt;element name="EnumerationValueParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}EnumerationValueParameter"/>
 *         &lt;element name="SymbolicParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}SymbolicParameter"/>
 *         &lt;element name="VariableParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}VariableParameter"/>
 *         &lt;element name="StructParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}StructParameter"/>
 *         &lt;element name="ListParameter" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}ListParameter"/>
 *       &lt;/choice>
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="index" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}Index" />
 *       &lt;attribute name="modifier" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="notification" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StructParameter", propOrder = {
    "parameterOrBooleanParameterOrByteParameter"
})
public class StructParameter {

    @XmlElements({
        @XmlElement(name = "DateParameter", type = DateParameter.class),
        @XmlElement(name = "DoubleParameter", type = DoubleParameter.class),
        @XmlElement(name = "ShortParameter", type = ShortParameter.class),
        @XmlElement(name = "StringParameter", type = StringParameter.class),
        @XmlElement(name = "Base64BinaryParameter", type = Base64BinaryParameter.class),
        @XmlElement(name = "FloatParameter", type = FloatParameter.class),
        @XmlElement(name = "ListParameter", type = ListParameter.class),
        @XmlElement(name = "DurationParameter", type = DurationParameter.class),
        @XmlElement(name = "IntParameter", type = IntParameter.class),
        @XmlElement(name = "Parameter", type = Parameter.class),
        @XmlElement(name = "HexBinaryParameter", type = HexBinaryParameter.class),
        @XmlElement(name = "StructParameter", type = StructParameter.class),
        @XmlElement(name = "VariableParameter", type = VariableParameter.class),
        @XmlElement(name = "EnumerationValueParameter", type = EnumerationValueParameter.class),
        @XmlElement(name = "BooleanParameter", type = BooleanParameter.class),
        @XmlElement(name = "DateTimeParameter", type = DateTimeParameter.class),
        @XmlElement(name = "SymbolicParameter", type = SymbolicParameter.class),
        @XmlElement(name = "ByteParameter", type = ByteParameter.class),
        @XmlElement(name = "LongParameter", type = LongParameter.class)
    })
    protected List<Object> parameterOrBooleanParameterOrByteParameter;
    @XmlAttribute(name = "namespace")
    protected String namespace;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "index")
    protected Long index;
    @XmlAttribute(name = "modifier")
    protected String modifier;
    @XmlAttribute(name = "notification")
    protected String notification;

    /**
     * Gets the value of the parameterOrBooleanParameterOrByteParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterOrBooleanParameterOrByteParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterOrBooleanParameterOrByteParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DateParameter }
     * {@link DoubleParameter }
     * {@link ShortParameter }
     * {@link StringParameter }
     * {@link Base64BinaryParameter }
     * {@link FloatParameter }
     * {@link ListParameter }
     * {@link DurationParameter }
     * {@link IntParameter }
     * {@link Parameter }
     * {@link HexBinaryParameter }
     * {@link StructParameter }
     * {@link VariableParameter }
     * {@link EnumerationValueParameter }
     * {@link BooleanParameter }
     * {@link DateTimeParameter }
     * {@link SymbolicParameter }
     * {@link ByteParameter }
     * {@link LongParameter }
     * 
     * 
     */
    public List<Object> getParameterOrBooleanParameterOrByteParameter() {
        if (parameterOrBooleanParameterOrByteParameter == null) {
            parameterOrBooleanParameterOrByteParameter = new ArrayList<Object>();
        }
        return this.parameterOrBooleanParameterOrByteParameter;
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

    /**
     * Gets the value of the notification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotification() {
        return notification;
    }

    /**
     * Sets the value of the notification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotification(String value) {
        this.notification = value;
    }

}
