package org.easy.ecm.service.rest.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="items")
public class UserList {

	private List<User> items;

	public UserList(List<User> users) {
		super();
		this.items = users;
	}

	public UserList() {
		super();
	}

	@XmlElement(name="user")
	public List<User> getItems() {
		return items;
	}

	public void setItems(List<User> users) {
		this.items = users;
	}
	
}
