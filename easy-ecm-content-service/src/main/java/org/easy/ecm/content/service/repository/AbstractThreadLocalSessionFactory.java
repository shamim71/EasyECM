package org.easy.ecm.content.service.repository;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.easy.ecm.common.exception.EcmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractThreadLocalSessionFactory {

    private static final Logger log = LoggerFactory.getLogger(AbstractThreadLocalSessionFactory.class);

    ThreadLocal<Map<String, Session>> container = new ThreadLocal<Map<String, Session>>()
    {
        @Override
        protected Map<String, Session> initialValue()
        {
            return new HashMap<String, Session>();
        }
    };
    
    protected abstract Credentials getCredentials();

    protected abstract Repository getRepository();

    public Session getSysSession() throws EcmException
    {
    	Session session = null;
            try
            {
            	  final Credentials credentials = getCredentials();
                log.debug("Opening managed jcr session to workspace: {} with credentials: {}",   credentials);
                session = getRepository().login();
            }
            catch (Exception e)
            {
                throw new EcmException("", e);
            }

        return session;
    }
    
    public Session getCurrentSession(String workspace) throws EcmException
    {
        final Map<String, Session> map = container.get();
        Session session = map.get(workspace);
        if (session != null && !session.isLive())
        {
            session = null;
        }
        if (session == null)
        {
            try
            {
                final Credentials credentials = getCredentials();
                log.debug("Opening managed jcr session to workspace: {} with credentials: {}",
                    workspace, credentials);
                session = getRepository().login(credentials, workspace);
            }
            catch (Exception e)
            {
                throw new EcmException(workspace, e);
            }
            map.put(workspace, session);
            container.set(map);
        }
        return session;
    }

	public Session getCurrentSession(String user, String password,
			String workspace) throws EcmException {
		
		Credentials credentials = new SimpleCredentials(user, password.toCharArray());
		
		return getCurrentSession(credentials, workspace);
	}
    public Session getCurrentSession(Credentials credentials,String workspace) throws EcmException
    {
        final Map<String, Session> map = container.get();
        Session session = map.get(workspace);
        if (session != null && !session.isLive())
        {
            session = null;
        }
        if (session == null)
        {
            try
            {
                log.debug("Opening managed jcr session to workspace: {} with credentials: {}",  workspace, credentials);
                session = getRepository().login(credentials, workspace);
            }
            catch (Exception e)
            {
                throw new EcmException(workspace, e);
            }
            map.put(workspace, session);
            container.set(map);
        }
        return session;
    }
    
    public void cleanup()
    {
        for (Session session : container.get().values())
        {
            if (session.isLive())
            {
                session.logout();
            }
        }
    }

    public Session createSession(String workspace) throws EcmException
    {
        try
        {
            final Credentials credentials = getCredentials();
            log.debug("Opening unmanaged jcr session to workspace: {} with credentials: {}", workspace, credentials);
            return getRepository().login(credentials, workspace);
        }
        catch (Exception e)
        {
            throw new EcmException(workspace, e);
        }
    }
    
    public Session createSession(Credentials credentials, String workspace) throws EcmException
    {
        try
        {
            log.debug("Opening unmanaged jcr session to workspace: {} with credentials: {}", workspace, credentials);
            return getRepository().login(credentials, workspace);
        }
        catch (Exception e)
        {
            throw new EcmException(workspace, e);
        }
    } 
    
}
