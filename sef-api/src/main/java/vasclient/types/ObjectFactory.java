
package vasclient.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the vasclient.types package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SendToSPS_QNAME = new QName("urn:VASClient/types", "sendToSPS");
    private final static QName _SendToSPSResponse_QNAME = new QName("urn:VASClient/types", "sendToSPSResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: vasclient.types
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendToSPS }
     * 
     */
    public SendToSPS createSendToSPS() {
        return new SendToSPS();
    }

    /**
     * Create an instance of {@link SendToSPSResponse }
     * 
     */
    public SendToSPSResponse createSendToSPSResponse() {
        return new SendToSPSResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendToSPS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:VASClient/types", name = "sendToSPS")
    public JAXBElement<SendToSPS> createSendToSPS(SendToSPS value) {
        return new JAXBElement<SendToSPS>(_SendToSPS_QNAME, SendToSPS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendToSPSResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:VASClient/types", name = "sendToSPSResponse")
    public JAXBElement<SendToSPSResponse> createSendToSPSResponse(SendToSPSResponse value) {
        return new JAXBElement<SendToSPSResponse>(_SendToSPSResponse_QNAME, SendToSPSResponse.class, null, value);
    }

}
