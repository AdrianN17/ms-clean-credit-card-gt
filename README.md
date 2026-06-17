# ms-clean-credit-card-gt

Microservicio de tarjeta de crédito construido con **Clean Architecture + Hexagonal (Ports & Adapters)**.

## Perfiles Spring Boot

| Perfil | Inbound | Outbound NoSQL |
|--------|---------|----------------|
| `old`  | SOAP (CXF/JAX-WS) | MongoDB |
| `new`  | REST (OpenAPI) | Azure Cosmos DB |

> Ambos perfiles comparten SQL Server para Card / Balance / Benefit.

---

## Diagrama de Arquitectura (C4 Level 4 – Code)

```mermaid
classDiagram
direction TB

%% ================================================================
%% ZONA 1 — INFRASTRUCTURE: INBOUND ADAPTERS (Presenter)
%% ================================================================

namespace infrastructure_soap {
    class CreditCardControllerSOAPImpl {
        <<controller>>
        -creditCardDelegateSOAP : CreditCardDelegateSOAP
        +retrieveBalance(RetrieveBalanceRequest) RetrieveBalanceResponse
        +initiateCard(InitiateCardRequest) InitiateCardResponse
        +controlCard(ControlCardRequest) ControlCardResponse
        +initiatePayment(InitiatePaymentRequest) InitiatePaymentResponse
        +controlPayment(ControlPaymentRequest) ControlPaymentResponse
        +initiateConsumption(InitiateConsumptionRequest) InitiateConsumptionResponse
        +exchangeConsumption(ExchangeConsumptionRequest) ExchangeConsumptionResponse
        +controlConsumption(ControlConsumptionRequest) ControlConsumptionResponse
        +retrieveConsumption(RetrieveConsumptionRequest) RetrieveConsumptionResponse
        +retrievePayment(RetrievePaymentRequest) RetrievePaymentResponse
    }
    class CreditCardControllerSOAP {
        <<portIn>>
        +retrieveBalance(RetrieveBalanceRequest)
        +initiateCard(InitiateCardRequest)
        +controlCard(ControlCardRequest)
        +initiatePayment(InitiatePaymentRequest)
        +controlPayment(ControlPaymentRequest)
        +initiateConsumption(InitiateConsumptionRequest)
        +exchangeConsumption(ExchangeConsumptionRequest)
        +controlConsumption(ControlConsumptionRequest)
        +retrieveConsumption(RetrieveConsumptionRequest)
        +retrievePayment(RetrievePaymentRequest)
    }
    class CreditCardDelegateSOAPImpl {
        <<delegate>>
        -cardCreateService : CardCreateService
        -cardCloseService : CardCloseService
        -cardFindByIdService : CardFindByIdService
        -paymentProcessService : PaymentProcessService
        -paymentCancelService : PaymentCancelService
        -paymentFindByDatesAndCardIdService : PaymentFindByDatesAndCardIdService
        -consumptionProcessService : ConsumptionProcessService
        -consumptionCancelService : ConsumptionCancelService
        -consumptionSplitService : ConsumptionSplitService
        -consumptionFindByDatesAndCardIdService : ConsumptionFindByDatesAndCardIdService
        -cardApiMapperRequestCommand : CardApiMapperRequestCommand_SOAP
        -consumptionApiMapperRequestCommand : ConsumptionApiMapperRequestCommand_SOAP
        -paymentApiMapperRequestCommand : PaymentApiMapperRequestCommand_SOAP
    }
    class CreditCardDelegateSOAP {
        <<portIn>>
        +retrieveBalance(RetrieveBalanceRequest) RetrieveBalanceResponse
        +initiateCard(InitiateCardRequest) InitiateCardResponse
        +controlCard(ControlCardRequest) ControlCardResponse
        +initiatePayment(InitiatePaymentRequest) InitiatePaymentResponse
        +controlPayment(ControlPaymentRequest) ControlPaymentResponse
        +initiateConsumption(InitiateConsumptionRequest) InitiateConsumptionResponse
        +exchangeConsumption(ExchangeConsumptionRequest) ExchangeConsumptionResponse
        +controlConsumption(ControlConsumptionRequest) ControlConsumptionResponse
        +retrieveConsumption(RetrieveConsumptionRequest) RetrieveConsumptionResponse
        +retrievePayment(RetrievePaymentRequest) RetrievePaymentResponse
    }
    class CardApiMapperRequestCommand_SOAP {
        <<mapper>>
        +toCommand(InitiateCardRequest) CardCreateCommand
        +toCommandId(ControlCardRequest) CardCloseCommand
        +toResponse(LoadCardBalanceBenefitView) CardResponse
    }
    class CardApiMapperRequestCommandImpl_SOAP {
        <<mapper>>
    }
    class ConsumptionApiMapperRequestCommand_SOAP {
        <<mapper>>
        +toCommand(InitiateConsumptionRequest) CardProcessConsumptionCommand
        +toCommandId(ControlConsumptionRequest) ConsumptionCancelCommand
        +toCommandIdR(ExchangeConsumptionRequest) ConsumptionSplitCommand
        +toResponse(LoadConsumptionView) ConsumptionResponseList
    }
    class ConsumptionApiMapperRequestCommandImpl_SOAP {
        <<mapper>>
    }
    class PaymentApiMapperRequestCommand_SOAP {
        <<mapper>>
        +toCommand(InitiatePaymentRequest) CardProcessPaymentCommand
        +toCommandId(ControlPaymentRequest) PaymentCancelCommand
        +toResponse(LoadPaymentView) PaymentResponseList
    }
    class PaymentApiMapperRequestCommandImpl_SOAP {
        <<mapper>>
    }
}

namespace infrastructure_rest {
    class CardManagementApiImpl {
        <<controller>>
        -cardManagementDelegate : CardManagementDelegate
        +retrieveBalance(cardId : Long) ResponseEntity
        +initiateCard(InitiateCardRequest, BindingResult) ResponseEntity
        +controlCard(cardId : Long) ResponseEntity
        +initiatePayment(cardId : Long, InitiatePaymentRequest) ResponseEntity
        +controlPayment(cardId : Long, paymentId : UUID) ResponseEntity
        +initiateConsumption(cardId : Long, InitiateConsumptionRequest) ResponseEntity
        +exchangeConsumption(cardId : Long, consumptionId : UUID) ResponseEntity
        +controlConsumption(cardId : Long, consumptionId : UUID) ResponseEntity
        +retrieveConsumption(cardId : Long, dateStart : LocalDate, dateEnd : LocalDate) ResponseEntity
        +retrievePayment(cardId : Long, dateStart : LocalDate, dateEnd : LocalDate) ResponseEntity
    }
    class CardManagementApi {
        <<portIn>>
    }
    class CardManagementDelegateImpl {
        <<delegate>>
        -cardCreateService : CardCreateService
        -cardCloseService : CardCloseService
        -cardFindByIdService : CardFindByIdService
        -paymentProcessService : PaymentProcessService
        -paymentCancelService : PaymentCancelService
        -paymentFindByDatesAndCardIdService : PaymentFindByDatesAndCardIdService
        -consumptionProcessService : ConsumptionProcessService
        -consumptionCancelService : ConsumptionCancelService
        -consumptionSplitService : ConsumptionSplitService
        -consumptionFindByDatesAndCardIdService : ConsumptionFindByDatesAndCardIdService
        -cardApiMapperRequestCommand : CardApiMapperRequestCommand_REST
        -consumptionApiMapperRequestCommand : ConsumptionApiMapperRequestCommand_REST
        -paymentApiMapperRequestCommand : PaymentApiMapperRequestCommand_REST
    }
    class CardManagementDelegate {
        <<portIn>>
        +retrieveBalance(cardId : Long) ResponseEntity
        +initiateCard(InitiateCardRequest, BindingResult) ResponseEntity
        +controlCard(cardId : Long) ResponseEntity
        +initiatePayment(cardId : Long, InitiatePaymentRequest, BindingResult) ResponseEntity
        +controlPayment(cardId : Long, paymentId : UUID, BindingResult) ResponseEntity
        +initiateConsumption(cardId : Long, InitiateConsumptionRequest, BindingResult) ResponseEntity
        +exchangeConsumption(cardId : Long, consumptionId : UUID, ExchangeConsumptionRequest, BindingResult) ResponseEntity
        +controlConsumption(cardId : Long, consumptionId : UUID) ResponseEntity
        +retrieveConsumption(cardId : Long, dateStart : LocalDate, dateEnd : LocalDate) ResponseEntity
        +retrievePayment(cardId : Long, dateStart : LocalDate, dateEnd : LocalDate) ResponseEntity
    }
    class CardApiMapperRequestCommand_REST {
        <<mapper>>
        +toCommand(card data) CardCreateCommand
        +toCommandId(cardId : Long) CardCloseCommand
        +toResponse(view) RetrieveBalance200Response
    }
    class CardApiMapperRequestCommandImpl_REST {
        <<mapper>>
    }
    class ConsumptionApiMapperRequestCommand_REST {
        <<mapper>>
    }
    class ConsumptionApiMapperRequestCommandImpl_REST {
        <<mapper>>
    }
    class PaymentApiMapperRequestCommand_REST {
        <<mapper>>
    }
    class PaymentApiMapperRequestCommandImpl_REST {
        <<mapper>>
    }
}

%% ================================================================
%% ZONA 2 — APPLICATION LAYER
%% ================================================================

namespace application_card {
    class CardCreateUseCase {
        <<useCase>>
        +execute(CardCreateCommand) CardId
    }
    class CardCloseUseCase {
        <<useCase>>
        +execute(CardCloseCommand) CardId
    }
    class CardFindByIdQuery {
        <<useCase>>
        +execute(cardId : Long) LoadCardBalanceBenefitView
    }
    class CardCreateService {
        <<appService>>
        -businessServiceCard : BusinessServiceCard
        -businessServiceBalance : BusinessServiceBalance
        -businessServiceBenefit : BusinessServiceBenefit
        -idGeneratePort : IdGeneratePort
        -loadCurrencyWSPort : LoadCurrencyWSPort
        -balanceFactory : BalanceFactory
        +execute(CardCreateCommand) CardId
    }
    class CardCloseService {
        <<appService>>
        -businessServiceCard : BusinessServiceCard
        -businessServiceBalance : BusinessServiceBalance
        -businessServiceBenefit : BusinessServiceBenefit
        +execute(CardCloseCommand) CardId
    }
    class CardFindByIdService {
        <<appService>>
        -cardBalanceBenefitFindByIdPort : CardBalanceBenefitFindByIdPort
        +execute(cardId : Long) LoadCardBalanceBenefitView
    }
    class CardDBSavePort {
        <<portOut>>
        +save(CardRequestDto) Optional~CardId~
    }
    class CardFindByIdPort {
        <<portOut>>
        +load(cardId : Long, currency : CurrencyRequestDto) Optional~CardResponseDto~
    }
    class CardDBFindCurrencyPort {
        <<portOut>>
        +load(cardId : Long) Optional~String~
    }
    class CardBalanceBenefitFindByIdPort {
        <<portOut>>
        +loadAll(cardId : Long) Optional~LoadCardBalanceBenefitView~
    }
    class BusinessServiceCard {
        <<bizService>>
        +get(cardId : Long) Card
        +save(card : Card) CardId
    }
    class BusinessServiceCardImpl {
        <<bizService>>
        -cardFindByIdPort : CardFindByIdPort
        -cardDBFindCurrencyPort : CardDBFindCurrencyPort
        -cardDBSavePort : CardDBSavePort
        -loadCurrencyWSPort : LoadCurrencyWSPort
    }
    class CardCreateCommand {
        <<command>>
        +typeCard() String
        +categoryCard() String
        +creditTotal() BigDecimal
        +debtTax() BigDecimal
        +currency() String
        +paymentDay() Short
        +hasDiscount() Boolean
        +multiplierPoints() BigDecimal
    }
    class CardCloseCommand {
        <<command>>
        +cardId() Long
    }
}

namespace application_consumption {
    class ConsumptionProcessUseCase {
        <<useCase>>
        +execute(CardProcessConsumptionCommand) ConsumptionId
    }
    class ConsumptionCancelUseCase {
        <<useCase>>
        +execute(ConsumptionCancelCommand) ConsumptionId
    }
    class ConsumptionSplitUseCase {
        <<useCase>>
        +execute(ConsumptionSplitCommand) List~ConsumptionId~
    }
    class ConsumptionFindByDatesAndCardIdQuery {
        <<useCase>>
        +execute(FindConsumptionByDatesAndCardIdCriteria) List~LoadConsumptionView~
    }
    class ConsumptionProcessService {
        <<appService>>
        -businessServiceCard : BusinessServiceCard
        -businessServiceBalance : BusinessServiceBalance
        -businessServiceBenefit : BusinessServiceBenefit
        -businessServiceConsumption : BusinessServiceConsumption
        -loadCurrencyWSPort : LoadCurrencyWSPort
        +execute(CardProcessConsumptionCommand) ConsumptionId
    }
    class ConsumptionCancelService {
        <<appService>>
        +execute(ConsumptionCancelCommand) ConsumptionId
    }
    class ConsumptionSplitService {
        <<appService>>
        +execute(ConsumptionSplitCommand) List~ConsumptionId~
    }
    class ConsumptionFindByDatesAndCardIdService {
        <<appService>>
        +execute(FindConsumptionByDatesAndCardIdCriteria) List~LoadConsumptionView~
    }
    class ConsumptionDBSavePort {
        <<portOut>>
        +save(ConsumptionRequestDto) Optional~ConsumptionId~
    }
    class ConsumptionFindByIdPort {
        <<portOut>>
        +load(consumptionId : UUID, cardId : String, currency : CurrencyRequestDto) Optional~ConsumptionResponseDto~
    }
    class ConsumptionDBFindCurrencyPort {
        <<portOut>>
        +load(consumptionId : UUID, cardId : String) Optional~String~
    }
    class ConsumptionsDBFindByDatesAndCardIdPort {
        <<portOut>>
        +load(FindConsumptionByDatesAndCardIdCriteria) List~LoadConsumptionView~
    }
    class BusinessServiceConsumption {
        <<bizService>>
        +get(consumptionId : UUID, cardId : Long) Consumption
        +save(consumption : Consumption) ConsumptionId
    }
    class BusinessServiceConsumptionImpl {
        <<bizService>>
    }
    class FindConsumptionByDatesAndCardIdCriteria {
        <<query>>
        +start() LocalDate
        +end() LocalDate
        +cardId() Long
    }
}

namespace application_payment {
    class PaymentProcessUseCase {
        <<useCase>>
        +execute(CardProcessPaymentCommand) PaymentId
    }
    class PaymentCancelUseCase {
        <<useCase>>
        +execute(PaymentCancelCommand) PaymentId
    }
    class PaymentFindByDatesAndCardIdQuery {
        <<useCase>>
        +execute(FindPaymentByDatesAndCardIdCriteria) List~LoadPaymentView~
    }
    class PaymentProcessService {
        <<appService>>
        -businessServiceCard : BusinessServiceCard
        -businessServiceBalance : BusinessServiceBalance
        -businessServiceBenefit : BusinessServiceBenefit
        -businessServicePayment : BusinessServicePayment
        -loadCurrencyWSPort : LoadCurrencyWSPort
        -paymentFactory : PaymentFactory
        +execute(CardProcessPaymentCommand) PaymentId
    }
    class PaymentCancelService {
        <<appService>>
        +execute(PaymentCancelCommand) PaymentId
    }
    class PaymentFindByDatesAndCardIdService {
        <<appService>>
        +execute(FindPaymentByDatesAndCardIdCriteria) List~LoadPaymentView~
    }
    class PaymentDBSavePort {
        <<portOut>>
        +save(PaymentRequestDto) Optional~PaymentId~
    }
    class PaymentFindByIdPort {
        <<portOut>>
        +load(paymentId : UUID, cardId : String, currency : CurrencyRequestDto) Optional~PaymentResponseDto~
    }
    class PaymentDBFindCurrencyPort {
        <<portOut>>
        +load(paymentId : UUID, cardId : String) Optional~String~
    }
    class PaymentsFindByDatesAndCardIdPort {
        <<portOut>>
        +load(FindPaymentByDatesAndCardIdCriteria) List~LoadPaymentView~
    }
    class BusinessServicePayment {
        <<bizService>>
        +get(paymentId : UUID, cardId : Long) Payment
        +save(payment : Payment) PaymentId
    }
    class BusinessServicePaymentImpl {
        <<bizService>>
    }
    class FindPaymentByDatesAndCardIdCriteria {
        <<query>>
        +start() LocalDate
        +end() LocalDate
        +cardId() Long
    }
}

namespace application_balance {
    class BalanceDBSavePort {
        <<portOut>>
        +save(balance) Optional~BalanceId~
    }
    class BalanceDBFindByIdPort {
        <<portOut>>
        +load(cardId : Long) Optional~Balance~
    }
    class BusinessServiceBalance {
        <<bizService>>
        +get(cardId : Long) Balance
        +save(balance : Balance) BalanceId
    }
    class BusinessServiceBalanceImpl {
        <<bizService>>
    }
}

namespace application_benefit {
    class BenefitDBSavePort {
        <<portOut>>
        +save(benefit) Optional~BenefitId~
    }
    class BenefitDBFindByIdPort {
        <<portOut>>
        +load(cardId : Long) Optional~Benefit~
    }
    class BusinessServiceBenefit {
        <<bizService>>
        +get(cardId : Long) Benefit
        +save(benefit : Benefit) BenefitId
    }
    class BusinessServiceBenefitImpl {
        <<bizService>>
    }
}

namespace application_currency {
    class LoadCurrencyWSPort {
        <<portOut>>
        +load(currency : String) Optional~CurrencyResponseDto~
    }
}

namespace application_generator {
    class IdGeneratePort {
        <<portOut>>
        +load() Optional~Long~
    }
}

%% ================================================================
%% ZONA 3 — DOMAIN LAYER
%% ================================================================

namespace domain_generic {
    class AggregateRoot {
        <<aggregate>>
        #id : T
        #status : StatusEnum
        #createdDate : LocalDateTime
        #updatedDate : LocalDateTime
        +softDelete() void
        +getId() T
        +getStatus() StatusEnum
    }
    class GenericDomain {
        <<entity>>
    }
    class EntityException {
        <<exception>>
    }
}

namespace domain_card {
    class Card {
        <<aggregate>>
        -typeCard : TypeCardEnum
        -categoryCard : CategoryCardEnum
        -credit : Credit
        -cardStatus : CardStatusEnum
        -cardAccountId : CardAccountId
        -paymentDay : PaymentDay
        +updateStatus(isOvercharged : Boolean) void
        +validateIfCardIfInDebt() void
        +getRatio() BigDecimal
        +close() void
    }
    class CardId {
        <<valueObject>>
        -value : Long
        +create(value : Long) CardId
        +getValue() Long
    }
    class CardAccountId {
        <<valueObject>>
        -value : Long
        +create(value : Long) CardAccountId
    }
    class Credit {
        <<valueObject>>
        -amount : Amount
        -debtTax : BigDecimal
        +create(amount : Amount, debtTax : BigDecimal) Credit
    }
    class PaymentDay {
        <<valueObject>>
        -value : Short
        +create(value : Short) PaymentDay
    }
    class CardException {
        <<exception>>
    }
}

namespace domain_balance {
    class Balance {
        <<entity>>
        +getCardId() CardId
        +getTotal() Amount
        +getAvailable() Amount
        +getOld() Amount
        +getDateRange() DateRange
        +getStatus() StatusEnum
        +getId() BalanceId
        +close() void
    }
    class BalanceOperable {
        <<entity>>
        -cardId : CardId
        -total : Amount
        -available : Amount
        -old : Amount
        -dateRange : DateRange
        +isOvercharged() boolean
    }
    class BalanceConsumo {
        <<entity>>
        +from(balance : Balance) BalanceConsumo
        +apply(consumptionAmount : Amount) void
    }
    class BalancePago {
        <<entity>>
        +from(balance : Balance) BalancePago
        +apply(paymentAmount : Amount) void
        +validateIfPaymentIsPossible() void
    }
    class BalanceSnapshot {
        <<entity>>
    }
    class BalanceFactory {
        <<factory>>
        +create(CreateBalanceRequestFirstDto) Balance
    }
    class BalanceFactoryImpl {
        <<factory>>
        +create(CreateBalanceRequestFirstDto) Balance
    }
    class BalanceId {
        <<valueObject>>
        -value : Long
    }
}

namespace domain_consumption {
    class Consumption {
        <<aggregate>>
        -consumptionId : ConsumptionId
        -cardId : Long
        -consumptionAmount : Amount
        -sellerName : String
        +builder() ConsumptionBuilder
    }
    class ConsumptionId {
        <<valueObject>>
        -value : UUID
    }
}

namespace domain_payment {
    class Payment {
        <<entity>>
        +validateIfPaymentIsPossible() void
        +getPaymentId() PaymentId
        +getCardId() Long
        +getPaymentAmount() Amount
    }
    class NormalPayment {
        <<entity>>
    }
    class MinimunPayment {
        <<entity>>
    }
    class Prepayment {
        <<entity>>
    }
    class TotalPayment {
        <<entity>>
    }
    class PaymentFactory {
        <<factory>>
        +create(CreatePaymentRequestFirstDto) Payment
    }
    class PaymentFactoryImpl {
        <<factory>>
        +create(CreatePaymentRequestFirstDto) Payment
    }
    class PaymentId {
        <<valueObject>>
        -value : UUID
    }
}

namespace domain_benefit {
    class Benefit {
        <<aggregate>>
        -cardId : Long
        -hasDiscount : Boolean
        -totalPoints : Point
        -multiplierPoints : BigDecimal
        +accumulate(amount : Amount, ratio : BigDecimal) void
        +discount(amount : Amount, point : Point) Amount
        +close() void
    }
    class BenefitId {
        <<valueObject>>
        -value : Long
    }
}

namespace domain_base {
    class Amount {
        <<valueObject>>
        -currency : Currency
        -amount : BigDecimal
        +create(currency : Currency, amount : BigDecimal) Amount
        +convertTo(targetCurrency : CurrencyEnum) BigDecimal
    }
    class Currency {
        <<valueObject>>
        -currency : CurrencyEnum
        -exchangeRate : BigDecimal
        +create(currency : CurrencyEnum, exchangeRate : BigDecimal) Currency
    }
    class DateRange {
        <<valueObject>>
        -startDate : LocalDate
        -endDate : LocalDate
    }
}

%% ================================================================
%% ZONA 4 — INFRASTRUCTURE: OUTBOUND ADAPTERS
%% ================================================================

namespace infrastructure_sqlserver {
    class CardJpaRepositoryAdapter {
        <<adapter>>
        -cardJpaRepository : CardJpaRepository
        -cardVOJpaRepository : CardVOJpaRepository
        -cardAccountJpaRepository : CardAccountJpaRepository
    }
    class BalanceJpaRepositoryAdapter {
        <<adapter>>
        -balanceJpaRepository : BalanceJpaRepository
        -balanceVOJpaRepository : BalanceVOJpaRepository
    }
    class BenefitJpaRepositoryAdapter {
        <<adapter>>
        -benefitJpaRepository : BenefitJpaRepository
        -benefitVOJpaRepository : BenefitVOJpaRepository
    }
    class CardJpaRepository {
        <<jpaRepo>>
        +save(CardEntity) CardEntity
    }
    class CardAccountJpaRepository {
        <<jpaRepo>>
    }
    class BalanceJpaRepository {
        <<jpaRepo>>
    }
    class BenefitJpaRepository {
        <<jpaRepo>>
    }
    class CardVOJpaRepository {
        <<jpaRepo>>
        +findActiveById(cardId : Long) Optional~CardEntityVO~
        +getCardAllProjectionByCardId(cardId : Long) Optional~CardSumaryProjection~
        +getCardCurrencyProjectionByCardId(cardId : Long) Optional~CardCurrencyProjection~
    }
    class BalanceVOJpaRepository {
        <<jpaRepo>>
    }
    class BenefitVOJpaRepository {
        <<jpaRepo>>
    }
    class CardAccountVOJpaRepository {
        <<jpaRepo>>
    }
    class CardEntity {
        <<entity>>
        -cardId : Long
        -typeCard : TypeCardEnum
        -categoryCard : CategoryCardEnum
    }
    class CardAccountEntity {
        <<entity>>
        -cardAccountId : Long
        -cardId : Long
        -creditTotal : BigDecimal
        -debtTax : BigDecimal
        -currency : CurrencyEnum
        -paymentDate : Short
        -cardStatus : CardStatusEnum
    }
    class BalanceEntity {
        <<entity>>
        -idBalance : Long
        -cardId : Long
        -totalAmount : BigDecimal
        -availableAmount : BigDecimal
        -oldAmount : BigDecimal
    }
    class BenefitEntity {
        <<entity>>
        -idBenefit : Long
        -cardId : Long
        -hasDiscount : Boolean
        -totalPoints : Integer
        -multiplierPoints : BigDecimal
    }
    class CardEntityVO {
        <<entity>>
        -cardId : Long
    }
    class CardAccountEntityVO {
        <<entity>>
    }
    class BalanceEntityVO {
        <<entity>>
    }
    class BenefitEntityVO {
        <<entity>>
    }
}

namespace infrastructure_mongo {
    class ConsumptionMongoRepositoryAdapter {
        <<adapter>>
        -consumptionMongoRepository : ConsumptionMongoRepository
    }
    class PaymentMongoRepositoryAdapter {
        <<adapter>>
        -paymentMongoRepository : PaymentMongoRepository
    }
    class ConsumptionMongoRepository {
        <<nosqlRepo>>
        +findByCardIdAndConsumptionDateBetween() List~ConsumptionEntityMongo~
        +findActiveById(consumptionId : UUID, cardId : String) Optional~ConsumptionEntityMongo~
    }
    class PaymentMongoRepository {
        <<nosqlRepo>>
        +findByCardIdAndPaymentDateBetween() List~PaymentEntityMongo~
        +findActiveById(paymentId : UUID, cardId : String) Optional~PaymentEntityMongo~
    }
    class ConsumptionEntityMongo {
        <<entity>>
        -consumptionId : UUID
        -cardId : String
        -amount : BigDecimal
        -currency : CurrencyEnum
        -sellerName : String
        -consumptionDate : LocalDateTime
    }
    class PaymentEntityMongo {
        <<entity>>
        -paymentId : UUID
        -cardId : String
        -amount : BigDecimal
        -currency : CurrencyEnum
        -paymentDate : LocalDateTime
    }
}

namespace infrastructure_cosmos {
    class ConsumptionCosmosRepositoryAdapter {
        <<adapter>>
        -consumptionCosmosRepository : ConsumptionCosmosRepository
    }
    class PaymentCosmosRepositoryAdapter {
        <<adapter>>
        -paymentCosmosRepository : PaymentCosmosRepository
    }
    class ConsumptionCosmosRepository {
        <<nosqlRepo>>
        +findByCardIdAndConsumptionDateBetween() List~ConsumptionEntityCosmos~
        +findActiveById(consumptionId : UUID, cardId : String) Optional~ConsumptionEntityCosmos~
    }
    class PaymentCosmosRepository {
        <<nosqlRepo>>
    }
    class ConsumptionEntityCosmos {
        <<entity>>
        -consumptionId : UUID
        -cardId : String
        -currency : CurrencyEnum
    }
    class PaymentEntityCosmos {
        <<entity>>
        -paymentId : UUID
        -cardId : String
    }
}

namespace infrastructure_ws {
    class CurrencyWSAdapter {
        <<adapter>>
        -currencyJsonServerWSRepository : CurrencyJsonServerWSRepository
        +load(currency : String) Optional~CurrencyResponseDto~
    }
    class CurrencyJsonServerWSRepository {
        <<externalRepo>>
        -restClient : RestClient
        +findByCurrency(currency : String) CurrencyWsDto
    }
}

namespace infrastructure_generator {
    class SnowflakeGenerator {
        <<adapter>>
        -machineId : long
        -sequence : long
        -lastTimestamp : long
        +load() Optional~Long~
    }
}

%% ================================================================
%% ZONA 5 — INFRASTRUCTURE: CROSS-CUTTING
%% ================================================================

namespace infrastructure_crosscutting {
    class TransactionalUseCase {
        <<aspect>>
    }
    class TransactionalUseCaseAspect {
        <<aspect>>
        +aroundTransactionalUseCase(ProceedingJoinPoint) Object
    }
    class GlobalControllAdvice {
        <<exception>>
        +handleGenericException(Exception, WebRequest) ResponseEntity
    }
    class CxfWebServiceConfig {
        <<config>>
        +creditCardServiceEndpoint(Bus, CreditCardControllerSOAPImpl) Endpoint
    }
    class SOAPPresenterConfig {
        <<config>>
        +creditCardDelegateSOAP() CreditCardDelegateSOAP
        +cardApiMapperRequestCommandSoap() CardApiMapperRequestCommand_SOAP
        +consumptionApiMapperRequestCommandSoap() ConsumptionApiMapperRequestCommand_SOAP
        +paymentApiMapperRequestCommandSoap() PaymentApiMapperRequestCommand_SOAP
    }
    class RestPresenterConfig {
        <<config>>
        +cardManagementDelegate() CardManagementDelegate
    }
    class RestClientConfig {
        <<config>>
        +restClient() RestClient
    }
    class CurrencyWSAdapterConfig {
        <<config>>
        +currencyWSAdapter() CurrencyWSAdapter
        +mapperCurrency() MapperCurrency
        +currencyJsonServerWSRepository() CurrencyJsonServerWSRepository
    }
}

%% ================================================================
%% RELACIONES — PRESENTACIÓN SOAP
%% ================================================================
CreditCardControllerSOAPImpl ..|> CreditCardControllerSOAP : implements
CreditCardControllerSOAPImpl --> CreditCardDelegateSOAP : delegates to
CreditCardDelegateSOAPImpl ..|> CreditCardDelegateSOAP : implements
CreditCardDelegateSOAPImpl --> CardCreateService : calls
CreditCardDelegateSOAPImpl --> CardCloseService : calls
CreditCardDelegateSOAPImpl --> CardFindByIdService : calls
CreditCardDelegateSOAPImpl --> PaymentProcessService : calls
CreditCardDelegateSOAPImpl --> PaymentCancelService : calls
CreditCardDelegateSOAPImpl --> PaymentFindByDatesAndCardIdService : calls
CreditCardDelegateSOAPImpl --> ConsumptionProcessService : calls
CreditCardDelegateSOAPImpl --> ConsumptionCancelService : calls
CreditCardDelegateSOAPImpl --> ConsumptionSplitService : calls
CreditCardDelegateSOAPImpl --> ConsumptionFindByDatesAndCardIdService : calls
CreditCardDelegateSOAPImpl --> CardApiMapperRequestCommand_SOAP : uses
CreditCardDelegateSOAPImpl --> ConsumptionApiMapperRequestCommand_SOAP : uses
CreditCardDelegateSOAPImpl --> PaymentApiMapperRequestCommand_SOAP : uses
CardApiMapperRequestCommandImpl_SOAP ..|> CardApiMapperRequestCommand_SOAP : implements
ConsumptionApiMapperRequestCommandImpl_SOAP ..|> ConsumptionApiMapperRequestCommand_SOAP : implements
PaymentApiMapperRequestCommandImpl_SOAP ..|> PaymentApiMapperRequestCommand_SOAP : implements

%% RELACIONES — PRESENTACIÓN REST
CardManagementApiImpl ..|> CardManagementApi : implements
CardManagementApiImpl --> CardManagementDelegate : delegates to
CardManagementDelegateImpl ..|> CardManagementDelegate : implements
CardManagementDelegateImpl --> CardCreateService : calls
CardManagementDelegateImpl --> CardCloseService : calls
CardManagementDelegateImpl --> CardFindByIdService : calls
CardManagementDelegateImpl --> PaymentProcessService : calls
CardManagementDelegateImpl --> PaymentCancelService : calls
CardManagementDelegateImpl --> PaymentFindByDatesAndCardIdService : calls
CardManagementDelegateImpl --> ConsumptionProcessService : calls
CardManagementDelegateImpl --> ConsumptionCancelService : calls
CardManagementDelegateImpl --> ConsumptionSplitService : calls
CardManagementDelegateImpl --> ConsumptionFindByDatesAndCardIdService : calls
CardManagementDelegateImpl --> CardApiMapperRequestCommand_REST : uses
CardManagementDelegateImpl --> ConsumptionApiMapperRequestCommand_REST : uses
CardManagementDelegateImpl --> PaymentApiMapperRequestCommand_REST : uses
CardApiMapperRequestCommandImpl_REST ..|> CardApiMapperRequestCommand_REST : implements
ConsumptionApiMapperRequestCommandImpl_REST ..|> ConsumptionApiMapperRequestCommand_REST : implements
PaymentApiMapperRequestCommandImpl_REST ..|> PaymentApiMapperRequestCommand_REST : implements

%% RELACIONES — CONFIG
CxfWebServiceConfig ..> CreditCardControllerSOAPImpl : publishes endpoint
SOAPPresenterConfig ..> CreditCardDelegateSOAPImpl : creates bean
RestPresenterConfig ..> CardManagementDelegateImpl : creates bean
CurrencyWSAdapterConfig ..> CurrencyWSAdapter : creates bean
CurrencyWSAdapterConfig ..> CurrencyJsonServerWSRepository : creates bean
RestClientConfig ..> CurrencyJsonServerWSRepository : provides RestClient

%% RELACIONES — APPLICATION CARD
CardCreateService ..|> CardCreateUseCase : implements
CardCloseService ..|> CardCloseUseCase : implements
CardFindByIdService ..|> CardFindByIdQuery : implements
CardCreateService --> BusinessServiceCard : uses
CardCreateService --> BusinessServiceBalance : uses
CardCreateService --> BusinessServiceBenefit : uses
CardCreateService --> IdGeneratePort : uses
CardCreateService --> LoadCurrencyWSPort : uses
CardCreateService --> BalanceFactory : uses
CardCloseService --> BusinessServiceCard : uses
CardCloseService --> BusinessServiceBalance : uses
CardCloseService --> BusinessServiceBenefit : uses
CardFindByIdService --> CardBalanceBenefitFindByIdPort : uses
BusinessServiceCardImpl ..|> BusinessServiceCard : implements
BusinessServiceCardImpl --> CardFindByIdPort : uses
BusinessServiceCardImpl --> CardDBFindCurrencyPort : uses
BusinessServiceCardImpl --> CardDBSavePort : uses
BusinessServiceCardImpl --> LoadCurrencyWSPort : uses

%% RELACIONES — APPLICATION CONSUMPTION
ConsumptionProcessService ..|> ConsumptionProcessUseCase : implements
ConsumptionCancelService ..|> ConsumptionCancelUseCase : implements
ConsumptionSplitService ..|> ConsumptionSplitUseCase : implements
ConsumptionFindByDatesAndCardIdService ..|> ConsumptionFindByDatesAndCardIdQuery : implements
ConsumptionProcessService --> BusinessServiceCard : uses
ConsumptionProcessService --> BusinessServiceBalance : uses
ConsumptionProcessService --> BusinessServiceBenefit : uses
ConsumptionProcessService --> BusinessServiceConsumption : uses
ConsumptionProcessService --> LoadCurrencyWSPort : uses
BusinessServiceConsumptionImpl ..|> BusinessServiceConsumption : implements
BusinessServiceConsumptionImpl --> ConsumptionDBSavePort : uses
BusinessServiceConsumptionImpl --> ConsumptionFindByIdPort : uses
BusinessServiceConsumptionImpl --> ConsumptionDBFindCurrencyPort : uses

%% RELACIONES — APPLICATION PAYMENT
PaymentProcessService ..|> PaymentProcessUseCase : implements
PaymentCancelService ..|> PaymentCancelUseCase : implements
PaymentFindByDatesAndCardIdService ..|> PaymentFindByDatesAndCardIdQuery : implements
PaymentProcessService --> BusinessServiceCard : uses
PaymentProcessService --> BusinessServiceBalance : uses
PaymentProcessService --> BusinessServiceBenefit : uses
PaymentProcessService --> BusinessServicePayment : uses
PaymentProcessService --> LoadCurrencyWSPort : uses
PaymentProcessService --> PaymentFactory : uses
BusinessServicePaymentImpl ..|> BusinessServicePayment : implements
BusinessServicePaymentImpl --> PaymentDBSavePort : uses
BusinessServicePaymentImpl --> PaymentFindByIdPort : uses
BusinessServicePaymentImpl --> PaymentDBFindCurrencyPort : uses

%% RELACIONES — APPLICATION BALANCE / BENEFIT
BusinessServiceBalanceImpl ..|> BusinessServiceBalance : implements
BusinessServiceBalanceImpl --> BalanceDBSavePort : uses
BusinessServiceBalanceImpl --> BalanceDBFindByIdPort : uses
BusinessServiceBenefitImpl ..|> BusinessServiceBenefit : implements
BusinessServiceBenefitImpl --> BenefitDBSavePort : uses
BusinessServiceBenefitImpl --> BenefitDBFindByIdPort : uses

%% RELACIONES — DOMAIN
Card --|> AggregateRoot : extends
Benefit --|> AggregateRoot : extends
Card --> CardId : identity
Card --> CardAccountId : has
Card --> Credit : has
Card --> PaymentDay : has
BalanceConsumo --|> BalanceOperable : extends
BalancePago --|> BalanceOperable : extends
BalanceOperable ..|> Balance : implements
BalanceSnapshot ..|> Balance : implements
BalanceFactoryImpl ..|> BalanceFactory : implements
NormalPayment ..|> Payment : implements
MinimunPayment ..|> Payment : implements
Prepayment ..|> Payment : implements
TotalPayment ..|> Payment : implements
PaymentFactoryImpl ..|> PaymentFactory : implements
Amount --> Currency : contains
CardCreateService --> Card : creates
ConsumptionProcessService --> Consumption : creates
ConsumptionProcessService --> BalanceConsumo : uses
PaymentProcessService --> BalancePago : uses

%% RELACIONES — OUTBOUND SQL SERVER
CardJpaRepositoryAdapter ..|> CardDBSavePort : implements
CardJpaRepositoryAdapter ..|> CardFindByIdPort : implements
CardJpaRepositoryAdapter ..|> CardDBFindCurrencyPort : implements
CardJpaRepositoryAdapter ..|> CardBalanceBenefitFindByIdPort : implements
CardJpaRepositoryAdapter --> CardJpaRepository : uses
CardJpaRepositoryAdapter --> CardVOJpaRepository : uses
CardJpaRepositoryAdapter --> CardAccountJpaRepository : uses
BalanceJpaRepositoryAdapter ..|> BalanceDBSavePort : implements
BalanceJpaRepositoryAdapter ..|> BalanceDBFindByIdPort : implements
BalanceJpaRepositoryAdapter --> BalanceJpaRepository : uses
BalanceJpaRepositoryAdapter --> BalanceVOJpaRepository : uses
BenefitJpaRepositoryAdapter ..|> BenefitDBSavePort : implements
BenefitJpaRepositoryAdapter ..|> BenefitDBFindByIdPort : implements
BenefitJpaRepositoryAdapter --> BenefitJpaRepository : uses
BenefitJpaRepositoryAdapter --> BenefitVOJpaRepository : uses
CardJpaRepository --> CardEntity : manages
CardAccountJpaRepository --> CardAccountEntity : manages
BalanceJpaRepository --> BalanceEntity : manages
BenefitJpaRepository --> BenefitEntity : manages
CardVOJpaRepository --> CardEntityVO : manages
BalanceVOJpaRepository --> BalanceEntityVO : manages
BenefitVOJpaRepository --> BenefitEntityVO : manages
CardEntityVO "1" *-- "1" CardAccountEntityVO : OneToOne
CardEntityVO "1" *-- "1" BalanceEntityVO : OneToOne
CardEntityVO "1" *-- "1" BenefitEntityVO : OneToOne

%% RELACIONES — OUTBOUND MONGODB
ConsumptionMongoRepositoryAdapter ..|> ConsumptionDBSavePort : implements
ConsumptionMongoRepositoryAdapter ..|> ConsumptionFindByIdPort : implements
ConsumptionMongoRepositoryAdapter ..|> ConsumptionDBFindCurrencyPort : implements
ConsumptionMongoRepositoryAdapter ..|> ConsumptionsDBFindByDatesAndCardIdPort : implements
ConsumptionMongoRepositoryAdapter --> ConsumptionMongoRepository : uses
ConsumptionMongoRepository --> ConsumptionEntityMongo : manages
PaymentMongoRepositoryAdapter ..|> PaymentDBSavePort : implements
PaymentMongoRepositoryAdapter ..|> PaymentFindByIdPort : implements
PaymentMongoRepositoryAdapter ..|> PaymentDBFindCurrencyPort : implements
PaymentMongoRepositoryAdapter ..|> PaymentsFindByDatesAndCardIdPort : implements
PaymentMongoRepositoryAdapter --> PaymentMongoRepository : uses
PaymentMongoRepository --> PaymentEntityMongo : manages

%% RELACIONES — OUTBOUND COSMOS DB
ConsumptionCosmosRepositoryAdapter ..|> ConsumptionDBSavePort : implements
ConsumptionCosmosRepositoryAdapter ..|> ConsumptionFindByIdPort : implements
ConsumptionCosmosRepositoryAdapter ..|> ConsumptionDBFindCurrencyPort : implements
ConsumptionCosmosRepositoryAdapter ..|> ConsumptionsDBFindByDatesAndCardIdPort : implements
ConsumptionCosmosRepositoryAdapter --> ConsumptionCosmosRepository : uses
ConsumptionCosmosRepository --> ConsumptionEntityCosmos : manages
PaymentCosmosRepositoryAdapter ..|> PaymentDBSavePort : implements
PaymentCosmosRepositoryAdapter ..|> PaymentFindByIdPort : implements
PaymentCosmosRepositoryAdapter ..|> PaymentDBFindCurrencyPort : implements
PaymentCosmosRepositoryAdapter ..|> PaymentsFindByDatesAndCardIdPort : implements
PaymentCosmosRepositoryAdapter --> PaymentCosmosRepository : uses
PaymentCosmosRepository --> PaymentEntityCosmos : manages

%% RELACIONES — WS / GENERATOR / AOP
CurrencyWSAdapter ..|> LoadCurrencyWSPort : implements
CurrencyWSAdapter --> CurrencyJsonServerWSRepository : calls
SnowflakeGenerator ..|> IdGeneratePort : implements
TransactionalUseCaseAspect --> TransactionalUseCase : intercepts
```

