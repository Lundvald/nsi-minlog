package dk.nsi.minlog.web;

import java.util.Collection;

import javax.inject.Inject;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeader;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.trifork.dgws.DgwsRequestContext;
import com.trifork.dgws.annotations.Protected;

import dk.nsi.minlog._2012._05._24.ListLogStatementsRequest;
import dk.nsi.minlog._2012._05._24.ListLogStatementsResponse;
import dk.nsi.minlog._2012._05._24.NameFormat;
import dk.nsi.minlog.domain.LogEntry;
import dk.nsi.minlog.server.dao.LogEntryDao;

@SuppressWarnings("restriction")
@Repository("minlogudtraekservice")
public class MinlogudtraekserviceImpl implements Minlogudtraekservice {
	private static Logger logger = Logger.getLogger(MinlogudtraekserviceImpl.class);

	@Inject
	DgwsRequestContext dgwsRequestContext;

	@Inject
	LogEntryDao logEntryDao;

	public MinlogudtraekserviceImpl() {}

	/**
	 * Webservice which lists all the logentries from the request.
	 */	
	@Override
	@Transactional(readOnly=true) //We will never modify, so lets optimize.
	@Protected(minAuthLevel=3) //aspect which check our request-header and populates the dgws context.
    @ResponsePayload
	public ListLogStatementsResponse listLogStatements(@RequestPayload ListLogStatementsRequest request, SoapHeader soapHeader) {
		final ListLogStatementsResponse response = new ListLogStatementsResponse();		
		final Collection<LogEntry> logEntries = logEntryDao.findLogEntriesByCPRAndDates(request.getCprNR(), nullableDateTime(request.getFraDato()), nullableDateTime(request.getTilDato()));
		
		logger.debug("Found " + logEntries.size() + " log entries for " + request.getCprNR());
		
		
		//Convert all the domain model entries into jaxb and set it on the response.
		response.getLogEntry().addAll(CollectionUtils.collect(
				logEntries, 
				new Transformer<LogEntry, dk.nsi.minlog._2012._05._24.LogEntry>() {
					@Override
					public dk.nsi.minlog._2012._05._24.LogEntry transform(LogEntry registering) {
						return toJaxbType(registering);
					}
				}
		));
		
		//Cpr is set for the entire response.
		response.setCprNrBorger(request.getCprNR());
		return response;
	}

	
	/**
	 * Converts a Logentry domain model to a jaxb type.
	 *
	 * @param entry
	 * @return
	 */
	private static dk.nsi.minlog._2012._05._24.LogEntry toJaxbType(final LogEntry entry) {
				
		//Compact and clean way to transform, but this codestyle is not oftenly used.
		//We create a anonymous instance which is return immediately.
		
		return new dk.nsi.minlog._2012._05._24.LogEntry() {{
			setRegKode(entry.getRegKode());
			setBruger(entry.getBruger());
			setAnsvarlig(entry.getAnsvarlig());
			
			BrugerOrganisation brugerOrganisation = new BrugerOrganisation();
			brugerOrganisation.setValue(entry.getOrgUsingID());
			
			String[] orgUsingID = entry.getOrgUsingID().split(":");			
			brugerOrganisation.setNameFormat(nfFromString(orgUsingID[0]));
			brugerOrganisation.setValue(orgUsingID[1]);
			setBrugerOrganisation(brugerOrganisation);
			
			setSystem(entry.getSystemName());
			setHandling(entry.getHandling());
			setSessionsId(entry.getSessionId());
			setTidspunkt(new XMLGregorianCalendarImpl(entry.getTidspunkt().toGregorianCalendar()));
		}};
	}
	
	/**
	 * Convert to DateTime or null if xmlDate is null. 
	 * 
	 * @param xmlDate
	 * @return A datetime from the xmlDate.
	 */
    private DateTime nullableDateTime(XMLGregorianCalendar xmlDate) {
        return xmlDate != null ? new DateTime(xmlDate.toGregorianCalendar()) : null;
    }    
    
    /**
     * Convert nameformat string to a enum defined by the xsd.
     * 
     * @param str
     * @return the nameformat as a enum.
     */
    private static NameFormat nfFromString(String str){
    	if(str.equals("YNUMBER")){
    		return NameFormat.MEDCOM_YNUMBER;
    	} else if(str.equals("PNUMBER")){
    		return NameFormat.MEDCOM_PNUMBER;
    	} else if(str.equals("SKSCODE")){
    		return NameFormat.MEDCOM_SKSCODE;
    	} else if(str.equals("CVRNUMBER")){
    		return NameFormat.MEDCOM_CVRNUMBER;
    	} else if(str.equals("COMMUNALNUMBER")){
    		return NameFormat.MEDCOM_COMMUNALNUMBER;
    	} else if(str.equals("SOR")){
    		return NameFormat.MEDCOM_SOR;
    	} else {
    		throw new RuntimeException(str + " is not a valid Nameformat");
    	}
    }
}