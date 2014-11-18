package org.easy.ecm.service.rest.controller;


import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.service.rest.exception.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


public abstract class BaseController {


	static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	protected MappingJackson2JsonView  jsonView = new MappingJackson2JsonView();
	
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView objectnotExist(Exception ex) {

    	if(logger.isDebugEnabled()){
    		logger.debug("An exception has been thrown");
    	}
    	 return new ModelAndView( jsonView, "error", new MessageResponse(HttpStatus.NOT_FOUND.name(), ex.getMessage()) );
    }
    
    /**
     * This method will be called when one or more parameters of a service are missing
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleClientErrors(Exception ex) {
    	return new ModelAndView( jsonView, "error", new MessageResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage()) );
    }
 
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleServerErrors(Exception ex) {
       
    	logger.error(ex.getMessage(), ex);
    	if(logger.isDebugEnabled()){
    		logger.debug("An exception has been thrown");
    	}
    	return new ModelAndView( jsonView, "error", new MessageResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage()) );
    }
    
    @ExceptionHandler(EcmException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ModelAndView handleEcmExceptionErrors(EcmException ex) {
    	logger.error(ex.getMessage(), ex);
    	if(logger.isDebugEnabled()){
    		logger.debug("An exception has been thrown");
    	}
    	 return new ModelAndView( jsonView, "error", new MessageResponse(HttpStatus.NOT_ACCEPTABLE.name(), ex.getMessage()) );
    }    
}
