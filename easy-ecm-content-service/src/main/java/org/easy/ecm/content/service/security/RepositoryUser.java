package org.easy.ecm.content.service.security;



public class RepositoryUser extends Authority{

	private String password;
	
	private String emailAddress;
	
	private String displayName;

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RepositoryUser [emailAddress=" + emailAddress
				+ ", displayName=" + displayName 
				+ ", id=" + id + ", isAdmin=" + isAdmin
				+ ", distinguishedName=" + distinguishedName
				+ ", memberOfGroups=" + memberOfGroups + "]";
	}
	

}
