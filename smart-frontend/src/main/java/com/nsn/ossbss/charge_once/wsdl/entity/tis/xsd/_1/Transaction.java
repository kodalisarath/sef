
package com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Transaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Transaction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="Assignment" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}Assignment"/>
 *         &lt;element name="Operation" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}Operation"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Transaction", propOrder = {
    "assignmentOrOperation"
})
public class Transaction {

    @XmlElements({
        @XmlElement(name = "Assignment", type = Assignment.class),
        @XmlElement(name = "Operation", type = Operation.class)
    })
    protected List<Object> assignmentOrOperation;

    /**
     * Gets the value of the assignmentOrOperation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assignmentOrOperation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssignmentOrOperation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Assignment }
     * {@link Operation }
     * 
     * 
     */
    public List<Object> getAssignmentOrOperation() {
        if (assignmentOrOperation == null) {
            assignmentOrOperation = new ArrayList<Object>();
        }
        return this.assignmentOrOperation;
    }

}
