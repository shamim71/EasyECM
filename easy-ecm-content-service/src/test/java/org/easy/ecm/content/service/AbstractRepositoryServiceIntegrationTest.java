package org.easy.ecm.content.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.easy.ecm.content.service.DocumentMetadata;
import org.easy.ecm.content.service.EcmDocument;
import org.junit.AfterClass;
import org.junit.BeforeClass;


public abstract class AbstractRepositoryServiceIntegrationTest {
	

    private static final String TEST_RESOURCE_FILE_PATH = "C:\\tools\\EasyECM\\easy-ecm-content-service\\src\\test\\resources\\files\\";
    
    @BeforeClass
	public static void setUp() throws Exception {
    	
	}
    
    @AfterClass
	public  static void tearDown() {
    	
	}

	public byte[] readFileAsByte(final String fileName) throws IOException{
		final String fileNamex = TEST_RESOURCE_FILE_PATH+ fileName;
		File file = new File(fileNamex);
		FileInputStream fin = new FileInputStream(file);
		byte fileContent[] = new byte[(int)file.length()];
		fin.read(fileContent);
		return fileContent;
	}
    
    protected EcmDocument creatDocument(String docName,String encoding,String mimeType) throws IOException{
    	EcmDocument doc = new EcmDocument();
    	doc.setFileName(docName);
    	doc.setContent(readFileAsByte(docName));
    	DocumentMetadata meta = new DocumentMetadata();
    	meta.setFileName(docName);
    	meta.setEncoding(encoding);
    	meta.setMimeType(mimeType);
    	
    	doc.setMetadata(meta);
    	
    	return doc;
    	
    }
}
