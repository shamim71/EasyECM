package org.easy.ecm.service.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.content.service.repository.WorkSpaceSessionFactory;
import org.easy.ecm.content.service.security.IUserGroupService;
import org.easy.ecm.content.service.security.RepositoryUser;
import org.easy.ecm.service.rest.bean.User;
import org.easy.ecm.service.rest.exception.MessageResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("security/users")
@Controller
public class DirectoryController extends BaseController{

	private static final String RANGE_PREFIX = "items=";
    private static final String CONTENT_RANGE_HEADER = "Content-Range";
    private static final String ACCEPT_JSON = "Accept=application/json";
	//private UserDirectoryService service;
	private IUserGroupService service;
	private WorkSpaceSessionFactory sessionFactory;
	
	public void setService(IUserGroupService service) {
		this.service = service;
	}
	
	private Jaxb2Marshaller jaxb2Mashaller;
	
	public void setJaxb2Mashaller(Jaxb2Marshaller jaxb2Mashaller) {
		this.jaxb2Mashaller = jaxb2Mashaller;
	}

	/**
	 * Return an user information either a XML or JSON format based on request header
	 * 
	 * @param id the user identifier, specially a distinguished name
	 * @return the generated User response
	 */
	@RequestMapping(method=RequestMethod.GET, value="/{id}", headers= ACCEPT_JSON)
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
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}") 
	@ResponseStatus(value=HttpStatus.NO_CONTENT) 
	public  @ResponseBody  MessageResponse  deleteRepositoryUser(@PathVariable String id) throws EcmException{

		service.setSession(this.getSession());

		service.removeUser(id);

		return  new MessageResponse(HttpStatus.NO_CONTENT.toString(), "User has been deleted");
	}	
	

	/**
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, headers=ACCEPT_JSON)
	public @ResponseBody HttpEntity<List<User>> listUser(@RequestHeader("Range") String ranges){

		HttpHeaders headers = new HttpHeaders();
		service.setSession(this.getSession());
		List<RepositoryUser> repUsers = new ArrayList<RepositoryUser>();
		long limits[] = new long[2];
		long maxCount = 0L;
		try {
			limits = extractRange(ranges);
			maxCount = limits[1] - limits[0];
			repUsers = service.listUser("",limits[0],maxCount);
			
		} catch (EcmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<User> users = new ArrayList<User>();
		
		for(RepositoryUser user: repUsers){
			users.add(new User(user));
		}

		int totalCount = users.size();

		headers.add(CONTENT_RANGE_HEADER, getContentRangeValue(limits[0],users.size(),totalCount));
		 
		return new HttpEntity<List<User>>(users, headers);
	}
	private long [] extractRange(String ranges){
		String values = ranges.substring(ranges.indexOf("=")+1);
		long limits[] = new long[2];
		limits[0] = 0;
		limits[1] = -1;
		if(!values.isEmpty()){
			String args[] = values.split("-");
			if(args != null){
				try{
					limits[0] = Long.valueOf(args[0]);
					limits[1] = Long.valueOf(args[1]);
				}
				catch(NumberFormatException ex){
					logger.error("Invalid range parameters, "+ ranges);
				}

			}
		}
		return limits;
	}
	/**
	 * Create a new repository user provided parameters from the client
	 * 
	 * @param body the user object as a request body
	 * @throws EcmException 
	 * 
	 */
	@RequestMapping(method={RequestMethod.POST, RequestMethod.PUT}, value="/{id}", headers={ACCEPT_JSON,"If-None-Match=*"})
	@ResponseStatus(value=HttpStatus.CREATED)
	public @ResponseBody MessageResponse createRepositoryUser(@RequestBody User  user) throws EcmException{

		this.validateUser(user);
		
		service.setSession(this.getSession());
		
		logger.debug(""+ user.toString());

			RepositoryUser repUser = user.toRepositoryUser();
			
			service.addUser(repUser);

		
		 return  new MessageResponse(HttpStatus.CREATED.toString(), "User has been created");
	}
		
	/**
	 * Create a new repository user provided parameters from the client
	 * 
	 * @param body the user object as a request body
	 * @throws EcmException 
	 * 
	 */
	@RequestMapping(method={RequestMethod.POST, RequestMethod.PUT}, value="/{id}", headers={ACCEPT_JSON,"If-Match=*"})
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void updateRepositoryUser(@PathVariable String id,@RequestBody User user) throws EcmException{
		
		this.validateUser(user);
		
		service.setSession(this.getSession());
		
		logger.debug(""+ user.toString());
		
		RepositoryUser repUser  = null;

			repUser = service.findUser(id);
	    	
	    	if(repUser != null){
		    	repUser.setEmailAddress(user.getEmailAddress());
		    	repUser.setDisplayName(user.getDistinguishName());	
		    	service.updateUser(repUser);
	    	}
	    	else{
	    		//TODO need to throw exception, 404
	    	}

		// return new ModelAndView( jsonView, "success", new MessageResponse(HttpStatus.NO_CONTENT.toString(), "User has been updated") );
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

    private String getContentRangeValue(long firstResult, Integer resultCount, Integer totalCount) {
        StringBuilder value = new StringBuilder("items "+firstResult+"-");
        if (resultCount == 0) {
            value.append("0");
        } else {
            value.append(firstResult + resultCount - 1);
        }
        value.append("/"+totalCount);
        return value.toString();
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
