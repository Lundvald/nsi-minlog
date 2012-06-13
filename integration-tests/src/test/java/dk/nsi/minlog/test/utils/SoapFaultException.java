package dk.nsi.minlog.test.utils;

public class SoapFaultException extends RuntimeException {

	private static final long serialVersionUID = 8519144561702116661L;

	public SoapFaultException() {
	}

	public SoapFaultException(String arg0) {
		super(arg0);
	}

	public SoapFaultException(Throwable arg0) {
		super(arg0);
	}

	public SoapFaultException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}