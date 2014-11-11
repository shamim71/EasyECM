package org.easy.ecm.service.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.easy.ecm.service.exception.ServiceException;
import org.springframework.http.HttpStatus;



/**
 * This class represents a generic message response, used as a result message
 * format for all server side actions.
 * 
 * result contains the result of the query, status_code/message contain a
 * description wether the request was successful or had errors.
 * 
 * @author Shamim Ahmmed
 * 
 * @param <T>
 */
@XmlRootElement(name = "Response")
public final class GenericResponse<T> {

	private String code;

	private String message;
	
	private T result;
	
	
	private HttpStatus httpStatus;
	
	@JsonIgnore
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public GenericResponse() {

	}

	public GenericResponse(final T result, String code, String message, HttpStatus httpStatus) {
		this.result = result;
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}
	public GenericResponse(final T result, String code, String message) {
		this.result = result;
		this.code = code;
		this.message = message;
	}
	public GenericResponse(String code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	
	/**
	 * Initialize response with ServiceException.
	 * 
	 * @param e
	 *            exception
	 */
	public GenericResponse(final ServiceException e) {
		this.code = e.getErrorCode();
		this.message = e.getMessage();
	}


	/**
	 * @return the result
	 */
	public T getResult() {
		return result;
	}


	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(final T result) {
		this.result = result;
	}



	@Override
	public String toString() {
		StringBuffer stringResult = new StringBuffer();
		stringResult.append("Code: ").append(code).append("Message: ")
				.append(message);
		if (result != null) {
			stringResult.append("Result: ").append(result.toString());
		}

		return stringResult.toString();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
