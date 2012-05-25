package dk.nsi.minlog.config;

import org.springframework.context.annotation.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@ComponentScan({"dk.nsi.minlog.server"})
//@EnableScheduling
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationRootConfig implements TransactionManagementConfigurer {
//    @Value("${jdbc.url}") String url;
//    @Value("${jdbc.username}") String username;
//    @Value("${jdbc.password}") String password;
/*
    @Bean
    public static PropertyPlaceholderConfigurer configuration() {
        final PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
        props.setLocations(new Resource[]{
                new ClassPathResource("default.properties"),
                new ClassPathResource("bms." + getProperty("user.name") + ".properties"),
                new ClassPathResource("jdbc.default.properties"),
                new ClassPathResource("jdbc." + getProperty("user.name") + ".properties"),
                new FileSystemResource(getProperty("user.home") + "/.bemyndigelsesservice/passwords.properties")
        });
        props.setIgnoreResourceNotFound(true);
        props.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return props;
    }
*/

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return null;
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


}
