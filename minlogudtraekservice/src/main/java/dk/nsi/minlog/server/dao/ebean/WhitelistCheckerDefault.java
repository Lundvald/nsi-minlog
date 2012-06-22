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

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.trifork.dgws.WhitelistChecker;

/**
 * Helper class for "Den gode webservice spring util" which 
 * provided all the legal cvr numbers from database.
 * 
 * @author kpi
 *
 */

@Repository
public class WhitelistCheckerDefault implements WhitelistChecker {
    @Inject
    EbeanServer ebeanServer;
    
    @Override
    public Set<String> getLegalCvrNumbers(String whitelist) {
        SqlQuery query = ebeanServer.createSqlQuery("SELECT legal_cvr FROM whitelist WHERE name = :whitelist");
        query.setParameter("whitelist", whitelist);
        final Set<SqlRow> sqlRows = query.findSet(); 
        		
        final Set<String> result = new HashSet<String>();
        for (SqlRow sqlRow : sqlRows) {
            result.add(sqlRow.getString("legal_cvr"));
        }
        return result;
    }
}
