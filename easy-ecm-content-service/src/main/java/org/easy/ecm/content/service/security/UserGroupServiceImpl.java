package org.easy.ecm.content.service.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.AuthorizableExistsException;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.Query;
import org.apache.jackrabbit.api.security.user.QueryBuilder;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.common.exception.ErrorCodes;
import org.easy.ecm.content.service.AbstractRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class UserGroupServiceImpl  extends AbstractRepositoryService  implements IUserGroupService{

	private void addMemberShip(Authority auth,Authorizable member) throws EcmException{
		
		UserManager uMgr = this.getJackRabbitUserManager();
		
		for(RepositoryGroup group: auth.getMemberOfGroups()){
			try {
				Group jGroup = (Group) uMgr.getAuthorizable(group.getDistinguishedName());
				if(jGroup != null){
					jGroup.addMember(member);
				}
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}
	}
	public void addUser(RepositoryUser user) throws EcmException {
		User newUser = null;
		UserManager uMgr = this.getJackRabbitUserManager();
			
		try {
			newUser = uMgr.createUser(user.getDistinguishedName(), user.getPassword());
			this.updateUser(user, newUser);
			
		} catch (AuthorizableExistsException e) {
			throw new EcmException("AuthorizableExistsException",e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		} catch (RepositoryException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		}
		if(newUser == null){
			throw new EcmException("Fail to create new user,"+ user.getDistinguishedName());
		}
		
		//TODO store property
		
		/** store membership if any */
		this.addMemberShip(user, newUser);
		
		/** Save the changes */
		try {
			this.session.save();
		}catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public void updateUser(RepositoryUser user) throws EcmException {


		UserManager uMgr = this.getJackRabbitUserManager();
		try {
			User usr = (User) uMgr.getAuthorizable(user.getDistinguishedName());
			//usr.changePassword("123");
			this.updateUser(user, usr);

		} catch (UnsupportedRepositoryOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void updateUser(RepositoryUser user, User usr) throws EcmException{
		ValueFactory vf;
		try {
			vf = this.getJackRabbitSession().getValueFactory();

			if(user.getEmailAddress()!= null && !user.getEmailAddress().equals("")){
				usr.setProperty("email", vf.createValue(user.getEmailAddress()));
			}
			if(user.getDisplayName()!= null && !user.getDisplayName().equals("")){
				usr.setProperty("displayName", vf.createValue(user.getDisplayName()));
			}
			
			this.getJackRabbitSession().save();
			
		} catch (UnsupportedRepositoryOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.easy.ecm.content.service.security.IUserGroupService#removeUser(org.easy.ecm.content.service.security.RepositoryUser)
	 */
	public void removeUser(RepositoryUser user) throws EcmException {

		UserManager uMgr = this.getJackRabbitUserManager();
		try {
			Authorizable usr = uMgr.getAuthorizable(user.getDistinguishedName());
			usr.remove();
			this.getJackRabbitSession().save();

		} catch (UnsupportedRepositoryOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void removeUser(String userName) throws EcmException {

		UserManager uMgr = this.getJackRabbitUserManager();
		try {
			Authorizable usr = uMgr.getAuthorizable(userName);
			usr.remove();
			this.getJackRabbitSession().save();

		} catch (UnsupportedRepositoryOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void addGroup(RepositoryGroup group) throws EcmException {
		Group newGroup = null;
		UserManager uMgr = this.getJackRabbitUserManager();
			
		try {
			newGroup = uMgr.createGroup(group.getDistinguishedName());
			
		} catch (AuthorizableExistsException e) {
			throw new EcmException("AuthorizableExistsException",e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		} catch (RepositoryException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		}
		if(newGroup == null){
			throw new EcmException("Fail to create new Group,"+ group.getDistinguishedName());
		}
		
		//TODO store property
		
		/** store membership if any */
		this.addMemberShip(group,newGroup);
		
		/** Save the changes */
		try {
			this.session.save();
		}catch (RepositoryException e) {
			e.printStackTrace();
		}

	}

	public void removeGroup(RepositoryGroup group) throws EcmException {
		throw new EcmException("Not yet implemented");

	}

	public void updateGroup(RepositoryGroup group) throws EcmException {
		throw new EcmException("Not yet implemented");

	}

	public void addGroupMember(RepositoryUser user, String group)
			throws EcmException {
		throw new EcmException("Not yet implemented");

	}
	public RepositoryUser findUser(String userName) throws EcmException {
		UserManager uMgr = this.getJackRabbitUserManager();
		RepositoryUser user = null;
		try {
			User usr = (User) uMgr.getAuthorizable(userName);
			user = getRepUser(usr);
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return user;
	}
	private RepositoryUser getRepUser(User usr) throws RepositoryException{
		RepositoryUser user = null;
		if(usr == null){
			return user;
		}
		user = new RepositoryUser();
		user.setDistinguishedName(usr.getID());
		user.setId(usr.getID());
		user.setAdmin(usr.isAdmin());
		
		Value email[] = usr.getProperty("email");
		if(email != null && email.length > 0){
			user.setEmailAddress(email[0].getString());
		}
		
		Value display[] = usr.getProperty("displayName");
		if(display != null && display.length > 0){
			user.setDisplayName(display[0].getString());
		}
		Iterator<Group> groups = usr.memberOf();
		if(groups == null){
			return user;
		}
		while(groups.hasNext()){
			Group grp = groups.next();
			RepositoryGroup group = new RepositoryGroup();
			group.setDistinguishedName(grp.getID());
			user.getMemberOfGroups().add(group);
		}
		
		return user;
	}
	
	public RepositoryGroup findGroup(String groupName) throws EcmException {
		UserManager uMgr = this.getJackRabbitUserManager();
		RepositoryGroup group = null;
		try {
			Group grp = (Group) uMgr.getAuthorizable(groupName);
			if(grp == null){
				return group;
			}
			group = new RepositoryGroup();
			group.setDistinguishedName(grp.getID());
			group.setId(grp.getID());

			Iterator<Group> groups = grp.memberOf();
			if(groups == null){
				return group;
			}
			while(groups.hasNext()){
				Group member = groups.next();
				RepositoryGroup repGroup = new RepositoryGroup();
				repGroup.setDistinguishedName(member.getID());
				group.getMemberOfGroups().add(repGroup);
			}
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return group;
	}
	
	/**
	 *  (non-Javadoc)
	 * @see org.easy.ecm.content.service.security.IUserGroupService#listUser(java.lang.String)
	 */
	public List<RepositoryUser> listUser(final String filter,final long offset,final long maxCount) throws EcmException {
		UserManager userMgr = this.getJackRabbitUserManager();
		List<RepositoryUser> users = new ArrayList<RepositoryUser>();
		  try {

		        Iterator<Authorizable> result = userMgr.findAuthorizables(new Query() {
		            public <T> void build(QueryBuilder<T> builder) { /* any */ 
		            	try{
		            		builder.setSelector(User.class);
		            		builder.setLimit(offset, maxCount);
		            		if(!filter.isEmpty()){
		            			builder.setCondition(builder.nameMatches("%"+ filter +"%"));
		            		}
		            		
		            	}
		            	catch(Exception ex){
		            		ex.printStackTrace();
		            	}
		            }
		        });
		        while(result.hasNext()){
		        	Authorizable obj = result.next();
		        	User usr = (User) obj;
		        	RepositoryUser user = getRepUser(usr);
		        	if(user != null){
		        		users.add(user);
		        	}
		        	//System.out.println("User name: "+obj.getPrincipal().getName());
		        }
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}



}
