
package com.bank.credit_bank.infrastructure.presenter.soap.schema.response;

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
 *         <element name="tracking" type="{http://bank.com/credit_card}Tracking"/>
 *         <element name="data" type="{http://bank.com/credit_card}LongResponse"/>
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
    "tracking",
    "data"
})
@XmlRootElement(name = "controlCardResponse")
public class ControlCardResponse {

    @XmlElement(required = true)
    protected Tracking tracking;
    @XmlElement(required = true)
    protected LongResponse data;

    /**
     * Obtiene el valor de la propiedad tracking.
     * 
     * @return
     *     possible object is
     *     {@link Tracking }
     *     
     */
    public Tracking getTracking() {
        return tracking;
    }

    /**
     * Define el valor de la propiedad tracking.
     * 
     * @param value
     *     allowed object is
     *     {@link Tracking }
     *     
     */
    public void setTracking(Tracking value) {
        this.tracking = value;
    }

    /**
     * Obtiene el valor de la propiedad data.
     * 
     * @return
     *     possible object is
     *     {@link LongResponse }
     *     
     */
    public LongResponse getData() {
        return data;
    }

    /**
     * Define el valor de la propiedad data.
     * 
     * @param value
     *     allowed object is
     *     {@link LongResponse }
     *     
     */
    public void setData(LongResponse value) {
        this.data = value;
    }

}
