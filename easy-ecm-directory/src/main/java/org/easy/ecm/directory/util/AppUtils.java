/**
 * 
 */
package org.easy.ecm.directory.util;

import org.springframework.context.ApplicationContext;

/**
 * @author bkurtz
 *
 */
public class AppUtils {

	/**
	 * 
	 */
	public AppUtils() {
		// TODO Auto-generated constructor stub
	}
	private static ApplicationContext applicationContext;

    /**
     * Return the application context.
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext() {
    	return (applicationContext);
    }

    /**
     * Set the application context.
     *
     * @param context the application context to set.
     */
    public static void setApplicationContext(final ApplicationContext context) {
    	applicationContext = context;
    }

    /**
     * Returns the Bean given the bean name
     * @param name bean name
     * @return bean instance
     */
    public static Object getBean(final String name) {
    	if(applicationContext == null) {
    		throw new IllegalArgumentException(
    				"ApplicationContext is not initialized");
    	}
    	return applicationContext.getBean(name);
    }

}
