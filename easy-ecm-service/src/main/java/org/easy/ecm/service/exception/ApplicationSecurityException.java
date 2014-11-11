package org.easy.ecm.service.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class to generate HTTP 401 error
 * 
 * @author Shamim Ahmmed
 * 
 */
@SuppressWarnings("serial")
public class ApplicationSecurityException extends ServiceException {

  public ApplicationSecurityException(String message, String errorCode) {
    super(message, errorCode, HttpStatus.UNAUTHORIZED.value());
  }


}
