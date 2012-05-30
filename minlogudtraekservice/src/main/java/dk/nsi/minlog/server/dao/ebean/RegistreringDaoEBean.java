package dk.nsi.minlog.server.dao.ebean;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import dk.nsi.minlog.domain.Registrering;
import dk.nsi.minlog.server.dao.RegistreringDao;

@Repository
public class RegistreringDaoEBean extends SupportDao<Registrering> implements RegistreringDao {

	protected RegistreringDaoEBean() {
		super(Registrering.class);
	}

	@Override
	public List<Registrering> findLogByCPR(String cpr) {
		return query().where().eq("cpr", cpr).findList();
	}

	@Override
	public List<Registrering> findLogByCPRAndRange(String cpr, Date from, Date to) {
		return query().where().ge("tidspunkt", from).lt("tidspunkt", to).findList();
	}
}
