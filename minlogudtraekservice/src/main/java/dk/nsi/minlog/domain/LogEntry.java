package dk.nsi.minlog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.DateTime;

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