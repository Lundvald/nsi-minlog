package dk.nsi.minlog;

import dk.nsi.minlog._2012._05._24.HentRegistreringerRequest;
import dk.nsi.minlog._2012._05._24.HentRegistreringerResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.addressing.server.annotation.Action;

@Endpoint
public interface Minlogudtraekservice {

    @PayloadRoot(localPart = "HentRegistreringerRequest", namespace = "http://nsi.dk/minlog/2012/05/24/")
    @Action("http://nsi.dk/minlog/2012/05/24/HentRegistreringerRequest")
    @ResponsePayload
    HentRegistreringerResponse  hentRegistreringer(@RequestPayload HentRegistreringerRequest request, SoapHeader soapHeader);

}
