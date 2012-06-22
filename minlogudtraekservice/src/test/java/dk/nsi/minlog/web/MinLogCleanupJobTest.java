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

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import dk.nsi.minlog.server.dao.LogEntryDao;

@RunWith(MockitoJUnitRunner.class)
public class MinLogCleanupJobTest {
	
	@Mock
	LogEntryDao registreringDao;
	
	@InjectMocks
	MinLogCleanupJob job;

	
	/**
	 * Make sure we call the dao on a standard clean up. 
	 */
	@Test
	public void removeExecution() {
		job.cleanup();
		verify(registreringDao).removeLogEntriesBefore((DateTime)any());
	}
	

	/**
	 * Test that we are not allowed to run two concurrent cleanup jobs at the same time.
	 */
	@Test
	public void concurrentCleanupRuns(){
		//Delay the answers so we make sure we only run job one at a time
		doAnswer(new Answer<Object>() {
		     public Object answer(InvocationOnMock invocation) {
		    	 try{ Thread.sleep(100); } catch(Exception e){}
		         return null;
		     }
		})
		.when(registreringDao).removeLogEntriesBefore((DateTime)any());		

		//Start first cleanup in a seperate thread, so we can run second cleanup async.
		new Thread(){
			public void run() {
				job.cleanup();				
			};
		}.start();
		
		job.cleanup();

		//Mockito assumes mocked object methods are only called once
		verify(registreringDao).removeLogEntriesBefore((DateTime)any());
	}
	
	/**
	 * Test that job is not running after exception is throw. This allows us to run the job again.
	 */
	@Test
	public void assumeNotRunningOnException(){
		doThrow(new RuntimeException()).when(registreringDao).removeLogEntriesBefore((DateTime)any());
		job.cleanup();
		assertFalse(job.isRunning());
	}
}