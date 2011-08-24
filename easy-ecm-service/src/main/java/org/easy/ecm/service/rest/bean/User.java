package org.easy.ecm.service.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

import org.easy.ecm.directory.domain.DirectoryUser;

@XmlRootElement(name="user")
public class User extends DirectoryUser{


	public User() {
	}

	public User(DirectoryUser user){
		this.setDirectoryName(user.getDirectoryName());
		this.setEmail(user.getEmail());
		this.setFullName(user.getFullName());
		this.setLoginName(user.getLoginName());
	}
}
