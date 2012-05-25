package dk.nsi.minlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.EndpointMapping;
import org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.server.SoapMessageDispatcher;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.SoapEnvelopeLoggingInterceptor;
import org.springframework.ws.transport.http.WebServiceMessageReceiverHandlerAdapter;
import org.springframework.ws.transport.http.WsdlDefinitionHandlerAdapter;
import org.springframework.ws.wsdl.WsdlDefinition;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

import java.util.HashMap;

@Configuration
@ComponentScan({"dk.nsi.minlog.web"})
@ImportResource({"classpath:/dk/trifork/dgws/dgws-protection.xml"})
public class WebConfig extends WebMvcConfigurationSupport {
    //@Inject
    //ApplicationRootConfig applicationRootConfig;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //TODO: add *.xsd mapping, http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-config-static-resources
    }

    @Bean
    public WsdlDefinition serviceDefinition() {
        final DefaultWsdl11Definition bean = new DefaultWsdl11Definition();
        bean.setSchema(schema1XsdSchema());
        bean.setPortTypeName("Minlogudtraekservice");
        bean.setLocationUri("http://localhost:8080/Minlogudtraekservice");
        return bean;
    }

    @Bean
    public SimpleXsdSchema schema1XsdSchema() {
        return new SimpleXsdSchema(new ClassPathResource("schema/minlogudtraekservice.xsd"));
    }

    @Bean
    public WsdlDefinitionHandlerAdapter wsdlDefinitionHandlerAdapter() {
        return new WsdlDefinitionHandlerAdapter();
    }

    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(1);
        final HashMap<String, Object> urlMap = new HashMap<String, Object>();
        urlMap.put("*.wsdl", serviceDefinition());
        mapping.setUrlMap(urlMap);
        return mapping;
    }

    @Override
    @Bean
    public BeanNameUrlHandlerMapping beanNameHandlerMapping() {
        final BeanNameUrlHandlerMapping mapping = super.beanNameHandlerMapping();
        mapping.setDefaultHandler(soapMessageDispatcher());
        return mapping;
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
