package com.easy.ecm.service.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class JcrSessionContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("Shutting down jcr repository....");
		RepositoryImpl repository = getContext(sce).getBean("repository",
				RepositoryImpl.class);

		repository.shutdown();

	}

	private WebApplicationContext getContext(ServletContextEvent event) {

		ServletContext context = event.getServletContext();
		WebApplicationContext springContext = WebApplicationContextUtils
				.getWebApplicationContext(context);
		return springContext;
	}
}
