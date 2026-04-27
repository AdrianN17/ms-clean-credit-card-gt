package com.bank.credit_bank.infrastructure.presenter.soap.util;

import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.Tracking;
import lombok.experimental.UtilityClass;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.UUID;

@UtilityClass
public class SoapMapperResponse {

    public static Tracking buildTracking() {
        Tracking tracking = new Tracking();
        tracking.setTrackingId(UUID.randomUUID().toString());
        tracking.setOperationDate(toXMLGregorianCalendar(LocalDateTime.now()));
        return tracking;
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(LocalDateTime localDateTime) {
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(LocalDate localDate) {
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}

