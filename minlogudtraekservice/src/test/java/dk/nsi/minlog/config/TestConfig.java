package dk.nsi.minlog.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.EndpointMapping;
import org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.server.SoapMessageDispatcher;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.SoapEnvelopeLoggingInterceptor;
import org.springframework.ws.transport.http.WebServiceMessageReceiverHandlerAdapter;
import org.springframework.ws.wsdl.WsdlDefinition;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

@Configuration
@ComponentScan({"dk.nsi.minlog.web"})
@ImportResource({"classpath:/dk/trifork/dgws/dgws-protection.xml"})
public class TestConfig {
    @Inject
    ApplicationRootConfig applicationRootConfig;

    @Bean
    public WsdlDefinition serviceDefinition() {
        final DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
        bean.setSchema(schema1XsdSchema());
        bean.setPortTypeName("Minlogudtraekservice");
        return bean;
    }

    @Bean
    public SimpleXsdSchema schema1XsdSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schema/minlogudtraekservice.xsd"));
    }

    @Bean
    public WebServiceMessageReceiverHandlerAdapter webServiceMessageReceiverHandlerAdapter(WebServiceMessageFactory messageFactory) {
        final WebServiceMessageReceiverHandlerAdapter bean = new WebServiceMessageReceiverHandlerAdapter();
        bean.setMessageFactory(messageFactory);
        return bean;
    }

    @Bean
    public SoapMessageDispatcher soapMessageDispatcher() {
        return new SoapMessageDispatcher();
    }

    @Bean
    public WebServiceMessageFactory messageFactory() {
        return new SaajSoapMessageFactory();
    }

    @Bean
    public EndpointMapping endpointMapping(EndpointInterceptor[] endpointInterceptors) {
        final PayloadRootAnnotationMethodEndpointMapping mapping = new PayloadRootAnnotationMethodEndpointMapping();
        mapping.setInterceptors(endpointInterceptors);
        return mapping;
    }

    @Bean
    public EndpointInterceptor SoapEnvelopeEndpointInterceptor() {
        return new SoapEnvelopeLoggingInterceptor();
    }

    @Bean
    public EndpointInterceptor payloadValidationEndpointInterceptor() {
        final PayloadValidatingInterceptor interceptor = new PayloadValidatingInterceptor();
        interceptor.setSchemas(new Resource[]{
                new ClassPathResource("schema/minlogudtraekservice.xsd"),
        });
        interceptor.setValidateRequest(true);
        interceptor.setValidateResponse(false);
        return interceptor;
    }
}
