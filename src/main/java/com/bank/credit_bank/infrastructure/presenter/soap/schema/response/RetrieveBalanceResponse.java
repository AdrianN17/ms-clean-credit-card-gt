
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
 *         <element name="data" type="{http://bank.com/credit_card}CardResponse"/>
 *         <element name="tracking" type="{http://bank.com/credit_card}Tracking"/>
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
    "data",
    "tracking"
})
@XmlRootElement(name = "retrieveBalanceResponse")
public class RetrieveBalanceResponse {

    @XmlElement(required = true)
    protected CardResponse data;
    @XmlElement(required = true)
    protected Tracking tracking;

    /**
     * Obtiene el valor de la propiedad data.
     * 
     * @return
     *     possible object is
     *     {@link CardResponse }
     *     
     */
    public CardResponse getData() {
        return data;
    }

    /**
     * Define el valor de la propiedad data.
     * 
     * @param value
     *     allowed object is
     *     {@link CardResponse }
     *     
     */
    public void setData(CardResponse value) {
        this.data = value;
    }

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

}
