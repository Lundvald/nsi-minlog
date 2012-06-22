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
package dk.nsi.minlog.test.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 * Helper class to create valid security headers.
 * 
 * @author kpi
 *
 */

public class TestHelper {
	public static String sendRequest(String url, String action, String docXml, boolean failOnError) throws IOException, ServiceException {
		URL u = new URL(url);
		HttpURLConnection uc = (HttpURLConnection) u.openConnection();
		uc.setDoOutput(true);
		uc.setDoInput(true);
		uc.setRequestMethod("POST");
		uc.setRequestProperty("SOAPAction", "\"" + action + "\"");
		uc.setRequestProperty("Content-Type", "text/xml; charset=utf-8;");
		OutputStream os = uc.getOutputStream();

		IOUtils.write(docXml, os, "UTF-8");
		os.flush();
		os.close();

		InputStream is;
		if (uc.getResponseCode() != 200) {
			is = uc.getErrorStream();
		} else {
			is = uc.getInputStream();
		}
		String res = IOUtils.toString(is);

		is.close();
		if (uc.getResponseCode() != 200 && (uc.getResponseCode() != 500 || failOnError)) {
			throw new ServiceException(res);
		}
		uc.disconnect();

		return res;
	}

	public static class ServiceException extends Exception {

		private static final long serialVersionUID = -391997961358118049L;
		private final String res;

		public ServiceException(String res) {
			this.res = res;
		}

		@Override
		public String getMessage() {
			return super.getMessage() + ". Result = "+res;
		}

		public String getResponse() {
			return res;
		}
	}

}