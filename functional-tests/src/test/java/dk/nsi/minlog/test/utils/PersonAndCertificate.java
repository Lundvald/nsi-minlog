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

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Helper class to create valid security headers.
 * 
 * @author kpi
 *
 */

public class PersonAndCertificate {
	
	private String firstName;
	private String lastName;
	private String email;
	private String cpr;
	private String cvr;
	private X509Certificate certificate;
	private PrivateKey privateKey;
	
	public PersonAndCertificate(String firstName, String lastName, String email, String cpr, String cvr,
			X509Certificate certificate, PrivateKey privateKey) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.cpr = cpr;
		this.cvr = cvr;
		this.certificate = certificate;
		this.privateKey = privateKey;
	}

	public String toString() {
		return firstName + " " + lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getCpr() {
		return cpr;
	}

	public String getCvr() {
		return cvr;
	}

	public X509Certificate getCertificate() {
		return certificate;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
}
