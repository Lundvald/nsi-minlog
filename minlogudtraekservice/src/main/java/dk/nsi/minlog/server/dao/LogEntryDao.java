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
*/package dk.nsi.minlog.server.dao;

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