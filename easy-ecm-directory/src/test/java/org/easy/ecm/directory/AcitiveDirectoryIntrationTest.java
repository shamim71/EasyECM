package org.easy.ecm.directory;

import java.util.List;

import org.easy.ecm.directory.domain.DirectoryUser;
import org.easy.ecm.directory.syn.AdUserSynchronization;
import org.easy.ecm.directory.syn.UserSynchronization;
import org.junit.Assert;
import org.junit.Test;

public class AcitiveDirectoryIntrationTest  extends AbstractDirectoryDaoIntegrationTest {

	@Test
	public void createUser(){

		UserSynchronization syn = new AdUserSynchronization();
		List<DirectoryUser> users = 	syn.getUsers();
		for(DirectoryUser user: users){
			System.out.println(user.toString());
		}
		Assert.assertTrue(true);
	}
}
