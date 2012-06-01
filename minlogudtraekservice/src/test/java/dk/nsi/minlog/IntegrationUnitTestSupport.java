package dk.nsi.minlog;

import org.springframework.test.context.ContextConfiguration;

import dk.nsi.minlog.config.ApplicationRootConfig;
import dk.nsi.minlog.config.WSConfig;

@ContextConfiguration(classes = {ApplicationRootConfig.class, DaoUnitTestSupport.MockContext.class, WSConfig.class})
public abstract class IntegrationUnitTestSupport extends DaoUnitTestSupport {
}