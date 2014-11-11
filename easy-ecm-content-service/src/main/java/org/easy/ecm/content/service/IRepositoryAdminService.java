package org.easy.ecm.content.service;

import java.io.InputStream;

import org.easy.ecm.common.exception.EcmException;

public interface IRepositoryAdminService {
	
	public void createNewWorkspace(String name) throws EcmException;
	
	public void createNewWorkspace(String name, InputStream templateFile) throws EcmException;
}
