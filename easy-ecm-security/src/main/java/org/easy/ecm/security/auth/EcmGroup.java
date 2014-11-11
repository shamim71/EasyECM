package org.easy.ecm.security.auth;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class EcmGroup implements Group, Serializable{
  
	private static final long serialVersionUID = 1L;
	private final String name;
	private final Set<Principal> users = new HashSet<Principal>();

	  
	public EcmGroup(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public boolean addMember(Principal user) {
		return this.users.add(user);
	}

	public boolean isMember(Principal member) {
		return users.contains(member);
	}

	public Enumeration<? extends Principal> members() {
		return Collections.enumeration(users);
	}

	public boolean removeMember(Principal user) {
		return users.remove(user);

	}

}
