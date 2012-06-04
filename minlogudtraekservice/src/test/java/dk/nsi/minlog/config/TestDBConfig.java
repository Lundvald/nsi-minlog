package dk.nsi.minlog.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.sql.DataSource;

import org.reflections.Reflections;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.springsupport.factory.EbeanServerFactoryBean;
import com.avaje.ebean.springsupport.txn.SpringAwareJdbcTransactionManager;
import com.googlecode.flyway.core.Flyway;

@Configuration
@EnableTransactionManagement
@ComponentScan("dk.nsi.minlog.server")
public class TestDBConfig implements TransactionManagementConfigurer {
    @Bean
    public static PropertyPlaceholderConfigurer configuration() {
        final PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
        props.setLocations(new Resource[]{
                new ClassPathResource("default.properties")
        });
        props.setIgnoreResourceNotFound(true);
        props.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return props;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource initDS = new DriverManagerDataSource(
        		"jdbc:mysql:mxj:///",
                "root",
                ""
        );
        initDS.setDriverClassName("com.mysql.jdbc.Driver");        
		try {
	        Connection c = initDS.getConnection();
	        c.createStatement().execute("drop database if exists minlog");
	        c.createStatement().execute("create database minlog");
	        c.close();
		} catch (SQLException e) {} 
    	
        final DriverManagerDataSource dataSource = new DriverManagerDataSource(
        		"jdbc:mysql:mxj:///minlog",
                "root",
                ""
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
    public Flyway flyway(DataSource dataSource){  	
        Flyway flyway = new Flyway();
        flyway.setDisableInitCheck(true);
        flyway.setDataSource(dataSource);       
        return flyway;
    }
       
    @Bean
    @DependsOn("flyway")
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
}