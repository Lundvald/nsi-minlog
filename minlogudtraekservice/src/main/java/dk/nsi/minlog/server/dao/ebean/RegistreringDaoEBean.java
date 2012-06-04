package dk.nsi.minlog.server.dao.ebean;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.avaje.ebean.ExpressionList;

import dk.nsi.minlog.domain.Registrering;
import dk.nsi.minlog.server.dao.RegistreringDao;

@Repository
public class RegistreringDaoEBean extends SupportDao<Registrering> implements RegistreringDao {
	protected RegistreringDaoEBean() {
		super(Registrering.class);
	}

	@Override
	public List<Registrering> findRegistreringByCPRAndDates(String cpr, DateTime from, DateTime to) {
		ExpressionList<Registrering> query = query().where().eq("cpr", cpr);
		if(from != null){
			query = query.ge("tidspunkt", from);
		}
		
		if(to != null){
			query.le("tidspunkt", to);
		}		
		return query.findList();
	}
	
	@Override
	public void removeRegistreringBefore(DateTime date){
		query().where().le("tidspunkt", date);
	}
}