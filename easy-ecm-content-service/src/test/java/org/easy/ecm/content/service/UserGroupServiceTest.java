package org.easy.ecm.content.service;

import java.util.List;

import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.content.service.repository.ManagedSessionFactory;
import org.easy.ecm.content.service.security.IUserGroupService;
import org.easy.ecm.content.service.security.RepositoryGroup;
import org.easy.ecm.content.service.security.RepositoryUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= { "/spring-jackrabbit-context.xml" })
public class UserGroupServiceTest{
	protected final Logger log = LoggerFactory.getLogger(UserGroupServiceTest.class);
	
    @Autowired
    private  ManagedSessionFactory sessionFactory = null;
    
    @Autowired
    private IUserGroupService userGroupService; 
    


    
    private static final String TEST_U1 ="testuser11";
    private static final String TEST_U2 ="testuser21";
    
    private static final String TEST_G1 ="testGroup111";
    
     private Session getSession(){
    	 Session ses = null;
		try {
			ses = this.sessionFactory.getCurrentSession("default");
		} catch (EcmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return ses;
     }
    @Test
    public void sessionIsOpen() {
    	log.debug("....................Validating session is open.........................");
    	Session session = null;
    	try {
			session = this.sessionFactory.getCurrentSession("default");

		} catch (EcmException e) {
			e.printStackTrace();
		}
    	Assert.assertTrue(session.isLive());
    	log.debug("....................Validating session is done.........................");
    }
    
    @Test
    public void createRepositoryGroups() {
    	log.debug("Creating User group in the JCR Repository...");
    	
    	try {
        	this.userGroupService.setSession(getSession());

        	RepositoryGroup group1 = null;
        	
        	group1 = this.userGroupService.findGroup(TEST_G1);
        	
        	if(group1 == null){      
        		group1 = new RepositoryGroup();
            	group1.setDistinguishedName(TEST_G1);
            	this.userGroupService.addGroup(group1);
        	}

        	Assert.assertTrue("A group has been created",true);
        	
		} catch (Exception e) {
			e.printStackTrace();
			
			Assert.assertTrue("An error occured during creating a group",false);
		}
    }
    
    @Test
    public void createRepositoryUser() {
    	log.debug("....................dxxxx........................");
    	
    	try {
        	this.userGroupService.setSession(getSession());
        	
        	RepositoryGroup group1 = this.userGroupService.findGroup(TEST_G1);
        	
        	RepositoryUser user1  = null;
        	user1 = this.userGroupService.findUser(TEST_U1);
        	
        	if(user1 == null){
            	user1  = new RepositoryUser();
            	user1.setPassword("123");
            	user1.getMemberOfGroups().add(group1);
            	user1.setDistinguishedName(TEST_U1);
            	this.userGroupService.addUser(user1);
        	}
        	System.out.println(user1.toString());

        	
        	RepositoryUser user2  = null;
        	user2 = this.userGroupService.findUser(TEST_U2);
        	if(user2 == null){
            	user2  = new RepositoryUser();
            	user2.setPassword("123");
            	user2.getMemberOfGroups().add(group1);
            	user2.setDistinguishedName(TEST_U2);
            	this.userGroupService.addUser(user2);
        	}
        	
        	Assert.assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
    	

    	
    	log.debug("....................done......................");
    }
    
    @Test
    public void ListRepositoryUsers() {
    	log.debug("Loading user list from the repository...");
    	try {
        	this.userGroupService.setSession(getSession());
        	int userCount = 0;
        	List<RepositoryUser> users = this.userGroupService.listUser("",0,100);
        	userCount = users.size();
        	
        	Assert.assertTrue(userCount > 0);
        	for(RepositoryUser user: users){
        		System.out.println(user.toString());
        	}
        	
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
    	
    }

    
    @Test
    public void updateRepositoryUser() {
    	log.debug("....................dxxxx........................");
    	
    	try {
        	this.userGroupService.setSession(getSession());
        	RepositoryUser user1  = null;
        	user1 = this.userGroupService.findUser(TEST_U1);
        	user1.setEmailAddress("admin@gmail.com");
        	user1.setDisplayName("Administrator");
        	this.userGroupService.updateUser(user1);
        	
        	Assert.assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
    	

    	
    	log.debug("....................done......................");
    }


    /**
     * Destroying all live session
     */
    @Test
    public void destroyAllActiveSession() {
    	log.debug("Destroying all live session");
    	this.sessionFactory.cleanup();

    	Assert.assertFalse("Session should not be open here...", false);
    }
}
