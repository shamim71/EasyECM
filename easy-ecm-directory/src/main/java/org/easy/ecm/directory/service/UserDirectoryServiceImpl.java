package org.easy.ecm.directory.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.naming.Name;

import org.apache.commons.codec.binary.Base64;
import org.easy.ecm.directory.DirectoryConstants;
import org.easy.ecm.directory.domain.DirectoryUser;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;


public final class UserDirectoryServiceImpl implements UserDirectoryService {

	private LdapTemplate ldapTemplate;
	
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
	
	public void createUser(DirectoryUser user) {
		Name dn = buildDn(user);
		DirContextAdapter context = new DirContextAdapter(dn);
		mapToContext(user, context);
		ldapTemplate.bind(dn, context, null);

	}

	public void updateUser(DirectoryUser user) {
		Name dn = buildDn(user);
		DirContextAdapter context = (DirContextAdapter) ldapTemplate.lookup(dn);
		mapToContext(user, context);
		ldapTemplate.modifyAttributes(dn, context.getModificationItems());

	}

	public void deleteUser(DirectoryUser user) {
		ldapTemplate.unbind(buildDn(user.getDirectoryName()));

	}

	public DirectoryUser findUserByDn(String directoryName) {
		DistinguishedName dn = buildDn(directoryName);
		return (DirectoryUser) ldapTemplate.lookup(dn, getContextMapper());
	}

	@SuppressWarnings("unchecked")
	public List<DirectoryUser> loadDirectoryUsers(int start, int size) {
		EqualsFilter filter = new EqualsFilter("objectclass", "person");
		return ldapTemplate.search(DistinguishedName.EMPTY_PATH, filter.encode(), getContextMapper());
	}

	private ContextMapper getContextMapper() {
		return new UserContextMapper();
	}
	
	/**
	 * Create a Distinguished name based on short name and base DN
	 * @param loginName
	 * @param baseDn
	 * @return the generated Distinguished name
	 */
	private DistinguishedName buildDn(DirectoryUser user) {
		DistinguishedName dn = new DistinguishedName();
		dn.add(DirectoryConstants.ORGNANIZATIONAL_UNIT, "people");
		dn.add(DirectoryConstants.ATTRIBUTE_LOGIN_NAME, user.getLoginName());
		return dn;
	}
	
	private DistinguishedName buildDn(String directoryName) {
		DistinguishedName dn = new DistinguishedName(directoryName);
		return dn;
	}	
	private void mapToContext(DirectoryUser user, DirContextAdapter context) {
		context.setAttributeValues("objectclass", new String[] { "top", "person" , "organizationalPerson","inetOrgPerson"});
		context.setAttributeValue(DirectoryConstants.ATTRIBUTE_LOGIN_NAME, user.getLoginName());
		context.setAttributeValue(DirectoryConstants.ATTRIUTE_FULL_NAME, user.getFullName());
		context.setAttributeValue(DirectoryConstants.ATTRIBUTE_EMAIL, user.getEmail());
		if(user.getPassword() != null && !user.getPassword().equals("")){
			String encodedPassword = this.digestMd5(user.getPassword());
			context.setAttributeValue(DirectoryConstants.ATTRIBUTE_PASSWORD, encodedPassword);
		}

	}

	/**
	 * 
	 */
	private static class UserContextMapper implements ContextMapper {

		public Object mapFromContext(Object ctx) {
			DirContextAdapter context = (DirContextAdapter) ctx;
			DistinguishedName dn = new DistinguishedName(context.getDn());
			DirectoryUser user = new DirectoryUser();
			user.setDirectoryName(dn.toString());
			user.setLoginName(context.getStringAttribute(DirectoryConstants.ATTRIBUTE_LOGIN_NAME));
			user.setFullName(context.getStringAttribute(DirectoryConstants.ATTRIUTE_FULL_NAME));
			user.setEmail(context.getStringAttribute(DirectoryConstants.ATTRIBUTE_EMAIL));
			return user;
		}
	}
	
	/**
	 * Convert the string into the MD5 base64 string
	 * @param password
	 * @return
	 */
	private String digestMd5(final String password) {
		  String base64 = "";
		     MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			     digest.update(password.getBytes());
			     base64 = new String(Base64.encodeBase64(digest.digest()));
			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			}

		return "{MD5}" + base64;
	}

}
