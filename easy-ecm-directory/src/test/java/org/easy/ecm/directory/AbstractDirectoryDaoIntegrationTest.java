package org.easy.ecm.directory;


import junit.framework.Assert;

import org.easy.ecm.directory.service.UserDirectoryService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public abstract class AbstractDirectoryDaoIntegrationTest extends Assert {

	public static ClassPathXmlApplicationContext context= null;
	protected static UserDirectoryService userDirectoryService;
	
	protected static String[] getConfigLocations() {return new String[] { "spring-directory-context-test.xml" };
	}
	
    @BeforeClass
	public static void setUp() throws Exception {
    	context = new ClassPathXmlApplicationContext(getConfigLocations());
    	userDirectoryService = context.getBean("userDirectoryService", UserDirectoryService.class);
	}
    
    @AfterClass
	public static void tearDown() {
    	userDirectoryService = null;
	}


}
