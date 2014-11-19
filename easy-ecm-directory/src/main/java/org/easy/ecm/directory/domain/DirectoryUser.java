package org.easy.ecm.directory.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DirectoryUser {

	private String loginName;
	
	private String password;
	
	private String fullName;
	
	private String Dn;
	
	public String getDn() {
		return Dn;
	}

	public void setDn(String dn) {
		Dn = dn;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String email;
	
	private String directoryName;
	
	private String memberOfGroups[];
	
	
    public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public String[] getMemberOfGroups() {
		return memberOfGroups;
	}

	public void setMemberOfGroups(String[] memberOfGroups) {
		this.memberOfGroups = memberOfGroups;
	}

	public boolean equals(Object obj) {
	     return EqualsBuilder.reflectionEquals(
	         this, obj);
    }

    public int hashCode() {
	     return HashCodeBuilder
	         .reflectionHashCode(this);
    }

    public String toString() {
	     return ToStringBuilder.reflectionToString(
	         this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
