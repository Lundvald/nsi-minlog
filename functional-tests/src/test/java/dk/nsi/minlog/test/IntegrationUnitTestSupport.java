package dk.nsi.minlog.test;

import org.springframework.test.context.ContextConfiguration;

import dk.nsi.minlog.config.WSConfig;

@ContextConfiguration(classes = {WSConfig.class})
public abstract class IntegrationUnitTestSupport extends DaoUnitTestSupport {
}