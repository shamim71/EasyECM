package org.easy.ecm.common.exception;

/**
 * A custom Exception class which encapsulate the repository related exception to the client classes.
 *   
 * @author Shamim Ahmmed
 */
public class EcmException extends Exception{

	private static final long serialVersionUID = 8803362033624130864L;
	
	/** Exception throw-able */
	private Throwable cause = null;
	
	/** Each error code is defined by unique ID */
	protected int ERROR_CODE;
	
	/** Indicate the level of error */
	protected ERROR_LEVEL errorLevel;
	
	/** Message key for localized error if any */
	protected String localizeMessageKey;

	/**
	 * Construct exception a error message only
	 * @param message
	 */
	public EcmException(String message) {
		super(message);
	}
	
	/**
	 * Construct an exception with error message and code
	 * 
	 * @param message the error message
	 * @param eRROR_CODE the error code
	 */
	public EcmException(String message,int eRROR_CODE) {
		super(message);
		this.ERROR_CODE = eRROR_CODE;
	}
	
	/**
	 * Construct an exception with error message, Throwable clause and error code
	 * 
	 * @param message the error message
	 * @param cause the throwable object
	 * @param eRROR_CODE the error code
	 */
	public EcmException(String message,Throwable cause,int eRROR_CODE) {
		super(message);
		this.cause = cause;
		this.ERROR_CODE = eRROR_CODE;
	}
	
	/**
	 * Construct a exception with error message and Throw-able object
	 * @param message
	 * @param cause
	 */
	public EcmException(String message,Throwable cause) {
		super(message);
		this.cause = cause;
	}

	public Throwable getCause() {
	    return cause;
	 }
	
	
}
