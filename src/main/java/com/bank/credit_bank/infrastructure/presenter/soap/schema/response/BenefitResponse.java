
package com.bank.credit_bank.infrastructure.presenter.soap.schema.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.math.BigDecimal;


/**
 * <p>Clase Java para BenefitResponse complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="BenefitResponse">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="hasDiscount" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="multiplierPoints" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         <element name="totalPoint" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BenefitResponse", propOrder = {
    "hasDiscount",
    "multiplierPoints",
    "totalPoint"
})
public class BenefitResponse {

    protected boolean hasDiscount;
    @XmlElement(required = true)
    protected BigDecimal multiplierPoints;
    protected int totalPoint;

    /**
     * Obtiene el valor de la propiedad hasDiscount.
     * 
     */
    public boolean isHasDiscount() {
        return hasDiscount;
    }

    /**
     * Define el valor de la propiedad hasDiscount.
     * 
     */
    public void setHasDiscount(boolean value) {
        this.hasDiscount = value;
    }

    /**
     * Obtiene el valor de la propiedad multiplierPoints.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMultiplierPoints() {
        return multiplierPoints;
    }

    /**
     * Define el valor de la propiedad multiplierPoints.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMultiplierPoints(BigDecimal value) {
        this.multiplierPoints = value;
    }

    /**
     * Obtiene el valor de la propiedad totalPoint.
     * 
     */
    public int getTotalPoint() {
        return totalPoint;
    }

    /**
     * Define el valor de la propiedad totalPoint.
     * 
     */
    public void setTotalPoint(int value) {
        this.totalPoint = value;
    }

}
