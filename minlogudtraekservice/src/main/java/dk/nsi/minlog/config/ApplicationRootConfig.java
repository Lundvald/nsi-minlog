package dk.nsi.minlog.config;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.sql.DataSource;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.springsupport.factory.EbeanServerFactoryBean;
import com.avaje.ebean.springsupport.txn.SpringAwareJdbcTransactionManager;
import com.googlecode.flyway.core.Flyway;

import static java.lang.System.getProperty;

@Configuration
@ComponentScan({"dk.nsi.minlog.server"})
//@EnableScheduling
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationRootConfig implements TransactionManagementConfigurer {
    @Value("${jdbc.url}") String url;
    @Value("${jdbc.username}") String username;
    @Value("${jdbc.password}") String password;

    @Bean
    public static PropertyPlaceholderConfigurer configuration() {
        final PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
        props.setLocations(new Resource[]{
                new ClassPathResource("default.properties"),
                new ClassPathResource("minlog." + getProperty("user.name") + ".properties"),
                new ClassPathResource("jdbc.default.properties"),
                new ClassPathResource("jdbc." + getProperty("user.name") + ".properties"),
                new FileSystemResource(getProperty("user.home") + "/.minlog/passwords.properties")
        });
        props.setIgnoreResourceNotFound(true);
        props.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return props;
    }

// Database layer
    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource(
                url,
                username,
                password
        );
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;
    }
    
    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }

    @Bean
    public EbeanServerFactoryBean ebeanServer(DataSource dataSource) throws Exception {
        final EbeanServerFactoryBean factoryBean = new EbeanServerFactoryBean();
        final ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName("localhostConfig");
        serverConfig.setClasses(new ArrayList<Class<?>>(new Reflections("dk.nsi.minlog.domain").getTypesAnnotatedWith(Entity.class)));
        serverConfig.setDataSource(dataSource);
        serverConfig.setExternalTransactionManager(new SpringAwareJdbcTransactionManager());
        factoryBean.setServerConfig(serverConfig);
        return factoryBean;
    }
    
    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        return flyway;
    }
// Database layer end


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
    
    @Bean(name = {"nspMarshaller", "nspUnarshaller"})
    public Jaxb2Marshaller nspMarshaller() {
        final Jaxb2Marshaller bean = new Jaxb2Marshaller();
        bean.setContextPath(
                "dk.nsi.minlog._2012._05._24"
        );
        return bean;
    }
}
