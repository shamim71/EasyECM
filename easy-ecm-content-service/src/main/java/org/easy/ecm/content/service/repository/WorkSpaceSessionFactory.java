package org.easy.ecm.content.service.repository;


import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;


public interface WorkSpaceSessionFactory {

    /**
     * Gets current session for specified workspace
     * 
     * @param workspace
     *            workspace name or <code>null</code> for default
     * @return jcr session
     */
    Session getCurrentSession(String workspace) throws EcmException;
    

    Session getCurrentSession(final String user,final String password,String workspace) throws EcmException;
    
    /**
     * Creates a new session. Sessions returned by this method are not managed by the session
     * factory implementation, the callee is responsible for closing the session.
     * 
     * @param workspace
     * @return
     * @throws EcmException
     */
    Session createSession(String workspace) throws EcmException;
    
    
}
