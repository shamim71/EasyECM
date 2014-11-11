package org.easy.ecm.service.exception;



/**
 * A service layer exception class that may throw by any service layer classes
 * along with error code and Throwable clause. We will use these exception class
 * from Spring exception handler context and transform them into the standard
 * HTTP style JSON format
 * 
 * @author Shamim Ahmmed
 * 
 */
public class ServiceException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  protected Throwable throwable;

  protected String errorCode;

  protected int httpErrorCode;

  /**
   * @return the throwable
   */
  public Throwable getThrowable() {
    return throwable;
  }

  /**
   * @param throwable
   *          the throwable to set
   */
  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }

  /**
   * @return the errorCode
   */
  public String getErrorCode() {
    return errorCode;
  }

  /**
   * @param errorCode
   *          the errorCode to set
   */
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public ServiceException(String message, String errorCode, Throwable throwable) {
    super(message);
    this.throwable = throwable;
    this.errorCode = errorCode;
  }

  public ServiceException(String message, String errorCode, int httpErrorCode,
      Throwable throwable) {
    super(message);
    this.throwable = throwable;
    this.errorCode = errorCode;
    this.httpErrorCode = httpErrorCode;
  }

  public ServiceException(String message, String errorCode, int httpErrorCode) {
    super(message);
    this.errorCode = errorCode;
    this.httpErrorCode = httpErrorCode;
  }

  
  /**
   * @return the httpErrorCode
   */
  public int getHttpErrorCode() {
    return httpErrorCode;
  }

  /**
   * @param httpErrorCode
   *          the httpErrorCode to set
   */
  public void setHttpErrorCode(int httpErrorCode) {
    this.httpErrorCode = httpErrorCode;
  }

}
