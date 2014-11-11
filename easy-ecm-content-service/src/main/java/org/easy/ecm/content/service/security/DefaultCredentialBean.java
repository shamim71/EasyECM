package org.easy.ecm.content.service.security;

import javax.jcr.Credentials;
import javax.jcr.SimpleCredentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class DefaultCredentialBean implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(DefaultCredentialBean.class);
	
	private Credentials credential;
	
	private String login;
	private String password;
	
	
	public DefaultCredentialBean(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		log.debug("After properties Set.........");
	}


	/**
	 * @return the credential
	 */
	public Credentials getCredential() {
		if(this.credential == null){
			this.credential =  new SimpleCredentials(this.login, this.password.toString().toCharArray());
		}
		return credential;
	}

	/**
	 * @param credential the credential to set
	 */
	public void setCredential(Credentials credential) {
		this.credential = credential;
	}

}
