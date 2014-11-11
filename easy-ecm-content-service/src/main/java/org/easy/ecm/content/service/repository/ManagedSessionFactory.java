package org.easy.ecm.content.service.repository;

import java.net.MalformedURLException;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.content.service.security.DefaultCredentialBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagedSessionFactory extends AbstractThreadLocalSessionFactory implements WorkSpaceSessionFactory{

	private static final Logger logger = LoggerFactory.getLogger(ManagedSessionFactory.class);

	@Autowired
    private Repository repository;
	
	private Credentials credentials;
        
    @Autowired
    private DefaultCredentialBean defaultCredentialBean;

    
    public ManagedSessionFactory() {
		super();
	}
	public ManagedSessionFactory(Repository repository, Credentials credentials)
    {
        if (repository == null)
        {
            throw new IllegalArgumentException("repository cannot be null");
        }

        if (credentials == null)
        {
            throw new IllegalArgumentException("credentials cannot be null");
        }
        this.credentials = credentials;
        this.repository = this.getRepository();
    }
	
	@Override
	protected Credentials getCredentials() {
		if(this.credentials == null){
			this.credentials = this.defaultCredentialBean.getCredential();
		}
		return this.credentials;
	}

	@Override
	protected Repository getRepository() {
		if(this.repository == null){
			this.repository = this.createRemoteRopositroy();
		}
		return this.repository;
	}
	
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public Repository createRemoteRopositroy(){
		Repository repository = null;
		try {
			repository = new URLRemoteRepository("http://localhost:8080/jackrabbit-webapp-2.2.8/rmi");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return repository;
	}


	
}
