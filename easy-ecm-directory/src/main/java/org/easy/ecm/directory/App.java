package org.easy.ecm.directory;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.easy.ecm.directory.domain.DirectoryUser;
import org.easy.ecm.directory.service.UserDirectoryService;
import org.easy.ecm.directory.util.AppUtils;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        BasicConfigurator.configure();
        GenericApplicationContext appContext = new GenericApplicationContext();
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(appContext);
        xmlReader.loadBeanDefinitions(new ClassPathResource("spring-directory-context.xml"));
        appContext.refresh();
        AppUtils.setApplicationContext(appContext);
        

        UserDirectoryService userDao = (UserDirectoryService) AppUtils.getBean("userDirectoryService");
       List<DirectoryUser> users = userDao.loadDirectoryUsers(1, 1);
       for(DirectoryUser person: users){
    	   System.out.println(person.toString());
       }
       
      //  Person person = dao.findByPrimaryKey("mark");
       // System.out.println(person.getFullName());
    }
}
