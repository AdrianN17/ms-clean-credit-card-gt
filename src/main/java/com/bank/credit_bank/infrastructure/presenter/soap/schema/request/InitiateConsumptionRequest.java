
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
 *         <element name="data" type="{http://bank.com/credit_card}ConsumptionRequest"/>
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
    "data"
})
@XmlRootElement(name = "initiateConsumptionRequest")
public class InitiateConsumptionRequest {

    protected long cardId;
    @XmlElement(required = true)
    protected ConsumptionRequest data;

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
     * Obtiene el valor de la propiedad data.
     * 
     * @return
     *     possible object is
     *     {@link ConsumptionRequest }
     *     
     */
    public ConsumptionRequest getData() {
        return data;
    }

    /**
     * Define el valor de la propiedad data.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsumptionRequest }
     *     
     */
    public void setData(ConsumptionRequest value) {
        this.data = value;
    }

}
