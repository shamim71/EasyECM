package org.easy.ecm.service.exception;

/**
 * Custom exception class to generate HTTP 404 error
 * 
 * @author Shamim Ahmmed
 * 
 */
@SuppressWarnings("serial")
public class ItemNotFoundException extends ServiceException {

  public ItemNotFoundException(String message, String errorCode,
      int httpErrorCode) {
    super(message, errorCode, httpErrorCode);
  }


}
