package org.easy.ecm.content.service.security;

import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.content.service.AbstractRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AbstractRepositoryService implements IUserService{

	public void changePassword(String userId, String oldPassword, String newPassword) {
		try {
			UserManager uMgr = this.getJackRabbitUserManager();
			User usr = null;
			usr.changePassword(newPassword);
			usr.isAdmin();
			
		} catch (EcmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void addToAdmin(String userId) {
		// TODO Auto-generated method stub
		
	}

	public List<RepositoryUser> listUser(String filter) throws EcmException {
		// TODO Auto-generated method stub
		return null;
	}

	public RepositoryUser findUser(String userName) throws EcmException {
		// TODO Auto-generated method stub
		return null;
	}

	public void addUser(RepositoryUser user) throws EcmException {
		// TODO Auto-generated method stub
		
	}

	public void updateUser(RepositoryUser user) throws EcmException {
		// TODO Auto-generated method stub
		
	}

	public void removeUser(RepositoryUser user) throws EcmException {
		// TODO Auto-generated method stub
		
	}

	public void changePassword(String userId, String newPassword) {
		// TODO Auto-generated method stub
		
	}

}
