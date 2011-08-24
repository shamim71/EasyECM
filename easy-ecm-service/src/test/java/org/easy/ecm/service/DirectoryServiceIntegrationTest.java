package org.easy.ecm.service;

import org.easy.ecm.service.rest.bean.User;
import org.easy.ecm.service.rest.bean.UserList;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class DirectoryServiceIntegrationTest extends AbstractServiceIntegrationTest{

	@Test
	public void listUser(){
		
		
		HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_JSON);
		
		ResponseEntity<UserList> response = template.exchange("http://localhost:8080/easy-ecm-service/service/users", 	HttpMethod.GET, entity, UserList.class);
		UserList employees = response.getBody();
		for(User e : employees.getUsers()) {
			System.out.println(e.toString());
		}
		assertNotNull(new Object());
	}
}
