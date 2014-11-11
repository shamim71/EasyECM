package org.easy.ecm.service.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.content.service.repository.WorkSpaceSessionFactory;
import org.easy.ecm.content.service.security.IUserGroupService;
import org.easy.ecm.content.service.security.RepositoryUser;
import org.easy.ecm.service.rest.bean.User;
import org.easy.ecm.service.rest.bean.UserList;
import org.easy.ecm.service.rest.exception.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class is responsible for providing user information such as list of users, manipulating a
 * repository user or removing user from the repository registry.
 * All the above mentioned services are provided via rest interfaces.
 * 
 * @author Shamim Ahmmed
 *
 */
@Controller
public class UserServiceController extends BaseController {

	static final Logger logger = LoggerFactory.getLogger(UserServiceController.class);

	/** Repository User Group service interface */
	private IUserGroupService service;
	
	/** Workspace session factory providing acquiring repository session */
	private WorkSpaceSessionFactory sessionFactory;

	/**JAXB Marshaler to rendering response to different type */
	private Jaxb2Marshaller jaxb2Mashaller;
	
	public void setService(IUserGroupService service) {
		this.service = service;
	}
		
	public void setJaxb2Mashaller(Jaxb2Marshaller jaxb2Mashaller) {
		this.jaxb2Mashaller = jaxb2Mashaller;
	}
	
	
	/**
	 * Return an user information either a XML or JSON format based on request header
	 * 
	 * @param id the user identifier, specially a distinguished name
	 * @return the generated User response
	 */
	@RequestMapping(method=RequestMethod.GET, value="/user/{id}", headers="Accept=application/xml, application/json")
	public @ResponseBody User getRepositoryUser(@PathVariable String id){
		User user = null;
		service.setSession(this.getSession());
		try {
			RepositoryUser usr = service.findUser(id);
			if(usr != null){
				user = new User(usr);
			}
			else{
				logger.error("User does not exist with name, "+ id);
				
				throw new RuntimeException("User does not exist");
			}
		} 
		catch (EcmException e) {
			logger.error(e.getMessage(), e);
		}
		return user;
	}
	
	/**
	 * Create a new repository user provided parameters from the client
	 * 
	 * @param body the user object as a request body
	 * 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/user", consumes="application/json")
	@ResponseStatus(value=HttpStatus.CREATED)
	public @ResponseBody MessageResponse createRepositoryUser(@RequestBody User  user){

		this.validateUser(user);
		
		service.setSession(this.getSession());
		
		logger.debug(""+ user.toString());
		try{
			RepositoryUser repUser = user.toRepositoryUser();
			
			service.addUser(repUser);
			
		}
		catch(EcmException e){
			logger.error("Fail to add new user",e);
		}
		
		 return  new MessageResponse(HttpStatus.CREATED.toString(), "User has been created");
	}
	
	
	@RequestMapping(method=RequestMethod.PUT, value="/user/{id}",consumes="application/json")
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public ModelAndView updateRepositoryUser(@PathVariable String id,@RequestBody User user){
		
		this.validateUser(user);
		
		service.setSession(this.getSession());
		
		logger.debug(""+ user.toString());
		
		RepositoryUser repUser  = null;
		
		try{
			repUser = service.findUser(id);
	    	
	    	if(repUser != null){
		    	repUser.setEmailAddress(user.getEmailAddress());
		    	repUser.setDisplayName(user.getDistinguishName());	
		    	service.updateUser(repUser);
	    	}
	    	else{
	    		//TODO need to throw exception, 404
	    	}
	    	
		}
		catch(EcmException e){
			logger.error("Fail to update user",e);
		}

		 return new ModelAndView( jsonView, "success", new MessageResponse(HttpStatus.NO_CONTENT.toString(), "User has been updated") );
	}
	
	/**
	 * Validate User input parameters
	 * @param user
	 */
	private void validateUser(User user){
		
		if(user.getDistinguishName().isEmpty()){
			throw new IllegalArgumentException();
		}
		if(user.getEmailAddress().isEmpty()){
			throw new IllegalArgumentException();
		}
	}
	
	private Session getSession(){
		Session session = null;
		try {
			session = this.sessionFactory.getCurrentSession("default");
		} catch (EcmException e1) {
			return null;
		}
		return session;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/users", headers="Accept=application/xml,application/json")
	public @ResponseBody UserList getAllUsers() {
		
		service.setSession(this.getSession());
		
		List<RepositoryUser> repUsers = new ArrayList<RepositoryUser>();
		try {
			repUsers = service.listUser("",0,100);
		} catch (EcmException e) {

			e.printStackTrace();
		}
		List<User> users = new ArrayList<User>();
		
		for(RepositoryUser user: repUsers){
			users.add(new User(user));
		}
		UserList list = new UserList(users);
		return list;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/users/{filter}", headers="Accept=application/xml, application/json")
	public @ResponseBody UserList getAllUsers(@PathVariable String filter) {
		
		service.setSession(this.getSession());
		
		List<RepositoryUser> repUsers = new ArrayList<RepositoryUser>();
		try {
			repUsers = service.listUser(filter,0,100);
		} catch (EcmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<User> users = new ArrayList<User>();
		
		for(RepositoryUser user: repUsers){
			users.add(new User(user));
		}
		UserList list = new UserList(users);
		return list;
	}
	
	/**
	 * @return the sessionFactory
	 */
	public WorkSpaceSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(WorkSpaceSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
