
package org.csapi.schema.parlayx.payment.amount_charging.v2_1.local;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.csapi.schema.parlayx.payment.amount_charging.v2_1.local package. 
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

    private final static QName _ChargeAmountResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/amount_charging/v2_1/local", "chargeAmountResponse");
    private final static QName _ChargeAmount_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/amount_charging/v2_1/local", "chargeAmount");
    private final static QName _RefundAmount_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/amount_charging/v2_1/local", "refundAmount");
    private final static QName _RefundAmountResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/amount_charging/v2_1/local", "refundAmountResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.csapi.schema.parlayx.payment.amount_charging.v2_1.local
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ChargeAmount }
     * 
     */
    public ChargeAmount createChargeAmount() {
        return new ChargeAmount();
    }

    /**
     * Create an instance of {@link RefundAmountResponse }
     * 
     */
    public RefundAmountResponse createRefundAmountResponse() {
        return new RefundAmountResponse();
    }

    /**
     * Create an instance of {@link RefundAmount }
     * 
     */
    public RefundAmount createRefundAmount() {
        return new RefundAmount();
    }

    /**
     * Create an instance of {@link ChargeAmountResponse }
     * 
     */
    public ChargeAmountResponse createChargeAmountResponse() {
        return new ChargeAmountResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChargeAmountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/amount_charging/v2_1/local", name = "chargeAmountResponse")
    public JAXBElement<ChargeAmountResponse> createChargeAmountResponse(ChargeAmountResponse value) {
        return new JAXBElement<ChargeAmountResponse>(_ChargeAmountResponse_QNAME, ChargeAmountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChargeAmount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/amount_charging/v2_1/local", name = "chargeAmount")
    public JAXBElement<ChargeAmount> createChargeAmount(ChargeAmount value) {
        return new JAXBElement<ChargeAmount>(_ChargeAmount_QNAME, ChargeAmount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefundAmount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/amount_charging/v2_1/local", name = "refundAmount")
    public JAXBElement<RefundAmount> createRefundAmount(RefundAmount value) {
        return new JAXBElement<RefundAmount>(_RefundAmount_QNAME, RefundAmount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RefundAmountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/amount_charging/v2_1/local", name = "refundAmountResponse")
    public JAXBElement<RefundAmountResponse> createRefundAmountResponse(RefundAmountResponse value) {
        return new JAXBElement<RefundAmountResponse>(_RefundAmountResponse_QNAME, RefundAmountResponse.class, null, value);
    }

}
