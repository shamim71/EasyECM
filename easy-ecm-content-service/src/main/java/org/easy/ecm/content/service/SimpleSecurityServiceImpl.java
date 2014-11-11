package org.easy.ecm.content.service;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jcr.AccessDeniedException;
import javax.jcr.PathNotFoundException;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlException;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicy;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlManager;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlPolicy;
import org.apache.jackrabbit.api.security.principal.PrincipalManager;
import org.apache.jackrabbit.core.security.authorization.PrivilegeRegistry;
import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.common.exception.ErrorCodes;
import org.springframework.stereotype.Service;


@Service
public class SimpleSecurityServiceImpl extends AbstractRepositoryService implements ISecurityService {

	/** The repository session object to access the content repository */

	private static final String FOLDER_MY_DOCUMENT = "/MyDocumentArchive/others";
	
	public void setSession(Session session) {
		this.session = session;

	}

	
    private JackrabbitAccessControlList getAclList(AccessControlManager acMgr, String path)
            throws  EcmException {
        // try applicable (new) ACLs first
        AccessControlPolicyIterator itr = null;
		try {
			itr = acMgr.getApplicablePolicies(path);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while (itr.hasNext()) {
            AccessControlPolicy policy = itr.nextAccessControlPolicy();
            if (policy instanceof JackrabbitAccessControlList) {
                return (JackrabbitAccessControlList) policy;
            }
        }
        // try if there is an acl that has been set before:
        AccessControlPolicy[] pcls = new AccessControlPolicy[]{};
		try {
			pcls = acMgr.getPolicies(path);
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for (AccessControlPolicy policy : pcls) {
            if (policy instanceof JackrabbitAccessControlList) {
                return (JackrabbitAccessControlList) policy;
            }
        }
        
/*        for (AccessControlPolicyIterator it = acMgr.getApplicablePolicies(path); it.hasNext();) {
            AccessControlPolicy acp = it.nextAccessControlPolicy();
            if (acp instanceof AccessControlList) {
                return (AccessControlList) acp;
            }
        }
        AccessControlPolicy[] acps = acMgr.getPolicies(path);
        for (int i = 0; i < acps.length; i++) {
            if (acps[i] instanceof AccessControlList) {
                return (AccessControlList) acps[i] ;
            }
        }*/
        throw new EcmException("No AccessControlList at " + path);
    }

    protected Map<String, Value> getRestrictions(Session s, String path) {
        return Collections.emptyMap();
    }
    protected Privilege[] privilegesFromNames(String[] privilegeNames) throws RepositoryException {
        //AccessControlManager acMgr = getAccessControlManager(this.session);
        Privilege[] privs = new Privilege[privilegeNames.length];
/*        for (int i = 0; i < privilegeNames.length; i++) {
            privs[i] = acMgr.privilegeFromName(privilegeNames[i]);
        }*/
        return privs;
    }
    


    protected JackrabbitAccessControlList givePrivileges(String nPath, Privilege[] privileges,
                                                         Map<String, Value> restrictions)
            throws EcmException, RepositoryException {
       // return modifyPrivileges(nPath, testUser.getPrincipal(), privileges, true, restrictions);
    	return null;
    }

    
    public void testAccessControlPrivileges() throws RepositoryException, EcmException {


        /* grant 'testUser' rep:write, rep:readAccessControl and
           rep:modifyAccessControl privileges at 'path' */
        Privilege[] privileges = privilegesFromNames(new String[] {
                PrivilegeRegistry.REP_WRITE,
                Privilege.JCR_READ_ACCESS_CONTROL,
                Privilege.JCR_MODIFY_ACCESS_CONTROL
        });
        JackrabbitAccessControlList tmpl = givePrivileges(FOLDER_MY_DOCUMENT, privileges, getRestrictions(this.session, FOLDER_MY_DOCUMENT));

/*        Session testSession = getTestSession();
        AccessControlManager testAcMgr = getTestACManager();*/
        /*
         testuser must have
         - permission to view AC items
         - permission to modify AC items
        */
        // the policy node however must be visible to the test-user
/*        assertTrue(testSession.itemExists(tmpl.getPath() + "/rep:policy"));

        testAcMgr.getPolicies(tmpl.getPath());
        testAcMgr.removePolicy(tmpl.getPath(), tmpl);*/
        
    }

    public void setAccessControllxxx(String path,String authority,String privilege) throws EcmException{
    	PrincipalManager pMgr = this.getPrincipalManager();
    	Principal principal = pMgr.getPrincipal(authority);
    	
    	JackrabbitAccessControlManager acMgr = this.getJackrabbitAccessControlManager();
    	Set<Principal> principals = new HashSet<Principal>();
    	principals.add(principal);
    	JackrabbitAccessControlList list = this.getAclList(acMgr, path);

    	try {
        	Privilege priv = acMgr.privilegeFromName(privilege);
	        
			// add entry
			Privilege[] privileges = new Privilege[] { priv };
			Map<String, Value> restrictions = new HashMap<String, Value>();
			ValueFactory vf = session.getValueFactory();
			restrictions.put("rep:nodePath", vf.createValue("/", PropertyType.PATH));
			restrictions.put("rep:glob", vf.createValue("*"));
			list.addEntry(principal, privileges, true /* allow or deny */, restrictions);

			// reorder entries
			//list.orderBefore(entry, entry2);

			// finally set policy again & save
			acMgr.setPolicy(list.getPath(), list);
			session.save();
			
/*
 	        AccessControlPolicy[] policies = acMgr.getPolicies(path);
 	        for (AccessControlPolicy policy : policies) {
	            if (policy instanceof JackrabbitAccessControlList) {
	            	JackrabbitAccessControlList pl = (JackrabbitAccessControlList) policy;
	            	if(pl != null){
	                	AccessControlEntry entries[] = pl.getAccessControlEntries();    
	                	for(AccessControlEntry entry: entries){
	                		System.out.println(entry.toString());
	                	}
	            	}
	
	            }
	        }
			*/
		} catch (AccessDeniedException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_ACCESS_DENIED);
			
		} catch (AccessControlException e) {
 			e.printStackTrace();
 			
 		} catch (UnsupportedRepositoryOperationException e) {
 			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
		
		} catch (RepositoryException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		}
    }
    public void setAccessControll(String path,String authority,String privilege) throws EcmException{
    	PrincipalManager pMgr = this.getPrincipalManager();
    	Principal principal = pMgr.getPrincipal(authority);
    	
    	JackrabbitAccessControlManager acMgr = this.getJackrabbitAccessControlManager();
    	JackrabbitAccessControlList list = this.getAclList(acMgr, path);

    	try {
        	Privilege priv = acMgr.privilegeFromName(privilege);
	        
        	// remove all existing entries
        	for (AccessControlEntry e : list.getAccessControlEntries()) {
        		//list.removeAccessControlEntry(e);
        		System.out.println(e.getPrincipal().getName());
        		Privilege [] pv = e.getPrivileges();
        		for(Privilege p: pv){
        			System.out.println(p.getName());
        		}
        	}
        	
			// add entry
			Privilege[] privileges = new Privilege[] { priv };
			list.addAccessControlEntry(principal, privileges);
			//list.addAccessControlEntry(principal, privileges);
			list.addEntry(principal, privileges,true);
			
			// finally set policy again & save
			acMgr.setPolicy(path, list);
			session.save();
			

		} catch (AccessDeniedException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_ACCESS_DENIED);
			
		} catch (AccessControlException e) {
 			e.printStackTrace();
 			
 		} catch (UnsupportedRepositoryOperationException e) {
 			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
		
		} catch (RepositoryException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		}
    }
    
