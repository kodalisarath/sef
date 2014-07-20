
package com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CommandResult" type="{http://nsn.com/ossbss/charge.once/wsdl/entity/Tis/xsd/1}CommandResult"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "commandResult"
})
@XmlRootElement(name = "CommandResponseData")
public class CommandResponseData {

    @XmlElement(name = "CommandResult", required = true)
    protected CommandResult commandResult;

    /**
     * Gets the value of the commandResult property.
     * 
     * @return
     *     possible object is
     *     {@link CommandResult }
     *     
     */
    public CommandResult getCommandResult() {
        return commandResult;
    }

    /**
     * Sets the value of the commandResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommandResult }
     *     
     */
    public void setCommandResult(CommandResult value) {
        this.commandResult = value;
    }

	@Override
	public String toString() {
		return "CommandResponseData [commandResult=" + commandResult + "]";
	}

}
