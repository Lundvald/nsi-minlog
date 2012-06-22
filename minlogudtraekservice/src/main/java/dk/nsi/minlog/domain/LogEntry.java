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
*/package dk.nsi.minlog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.DateTime;

/**
 * Logentry domain model.
 * 
 * @author kpi
 *
 */
@Entity
public class LogEntry {
    @Id
    private Long id;
	private String regKode;
    private String cprNrBorger;
    private DateTime tidspunkt;
    private String bruger;
    private String ansvarlig;
    private String orgUsingID;
    private String systemName;
    private String handling;
    private String sessionId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRegKode() {
		return regKode;
	}
	public void setRegKode(String regKode) {
		this.regKode = regKode;
	}
	public String getCprNrBorger() {
		return cprNrBorger;
	}
	public void setCprNrBorger(String cprNrBorger) {
		this.cprNrBorger = cprNrBorger;
	}
	public DateTime getTidspunkt() {
		return tidspunkt;
	}
	public void setTidspunkt(DateTime tidspunkt) {
		this.tidspunkt = tidspunkt;
	}
	public String getBruger() {
		return bruger;
	}
	public void setBruger(String bruger) {
		this.bruger = bruger;
	}
	public String getAnsvarlig() {
		return ansvarlig;
	}
	public void setAnsvarlig(String ansvarlig) {
		this.ansvarlig = ansvarlig;
	}
	public String getOrgUsingID() {
		return orgUsingID;
	}
	public void setOrgUsingID(String orgUsingID) {
		this.orgUsingID = orgUsingID;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getHandling() {
		return handling;
	}
	public void setHandling(String handling) {
		this.handling = handling;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}