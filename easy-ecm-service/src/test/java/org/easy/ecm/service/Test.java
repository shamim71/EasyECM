package org.easy.ecm.service;

import org.easy.ecm.service.rest.bean.Employee;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Test {

	public static void main(String args[]){
		
		RestTemplate template = getTemplate();
		
	//	postEmployee(template);
	}
	
/*	public static void postEmployee(RestTemplate rest) {
		Employee newEmp = new Employee(99, "guest", "guest@ibm.com");
		HttpEntity<Employee> entity = new HttpEntity<Employee>(newEmp);
		
		ResponseEntity<Employee> response = rest.postForEntity("http://localhost:8080/easy-ecm-service/service/employee", entity, Employee.class);
		
		Employee e = response.getBody();
		System.out.println("New employee: " + e.getId() + ", " + e.getName());
	}
	*/
	private static RestTemplate getTemplate() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("src\\test\\resources\\easy-ecm-rest-client.xml");
		RestTemplate template = (RestTemplate) ctx.getBean("restTemplate");
		return template;
	}
	
}
