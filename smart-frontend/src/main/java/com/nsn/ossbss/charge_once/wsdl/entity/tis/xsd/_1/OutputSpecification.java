
package com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OutputSpecification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutputSpecification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="SimpleParam" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}OutSimpleParamId"/>
 *         &lt;element name="StructParam" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}OutStructParamId"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutputSpecification", propOrder = {
    "simpleParamOrStructParam"
})
public class OutputSpecification {

    @XmlElements({
        @XmlElement(name = "SimpleParam", type = OutSimpleParamId.class),
        @XmlElement(name = "StructParam", type = OutStructParamId.class)
    })
    protected List<Object> simpleParamOrStructParam;

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
     * {@link OutSimpleParamId }
     * {@link OutStructParamId }
     * 
     * 
     */
    public List<Object> getSimpleParamOrStructParam() {
        if (simpleParamOrStructParam == null) {
            simpleParamOrStructParam = new ArrayList<Object>();
        }
        return this.simpleParamOrStructParam;
    }

}
