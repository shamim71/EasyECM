package org.easy.ecm.content.service;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Workspace;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlManager;
import org.apache.jackrabbit.api.security.principal.PrincipalManager;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.common.exception.ErrorCodes;

public abstract class AbstractRepositoryService {

	protected Session session;

	public void setSession(Session session) {
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
	
	protected JackrabbitAccessControlManager getJackrabbitAccessControlManager()throws EcmException{
		JackrabbitAccessControlManager acMgr = null;
		try {
			acMgr = (JackrabbitAccessControlManager) this.getJackRabbitSession().getAccessControlManager();
		} catch (UnsupportedRepositoryOperationException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
			
		} catch (RepositoryException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		}
		return acMgr;
	}
	protected PrincipalManager getPrincipalManager() throws EcmException{
		PrincipalManager pManager = null;
		try {
			pManager = this.getJackRabbitSession().getPrincipalManager();
		} catch (AccessDeniedException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_ACCESS_DENIED);
			
		} catch (UnsupportedRepositoryOperationException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
			
		} catch (RepositoryException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		}
		return pManager;
	}
	
	
	protected UserManager getJackRabbitUserManager() throws EcmException{
		UserManager uMgr = null;

		try {
			uMgr = this.getJackRabbitSession().getUserManager();
		} catch (AccessDeniedException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_ACCESS_DENIED);
			
		} catch (UnsupportedRepositoryOperationException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
			
		} catch (RepositoryException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		}
		return uMgr;
	}
}
