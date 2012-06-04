package dk.nsi.minlog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import org.joda.time.DateTime;

@Entity
public class LogEntry {
    @Id
    private Long id;
	private String cpr;
    private DateTime tidspunkt;
    private String bruger;
    private String paavegneaf;
    private String organisation;
    private String system;
    private String handling;
    private String session;
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCpr() {
		return cpr;
	}
	public void setCpr(String cpr) {
		this.cpr = cpr;
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
	public String getPaavegneaf() {
		return paavegneaf;
	}
	public void setPaavegneaf(String paavegneaf) {
		this.paavegneaf = paavegneaf;
	}
	public String getOrganisation() {
		return organisation;
	}
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getHandling() {
		return handling;
	}
	public void setHandling(String handling) {
		this.handling = handling;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}   
}