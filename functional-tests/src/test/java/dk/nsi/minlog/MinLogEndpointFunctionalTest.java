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
package dk.nsi.minlog;

import static net.javacrumbs.smock.common.SmockCommon.*;
import static net.javacrumbs.smock.common.server.CommonSmockServer.*;
import static org.springframework.ws.test.server.ResponseMatchers.*;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;

import dk.nsi.minlog.test.IntegrationUnitTestSupport;
import dk.nsi.minlog.test.utils.SoapHeaders;

public class MinLogEndpointFunctionalTest extends IntegrationUnitTestSupport{
	@Resource
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;
    
    @Before
    public void createClient() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }
	
	@Test
	public void allByCpr() throws Exception{
		String payload = 
				"<ml:ListLogStatementsRequest xmlns:ml=\"http://nsi.dk/minlog/2012/05/24/\">"+
				"<cprNR>1111111999</cprNR>" +
				"</ml:ListLogStatementsRequest>"; 		
		mockClient
		.sendRequest(withMessage(SoapHeaders.getSoapEnvelope(payload)))
		.andExpect(noFault())
		.andExpect(payload(resource("ws/unbound/response.xml")));
	}
	
	@Test
	public void fromDateByCpr() throws Exception{
		String payload =
				"<ml:ListLogStatementsRequest xmlns:ml=\"http://nsi.dk/minlog/2012/05/24/\">"+
				"<cprNR>1111111999</cprNR>" +
				"<fraDato>2012-01-01T12:51:00Z</fraDato>" +
				"</ml:ListLogStatementsRequest>"; 		
		mockClient
		.sendRequest(withMessage(SoapHeaders.getSoapEnvelope(payload)))
		.andExpect(noFault())
		.andExpect(payload(resource("ws/from/response.xml")));
	}
	
	@Test
	public void toDateByCpr() throws Exception{
		String payload =
				"<ml:ListLogStatementsRequest xmlns:ml=\"http://nsi.dk/minlog/2012/05/24/\">"+
				"<cprNR>1111111999</cprNR>" +
				"<fraDato>2012-01-02T07:00:00Z</fraDato>" +
				"<tilDato>2012-01-02T08:00:00Z</tilDato>" +
				"</ml:ListLogStatementsRequest>"; 		
		mockClient
		.sendRequest(withMessage(SoapHeaders.getSoapEnvelope(payload)))
		.andExpect(noFault())
		.andExpect(payload(resource("ws/to/response.xml")));
	}
}
