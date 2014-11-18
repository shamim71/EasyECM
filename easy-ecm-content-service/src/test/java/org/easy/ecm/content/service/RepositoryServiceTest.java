package org.easy.ecm.content.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.content.service.repository.ManagedSessionFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= { "/spring-jackrabbit-context.xml" })
public class RepositoryServiceTest{
	protected final Logger log = LoggerFactory.getLogger(RepositoryServiceTest.class);
	
    @Autowired
    private  ManagedSessionFactory sessionFactory = null;
    
    @Autowired
    private IDocumentService documentService; 
    
    protected  static Session session = null;
    
    private static final String FOLDER_MY_DOCUMENT = "MyDocument";

    @Autowired 
    ApplicationContext context;
    
    @BeforeClass
	public static void setUp() throws Exception {
    	
	}
    
    @AfterClass
	public  static void tearDown() {
    	
	}

	public byte[] readFileAsByte(final String fileName) throws IOException{
		
		Resource resource =	context.getResource("classpath:files/"+ fileName);
		File file = resource.getFile();
		FileInputStream fin = new FileInputStream(file);
		byte fileContent[] = new byte[(int)file.length()];
		fin.read(fileContent);
		fin.close();
		return fileContent;
	}

    protected EcmDocument creatDocument(String docName,String encoding,String mimeType) throws IOException{
    	EcmDocument doc = new EcmDocument();
    	doc.setFileName(docName);
    	doc.setContent(readFileAsByte(docName));
		Resource resource =	context.getResource("classpath:files/"+ docName);
		File file = resource.getFile();
		FileInputStream fin = new FileInputStream(file);
		doc.setInputStream(fin);
    	DocumentMetadata meta = new DocumentMetadata();
    	meta.setFileName(docName);
    	meta.setEncoding(encoding);
    	meta.setMimeType(mimeType);
    	
    	doc.setMetadata(meta);
    	
    	return doc;
    	
    }
    
    @Test
    public void sessionIsOpen() {
    	log.debug("....................Validating session is open.........................");
    	try {
			session = this.sessionFactory.getCurrentSession("default");
		} catch (EcmException e) {
			e.printStackTrace();
		}
    	Assert.assertTrue(session.isLive());
    	log.debug("....................Validating session is done.........................");
    }
    
