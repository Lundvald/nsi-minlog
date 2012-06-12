package dk.nsi.minlog.config;

import javax.inject.Inject;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dk.nsi.minlog.web.Minlogudtraekservice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationRootConfig.class, WSConfig.class})
public class SpringConfig {
	
	@Inject
	Minlogudtraekservice minlogudtraekservice;
	
	@Test
	public void validateSetup(){
		assertNotNull(minlogudtraekservice);
	}	
}
