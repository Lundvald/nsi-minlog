package dk.nsi.minlog.server.dao;

import java.util.List;

import org.joda.time.DateTime;

import dk.nsi.minlog.domain.LogEntry;

public interface LogEntryDao {
	List<LogEntry> findLogEntriesByCPRAndDates(String cpr, DateTime from, DateTime to);
	long removeLogEntriesBefore(DateTime date);
}