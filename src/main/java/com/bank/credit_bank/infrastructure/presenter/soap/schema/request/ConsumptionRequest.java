
package com.bank.credit_bank.infrastructure.presenter.soap.schema.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigDecimal;


/**
 * <p>Clase Java para ConsumptionRequest complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="ConsumptionRequest">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="sellerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="currency" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsumptionRequest", propOrder = {
    "sellerName",
    "currency",
    "amount"
})
public class ConsumptionRequest {

    @XmlElement(required = true)
    protected String sellerName;
    @XmlElement(required = true)
    protected String currency;
    @XmlElement(required = true)
    protected BigDecimal amount;

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

}
