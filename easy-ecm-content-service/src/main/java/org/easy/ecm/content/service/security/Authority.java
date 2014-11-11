package org.easy.ecm.content.service.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.Value;

public abstract class Authority {

	protected String id;
	
	protected boolean isAdmin;
	
	protected String distinguishedName;
	
	protected List<RepositoryGroup> memberOfGroups = new ArrayList<RepositoryGroup>();

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the isAdmin
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the distinguishedName
	 */
	public String getDistinguishedName() {
		return distinguishedName;
	}

	/**
	 * @param distinguishedName the distinguishedName to set
	 */
	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	/**
	 * @return the memberOfGroups
	 */
	public List<RepositoryGroup> getMemberOfGroups() {
		return memberOfGroups;
	}

	/**
	 * @param memberOfGroups the memberOfGroups to set
	 */
	public void setMemberOfGroups(List<RepositoryGroup> memberOfGroups) {
		this.memberOfGroups = memberOfGroups;
	}

	
	
}
