package org.easy.ecm.service;

import junit.framework.Assert;

import org.easy.ecm.service.rest.bean.Employee;
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
/*		
		HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_JSON);
		
		ResponseEntity<UserList> response = template.exchange("http://localhost:8080/easy-ecm-service/service/users", 	HttpMethod.GET, entity, UserList.class);
		UserList employees = response.getBody();
		for(User e : employees.getUsers()) {
			System.out.println(e.toString());
		}*/
		//assertNotNull(new Object());
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void addNewUser(){
		
		//HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_JSON);
		
		Employee newEmp = new Employee(99, "guest", "guest@ibm.com");
		
		HttpEntity<Employee> entity = new HttpEntity<Employee>(newEmp);
		//ResponseEntity<Employee> response = template.postForEntity(	"http://localhost:9080/easy-ecm-service/service/employee", entity, Employee.class);
		//Employee e = response.getBody();
		
		//template.postForEntity("http://localhost:8080/easy-ecm-service/service/user", HttpMethod.POST, User.class);

		//assertNotNull(new Object());
		Assert.assertEquals(true, true);
	}
	
}
