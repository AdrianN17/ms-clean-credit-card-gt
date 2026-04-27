
package com.bank.credit_bank.infrastructure.presenter.soap.schema.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigDecimal;


/**
 * <p>Clase Java para AccountRequest complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="AccountRequest">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="creditTotal" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         <element name="debtTax" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         <element name="currency" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="paymentDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountRequest", propOrder = {
    "creditTotal",
    "debtTax",
    "currency",
    "paymentDate"
})
public class AccountRequest {

    @XmlElement(required = true)
    protected BigDecimal creditTotal;
    @XmlElement(required = true)
    protected BigDecimal debtTax;
    @XmlElement(required = true)
    protected String currency;
    @XmlElement(required = true)
    protected String paymentDate;

    /**
     * Obtiene el valor de la propiedad creditTotal.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCreditTotal() {
        return creditTotal;
    }

    /**
     * Define el valor de la propiedad creditTotal.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCreditTotal(BigDecimal value) {
        this.creditTotal = value;
    }

    /**
     * Obtiene el valor de la propiedad debtTax.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDebtTax() {
        return debtTax;
    }

    /**
     * Define el valor de la propiedad debtTax.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDebtTax(BigDecimal value) {
        this.debtTax = value;
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
     * Obtiene el valor de la propiedad paymentDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentDate() {
        return paymentDate;
    }

    /**
     * Define el valor de la propiedad paymentDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentDate(String value) {
        this.paymentDate = value;
    }

}
