
package com.bank.credit_bank.infrastructure.presenter.soap.schema.request;

import jakarta.xml.bind.annotation.*;


/**
 * <p>Clase Java para anonymous complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="cardId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         <element name="consumptionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cardId",
    "consumptionId"
})
@XmlRootElement(name = "controlConsumptionRequest")
public class ControlConsumptionRequest {

    protected long cardId;
    @XmlElement(required = true)
    protected String consumptionId;

    /**
     * Obtiene el valor de la propiedad cardId.
     * 
     */
    public long getCardId() {
        return cardId;
    }

    /**
     * Define el valor de la propiedad cardId.
     * 
     */
    public void setCardId(long value) {
        this.cardId = value;
    }

    /**
     * Obtiene el valor de la propiedad consumptionId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumptionId() {
        return consumptionId;
    }

    /**
     * Define el valor de la propiedad consumptionId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumptionId(String value) {
        this.consumptionId = value;
    }

}