    /**
     * Create a new folder under the workspace connected
     */
    @Test
    public void createFolder() {
    	final String pPath = "/"+FOLDER_MY_DOCUMENT;
    	try {
    		this.createSession();
    		this.documentService.setSession(session);
			
    		this.cleanUpNode(pPath, session);
    		
    		this.createParentFolder(pPath);
			
    		this.documentService.removeNode(pPath);
    		
			Assert.assertTrue(true);
		} catch (EcmException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
    }
    
    /**
     * Load existing folder
     */
     
    @Test
    public void getFolder() {
    	final String parentPath = "/"+FOLDER_MY_DOCUMENT;
    	try {
    		this.createSession();
    		this.documentService.setSession(session);
    		this.createParentFolder(parentPath);
			Node folder = this.documentService.getNode("/"+FOLDER_MY_DOCUMENT);
			Assert.assertNotNull("Checking created folder", folder);
			
		} catch (EcmException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
    	
    }
    private void createSession(){
		if(session == null){
	    	log.debug("....................Validating session is open.........................");
	    	try {
				session = this.sessionFactory.getCurrentSession("default");
				this.documentService.setSession(session);
			} catch (EcmException e) {
				e.printStackTrace();
			}
		}
    }
    private void cleanUpNode(String fullPath, Session session){
    	
		EcmDocument existingdoc;
		try {
			this.documentService.setSession(session);
			existingdoc = this.documentService.retrieveDocument(fullPath);
			if(existingdoc != null){
				this.documentService.removeNode(fullPath);
			}
			
		} catch (EcmException e) {
			e.printStackTrace();
		}
		
    }
    private void createParentFolder(String pPath){
		Node pFolder;
		try {
			pFolder = this.documentService.getNode(pPath);
			if(pFolder == null){
				int lastIndex = pPath.lastIndexOf("/")+1;
				String fName = pPath.substring(lastIndex);
				this.documentService.createFolder(fName);
			}
		} catch (EcmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
    }
    /**
     * Add non version able document to a folder 
     */
    @Test
    public void addDocumentFolder() {
    	final String fileName = "MyTextDoc1.txt";
    	final String parentPath = "/"+FOLDER_MY_DOCUMENT;
    	final String fullPath = parentPath + "/"+fileName;

    	try {
    		this.createSession();
    		
    		this.documentService.setSession(session);
    		
			EcmDocument doc = this.creatDocument(fileName, "utf-8", "text/plain");
			
			this.createParentFolder(parentPath);
			
			this.cleanUpNode(fullPath, session);
			
									
			this.documentService.addDocumentToFolder(parentPath, doc);
			
			this.documentService.removeNode(fullPath);
			Assert.assertTrue(true);
			
		} catch (EcmException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
			
		} catch (IOException e) {
			Assert.assertTrue(false);
		}
    	
    }
    
    @Test
    public void addAndGetDocumentByIdentifier() {

    	final String fileName = "MyTextDoc1.txt";
    	final String parentPath = "/"+FOLDER_MY_DOCUMENT;
    	final String fullPath = parentPath + "/"+fileName;
 
			EcmDocument doc = null;
			try {
				doc = this.creatDocument(fileName, "utf-8", "text/plain");
			} catch (IOException e) {

				e.printStackTrace();
				Assert.assertTrue(false);
			}
			if(doc == null){
				return;
			}
			
			this.createSession();
	    	
			this.documentService.setSession(session);
	    	
			this.cleanUpNode(fullPath, session);
			
			this.createParentFolder(parentPath);
			
			try {
				this.documentService.addDocumentToFolder(parentPath , doc);
			} catch (EcmException e) {
				e.printStackTrace();
				Assert.assertTrue(false);
			}
			
			EcmDocument loadedDoc;
			try {
				System.out.println("Loading document by idenfier:"+ doc.getMetadata().getIdentifier());
				Node n = session.getNodeByIdentifier(doc.getMetadata().getIdentifier());
				System.out.println(n.getPath());
				
				String pname = n.getParent().getName();
				System.out.println(pname);
				loadedDoc = this.documentService.loadDocumentByIdentifier(doc.getMetadata().getIdentifier());
				System.out.println(loadedDoc.getMetadata().getFullPath());
				
				this.documentService.removeNode(fullPath);
			} catch (EcmException e) {
				e.printStackTrace();
				Assert.assertTrue(false);
			} catch (ItemNotFoundException e) {

				e.printStackTrace();
			} catch (RepositoryException e) {

				e.printStackTrace();
			}

			
			Assert.assertTrue(true);
			

    	
    }
    
    /**
     * Remove the added document
     */
    @Test
    public void retrieveDocument() {
      	final String fileName = "MyTextDoc1.txt";
    	final String parentPath = "/"+FOLDER_MY_DOCUMENT;
    	final String fullPath = parentPath + "/"+fileName;
    	
    	
    	try {
    		this.createSession();
    		
    		this.documentService.setSession(session);
    		
			EcmDocument doc = this.creatDocument(fileName, "utf-8", "text/plain");
			
			this.createParentFolder(parentPath);
			
			this.cleanUpNode(fullPath, session);
			
			this.documentService.addDocumentToFolder(parentPath, doc);
    		
			EcmDocument newDoc =  this.documentService.retrieveDocument(fullPath);

			Assert.assertNotNull("Retrieving newly created document. ",newDoc);
			
		} catch (EcmException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    
    /**
     * Remove the added document
     */
    @Test
    public void removeaDocument() {
    	final String fileName = "MyTextDoc1.txt";
    	final String parentPath = "/"+FOLDER_MY_DOCUMENT;
    	final String fullPath = parentPath + "/"+fileName;
    	
    	try {
    		this.createSession();
    		
    		this.documentService.setSession(session);
    		EcmDocument doc = this.creatDocument(fileName, "utf-8", "text/plain");
    		
    		this.createParentFolder(parentPath);
    		
    		
    		this.cleanUpNode(fullPath, session);
    		
    		this.documentService.addDocumentToFolder(parentPath, doc);
			
			this.documentService.removeNode(fullPath);
			
			Assert.assertTrue("Removing created folder. ",true);
			
		} catch (EcmException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    
    /**
     * Remove the created folder.
     */
    @Test
    public void removeFolder() {
    	final String parentPath = "/"+FOLDER_MY_DOCUMENT;
    	try {
    		this.createSession();
    		this.documentService.setSession(session);
    		
    		this.createParentFolder(parentPath);
			this.documentService.removeNode("/"+FOLDER_MY_DOCUMENT);
			Assert.assertTrue("Removing created folder. ",true);
			
		} catch (EcmException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
    	
    }


    @Test
    public void sessionIsClosed() {
    	log.debug("....................Validating session is closed........................");
    	this.createSession();
    	
    	Assert.assertTrue(session.isLive());
    	log.debug("....................Validating session closed done......................");
    }
    
    @Test
    public void destroyAllActiveSession() {
    	log.debug("Destroying all live session");
    	this.sessionFactory.cleanup();
    	Assert.assertTrue(true);
    }
}
