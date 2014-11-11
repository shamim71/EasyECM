package org.easy.ecm.service;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class AbstractServiceIntegrationTest extends Assert {
		
	protected static String[] getConfigLocations() {return new String[] { "easy-ecm-rest-client.xml" };
	}
	
	public static ClassPathXmlApplicationContext context= null;
	
	protected static RestTemplate template;
	
    @BeforeClass
	public static void setUp() throws Exception {
    	context = new ClassPathXmlApplicationContext(getConfigLocations());
		template = (RestTemplate) context.getBean("restTemplate");
	}
        
    @AfterClass
	public static void tearDown() {
    	
	}
	protected static HttpEntity<String> prepareGet(MediaType type) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(type);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return entity;
	}
		
}
