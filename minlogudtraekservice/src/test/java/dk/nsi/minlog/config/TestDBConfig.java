package dk.nsi.minlog.config;

import java.io.File;
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
	public static String JAVA_IO_TMPDIR = "java.io.tmpdir";

	@Bean
	public File getDatabaseDir(){
    	File ourAppDir = new File(System.getProperty(JAVA_IO_TMPDIR));
    	File databaseDir = new File(ourAppDir, "min-log-test");

    	return databaseDir;
	}
	
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
        final DriverManagerDataSource dataSource = new DriverManagerDataSource(
        		"jdbc:mysql:mxj:///minlog" + 
        		"?server.basedir=" + getDatabaseDir() +
        		"&createDatabaseIfNotExist=true",
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
        serverConfig.setNamingConvention(new com.avaje.ebean.config.MatchingNamingConvention());
        serverConfig.setExternalTransactionManager(new SpringAwareJdbcTransactionManager());
        factoryBean.setServerConfig(serverConfig);
        return factoryBean;
    }
}