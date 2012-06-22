package dk.nsi.minlog.server.dao.ebean;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.avaje.ebean.EbeanServer;

import dk.nsi.minlog.domain.LogEntry;
import dk.nsi.minlog.server.dao.LogEntryDao;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LogEntryDaoEBeanTest {

	@InjectMocks
	LogEntryDao logEntryDao = new LogEntryDaoEBean();
	
	@Mock(answer=Answers.RETURNS_DEEP_STUBS)
	EbeanServer ebeanServer;
		
	public List<LogEntry> createResult(){
		List<LogEntry> entries = new ArrayList<LogEntry>();
		entries.add(new LogEntry());
		entries.add(new LogEntry());
		entries.add(new LogEntry());		
		return entries;
	}
	
	@Test
	public void findByCpr() throws Exception {			
		List<LogEntry> entries = createResult();
		when(ebeanServer.find(LogEntry.class).where().eq("cprNrBorger", "1234").findList()).thenReturn(entries);
		List<LogEntry> result = logEntryDao.findLogEntriesByCPRAndDates("1234", null, null);
		
		assertEquals(entries, result);		
	}

	@Test
	public void findByCprAndFrom() throws Exception {
		List<LogEntry> entries = createResult();
		when(ebeanServer.find(LogEntry.class).where().eq("cprNrBorger", "1234").ge(eq("tidspunkt"), any()).findList()).thenReturn(entries);		
		List<LogEntry> result = logEntryDao.findLogEntriesByCPRAndDates("1234", DateTime.now(), null);
		
		assertEquals(entries, result);		
	}

	@Test
	public void findByCprAndTo() throws Exception {
		List<LogEntry> entries = createResult();
		when(ebeanServer.find(LogEntry.class).where().eq("cprNrBorger", "1234").le(eq("tidspunkt"), any()).findList()).thenReturn(entries);		
		List<LogEntry> result = logEntryDao.findLogEntriesByCPRAndDates("1234", null, DateTime.now());
		
		assertEquals(entries, result);		
	}

}