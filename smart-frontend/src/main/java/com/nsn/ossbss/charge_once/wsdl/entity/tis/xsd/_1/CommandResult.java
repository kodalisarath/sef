
package com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CommandResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommandResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="OperationResult" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}OperationResult"/>
 *         &lt;element name="TransactionResult" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}TransactionResult"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommandResult", propOrder = {
    "operationResult",
    "transactionResult"
})
public class CommandResult {

    @XmlElement(name = "OperationResult")
    protected OperationResult operationResult;
    @XmlElement(name = "TransactionResult")
    protected TransactionResult transactionResult;

    /**
     * Gets the value of the operationResult property.
     * 
     * @return
     *     possible object is
     *     {@link OperationResult }
     *     
     */
    public OperationResult getOperationResult() {
        return operationResult;
    }

    /**
     * Sets the value of the operationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationResult }
     *     
     */
    public void setOperationResult(OperationResult value) {
        this.operationResult = value;
    }

    /**
     * Gets the value of the transactionResult property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionResult }
     *     
     */
    public TransactionResult getTransactionResult() {
        return transactionResult;
    }

    /**
     * Sets the value of the transactionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionResult }
     *     
     */
    public void setTransactionResult(TransactionResult value) {
        this.transactionResult = value;
    }

	@Override
	public String toString() {
		return "CommandResult [operationResult=" + operationResult + ", transactionResult=" + transactionResult + "]";
	}

}
