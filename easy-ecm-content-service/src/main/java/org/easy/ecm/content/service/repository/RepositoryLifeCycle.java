package org.easy.ecm.content.service.repository;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="repositoryLifeCycle")
public class RepositoryLifeCycle implements InitializingBean, DisposableBean {

	@Autowired
	private RepositoryImpl repository;

	public void destroy() throws Exception {
		if(repository != null){
			repository.shutdown();
		}
		System.out.println("Repository has been shutdown.......................");
	}

	public void afterPropertiesSet() throws Exception {
				
	}

}
