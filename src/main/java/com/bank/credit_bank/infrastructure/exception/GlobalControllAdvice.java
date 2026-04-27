package com.bank.credit_bank.infrastructure.exception;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.benefit.exceptions.ApplicationBenefitException;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.consumption.exceptions.ApplicationConsumptionException;
import com.bank.credit_bank.application.generator.exceptions.ApplicationGeneratorException;
import com.bank.credit_bank.application.generic.exceptions.ApplicationException;
import com.bank.credit_bank.application.payment.exceptions.ApplicationPaymentException;
import com.bank.credit_bank.domain.base.exceptions.DateRangeException;
import com.bank.credit_bank.domain.benefit.model.exceptions.BenefitException;
import com.bank.credit_bank.domain.benefit.model.exceptions.DiscountPolicyException;
import com.bank.credit_bank.domain.benefit.model.exceptions.PointException;
import com.bank.credit_bank.domain.card.model.vo.cardAccountId.CardAccountException;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardIdException;
import com.bank.credit_bank.domain.card.model.vo.creditId.CreditException;
import com.bank.credit_bank.domain.consumption.model.exceptions.ConsumptionException;
import com.bank.credit_bank.domain.generic.exceptions.EntityException;
import com.bank.credit_bank.domain.payment.model.exceptions.PaymentException;
import com.bank.credit_bank.infrastructure.db.generic.exception.PersistenceException;
import com.bank.credit_bank.infrastructure.db.nosql.common.exception.ConsumptionPersistanceException;
import com.bank.credit_bank.infrastructure.db.nosql.common.exception.PaymentPersistanceException;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.BalancePersistanceException;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.BenefitPersistanceException;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.exception.CardPersistanceException;
import com.bank.credit_bank.infrastructure.presenter.rest.exception.RequestValidationException;
import com.bank.credit_bank.infrastructure.ws.exception.ConverterWSClientException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalControllAdvice {

    // ── Validación automática de @Valid @RequestBody (BAD REQUEST → 400) ─────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fe -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("field", fe.getField());
                    error.put("message", fe.getDefaultMessage());
                    error.put("rejectedValue",
                            fe.getRejectedValue() != null ? fe.getRejectedValue().toString() : "null");
                    return error;
                })
                .toList();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("code", "VALIDATION_ERROR");
        body.put("message", "Request validation failed");
        body.put("fieldErrors", fieldErrors);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ── @Validated en interfaz/controller con @Min/@Max en objetos anidados ──
    // Cuando @Validated está activo a nivel de clase, el MethodValidationInterceptor
    // lanza ConstraintViolationException en lugar de dejar que BindingResult la capture.

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {

        List<Map<String, String>> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(cv -> {
                    Map<String, String> error = new HashMap<>();
                    // Extraer solo el nombre del campo (último segmento del propertyPath)
                    String path = cv.getPropertyPath().toString();
                    String field = path.contains(".")
                            ? path.substring(path.lastIndexOf('.') + 1)
                            : path;
                    error.put("field", field);
                    error.put("message", cv.getMessage());
                    error.put("rejectedValue",
                            cv.getInvalidValue() != null ? cv.getInvalidValue().toString() : "null");
                    return error;
                })
                .toList();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("code", "VALIDATION_ERROR");
        body.put("message", "Request validation failed");
        body.put("fieldErrors", fieldErrors);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ── Validación manual con BindingResult (BAD REQUEST → 400) ──────────────

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<Map<String, Object>> handleRequestValidationException(
            RequestValidationException ex, WebRequest request) {

        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fe -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("field", fe.getField());
                    error.put("message", fe.getDefaultMessage());
                    error.put("rejectedValue",
                            fe.getRejectedValue() != null ? fe.getRejectedValue().toString() : "null");
                    return error;
                })
                .toList();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("code", "VALIDATION_ERROR");
        body.put("message", "Request validation failed");
        body.put("fieldErrors", fieldErrors);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ── Persistencia NOT FOUND (404) ─────────────────────────────────────────

    @ExceptionHandler({
            CardPersistanceException.class,
            BenefitPersistanceException.class,
            BalancePersistanceException.class,
            ConsumptionPersistanceException.class,
            PaymentPersistanceException.class
    })
    public ResponseEntity<Map<String, Object>> handleCustomPersistenceException(
            RuntimeException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "PERSISTENCE_ERROR", ex.getMessage(), request);
    }

    // ── Persistencia genérica (500) ──────────────────────────────────────────

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Map<String, Object>> handleGenericPersistenceException(
            PersistenceException ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "PERSISTENCE_ERROR", ex.getMessage(), request);
    }

    // ── Dominio (BAD REQUEST → 400) ──────────────────────────────────────────

    @ExceptionHandler({
            EntityException.class,
            ConsumptionException.class,
            BenefitException.class,
            PointException.class,
            CardIdException.class,
            CardAccountException.class,
            CreditException.class,
            DiscountPolicyException.class,
            DateRangeException.class,
            PaymentException.class
    })
    public ResponseEntity<Map<String, Object>> handleDomainException(
            RuntimeException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, "DOMAIN_ERROR", ex.getMessage(), request);
    }

    // ── Aplicación (UNPROCESSABLE ENTITY → 422) ──────────────────────────────

    @ExceptionHandler({
            ApplicationException.class,
            ApplicationCardException.class,
            ApplicationConsumptionException.class,
            ApplicationPaymentException.class,
            ApplicationGeneratorException.class,
            ApplicationBenefitException.class,
            ApplicationBalanceException.class
    })
    public ResponseEntity<Map<String, Object>> handleApplicationException(
            ApplicationException ex, WebRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "APPLICATION_ERROR", ex.getMessage(), request);
    }

    // ── WS / Client (BAD GATEWAY → 502) ──────────────────────────────────────

    @ExceptionHandler(ConverterWSClientException.class)
    public ResponseEntity<Map<String, Object>> handleConverterWSClientException(
            ConverterWSClientException ex, WebRequest request) {
        return buildResponse(HttpStatus.BAD_GATEWAY, "WS_CLIENT_ERROR", ex.getMessage(), request);
    }

    // ── Fallback general (500) ────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "An unexpected error occurred", request);
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String errorCode, String message, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("code", errorCode);
        body.put("message", message != null ? message : "No message available");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, status);
    }
}
