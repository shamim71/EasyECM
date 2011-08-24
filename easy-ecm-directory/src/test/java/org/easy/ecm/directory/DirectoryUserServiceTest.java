package org.easy.ecm.directory;

import java.util.List;

import org.easy.ecm.directory.domain.DirectoryUser;
import org.junit.Assert;
import org.junit.Test;


public class DirectoryUserServiceTest extends AbstractDirectoryDaoIntegrationTest {

	
	@Test
	public void userServiceLoad(){
		Assert.assertNotNull(userDirectoryService);
	}
	
	@Test
	public void createUser(){
		userDirectoryService.createUser(this.createTestUser());
		Assert.assertTrue(true);
	}
	
	@Test
	public void findUser(){
		String userDn = "cn=roger,ou=people";
		DirectoryUser user = userDirectoryService.findUserByDn(userDn);
		System.out.println("findUser: "+user.toString());
		Assert.assertNotNull(user);
	}
	
	@Test
	public void updateUser(){
		String userDn = "cn=roger,ou=people";
		DirectoryUser user = userDirectoryService.findUserByDn(userDn);
		System.out.println("Updating: "+user.toString());
		user.setFullName("Modified Mark Rogers");
		userDirectoryService.updateUser(user);
		DirectoryUser user2 = userDirectoryService.findUserByDn(userDn);
		
		Assert.assertEquals("Modified Mark Rogers", user2.getFullName());
	}
	@Test
	public void deleteUser(){
		String userDn = "cn=roger,ou=people";
		DirectoryUser user = userDirectoryService.findUserByDn(userDn);
		System.out.println("Deleting user: "+user.toString());
		userDirectoryService.deleteUser(user);
		Assert.assertTrue(true);
	}
		
	@Test
	public void loadAllUsers(){
		List<DirectoryUser> users = userDirectoryService.loadDirectoryUsers(0, 0);
		for(DirectoryUser user: users){
			System.out.println(user.toString());
		}
		
		Assert.assertTrue(true);
	}
	
	private DirectoryUser createTestUser(){
		DirectoryUser user = new DirectoryUser();
		user.setLoginName("roger");
		user.setEmail("roger@gmail.com");
		user.setFullName("Mark Rogers");
		user.setPassword("123");
		return user;
	}
}
