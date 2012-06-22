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
*/package dk.nsi.minlog.web;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.jsp.JspWriter;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsAliveTest {
	@Mock(answer=Answers.RETURNS_DEEP_STUBS)
	DataSource dataSource;
	
	@Mock
	JspWriter out;
	
	/**
	 * Check if we can hit the datasource without the code blowing up
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */	
	@Test
	public void checkAll() throws SQLException, IOException {
		when(dataSource.getConnection().createStatement().executeQuery("SELECT 1").next()).thenReturn(true);
		when(dataSource.getConnection().createStatement().executeQuery("SELECT 1").getInt(1)).thenReturn(1);
		
		IsAlive isAlive = new IsAlive();
		isAlive.dataSource = dataSource;
		
		isAlive.checkAll(out);

		//Success if we do not get an exception
	}
	
	/**
	 * Check if the we get an exception, when the datasource does not answer correctly
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test(expected=RuntimeException.class)
	public void checkAllFail() throws SQLException, IOException {
		when(dataSource.getConnection().createStatement().executeQuery("SELECT 1").next()).thenReturn(false);
		when(dataSource.getConnection().createStatement().executeQuery("SELECT 1").getInt(1)).thenReturn(1);
		
		IsAlive isAlive = new IsAlive();
		isAlive.dataSource = dataSource;
		
		isAlive.checkAll(out);
	}

}
