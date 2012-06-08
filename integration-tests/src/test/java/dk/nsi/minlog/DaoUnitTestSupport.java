package dk.nsi.minlog;

import static org.mockito.Mockito.mock;

import java.io.File;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.flyway.core.Flyway;
import com.mysql.management.driverlaunched.ServerLauncherSocketFactory;

import dk.nsi.minlog.config.TestDBConfig;
import dk.nsi.minlog.config.WSConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDBConfig.class, WSConfig.class, DaoUnitTestSupport.MockContext.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class DaoUnitTestSupport extends AbstractJUnit4SpringContextTests {	
	@Inject 
	Flyway flyway;
	
	@Inject
	File databaseDir;

	public static class MockContext {
        @Bean
        public ServletContext servletContext() {
            return mock(ServletContext.class);
        }
    }
    
    @Before
    public void setup() throws Exception {
    	flyway.clean();
        flyway.setBaseDir("db/migration");
        flyway.setTable("schema_version");
    	flyway.migrate();
        
        flyway.setBaseDir("db/testData");
        flyway.setTable("testschema_version");
    	flyway.migrate();
    }    
    
    @After
    public void tearDown() throws Exception {
        ServerLauncherSocketFactory.shutdown(databaseDir, null);
    }
}