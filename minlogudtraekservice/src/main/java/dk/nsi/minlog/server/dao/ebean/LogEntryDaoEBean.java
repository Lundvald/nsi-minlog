package dk.nsi.minlog.server.dao.ebean;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.avaje.ebean.ExpressionList;

import dk.nsi.minlog.domain.LogEntry;
import dk.nsi.minlog.server.dao.LogEntryDao;

@Repository
public class LogEntryDaoEBean extends SupportDao<LogEntry> implements LogEntryDao {
	protected LogEntryDaoEBean() {
		super(LogEntry.class);
	}

	@Override
	public List<LogEntry> findLogEntriesByCPRAndDates(String cpr, DateTime from, DateTime to) {
		ExpressionList<LogEntry> query = query().where().eq("cprNrBorger", cpr);
		if(from != null){
			query = query.ge("tidspunkt", from);
		}
		
		if(to != null){
			query.le("tidspunkt", to);
		}		
		return query.findList();
	}
	
	@Override
	public void removeLogEntriesBefore(DateTime date){
		query().where().le("tidspunkt", date);
	}
}