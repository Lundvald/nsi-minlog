	package dk.nsi.minlog.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.ws.server.MessageDispatcher;

/**
 * Setup of web context and dispatcher.
 * 
 * @author kpi
 *
 */
@Configuration
@DependsOn("WSConfig")
public class WebConfig extends WebMvcConfigurationSupport {
    @Inject
    ApplicationRootConfig applicationRootConfig;

    @Inject
    WSConfig wsConfig;

    @Inject 
    MessageDispatcher messageDispatcher;
        
    @Override
    @Bean
    public BeanNameUrlHandlerMapping beanNameHandlerMapping() {
        final BeanNameUrlHandlerMapping mapping = super.beanNameHandlerMapping();
        mapping.setDefaultHandler(messageDispatcher);
        return mapping;
    }    
}