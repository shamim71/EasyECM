package org.easy.ecm.service.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.easy.ecm.directory.domain.DirectoryUser;
import org.easy.ecm.directory.service.UserDirectoryService;
import org.easy.ecm.service.rest.bean.User;
import org.easy.ecm.service.rest.bean.UserList;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DirectoryController {

	private UserDirectoryService service;


	public void setService(UserDirectoryService service) {
		this.service = service;
	}
	private Jaxb2Marshaller jaxb2Mashaller;
	
	public void setJaxb2Mashaller(Jaxb2Marshaller jaxb2Mashaller) {
		this.jaxb2Mashaller = jaxb2Mashaller;
	}

	@RequestMapping(method=RequestMethod.GET, value="/users", headers="Accept=application/xml, application/json")
	public @ResponseBody UserList getAllUsers() {
		List<DirectoryUser> employees = service.loadDirectoryUsers(0, 0);
		List<User> users = new ArrayList<User>();
		for(DirectoryUser user: employees){
			users.add(new User(user));
		}
		UserList list = new UserList(users);
		return list;
	}
	
}
