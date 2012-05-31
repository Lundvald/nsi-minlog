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
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeader;

import com.trifork.dgws.DgwsRequestContext;

import dk.nsi.minlog.Minlogudtraekservice;
import dk.nsi.minlog._2012._05._24.HentRegistreringerRequest;
import dk.nsi.minlog._2012._05._24.HentRegistreringerResponse;
import dk.nsi.minlog.domain.Registrering;
import dk.nsi.minlog.server.dao.RegistreringDao;

@Repository("minlogudtraekservice")
@Endpoint
public class MinlogudtraekserviceImpl implements Minlogudtraekservice {
	private static Logger logger = Logger.getLogger(MinlogudtraekserviceImpl.class);

	@Inject
	DgwsRequestContext dgwsRequestContext;

	@Inject
	RegistreringDao registreringDao;

	public MinlogudtraekserviceImpl() {}

	@Override
	@Transactional
	@ResponsePayload
	public HentRegistreringerResponse hentRegistreringer(@RequestPayload HentRegistreringerRequest request, SoapHeader soapHeader) {
		final HentRegistreringerResponse response = new HentRegistreringerResponse();
		// TODO: Check for security level here		
		final Collection<Registrering> registeringer = registreringDao.findRegistreringByCPRAndDates(request.getCprNr(), nullableDateTime(request.getFraDato()), nullableDateTime(request.getTilDato()));

		response.getRegistreringer().addAll(CollectionUtils.collect(
				registeringer, 
				new Transformer<Registrering, dk.nsi.minlog._2012._05._24.Registrering>() {
					@Override
					public dk.nsi.minlog._2012._05._24.Registrering transform(Registrering registering) {
						return toJaxbType(registering);
					}
				}
		));
		return response;
	}

	private static dk.nsi.minlog._2012._05._24.Registrering toJaxbType(final Registrering reg) {
		return new dk.nsi.minlog._2012._05._24.Registrering() {{
			
		}};
	}
	
    private DateTime nullableDateTime(XMLGregorianCalendar xmlDate) {
        return xmlDate != null ? new DateTime(xmlDate.toGregorianCalendar()) : null;
    }

}
