package org.easy.ecm.content.service.security;

import java.util.List;

import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;

public interface IUserGroupService {

	public void setSession(Session session);
	
	public List<RepositoryUser> listUser(String filter,long offset, long maxCount) throws EcmException;
		
	public RepositoryUser findUser(String userName) throws EcmException;
	
	public void addUser(RepositoryUser user) throws EcmException;
	
	public void updateUser(RepositoryUser user) throws EcmException;
	
	public void removeUser(String userName) throws EcmException;
	
	public RepositoryGroup findGroup(String groupName) throws EcmException;
	
	public void addGroup(RepositoryGroup group) throws EcmException;
	
	public void removeGroup(RepositoryGroup group) throws EcmException;
	
	public void updateGroup(RepositoryGroup group) throws EcmException;
	
	public void addGroupMember(RepositoryUser user, String group) throws EcmException;
}
