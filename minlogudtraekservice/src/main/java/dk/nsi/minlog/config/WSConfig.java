package dk.nsi.minlog.config;

import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
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

@Configuration
@ComponentScan({"dk.nsi.minlog.web", "dk.nsi.minlog.server"})
@ImportResource({"classpath:/dk/trifork/dgws/dgws-protection.xml"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WSConfig {
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
    


    @Bean(name = {"serviceMarshaller", "serviceUnmarshaller"}) @Primary
    public Jaxb2Marshaller serviceMarshaller() {
        final Jaxb2Marshaller bean = new Jaxb2Marshaller();
        bean.setContextPaths(
                "dk.nsi.minlog._2012._05._24",
                "dk.medcom.dgws._2006._04.dgws_1_0",
                "org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0",
                "org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0",
                "org.w3._2000._09.xmldsig",
                "oasis.names.tc.saml._2_0.assertion",
                "dk.oio.rep.cpr_dk.xml.schemas.core._2005._03._18"
        );
        return bean;
    }
    
    @Bean(name = {"nspMarshaller", "nspUnmarshaller"})
    public Jaxb2Marshaller nspMarshaller() {
        final Jaxb2Marshaller bean = new Jaxb2Marshaller();
        bean.setContextPath(
                "dk.nsi.minlog._2012._05._24"
        );
        return bean;
    }
}
