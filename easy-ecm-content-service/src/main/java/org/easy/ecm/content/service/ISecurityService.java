package org.easy.ecm.content.service;

import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;

public interface ISecurityService {

	public void setSession(Session session);
	
	public void setAccessControll(String path,String authority,String privilege) throws EcmException;
	
	public void denyAccessControll(String path,String authority,String privilege) throws EcmException;
	
	public boolean hasAccessControll(String path,String authority,String privilege) throws EcmException;
	
	public String[] getSupportedPrivileges(String path) throws EcmException;
	 
	public void listACL();
}