---

## Leyenda de Estereotipos

| Estereotipo | Capa / Rol |
|---|---|
| `<<aggregate>>` | Aggregate Root (Domain) |
| `<<entity>>` | Entity / JPA Entity |
| `<<valueObject>>` | Value Object |
| `<<factory>>` | Factory |
| `<<useCase>>` | Input Port / Use Case |
| `<<appService>>` | Application Service (implementación UC) |
| `<<bizService>>` | Business Service (orquestador interno) |
| `<<portIn>>` | Puerto de entrada (interfaz) |
| `<<portOut>>` | Puerto de salida (interfaz) |
| `<<controller>>` | Adaptador inbound Controller |
| `<<delegate>>` | Delegate (lógica presenter) |
| `<<adapter>>` | Adaptador outbound |
| `<<jpaRepo>>` | Repositorio JPA (Spring Data) |
| `<<nosqlRepo>>` | Repositorio NoSQL |
| `<<externalRepo>>` | Cliente REST externo |
| `<<mapper>>` | Mapper request/response |
| `<<aspect>>` | AOP / Cross-cutting |
| `<<config>>` | Configuración Spring |
| `<<exception>>` | Manejo de excepciones |
| `<<command>>` | Objeto comando (CQRS) |
| `<<query>>` | Objeto consulta (CQRS) |

---

## Regla de Dependencia

```
Domain  ←  Application  ←  Infrastructure
```

Toda dependencia cruza exclusivamente a través de **interfaces (Ports & Adapters)**.  
El generador de IDs utiliza el algoritmo **Snowflake** (sin base de datos).  
El endpoint SOAP se registra via **Apache CXF** en `/ws/CreditCardService`.

````
<userPrompt>
Provide the fully rewritten file, incorporating the suggested code change. You must produce the complete file.
</userPrompt>

