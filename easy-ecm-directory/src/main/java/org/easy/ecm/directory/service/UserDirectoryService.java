package org.easy.ecm.directory.service;

import java.util.List;

import org.easy.ecm.directory.domain.DirectoryUser;


public interface UserDirectoryService {

	public void createUser(DirectoryUser user);
	
	public void updateUser(DirectoryUser user);
	
	public void deleteUser(DirectoryUser user);
	
	public DirectoryUser findUserByDn(String directoryName);
	
	public List<DirectoryUser> loadDirectoryUsers(int start, int size);
	
}
