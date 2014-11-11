package org.easy.ecm.content.service;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Workspace;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.UserManager;



public abstract class AbstractRepositoryAdminService implements IRepositoryAdminService{

	protected Session session;

	public AbstractRepositoryAdminService(Session session) {
		this.session = session;
	}
	
	protected Workspace getSessionWorkspace(){
		return this.session.getWorkspace();
	}
	
	protected JackrabbitSession getJackRabbitSession(){
		if(this.session != null){
			return (JackrabbitSession) this.session;
		}
		return null;
	}
	protected UserManager getJackRabbitUserManager() throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException{
		return this.getJackRabbitSession().getUserManager();
	}
}
