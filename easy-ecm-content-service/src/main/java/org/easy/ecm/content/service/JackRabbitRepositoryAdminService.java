package org.easy.ecm.content.service;

import java.io.InputStream;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.JackrabbitWorkspace;
import org.easy.ecm.common.exception.EcmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;


@Service
public class JackRabbitRepositoryAdminService  extends AbstractRepositoryAdminService{
	
	final Logger logger = LoggerFactory.getLogger(JackRabbitRepositoryAdminService.class);
	
	public JackRabbitRepositoryAdminService(Session session) {
		super(session);
	}
	public JackRabbitRepositoryAdminService() {
		super(null);
	}
	
	private JackrabbitWorkspace getJackrabbitWorkspace(){
		return (JackrabbitWorkspace) this.getSessionWorkspace();
	}
	
	public void createNewWorkspace(String name) throws EcmException {
		logger.debug("Creating repository workspace with name: "+ name);
        
/*		JackrabbitWorkspace workspace =  this.getJackrabbitWorkspace();
        try {
			workspace.createWorkspace(name);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EcmException(e.getMessage());
		}*/
		
	}

	public void createNewWorkspace(String name, InputStream templateFile)	throws EcmException {
		logger.debug("Creating repository workspace with name: "+ name +" and template file: "+ templateFile);
		
		InputSource src = new InputSource(templateFile);
		JackrabbitWorkspace workspace =  this.getJackrabbitWorkspace();
		try {
			workspace.createWorkspace(name, src);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
