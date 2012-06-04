package dk.nsi.minlog.web;

import java.util.Collection;

import javax.inject.Inject;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeader;

import com.trifork.dgws.DgwsRequestContext;
import com.trifork.dgws.annotations.Protected;

import dk.nsi.minlog._2012._05._24.ListLogStatements;
import dk.nsi.minlog._2012._05._24.LogEntries;
import dk.nsi.minlog.domain.LogEntry;
import dk.nsi.minlog.server.dao.LogEntryDao;

@Repository("minlogudtraekservice")
@Endpoint
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
	public LogEntries listLogStatements(@RequestPayload ListLogStatements request, SoapHeader soapHeader) {
		final LogEntries response = new LogEntries();		
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
		return response;
	}

	private static dk.nsi.minlog._2012._05._24.LogEntry toJaxbType(final LogEntry reg) {
		return new dk.nsi.minlog._2012._05._24.LogEntry() {{
			
		}};
	}
	
    private DateTime nullableDateTime(XMLGregorianCalendar xmlDate) {
        return xmlDate != null ? new DateTime(xmlDate.toGregorianCalendar()) : null;
    }
}