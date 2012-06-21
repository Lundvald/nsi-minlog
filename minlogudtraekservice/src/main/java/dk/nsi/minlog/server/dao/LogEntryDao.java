package dk.nsi.minlog.server.dao;

import java.util.List;

import org.joda.time.DateTime;

import dk.nsi.minlog.domain.LogEntry;

/**
 * Dao for logEntry
 * @author kpi
 *
 */
public interface LogEntryDao {
	/**
	 * finds all the logentries for a given cpr number and a date range.
	 * 
	 * @param cpr The cpr number to look logs for.
	 * @param from Logentries that has a timestamp after from. If from is null, no from range is assumed. 
	 * @param to Logentries that has a timestamp before to. If to is null, no to range is assumed.
	 * @return A list of log entries.
	 */
	List<LogEntry> findLogEntriesByCPRAndDates(String cpr, DateTime from, DateTime to);
	long removeLogEntriesBefore(DateTime date);
}