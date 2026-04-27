
package com.bank.credit_bank.infrastructure.presenter.soap.schema.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ExchangeData complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="ExchangeData">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="installments">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               <minInclusive value="1"/>
 *               <maxInclusive value="36"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExchangeData", propOrder = {
    "installments"
})
public class ExchangeData {

    protected int installments;

    /**
     * Obtiene el valor de la propiedad installments.
     * 
     */
    public int getInstallments() {
        return installments;
    }

    /**
     * Define el valor de la propiedad installments.
     * 
     */
    public void setInstallments(int value) {
        this.installments = value;
    }

}
