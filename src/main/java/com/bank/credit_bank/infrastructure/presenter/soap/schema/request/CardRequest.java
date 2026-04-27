
package com.bank.credit_bank.infrastructure.presenter.soap.schema.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para CardRequest complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="CardRequest">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="typeCard" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="categoryCard" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="benefit" type="{http://bank.com/credit_card}BenefitRequest"/>
 *         <element name="account" type="{http://bank.com/credit_card}AccountRequest"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardRequest", propOrder = {
    "typeCard",
    "categoryCard",
    "benefit",
    "account"
})
public class CardRequest {

    @XmlElement(required = true)
    protected String typeCard;
    @XmlElement(required = true)
    protected String categoryCard;
    @XmlElement(required = true)
    protected BenefitRequest benefit;
    @XmlElement(required = true)
    protected AccountRequest account;

    /**
     * Obtiene el valor de la propiedad typeCard.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeCard() {
        return typeCard;
    }

    /**
     * Define el valor de la propiedad typeCard.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeCard(String value) {
        this.typeCard = value;
    }

    /**
     * Obtiene el valor de la propiedad categoryCard.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryCard() {
        return categoryCard;
    }

    /**
     * Define el valor de la propiedad categoryCard.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryCard(String value) {
        this.categoryCard = value;
    }

    /**
     * Obtiene el valor de la propiedad benefit.
     * 
     * @return
     *     possible object is
     *     {@link BenefitRequest }
     *     
     */
    public BenefitRequest getBenefit() {
        return benefit;
    }

    /**
     * Define el valor de la propiedad benefit.
     * 
     * @param value
     *     allowed object is
     *     {@link BenefitRequest }
     *     
     */
    public void setBenefit(BenefitRequest value) {
        this.benefit = value;
    }

    /**
     * Obtiene el valor de la propiedad account.
     * 
     * @return
     *     possible object is
     *     {@link AccountRequest }
     *     
     */
    public AccountRequest getAccount() {
        return account;
    }

    /**
     * Define el valor de la propiedad account.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountRequest }
     *     
     */
    public void setAccount(AccountRequest value) {
        this.account = value;
    }

}
