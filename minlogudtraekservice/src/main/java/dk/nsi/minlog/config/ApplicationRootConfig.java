/*
* The MIT License
*
* Original work sponsored and donated by National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
*
* Copyright (C) 2011 National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy of
* this software and associated documentation files (the "Software"), to deal in
* the Software without restriction, including without limitation the rights to
* use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
* of the Software, and to permit persons to whom the Software is furnished to do
* so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
* $HeadURL$
* $Id$
*/
package dk.nsi.minlog.config;

import static java.lang.System.*;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.springsupport.factory.EbeanServerFactoryBean;
import com.avaje.ebean.springsupport.txn.SpringAwareJdbcTransactionManager;

import dk.nsi.minlog.domain.LogEntry;

/**
 * Setup of spring database and transaction handling.
 *  
 * @author kpi
 * 
 */
@Configuration
@EnableScheduling
@EnableTransactionManagement
public class ApplicationRootConfig implements TransactionManagementConfigurer {	
    @Value("${jdbc.url}") String url;
    @Value("${jdbc.username}") String username;
    @Value("${jdbc.password}") String password;

    @Bean
    public static PropertyPlaceholderConfigurer configuration() {
        final PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
        props.setLocations(new Resource[]{
                new ClassPathResource("default.properties"),
                new FileSystemResource(getProperty("jboss.server.config.url") + "minlog." + getProperty("user.name") + ".properties"),
                new ClassPathResource("minlog." + getProperty("user.name") + ".properties"),
                new ClassPathResource("jdbc.default.properties"),
                new FileSystemResource(getProperty("jboss.server.config.url") + "jdbc." + getProperty("user.name") + ".properties"),
                new ClassPathResource("jdbc." + getProperty("user.name") + ".properties"),
                new FileSystemResource(getProperty("user.home") + "/.minlog/passwords.properties")                
        });
        props.setIgnoreResourceNotFound(true);
        props.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        
        return props;
    }

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
        final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(LogEntry.class);
        
        serverConfig.setName("localhostConfig");
        serverConfig.setClasses(classes);
        serverConfig.setDataSource(dataSource);
        serverConfig.setExternalTransactionManager(new SpringAwareJdbcTransactionManager());
        serverConfig.setNamingConvention(new com.avaje.ebean.config.MatchingNamingConvention());
        factoryBean.setServerConfig(serverConfig);
        return factoryBean;
    }
}
