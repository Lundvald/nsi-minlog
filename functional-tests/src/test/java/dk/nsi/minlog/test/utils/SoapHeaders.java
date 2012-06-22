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
package dk.nsi.minlog.test.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import dk.sdsd.dgws._2010._08.NameFormat;
import dk.sdsd.dgws._2010._08.OrgUsingID;
import dk.sosi.seal.SOSIFactory;
import dk.sosi.seal.model.SignatureUtil;
import dk.sosi.seal.model.constants.FlowStatusValues;
import dk.sosi.seal.pki.SOSITestFederation;
import dk.sosi.seal.vault.FileBasedCredentialVault;
import dk.sosi.seal.vault.GenericCredentialVault;

/**
 * Helper class to create valid security headers.
 * 
 * @author kpi
 *
 */

public class SoapHeaders {
    /** Path in classpath to a keystoreFile to be used to create an IDCard for the Soap Header. */
    public static String keystoreFile = "validMocesVault.jks";

    /** Password for the keystoreFile. */
    public static String keystorePassword = "Test1234";


    // MessageID (both WSA and medcom header). Null means generate new.
    public static String messageID = null;

    // SDSD System Header
    public static String systemOwnerName = "Trifork";
    public static String systemName = "DDV integrationstest";
    public static String systemVersion = "1.0";
    public static String orgResponsibleName = "Trifork";
    public static String orgUsingName = "Trifork";
    public static OrgUsingID orgUsingID = new OrgUsingID();

    public static String requestedRole;
    public static String onBehalfOf;



    public static final String SOAP_HEADER_OPEN = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.sdsd.dk/dgws/2010/08\" xmlns:ns1=\"http://www.sdsd.dk/dgws/2012/06\" xmlns:ns2=\"http://vaccinationsregister.dk/schemas/2010/07/01\">\n" +
            "   <soapenv:Header>\n";

    private static final String SOAP_BODY_OPEN = "   <soapenv:Body>\n";
    private static final String SOAP_BODY_CLOSE = "   </soapenv:Body>\n";

    private static final String SOAP_ENV_CLOSE = "</soapenv:Envelope>";

    public static final String ONBEHALFOF_HEADER = ""; /* "      <ns2:OnBehalfOf>\n" +
            "         <ns2:AuthorisationIdentifier>123AB</ns2:AuthorisationIdentifier>\n" +
            "      </ns2:OnBehalfOf>\n";*/


    private static GenericCredentialVault vault;
    private static SOSIFactory factory;
    private static Properties props;
    private static SOSITestFederation federation;



    private static final String PLACEHOLDER_FLOWID = "REPLACE_FLOWID";
    private static final String PLACEHOLDER_MESSAGEID = "REPLACE_MESSAGEID";

    private static final String MEDCOM_HEADER =             "      <Header xmlns=\"http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd\">\n" +
            "         <SecurityLevel>3</SecurityLevel>\n" +
            "         <TimeOut>1440</TimeOut>\n" +
            "         <Linking>\n" +
            "            <FlowID>" + PLACEHOLDER_FLOWID + "</FlowID>\n" +
            "            <MessageID>" + PLACEHOLDER_MESSAGEID + "</MessageID>\n" +
            "         </Linking>\n" +
            "         <FlowStatus>"+ FlowStatusValues.FLOW_RUNNING+"</FlowStatus>\n" +
            "         <Priority>RUTINE</Priority>\n" +
            "         <RequireNonRepudiationReceipt>no</RequireNonRepudiationReceipt>\n" +
            "      </Header>\n";

    private static final String SECURITY_OPEN = "      <Security xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
            "         <Timestamp xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
            "            <Created>"+ ISODateTimeFormat.dateTimeNoMillis().print(System.currentTimeMillis()) +"</Created>\n" +
            "         </Timestamp>\n";
    //datetime format 2010-09-23T12:18:04Z


    private static final String SECURITY_CLOSE = "      </Security>\n";

    private static final String SOAP_HEADER_CLOSE = "   </soapenv:Header>\n";

    public static String getMedcomRequestHeader(String messageId, String flowId) {
        return MEDCOM_HEADER.replaceAll(PLACEHOLDER_MESSAGEID, messageId).replaceAll(PLACEHOLDER_FLOWID, flowId);
    }

    public static String getSecurityElement() throws Exception {
        return SECURITY_OPEN + getSamlAssertion() + SECURITY_CLOSE;
    }

    public static String getSamlAssertion() throws Exception {
        String alias = "sosi:alias_system";
        PrivateKey key = (PrivateKey) vault.getKeyStore().getKey(alias, keystorePassword.toCharArray());
        X509Certificate cert = (X509Certificate) vault.getKeyStore().getCertificate(alias);

        PersonAndCertificate personAndCertificate = new PersonAndCertificate("Muhamad", "Danielsen", "muhamad@somedomain.dk", "2006271866", orgUsingID.getValue(), cert, key);
        return getSosiIdCard(personAndCertificate);
    }

    protected static String getSosiIdCard(PersonAndCertificate person) throws Exception {
        String idCard = SOSI.getIDCard(true, factory, vault, person);
        // This code formats the XML nicely, but then the signature validation fails...
        // String formattedXml = XmlFormatter.formatXml(idCard);
        int index = idCard.indexOf("?>");
        if (index > 0 && idCard.length() > index + 2) {
            idCard = idCard.substring(index + 2);
        }
        return idCard;
    }

    public static Resource getSoapEnvelope(String payload) throws Exception {
		orgUsingID.setNameFormat(NameFormat.MEDCOM_CVRNUMBER);
		orgUsingID.setValue("25520041");

        props = SignatureUtil.setupCryptoProviderForJVM();
        File validMocesVault = new File(Thread.currentThread().getContextClassLoader().getResource("validMocesVault.jks").getFile());
        assertTrue("validMocesVault.jks could not be found at " + validMocesVault.getAbsolutePath(), validMocesVault.exists());

        vault = new FileBasedCredentialVault(props, validMocesVault, keystorePassword);
        federation = new SOSITestFederation(props);
        factory = new SOSIFactory(federation, vault, props);
    	
        String soapRequest = SOAP_HEADER_OPEN + getHeaderElements() + SOAP_HEADER_CLOSE + SOAP_BODY_OPEN + payload + SOAP_BODY_CLOSE + SOAP_ENV_CLOSE;
        
        return new ByteArrayResource(soapRequest.getBytes("UTF-8"));
    }
    
    public static String getHeaderElements() throws Exception {
        String messageId = UUID.randomUUID().toString().replaceAll("-", "");
        String flowId = UUID.randomUUID().toString().replaceAll("-", "");
        return getSecurityElement() + getMedcomRequestHeader(messageId, flowId) + ONBEHALFOF_HEADER;
    }
}
