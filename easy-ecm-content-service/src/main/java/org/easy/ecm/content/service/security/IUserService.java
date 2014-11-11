/*
 * Copyright 2011-20012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easy.ecm.content.service.security;

import java.util.List;

import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;

/**
 * Interface to be implemented by beans that want to provide user management
 * service. A BeanFactory is supposed to invoke the destroy
 * method if it disposes a cached singleton. An application context
 * is supposed to dispose all of its singletons on close.
 *
 * <p>An alternative to implementing DisposableBean is specifying a custom
 * destroy-method, for example in an XML bean definition.
 * For a list of all bean lifecycle methods, see the BeanFactory javadocs.
 *
 * @author Shamim Ahmmed
 * @since 1.0
 */
public interface IUserService {
	
	/**
	 * @param session
	 */
	public void setSession(Session session);
	
	/**
	 * @param filter
	 * @return
	 * @throws EcmException
	 */
	public List<RepositoryUser> listUser(String filter) throws EcmException;
		
	public RepositoryUser findUser(String userName) throws EcmException;
	
	public void addUser(RepositoryUser user) throws EcmException;
	
	public void updateUser(RepositoryUser user) throws EcmException;
	
	public void removeUser(RepositoryUser user) throws EcmException;

	public void changePassword(String userId,String newPassword);
	
	public void addToAdmin(String userId);
}
