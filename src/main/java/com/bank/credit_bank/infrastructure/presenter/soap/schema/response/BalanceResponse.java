
package com.bank.credit_bank.infrastructure.presenter.soap.schema.response;

import jakarta.xml.bind.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;


/**
 * <p>Clase Java para BalanceResponse complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="BalanceResponse">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="totalAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         <element name="oldAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         <element name="availableAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         <element name="startDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         <element name="endDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BalanceResponse", propOrder = {
    "totalAmount",
    "oldAmount",
    "availableAmount",
    "startDate",
    "endDate"
})
public class BalanceResponse {

    @XmlElement(required = true)
    protected BigDecimal totalAmount;
    @XmlElement(required = true)
    protected BigDecimal oldAmount;
    @XmlElement(required = true)
    protected BigDecimal availableAmount;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;

    /**
     * Obtiene el valor de la propiedad totalAmount.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * Define el valor de la propiedad totalAmount.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalAmount(BigDecimal value) {
        this.totalAmount = value;
    }

    /**
     * Obtiene el valor de la propiedad oldAmount.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    /**
     * Define el valor de la propiedad oldAmount.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOldAmount(BigDecimal value) {
        this.oldAmount = value;
    }

    /**
     * Obtiene el valor de la propiedad availableAmount.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    /**
     * Define el valor de la propiedad availableAmount.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAvailableAmount(BigDecimal value) {
        this.availableAmount = value;
    }

    /**
     * Obtiene el valor de la propiedad startDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Define el valor de la propiedad startDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Obtiene el valor de la propiedad endDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Define el valor de la propiedad endDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

}
