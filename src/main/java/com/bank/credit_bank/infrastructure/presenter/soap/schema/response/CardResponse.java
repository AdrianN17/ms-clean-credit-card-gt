
package com.bank.credit_bank.infrastructure.presenter.soap.schema.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para CardResponse complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="CardResponse">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="typeCard" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="categoryCard" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="benefit" type="{http://bank.com/credit_card}BenefitResponse"/>
 *         <element name="balance" type="{http://bank.com/credit_card}BalanceResponse"/>
 *         <element name="account" type="{http://bank.com/credit_card}AccountResponse"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardResponse", propOrder = {
    "typeCard",
    "categoryCard",
    "benefit",
    "balance",
    "account"
})
public class CardResponse {

    @XmlElement(required = true)
    protected String typeCard;
    @XmlElement(required = true)
    protected String categoryCard;
    @XmlElement(required = true)
    protected BenefitResponse benefit;
    @XmlElement(required = true)
    protected BalanceResponse balance;
    @XmlElement(required = true)
    protected AccountResponse account;

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
     *     {@link BenefitResponse }
     *     
     */
    public BenefitResponse getBenefit() {
        return benefit;
    }

    /**
     * Define el valor de la propiedad benefit.
     * 
     * @param value
     *     allowed object is
     *     {@link BenefitResponse }
     *     
     */
    public void setBenefit(BenefitResponse value) {
        this.benefit = value;
    }

    /**
     * Obtiene el valor de la propiedad balance.
     * 
     * @return
     *     possible object is
     *     {@link BalanceResponse }
     *     
     */
    public BalanceResponse getBalance() {
        return balance;
    }

    /**
     * Define el valor de la propiedad balance.
     * 
     * @param value
     *     allowed object is
     *     {@link BalanceResponse }
     *     
     */
    public void setBalance(BalanceResponse value) {
        this.balance = value;
    }

    /**
     * Obtiene el valor de la propiedad account.
     * 
     * @return
     *     possible object is
     *     {@link AccountResponse }
     *     
     */
    public AccountResponse getAccount() {
        return account;
    }

    /**
     * Define el valor de la propiedad account.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountResponse }
     *     
     */
    public void setAccount(AccountResponse value) {
        this.account = value;
    }

}
