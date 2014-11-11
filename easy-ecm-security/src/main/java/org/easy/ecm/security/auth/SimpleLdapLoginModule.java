package org.easy.ecm.security.auth;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class SimpleLdapLoginModule  implements LoginModule{
	
	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;

	private boolean commitSucceeded = false;
	private boolean loginSucceeded = false;

	private String username;
	private EcmPrincipal user;
	private EcmPrincipal[] roles;
	
    // temporary state
    Vector   tempCredentials;
    Vector   tempPrincipals;

    // the authentication status
    boolean  success;

    // configurable options
    boolean  debug;
    

	public SimpleLdapLoginModule() {
        tempCredentials = new Vector();
        tempPrincipals  = new Vector();
        success = false;
        debug   = false;
	}

	public boolean abort() throws LoginException {
        if (debug)
            System.out.println("\t\t[SimpleLdapLoginModule] abort");

        // Clean out state
        success = false;

        tempPrincipals.clear();
        tempCredentials.clear();

        if (callbackHandler instanceof PassiveCallbackHandler)
            ((PassiveCallbackHandler)callbackHandler).clearPassword();

        logout();

        return(true);
	}

	public boolean commit() throws LoginException {

        if (debug)
            System.out.println("\t\t[RdbmsLoginModule] commit");

        if (success) {

            if (subject.isReadOnly()) {
                throw new LoginException ("Subject is Readonly");
            }

            try {
                Iterator it = tempPrincipals.iterator();
                
                if (debug) {
                    while (it.hasNext())
                        System.out.println("\t\t[SimpleLdapLoginModule] Principal: " + it.next().toString());
                }

                subject.getPrincipals().addAll(tempPrincipals);
                subject.getPublicCredentials().addAll(tempCredentials);

                tempPrincipals.clear();
                tempCredentials.clear();

                if(callbackHandler instanceof PassiveCallbackHandler)
                    ((PassiveCallbackHandler)callbackHandler).clearPassword();

                return(true);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                throw new LoginException(ex.getMessage());
            }
        } else {
            tempPrincipals.clear();
            tempCredentials.clear();
            return(true);
        }
	}

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {

        // save the initial state
        this.callbackHandler = callbackHandler;
        this.subject     = subject;
        this.sharedState = sharedState;
        this.options     = options;

        // initialize any configured options
        if (options.containsKey("debug"))
            debug = "true".equalsIgnoreCase((String)options.get("debug"));


        if (debug) {
            System.out.println("\t\t[SimpleLdapLoginModule] initialize");
        }
    }


	public boolean login() throws LoginException {
        if (debug)
            System.out.println("\t\t[SimpleLdapLoginModule] login");

        if (callbackHandler == null)
            throw new LoginException("Error: no CallbackHandler available " +
                    "to garner authentication information from the user");

        try {
            // Setup default callback handlers.
            Callback[] callbacks = new Callback[] {
                new NameCallback("Username: "),
                new PasswordCallback("Password: ", false)
            };

            callbackHandler.handle(callbacks);

            String username = ((NameCallback)callbacks[0]).getName();
            String password = new String(((PasswordCallback)callbacks[1]).getPassword());

            ((PasswordCallback)callbacks[1]).clearPassword();

           //success = rdbmsValidate(username, password);

            callbacks[0] = null;
            callbacks[1] = null;

            if (!success)
                throw new LoginException("Authentication failed: Password does not match");

            return(true);
            
        } catch (LoginException ex) {
            throw ex;
        } catch (Exception ex) {
            success = false;
            throw new LoginException(ex.getMessage());
        }

	}

	public boolean logout() throws LoginException {
        if (debug)
            System.out.println("\t\t[RdbmsLoginModule] logout");

        tempPrincipals.clear();
        tempCredentials.clear();

        if (callbackHandler instanceof PassiveCallbackHandler)
            ((PassiveCallbackHandler)callbackHandler).clearPassword();

        // remove the principals the login module added
        Iterator it = subject.getPrincipals(EcmPrincipal.class).iterator();
        while (it.hasNext()) {
        	EcmPrincipal p = (EcmPrincipal)it.next();
            if(debug)
                System.out.println("\t\t[RdbmsLoginModule] removing principal "+p.toString());
            subject.getPrincipals().remove(p);
        }

        // remove the credentials the login module added
        it = subject.getPublicCredentials(EcmCredential.class).iterator();
        while (it.hasNext()) {
        	EcmCredential c = (EcmCredential)it.next();
            if(debug)
                System.out.println("\t\t[RdbmsLoginModule] removing credential "+c.toString());
            subject.getPrincipals().remove(c);
        }

        return(true);
	}

}
