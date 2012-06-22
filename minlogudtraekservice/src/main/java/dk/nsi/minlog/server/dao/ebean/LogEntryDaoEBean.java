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
*/
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
	public long removeLogEntriesBefore(DateTime date){
		ExpressionList<LogEntry> query = query().where().le("tidspunkt", date);
		List<Object> ids = query.findIds();
		long numberOfIds = ids.size();
		ebeanServer.delete(LogEntry.class, ids);
		
		return numberOfIds;
	}
}