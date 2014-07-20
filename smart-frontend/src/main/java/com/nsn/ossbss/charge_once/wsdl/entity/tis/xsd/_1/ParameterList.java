
package com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParameterList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParameterList">
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
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParameterList", propOrder = {
    "parameterOrBooleanParameterOrByteParameter"
})
public class ParameterList {

    @XmlElements({
        @XmlElement(name = "IntParameter", type = IntParameter.class),
        @XmlElement(name = "StringParameter", type = StringParameter.class),
        @XmlElement(name = "StructParameter", type = StructParameter.class),
        @XmlElement(name = "SymbolicParameter", type = SymbolicParameter.class),
        @XmlElement(name = "ByteParameter", type = ByteParameter.class),
        @XmlElement(name = "Base64BinaryParameter", type = Base64BinaryParameter.class),
        @XmlElement(name = "BooleanParameter", type = BooleanParameter.class),
        @XmlElement(name = "DateTimeParameter", type = DateTimeParameter.class),
        @XmlElement(name = "DateParameter", type = DateParameter.class),
        @XmlElement(name = "LongParameter", type = LongParameter.class),
        @XmlElement(name = "DurationParameter", type = DurationParameter.class),
        @XmlElement(name = "DoubleParameter", type = DoubleParameter.class),
        @XmlElement(name = "VariableParameter", type = VariableParameter.class),
        @XmlElement(name = "EnumerationValueParameter", type = EnumerationValueParameter.class),
        @XmlElement(name = "ShortParameter", type = ShortParameter.class),
        @XmlElement(name = "FloatParameter", type = FloatParameter.class),
        @XmlElement(name = "HexBinaryParameter", type = HexBinaryParameter.class),
        @XmlElement(name = "Parameter", type = Parameter.class),
        @XmlElement(name = "ListParameter", type = ListParameter.class)
    })
    protected List<Object> parameterOrBooleanParameterOrByteParameter;

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
     * {@link IntParameter }
     * {@link StringParameter }
     * {@link StructParameter }
     * {@link SymbolicParameter }
     * {@link ByteParameter }
     * {@link Base64BinaryParameter }
     * {@link BooleanParameter }
     * {@link DateTimeParameter }
     * {@link DateParameter }
     * {@link LongParameter }
     * {@link DurationParameter }
     * {@link DoubleParameter }
     * {@link VariableParameter }
     * {@link EnumerationValueParameter }
     * {@link ShortParameter }
     * {@link FloatParameter }
     * {@link HexBinaryParameter }
     * {@link Parameter }
     * {@link ListParameter }
     * 
     * 
     */
    public List<Object> getParameterOrBooleanParameterOrByteParameter() {
        if (parameterOrBooleanParameterOrByteParameter == null) {
            parameterOrBooleanParameterOrByteParameter = new ArrayList<Object>();
        }
        return this.parameterOrBooleanParameterOrByteParameter;
    }

	@Override
	public String toString() {
		return "ParameterList [parameterOrBooleanParameterOrByteParameter=" + parameterOrBooleanParameterOrByteParameter + "]";
	}

}
