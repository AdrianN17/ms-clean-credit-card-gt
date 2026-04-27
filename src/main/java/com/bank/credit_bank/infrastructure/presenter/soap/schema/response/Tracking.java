
package com.bank.credit_bank.infrastructure.presenter.soap.schema.response;

import jakarta.xml.bind.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para Tracking complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="Tracking">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="trackingId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="operationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tracking", propOrder = {
    "trackingId",
    "operationDate"
})
public class Tracking {

    @XmlElement(required = true)
    protected String trackingId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar operationDate;

    /**
     * Obtiene el valor de la propiedad trackingId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrackingId() {
        return trackingId;
    }

    /**
     * Define el valor de la propiedad trackingId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrackingId(String value) {
        this.trackingId = value;
    }

    /**
     * Obtiene el valor de la propiedad operationDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOperationDate() {
        return operationDate;
    }

    /**
     * Define el valor de la propiedad operationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOperationDate(XMLGregorianCalendar value) {
        this.operationDate = value;
    }

}
