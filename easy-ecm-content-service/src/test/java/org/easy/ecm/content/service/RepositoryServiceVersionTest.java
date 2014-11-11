package org.easy.ecm.content.service;

import java.util.List;

import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.content.service.repository.ManagedSessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A JUnit Test class for SimpleDocumentServiceImpl class which implements
 * IDocumentService interface consisting of repository document service
 * operations.
 * 
 * @author Shamim Ahmmed
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= { "/spring-jackrabbit-context.xml" })
public class RepositoryServiceVersionTest extends
		AbstractJUnit4SpringContextTests {

	/** Logger instance */
	private final Logger log = LoggerFactory
			.getLogger(RepositoryServiceVersionTest.class);

	/** Thread safe JCR Session manager factory */
	@Autowired
	private ManagedSessionFactory sessionFactory;


	@Autowired
	IDocumentService documentService;
	

	/** The JCR session instance, inject via spring */
	protected static Session session = null;



	/**
	 * Test whether a valid session is created or not based on the workspace
	 * name provided.
	 */
	@Test
	public void sessionIsOpen() {
		log.debug("Validating session is open");
		try {
			session = this.sessionFactory.getCurrentSession("default");
		} catch (EcmException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Assert.assertTrue(session.isLive());
		log.debug("Validating session is done");
	}

	/**
	 * Destroying all live session
	 */
	@Test
	public void readRepository() {
		log.debug("Loading repository");

		
	try {
		session = this.sessionFactory.getCurrentSession("default");
		documentService.setSession(session);
		
		List<EcmDocument> items =	documentService.readWorkspace();
		Assert.assertTrue(true);
	} catch (EcmException e) {

		e.printStackTrace();
	}
		
		
		Assert.assertTrue(true);
	}
	
	/**
	 * Destroying all live session
	 */
	@Test
	public void destroyAllActiveSession() {
		log.debug("Destroying all live session");
		this.sessionFactory.cleanup();
		Assert.assertTrue(true);
	}
}
