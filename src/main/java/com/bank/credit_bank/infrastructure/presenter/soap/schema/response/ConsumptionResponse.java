
package com.bank.credit_bank.infrastructure.presenter.soap.schema.response;

import jakarta.xml.bind.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;


/**
 * <p>Clase Java para ConsumptionResponse complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="ConsumptionResponse">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="sellerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="currency" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         <element name="consumptionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         <element name="consumptionApprobationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsumptionResponse", propOrder = {
    "sellerName",
    "currency",
    "amount",
    "consumptionDate",
    "consumptionApprobationDate"
})
public class ConsumptionResponse {

    @XmlElement(required = true)
    protected String sellerName;
    @XmlElement(required = true)
    protected String currency;
    @XmlElement(required = true)
    protected BigDecimal amount;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consumptionDate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consumptionApprobationDate;

    /**
     * Obtiene el valor de la propiedad sellerName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * Define el valor de la propiedad sellerName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerName(String value) {
        this.sellerName = value;
    }

    /**
     * Obtiene el valor de la propiedad currency.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Define el valor de la propiedad currency.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Obtiene el valor de la propiedad amount.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Define el valor de la propiedad amount.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Obtiene el valor de la propiedad consumptionDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getConsumptionDate() {
        return consumptionDate;
    }

    /**
     * Define el valor de la propiedad consumptionDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setConsumptionDate(XMLGregorianCalendar value) {
        this.consumptionDate = value;
    }

    /**
     * Obtiene el valor de la propiedad consumptionApprobationDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getConsumptionApprobationDate() {
        return consumptionApprobationDate;
    }

    /**
     * Define el valor de la propiedad consumptionApprobationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setConsumptionApprobationDate(XMLGregorianCalendar value) {
        this.consumptionApprobationDate = value;
    }

}
