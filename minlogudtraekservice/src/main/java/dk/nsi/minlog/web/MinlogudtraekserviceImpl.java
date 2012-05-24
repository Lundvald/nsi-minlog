package dk.nsi.minlog.web;

import com.trifork.dgws.DgwsRequestContext;
import dk.nsi.minlog.Minlogudtraekservice;
import dk.nsi.minlog._2012._05._24.HentRegistreringerRequest;
import dk.nsi.minlog._2012._05._24.HentRegistreringerResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.soap.SoapHeader;

import javax.inject.Inject;


@Repository("minlogudtraekservice")
@Endpoint
public class MinlogudtraekserviceImpl implements Minlogudtraekservice {
    private static Logger logger = Logger.getLogger(MinlogudtraekserviceImpl.class);

    @Inject
    DgwsRequestContext dgwsRequestContext;

    public MinlogudtraekserviceImpl() {

    }

    @Override
    public HentRegistreringerResponse hentRegistreringer(@RequestPayload HentRegistreringerRequest request, SoapHeader soapHeader) {

        final HentRegistreringerResponse response = new HentRegistreringerResponse();

        return response;
    }
}
