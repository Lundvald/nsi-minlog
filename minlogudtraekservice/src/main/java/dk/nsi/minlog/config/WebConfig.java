package dk.nsi.minlog.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.ws.soap.server.SoapMessageDispatcher;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Inject
    ApplicationRootConfig applicationRootConfig;

    @Inject
    WSConfig wsConfig;

    @Override
    @Bean
    public BeanNameUrlHandlerMapping beanNameHandlerMapping() {
        final BeanNameUrlHandlerMapping mapping = super.beanNameHandlerMapping();
        mapping.setDefaultHandler(soapMessageDispatcher());
        return mapping;
    }


    @Bean
    public SoapMessageDispatcher soapMessageDispatcher() {
        return new SoapMessageDispatcher();
    }

}
