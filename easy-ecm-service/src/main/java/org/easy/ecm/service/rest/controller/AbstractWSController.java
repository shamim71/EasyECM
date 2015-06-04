package org.easy.ecm.service.rest.controller;


import static org.easy.ecm.service.Constants.ERROR_CODE_INVALID_MESSAGE_STRUCTURE;
import static org.easy.ecm.service.Constants.ERROR_CODE_NULL_POINTER_ENCOUNTERED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;



import org.easy.ecm.service.exception.ApplicationSecurityException;
import org.easy.ecm.service.exception.ServiceException;
import org.easy.ecm.service.rest.bean.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.easy.ecm.service.Constants;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;




/**
 * This class contains common methods and exception handler methods that will be
 * used all controller layer classes
 * 
 * @author Shamim Ahmmed
 * 
 */
public abstract class AbstractWSController {

	Logger logger = LoggerFactory.getLogger(getClass().getCanonicalName());
	
	private static final String CONTENT_TYPE_JSON = "application/json";
	private static final String CONTENT_TYPE_XML = "application/xml";

	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;

	protected final void writeAsJson(final HttpServletResponse response,
			final GenericResponse<?> message)
			throws HttpMessageNotWritableException, IOException {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		//MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		response.setContentType(CONTENT_TYPE_JSON);

		if(message.getHttpStatus() != null){
			response.setStatus(message.getHttpStatus().value());
		}

		HttpOutputMessage outputMessage = new ServletServerHttpResponse(
				response);

		converter.write(message, new MediaType("application", "json"),
				outputMessage);
	}
	
	/**
	 * Write an instance of GenericMessageResponse into the response along with
	 * the status in the response header
	 * 
	 * @param response
	 *            the HttpServletResponse instance
	 * @param message
	 *            the GenericMessageResponse instance
	 * @param status
	 *            the HTTP status code
	 * 
	 * @throws HttpMessageNotWritableException
	 * @throws IOException
	 */
	protected void writeObjectAsJsonString(HttpServletResponse response,
			GenericResponse<?> message) throws HttpMessageNotWritableException,
			IOException {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		response.setContentType(CONTENT_TYPE_JSON);
		response.setStatus(message.getHttpStatus().value());
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(
				response);

		converter.write(message, new MediaType("application", "json"),
				outputMessage);
	}

	/**
	 * Write the object as JSON string provided by the content type.
	 * 
	 * @param response
	 * @param message
	 * @param contentType
	 * @throws HttpMessageNotWritableException
	 * @throws IOException
	 */
	protected void writeObjectAsJsonString(HttpServletResponse response,
			GenericResponse<?> message, String contentType)
			throws HttpMessageNotWritableException, IOException {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		response.setContentType("application/" + contentType);
		response.setStatus(message.getHttpStatus().value());
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(
				response);

		converter.write(message, new MediaType("application", contentType),
				outputMessage);
	}
	/**
	 * Write object as json string including a custom HttpStatus code.
	 * 
	 * @param response
	 *            HttpServletResponse instance
	 * @param message
	 *            message object
	 * @param status
	 *            HTTPStatus of this message
	 * @throws HttpMessageNotWritableException
	 * @throws IOException
	 */
	protected final void writeObjectAsJsonString(
			final HttpServletResponse response, final Object message,
			final HttpStatus status) throws HttpMessageNotWritableException,
			IOException {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		response.setContentType(CONTENT_TYPE_JSON);
		response.setStatus(status.value());
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(
				response);

		converter.write(message, new MediaType("application", "json"),
				outputMessage);
	}
	
	protected void writeObjectAsXMLString(HttpServletResponse response,
			Object message) throws HttpMessageNotWritableException, IOException {
		MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();
		converter.setMarshaller(jaxb2Marshaller);
		response.setContentType(CONTENT_TYPE_XML);
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(
				response);

		converter.write(message, new MediaType("application", "xml"),
				outputMessage);
	}

