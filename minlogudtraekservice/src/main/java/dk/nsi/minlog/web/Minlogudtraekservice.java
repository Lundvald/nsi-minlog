package dk.nsi.minlog.web;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.addressing.server.annotation.Action;

import dk.nsi.minlog._2012._05._24.ListLogStatementsRequest;
import dk.nsi.minlog._2012._05._24.ListLogStatementsResponse;

@Endpoint
public interface Minlogudtraekservice {

    @PayloadRoot(localPart = "ListLogStatementsRequest", namespace = "http://nsi.dk/minlog/2012/05/24/")
    @Action("http://nsi.dk/minlog/2012/05/24/ListLogStatements")
    @ResponsePayload
    ListLogStatementsResponse listLogStatements(@RequestPayload ListLogStatementsRequest request, SoapHeader soapHeader);

}
