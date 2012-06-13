package dk.nsi.minlog.server.dao.ebean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;

import dk.nsi.minlog.test.IntegrationUnitTestSupport;


public class WhitelistCheckerDefaultTest extends IntegrationUnitTestSupport {
    @Inject
    WhitelistCheckerDefault whitelistChecker;

    @Test
    public void canFetchTestDataCvrNumbers() throws Exception {
        Set<String> testWhitelist = whitelistChecker.getLegalCvrNumbers("test");
        assertEquals(2, testWhitelist.size());
        assertThat(testWhitelist, hasItems("1", "2"));
    }
}