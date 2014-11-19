package org.easy.ecm.directory.syn;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.easy.ecm.directory.domain.DirectoryUser;
import org.easy.ecm.directory.syn.UserSynchronization;

public class AdUserSynchronization implements UserSynchronization {

	private Hashtable<Object, Object> env = new Hashtable<Object, Object>();
	
	public AdUserSynchronization() {

		String adminName = "CN=Administrator,CN=Users,DC=demoserver,DC=local";
		String adminPassword = "Demos1";
		String ldapURL = "ldap://demoserver1.demoserver.local:389";
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		
		// set security credentials, note using simple cleartext authentication
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, adminName);
		env.put(Context.SECURITY_CREDENTIALS, adminPassword);

		// connect to my domain controller
		env.put(Context.PROVIDER_URL, ldapURL);
	}

	@Override
	public List<DirectoryUser> getUsers() {
		List<DirectoryUser> users = new ArrayList<DirectoryUser>();
		try {

			// Create the initial directory context
			LdapContext ctx = new InitialLdapContext(env, null);

			// Create the search controls
			// SearchControls userSearchCtls = new SearchControls();
			SearchControls ctls = new SearchControls();
			String[] attrIDs = { "distinguishedName", "cn", "name", "uid",
					"sn", "givenname", "memberOf", "samaccountname",
					"userPrincipalName" };

			ctls.setReturningAttributes(attrIDs);
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// Specify the search scope
			// userSearchCtls.setSearchScope(SearchControls.OBJECT_SCOPE);

			// specify the LDAP search filter to find the user in question
			String userSearchFilter = "(objectClass=user)";

			// Specify the Base for the search
			String userSearchBase = "CN=Users,DC=demoserver,DC=local";

			// Specify the attributes to return
			// String userReturnedAtts[]={"tokenGroups"};
			// userSearchCtls.setReturningAttributes(userReturnedAtts);

			// Search for objects using the filter
			NamingEnumeration answer = ctx.search(userSearchBase,
					userSearchFilter, ctls);
			while (answer.hasMore()) {

				SearchResult rslt = (SearchResult) answer.next();
				Attributes attrs = rslt.getAttributes();

				DirectoryUser usr = new DirectoryUser();
				Attribute attrAcc = attrs.get("samaccountname");
				if(attrAcc != null){
					String account = (String) attrAcc.get(0);
					usr.setLoginName(account);
				}
				Attribute attrDn = attrs.get("distinguishedname");
				if(attrDn != null){
					String dn = (String) attrDn.get(0);
					usr.setDn(dn);
				}
				Attribute attrFullName = attrs.get("name");
				if(attrFullName != null){
					String value = (String) attrFullName.get(0);
					usr.setFullName(value);
				}
				users.add(usr);
			}



			ctx.close();

		}

		catch (NamingException e) {
			System.err.println("Problem searching directory: " + e);
		}
		return users;
	}

}