	/**
	 * Transform the NullPointerException to standard HTTP style JSON format
	 * 
	 * @param ex
	 *            the NullPointerException instance
	 * @param response
	 *            the HttpServletResponse instance
	 * 
	 * @throws HttpMessageNotWritableException
	 * @throws IOException
	 */
	@ExceptionHandler(NullPointerException.class)
	public void handleNullPointerException(NullPointerException ex,
			HttpServletResponse response)
			throws HttpMessageNotWritableException, IOException {

		logger.error("NullPointer Exception : ", ex);

		GenericResponse<?> msg = new GenericResponse<String>();

		String warning = "We have encountered a problem during processing your request, "
				+ ex.getMessage();
		msg.setCode(ERROR_CODE_NULL_POINTER_ENCOUNTERED);
		msg.setMessage(warning);
		msg.setHttpStatus(INTERNAL_SERVER_ERROR);

		this.writeObjectAsJsonString(response, msg);
	}

	/**
	 * Transform the HttpMessageNotReadableException to standard HTTP style JSON
	 * format
	 * 
	 * @param ex
	 *            the NullPointerException instance
	 * @param response
	 *            the HttpServletResponse instance
	 * 
	 * @throws HttpMessageNotWritableException
	 * @throws IOException
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public void handleInvalidMessagePerceived(
			HttpMessageNotReadableException ex, HttpServletResponse response)
			throws HttpMessageNotWritableException, IOException {

		logger.error("HttpMessageNotReadableException Exception : ", ex);

		GenericResponse<?> msg = new GenericResponse<String>();

		String warning = " Please check your request message format and try again";

		if (ex.getCause() instanceof UnrecognizedPropertyException) {
			UnrecognizedPropertyException uex = (UnrecognizedPropertyException) ex
					.getCause();
			warning = "Message property '" + uex.getUnrecognizedPropertyName()
					+ "' is not recognized";
		}

		msg.setCode(ERROR_CODE_INVALID_MESSAGE_STRUCTURE);
		msg.setMessage(warning);
		msg.setHttpStatus(BAD_REQUEST);

		this.writeObjectAsJsonString(response, msg);
	}

	/**
	 * Transform the NumberFormatException into a standard HTTP style response
	 * message along with application level error code
	 * 
	 * @param ex
	 *            the NumberFormatException instance
	 * @param response
	 *            the HttpServletResponse instance
	 * 
	 * @throws HttpMessageNotWritableException
	 * @throws IOException
	 */
	@ExceptionHandler(NumberFormatException.class)
	public void invalidNumberFormatRecognized(NumberFormatException ex,
			HttpServletResponse response)
			throws HttpMessageNotWritableException, IOException {

		logger.error("NumberFormatException : ", ex);
		String warning = "Invalid input encountered, " + ex.getMessage();

		GenericResponse<?> msg = new GenericResponse<String>(
				ERROR_CODE_INVALID_MESSAGE_STRUCTURE, warning, BAD_REQUEST);

		this.writeObjectAsJsonString(response, msg);
	}

	/**
	 * Transform the service related exception into a standard HTTP style
	 * exception along with application level error code and error message.
	 * 
	 * @param ex
	 *            the instance of GiftServiceException
	 * @param response
	 *            the HttpServletResponse instance
	 * 
	 * @throws HttpMessageNotWritableException
	 * @throws IOException
	 */
	@ExceptionHandler(ServiceException.class)
	public void handleServiceException(final ServiceException ex,
			final HttpServletResponse response)
			throws HttpMessageNotWritableException, IOException {

		logger.debug("Service Exception : ", ex);

		GenericResponse<?> msg = new GenericResponse<String>(ex.getErrorCode(),
				ex.getMessage(), BAD_REQUEST);

		this.writeObjectAsJsonString(response, msg);

	}

	@ExceptionHandler(ApplicationSecurityException.class)
	public void handleServiceSecurityException(
			final ApplicationSecurityException ex,
			final HttpServletResponse response)
			throws HttpMessageNotWritableException, IOException {

		logger.debug("Service Exception : ", ex);

		GenericResponse<?> msg = new GenericResponse<String>(ex.getErrorCode(),
				ex.getMessage(), HttpStatus.UNAUTHORIZED);

		this.writeObjectAsJsonString(response, msg);

	}



	@ExceptionHandler(DataIntegrityViolationException.class)
	public void duplicateRecordCheck(DataIntegrityViolationException ex,
			HttpServletResponse response)
			throws HttpMessageNotWritableException, IOException {

		logger.error("Duplicate record  : ", ex);

		String warning = "Record already exist, " + ex.getMessage();

		GenericResponse<String> message = new GenericResponse<String>(
				Constants.ERROR_CODE_BAD_REQUEST, warning, BAD_REQUEST);

		this.writeObjectAsJsonString(response, message);

	}

}
