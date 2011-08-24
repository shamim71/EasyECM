package org.easy.ecm.service.rest.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="users")
public class UserList {

	private List<User> users;

	public UserList(List<User> users) {
		super();
		this.users = users;
	}

	public UserList() {
		super();
	}

	@XmlElement(name="user")
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
