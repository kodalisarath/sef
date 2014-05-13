
package com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Assignment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Assignment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Value" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}Value"/>
 *         &lt;element name="BooleanValue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ByteValue" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}ByteValue"/>
 *         &lt;element name="ShortValue" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}ShortValue"/>
 *         &lt;element name="IntValue" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}IntValue"/>
 *         &lt;element name="LongValue" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}LongValue"/>
 *         &lt;element name="FloatValue" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}FloatValue"/>
 *         &lt;element name="DoubleValue" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}DoubleValue"/>
 *         &lt;element name="DateValue" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="DateTimeValue" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="DurationValue" type="{http://www.w3.org/2001/XMLSchema}duration"/>
 *         &lt;element name="StringValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="HexBinaryValue" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/>
 *         &lt;element name="Base64BinaryValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="EnumerationValueValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SymbolicValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Operation" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}Operation"/>
 *       &lt;/choice>
 *       &lt;attribute name="variableName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Assignment", propOrder = {
    "value",
    "booleanValue",
    "byteValue",
    "shortValue",
    "intValue",
    "longValue",
    "floatValue",
    "doubleValue",
    "dateValue",
    "dateTimeValue",
    "durationValue",
    "stringValue",
    "hexBinaryValue",
    "base64BinaryValue",
    "enumerationValueValue",
    "symbolicValue",
    "operation"
})
public class Assignment {

    @XmlElement(name = "Value")
    protected Value value;
    @XmlElement(name = "BooleanValue")
    protected Boolean booleanValue;
    @XmlElement(name = "ByteValue")
    protected Integer byteValue;
    @XmlElement(name = "ShortValue")
    protected Integer shortValue;
    @XmlElement(name = "IntValue")
    protected Integer intValue;
    @XmlElement(name = "LongValue")
    protected Long longValue;
    @XmlElement(name = "FloatValue")
    protected Float floatValue;
    @XmlElement(name = "DoubleValue")
    protected Double doubleValue;
    @XmlElement(name = "DateValue")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateValue;
    @XmlElement(name = "DateTimeValue")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateTimeValue;
    @XmlElement(name = "DurationValue")
    protected Duration durationValue;
    @XmlElement(name = "StringValue")
    protected String stringValue;
    @XmlElement(name = "HexBinaryValue", type = String.class)
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    @XmlSchemaType(name = "hexBinary")
    protected byte[] hexBinaryValue;
    @XmlElement(name = "Base64BinaryValue")
    protected byte[] base64BinaryValue;
    @XmlElement(name = "EnumerationValueValue")
    protected String enumerationValueValue;
    @XmlElement(name = "SymbolicValue")
    protected String symbolicValue;
    @XmlElement(name = "Operation")
    protected Operation operation;
    @XmlAttribute(name = "variableName", required = true)
    protected String variableName;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Value }
     *     
     */
    public Value getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Value }
     *     
     */
    public void setValue(Value value) {
        this.value = value;
    }

    /**
     * Gets the value of the booleanValue property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBooleanValue() {
        return booleanValue;
    }

    /**
     * Sets the value of the booleanValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBooleanValue(Boolean value) {
        this.booleanValue = value;
    }

    /**
     * Gets the value of the byteValue property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getByteValue() {
        return byteValue;
    }

    /**
     * Sets the value of the byteValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setByteValue(Integer value) {
        this.byteValue = value;
    }

    /**
     * Gets the value of the shortValue property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getShortValue() {
        return shortValue;
    }

    /**
     * Sets the value of the shortValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setShortValue(Integer value) {
        this.shortValue = value;
    }

    /**
     * Gets the value of the intValue property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIntValue() {
        return intValue;
    }

    /**
     * Sets the value of the intValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIntValue(Integer value) {
        this.intValue = value;
    }

    /**
     * Gets the value of the longValue property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLongValue() {
        return longValue;
    }

    /**
     * Sets the value of the longValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLongValue(Long value) {
        this.longValue = value;
    }

    /**
     * Gets the value of the floatValue property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getFloatValue() {
        return floatValue;
    }

    /**
     * Sets the value of the floatValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setFloatValue(Float value) {
        this.floatValue = value;
    }

    /**
     * Gets the value of the doubleValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDoubleValue() {
        return doubleValue;
    }

    /**
     * Sets the value of the doubleValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDoubleValue(Double value) {
        this.doubleValue = value;
    }

    /**
     * Gets the value of the dateValue property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateValue() {
        return dateValue;
    }

    /**
     * Sets the value of the dateValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateValue(XMLGregorianCalendar value) {
        this.dateValue = value;
    }

    /**
     * Gets the value of the dateTimeValue property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateTimeValue() {
        return dateTimeValue;
    }

    /**
     * Sets the value of the dateTimeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateTimeValue(XMLGregorianCalendar value) {
        this.dateTimeValue = value;
    }

    /**
     * Gets the value of the durationValue property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getDurationValue() {
        return durationValue;
    }

    /**
     * Sets the value of the durationValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setDurationValue(Duration value) {
        this.durationValue = value;
    }

    /**
     * Gets the value of the stringValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the value of the stringValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStringValue(String value) {
        this.stringValue = value;
    }

    /**
     * Gets the value of the hexBinaryValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public byte[] getHexBinaryValue() {
        return hexBinaryValue;
    }

    /**
     * Sets the value of the hexBinaryValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHexBinaryValue(byte[] value) {
        this.hexBinaryValue = ((byte[]) value);
    }

    /**
     * Gets the value of the base64BinaryValue property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBase64BinaryValue() {
        return base64BinaryValue;
    }

    /**
     * Sets the value of the base64BinaryValue property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBase64BinaryValue(byte[] value) {
        this.base64BinaryValue = ((byte[]) value);
    }

    /**
     * Gets the value of the enumerationValueValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnumerationValueValue() {
        return enumerationValueValue;
    }

    /**
     * Sets the value of the enumerationValueValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnumerationValueValue(String value) {
        this.enumerationValueValue = value;
    }

    /**
     * Gets the value of the symbolicValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSymbolicValue() {
        return symbolicValue;
    }

    /**
     * Sets the value of the symbolicValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSymbolicValue(String value) {
        this.symbolicValue = value;
    }

    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link Operation }
     *     
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Operation }
     *     
     */
    public void setOperation(Operation value) {
        this.operation = value;
    }

    /**
     * Gets the value of the variableName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets the value of the variableName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariableName(String value) {
        this.variableName = value;
    }

}
