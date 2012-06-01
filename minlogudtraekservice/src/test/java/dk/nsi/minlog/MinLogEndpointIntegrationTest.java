package dk.nsi.minlog;

import static net.javacrumbs.smock.common.server.CommonSmockServer.withMessage;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;

public class MinLogEndpointIntegrationTest extends IntegrationUnitTestSupport{
	@Resource
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    
    @Before
    public void createClient() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }
	
	@Test
	public void allByCpr(){
		mockClient.sendRequest(withMessage("ws/unbound/request.xml")).andExpect(noFault());
	}
}
