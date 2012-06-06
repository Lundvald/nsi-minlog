package dk.nsi.minlog.web;

import java.util.Collection;

import javax.inject.Inject;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
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
import dk.nsi.minlog.domain.LogEntry;
import dk.nsi.minlog.server.dao.LogEntryDao;

@SuppressWarnings("restriction")
@Repository("minlogudtraekservice")
public class MinlogudtraekserviceImpl implements Minlogudtraekservice {
	//private static Logger logger = Logger.getLogger(MinlogudtraekserviceImpl.class);

	@Inject
	DgwsRequestContext dgwsRequestContext;

	@Inject
	LogEntryDao logEntryDao;

	public MinlogudtraekserviceImpl() {}

	@Override
	@Transactional(readOnly=true)
	@Protected(minAuthLevel=3)
    @ResponsePayload
	public ListLogStatementsResponse listLogStatements(@RequestPayload ListLogStatementsRequest request, SoapHeader soapHeader) {
		final ListLogStatementsResponse response = new ListLogStatementsResponse();		
		final Collection<LogEntry> logEntries = logEntryDao.findLogEntriesByCPRAndDates(request.getCprNR(), nullableDateTime(request.getFraDato()), nullableDateTime(request.getTilDato()));
		
		response.getLogEntry().addAll(CollectionUtils.collect(
				logEntries, 
				new Transformer<LogEntry, dk.nsi.minlog._2012._05._24.LogEntry>() {
					@Override
					public dk.nsi.minlog._2012._05._24.LogEntry transform(LogEntry registering) {
						return toJaxbType(registering);
					}
				}
		));
		response.setCprNrBorger(request.getCprNR());
		return response;
	}

	private static dk.nsi.minlog._2012._05._24.LogEntry toJaxbType(final LogEntry entry) {
		return new dk.nsi.minlog._2012._05._24.LogEntry() {{
			setRegKode(entry.getRegKode());
			setBruger(entry.getBruger());
			setAnsvarlig(entry.getAnsvarlig());
			setBrugerOrganisation(entry.getOrgUsingID()); //TODO: we need to set the name formatter here as well;
			setSystem(entry.getSystemName());
			setHandling(entry.getHandling());
			setSessionsId(entry.getSessionId());
			setTidspunkt(new XMLGregorianCalendarImpl(entry.getTidspunkt().toGregorianCalendar()));
		}};
	}
	
    private DateTime nullableDateTime(XMLGregorianCalendar xmlDate) {
        return xmlDate != null ? new DateTime(xmlDate.toGregorianCalendar()) : null;
    }
}