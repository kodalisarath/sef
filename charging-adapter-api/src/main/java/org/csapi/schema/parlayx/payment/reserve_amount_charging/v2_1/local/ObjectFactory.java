
package org.csapi.schema.parlayx.payment.reserve_amount_charging.v2_1.local;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.csapi.schema.parlayx.payment.reserve_amount_charging.v2_1.local package. 
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

    private final static QName _ChargeReservation_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", "chargeReservation");
    private final static QName _ReleaseReservation_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", "releaseReservation");
    private final static QName _ReserveAdditionalAmountResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", "reserveAdditionalAmountResponse");
    private final static QName _ReserveAdditionalAmount_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", "reserveAdditionalAmount");
    private final static QName _ReleaseReservationResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", "releaseReservationResponse");
    private final static QName _ReserveAmount_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", "reserveAmount");
    private final static QName _ChargeReservationResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", "chargeReservationResponse");
    private final static QName _ReserveAmountResponse_QNAME = new QName("http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", "reserveAmountResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.csapi.schema.parlayx.payment.reserve_amount_charging.v2_1.local
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReleaseReservation }
     * 
     */
    public ReleaseReservation createReleaseReservation() {
        return new ReleaseReservation();
    }

    /**
     * Create an instance of {@link ChargeReservation }
     * 
     */
    public ChargeReservation createChargeReservation() {
        return new ChargeReservation();
    }

    /**
     * Create an instance of {@link ReserveAmountResponse }
     * 
     */
    public ReserveAmountResponse createReserveAmountResponse() {
        return new ReserveAmountResponse();
    }

    /**
     * Create an instance of {@link ChargeReservationResponse }
     * 
     */
    public ChargeReservationResponse createChargeReservationResponse() {
        return new ChargeReservationResponse();
    }

    /**
     * Create an instance of {@link ReserveAdditionalAmountResponse }
     * 
     */
    public ReserveAdditionalAmountResponse createReserveAdditionalAmountResponse() {
        return new ReserveAdditionalAmountResponse();
    }

    /**
     * Create an instance of {@link ReleaseReservationResponse }
     * 
     */
    public ReleaseReservationResponse createReleaseReservationResponse() {
        return new ReleaseReservationResponse();
    }

    /**
     * Create an instance of {@link ReserveAdditionalAmount }
     * 
     */
    public ReserveAdditionalAmount createReserveAdditionalAmount() {
        return new ReserveAdditionalAmount();
    }

    /**
     * Create an instance of {@link ReserveAmount }
     * 
     */
    public ReserveAmount createReserveAmount() {
        return new ReserveAmount();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChargeReservation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", name = "chargeReservation")
    public JAXBElement<ChargeReservation> createChargeReservation(ChargeReservation value) {
        return new JAXBElement<ChargeReservation>(_ChargeReservation_QNAME, ChargeReservation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReleaseReservation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", name = "releaseReservation")
    public JAXBElement<ReleaseReservation> createReleaseReservation(ReleaseReservation value) {
        return new JAXBElement<ReleaseReservation>(_ReleaseReservation_QNAME, ReleaseReservation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveAdditionalAmountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", name = "reserveAdditionalAmountResponse")
    public JAXBElement<ReserveAdditionalAmountResponse> createReserveAdditionalAmountResponse(ReserveAdditionalAmountResponse value) {
        return new JAXBElement<ReserveAdditionalAmountResponse>(_ReserveAdditionalAmountResponse_QNAME, ReserveAdditionalAmountResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveAdditionalAmount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", name = "reserveAdditionalAmount")
    public JAXBElement<ReserveAdditionalAmount> createReserveAdditionalAmount(ReserveAdditionalAmount value) {
        return new JAXBElement<ReserveAdditionalAmount>(_ReserveAdditionalAmount_QNAME, ReserveAdditionalAmount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReleaseReservationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", name = "releaseReservationResponse")
    public JAXBElement<ReleaseReservationResponse> createReleaseReservationResponse(ReleaseReservationResponse value) {
        return new JAXBElement<ReleaseReservationResponse>(_ReleaseReservationResponse_QNAME, ReleaseReservationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveAmount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", name = "reserveAmount")
    public JAXBElement<ReserveAmount> createReserveAmount(ReserveAmount value) {
        return new JAXBElement<ReserveAmount>(_ReserveAmount_QNAME, ReserveAmount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChargeReservationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", name = "chargeReservationResponse")
    public JAXBElement<ChargeReservationResponse> createChargeReservationResponse(ChargeReservationResponse value) {
        return new JAXBElement<ChargeReservationResponse>(_ChargeReservationResponse_QNAME, ChargeReservationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveAmountResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.csapi.org/schema/parlayx/payment/reserve_amount_charging/v2_1/local", name = "reserveAmountResponse")
    public JAXBElement<ReserveAmountResponse> createReserveAmountResponse(ReserveAmountResponse value) {
        return new JAXBElement<ReserveAmountResponse>(_ReserveAmountResponse_QNAME, ReserveAmountResponse.class, null, value);
    }

}