    public void denyAccessControll(String path,String authority,String privilege) throws EcmException{
    	PrincipalManager pMgr = this.getPrincipalManager();
    	Principal principal = pMgr.getPrincipal(authority);
    	
    	JackrabbitAccessControlManager acMgr = this.getJackrabbitAccessControlManager();
    	JackrabbitAccessControlList list = this.getAclList(acMgr, path);

    	try {
        	Privilege priv = acMgr.privilegeFromName(privilege);
	        
        	// remove all existing entries
        	for (AccessControlEntry e : list.getAccessControlEntries()) {
        		//list.removeAccessControlEntry(e);
        		System.out.println(e.getPrincipal().getName());
        		Privilege [] pv = e.getPrivileges();
        		for(Privilege p: pv){
        			System.out.println(p.getName());
        		}
        	}
        	
			// add entry
			Privilege[] privileges = new Privilege[] { priv };
			list.addEntry(principal, privileges,false);

			// finally set policy again & save
			acMgr.setPolicy(path, list);
			session.save();
			

		} catch (AccessDeniedException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_ACCESS_DENIED);
			
		} catch (AccessControlException e) {
 			e.printStackTrace();
 			
 		} catch (UnsupportedRepositoryOperationException e) {
 			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
		
		} catch (RepositoryException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		}
    }
    
    public boolean hasAccessControll(String path,String authority,String privilege) throws EcmException{
    	boolean hasPriv = false;
    	PrincipalManager pMgr = this.getPrincipalManager();
    	Principal principal = pMgr.getPrincipal(authority);
    	
    	JackrabbitAccessControlManager acMgr = this.getJackrabbitAccessControlManager();
    	Set<Principal> principals = new HashSet<Principal>();
    	principals.add(principal);
    	

    	try {
        	Privilege priv = acMgr.privilegeFromName(privilege);
        	
        	hasPriv = acMgr.hasPrivileges(path,principals, new Privilege[]{priv});
			
		} catch (AccessDeniedException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_ACCESS_DENIED);
			
		} catch (AccessControlException e) {
 			e.printStackTrace();
 			
 		} catch (UnsupportedRepositoryOperationException e) {
 			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
		
		} catch (RepositoryException e) {
			throw new EcmException(e.getMessage(),e, ErrorCodes.REPOSITROY_ERR_GENERIC);
			
		}
    	return hasPriv;
    }
    
	public void listACL() {
		try {
			final String path = FOLDER_MY_DOCUMENT;

			JackrabbitSession js = this.getJackRabbitSession();
			
			//this.createUser();

			
			//PrincipalManager pm = js.getPrincipalManager();
			
			PrincipalManager pMgr = js.getPrincipalManager();
			System.out.println("User id:  "+ session.getUserID());
			Principal principal = pMgr.getPrincipal(session.getUserID());
			
			// get the Jackrabbit access control manager
			JackrabbitAccessControlManager acMgr = (JackrabbitAccessControlManager) session.getAccessControlManager();

			JackrabbitAccessControlPolicy[] ps = acMgr.getApplicablePolicies(principal); // getPolicies or getApplicablePolicies()
			JackrabbitAccessControlList list = null;
			try {
				list = this.getAclList(acMgr, path);
			} catch (EcmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(ps.length>0)
			list = (JackrabbitAccessControlList) ps[0];

			// list entries
			if(list.getAccessControlEntries().length > 0){
				
				AccessControlEntry  aclEntries[] = null;
				aclEntries = list.getAccessControlEntries();
				for(AccessControlEntry en: aclEntries){
					String principalName = en.getPrincipal().getName();
					System.out.println("Principal name: "+ principalName);
					Privilege privileages[]= en.getPrivileges();
					for(Privilege pri: privileages){
						System.out.println("Privileage: "+ pri.getName() );
					}
				}
			}
			else{
				//list = new new ACLTemplate(path, pMgr, js.get, sImpl.getValueFactory(), sImpl)
			}

			
			// add entry
			Privilege[] privileges = new Privilege[] { acMgr.privilegeFromName(Privilege.JCR_ALL)  };
			Map<String, Value> restrictions = new HashMap<String, Value>();
			ValueFactory vf = session.getValueFactory();
			restrictions.put("rep:nodePath", vf.createValue("/", PropertyType.PATH));
			restrictions.put("rep:glob", vf.createValue("*"));
			list.addEntry(principal, privileges, true /* allow or deny */, restrictions);

			// reorder entries
			//list.orderBefore(entry, entry2);

			// finally set policy again & save
			acMgr.setPolicy(list.getPath(), list);
			session.save();
			

			
/*			UserManager uMgr = this.getJackRabbitUserManager();
			//uMgr.getAuthorizable("");

/*			// remove all existing entries
			for (AccessControlEntry e : acl.getAccessControlEntries()) {
			    acl.removeAccessControlEntry(e);
			}
			// add a new one for the special "everyone" principal
			acl.addAccessControlEntry(EveryonePrincipal.getInstance(), privileges);

			// the policy must be re-set
			aMgr.setPolicy(path, acl);*/
			this.session.save();
			
			
			
		} catch (UnsupportedRepositoryOperationException e) {
			
			e.printStackTrace();
		} catch (RepositoryException e) {
			
			e.printStackTrace();
		}

	}


	public String[] getSupportedPrivileges(String path) throws EcmException {
    	JackrabbitAccessControlManager acMgr = this.getJackrabbitAccessControlManager();
    	 try {
    		 Privilege[] privileges  = acMgr.getSupportedPrivileges(path);
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
