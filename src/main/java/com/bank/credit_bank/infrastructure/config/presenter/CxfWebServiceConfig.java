package com.bank.credit_bank.infrastructure.config.presenter;

import com.bank.credit_bank.infrastructure.presenter.soap.controller.CreditCardControllerSOAPImpl;
import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("old")
public class CxfWebServiceConfig {

    @Bean
    public Endpoint creditCardServiceEndpoint(Bus bus, CreditCardControllerSOAPImpl creditCardControllerSOAP) {
        EndpointImpl endpoint = new EndpointImpl(bus, creditCardControllerSOAP);
        endpoint.publish("/CreditCardService");
        return endpoint;
    }
}

