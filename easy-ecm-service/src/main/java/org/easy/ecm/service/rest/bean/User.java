package org.easy.ecm.service.rest.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.easy.ecm.content.service.security.RepositoryUser;

@XmlRootElement(name="user")
public class User  implements Serializable{

	private String distinguishName;
	
	private String emailAddress;
	
	private String password;
	
	private String displayName;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User() {
	}

	public User(RepositoryUser user){
		this.setDisplayName(user.getDisplayName());
		this.distinguishName = user.getDistinguishedName();
		this.emailAddress = user.getEmailAddress();
	}

	public String getDistinguishName() {
		return distinguishName;
	}

	public void setDistinguishName(String distinguishName) {
		this.distinguishName = distinguishName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public RepositoryUser toRepositoryUser(){
		RepositoryUser user = new RepositoryUser();
		user.setDistinguishedName(this.distinguishName);
		user.setEmailAddress(emailAddress);
		user.setPassword(this.password);
		user.setDisplayName(this.displayName);
		
		return user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
