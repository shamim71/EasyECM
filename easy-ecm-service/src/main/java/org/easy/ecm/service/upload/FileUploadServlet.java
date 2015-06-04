package org.easy.ecm.service.upload;

import static org.easy.ecm.service.Constants.PATH_SEPARATOR;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.jackrabbit.util.Text;
import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.content.service.DocumentMetadata;
import org.easy.ecm.content.service.EcmDocument;
import org.easy.ecm.content.service.IDocumentService;
import org.easy.ecm.content.service.repository.WorkSpaceSessionFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;







import org.easy.ecm.service.Constants;

@SuppressWarnings("serial")
@WebServlet(name = "FileUploadServlet", urlPatterns = { "/FileUploadServlet" })
@MultipartConfig(location = "/tmp")
public class FileUploadServlet extends HttpServlet {

	private final static String DOWNLOAD_URL = "/service/file/content?";
	private final static String ALTERNATIVE_DOWNLOAD_URL = "/service/f?";
	/** Logger handler */

	
	Logger logger = LoggerFactory.getLogger(getClass().getCanonicalName());
	
	private String getUrlEncodedValue(String value) {
		String p = null;
		try {
			p = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			p = value;
		}
		return p;
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	    
		final String folderPath = request.getParameter("path");
		String workSpace = request.getParameter("workspace");
		final String returnURL = request.getParameter("callback");

		 String apiKey = request.getParameter("api_key");
		 String apiSecreet = request.getParameter("api_secreet");

		WorkSpaceSessionFactory sessionFactory = getContext().getBean(
				"sessionFactory", WorkSpaceSessionFactory.class);
		Session session = null;
		IDocumentService documentService = null;
		String downloadLink = "";
		String alternativeDownloadLink = "";
		
		String params = "api_key=" + getUrlEncodedValue(apiKey)
				+ "&api_secreet=" + getUrlEncodedValue(apiSecreet)
				+ "&path=";
		

		
		for (Part part : request.getParts()) {

			String filename = "";
			for (String s : part.getHeader("content-disposition").split(";")) {
				if (s.trim().startsWith("filename")) {
					filename = s.split("=")[1].replaceAll("\"", "");
				}
			}
			

			filename = Text.escapeIllegalJcrChars(filename);

			logger.info("File Name: "+filename);
			logger.info(".........................................");

			if (!StringUtils.isEmpty(filename)) {
				InputStream filecontent = part.getInputStream();

				EcmDocument document = creatDocument(filename, "utf-8",
						part.getContentType(), filecontent);
				
				
				final String parameters = params
						+ getUrlEncodedValue(folderPath
								+ PATH_SEPARATOR + document.getFileName());

				final String callBackPath1 = request.getContextPath()
						+ DOWNLOAD_URL + parameters;
				
				final String callBackPath2 = request.getContextPath()
						+ ALTERNATIVE_DOWNLOAD_URL;
				
				URL url1 = new URL(request.getScheme(),
						request.getServerName(),
						request.getServerPort(), callBackPath1);
				
				URL url2 = new URL(request.getScheme(),
						request.getServerName(),
						request.getServerPort(), callBackPath2);
				
				downloadLink = url1.toString();
				alternativeDownloadLink = url2.toString();
				try {

					// Check if the file is exist


					
					synchronized(this){
						try {
							if (StringUtils.isEmpty(workSpace)) {
								workSpace = Constants.DEFAULT_WORKSPACE;
							}
							session = sessionFactory.getCurrentSession(apiKey, apiSecreet,
									workSpace);

							documentService = getDocumentService();
							documentService.setSession(session);

							createParentFolder(documentService, folderPath);
						

						} catch (EcmException e1) {

							e1.printStackTrace();
						}
						
						Node fileNode = null;
						try {

							fileNode = documentService.getNode(folderPath
									+ PATH_SEPARATOR + document.getFileName());
						} catch (EcmException e) {
							e.printStackTrace();
						}
						
						if (fileNode == null) {
							documentService.addDocumentToFolder(folderPath,
									document);
						}
						// Update node
						else {
							documentService.updateDocumentToFolder(folderPath,
									document);
						}
						//alternativeDownloadLink = alternativeDownloadLink + document.getMetadata().getIdentifier()+"/"+document.getFileName();
						alternativeDownloadLink = alternativeDownloadLink +"p="+ folderPath+"/"+document.getFileName();
					}

				
				} catch (EcmException e) {

					e.printStackTrace();
				}
				part.write(filename);
				// part.delete();
			}

		}

		if (!StringUtils.isEmpty(returnURL)) {
			response.sendRedirect(returnURL);
		} else {
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			// Assuming your json object is **jsonObject**, perform the
			// following, it will return your json object
			out.print("{\"success\":\"true\",\"data\":\""+ downloadLink +"\",\"url\":\""+ alternativeDownloadLink +"\"}");
			out.flush();
		}
	}

	private Node createParentFolder(IDocumentService documentService,
			final String folderPath) throws EcmException {

		String paths[] = folderPath.split(PATH_SEPARATOR);
		String parentPath = "";
		Node node = null;
		Node parent = null;
		for (int i = 0; i < paths.length; i++) {
			if (!StringUtils.isEmpty(paths[i])) {
				final String path = paths[i];//ISO9075.encode(paths[i]);
				
				parentPath = parentPath + PATH_SEPARATOR + path;
				
				node = documentService.getNode(parentPath);
			
				if (node == null && parent == null) {
					
					node = documentService.createFolder(parentPath);
				}
				else if(node == null && parent != null ){
					node = documentService.createFolder(parent,path);
				}
				parent = node;
				
			}

		}
		return node;
	}

	protected EcmDocument creatDocument(String docName, String encoding,
			String mimeType, InputStream inStream) throws IOException {

		EcmDocument doc = new EcmDocument();
		doc.setFileName(docName);
		doc.setInputStream(inStream);
		DocumentMetadata meta = new DocumentMetadata();
		meta.setFileName(docName);
		meta.setEncoding(encoding);
		meta.setMimeType(mimeType);

		doc.setMetadata(meta);

		return doc;

	}

	private IDocumentService getDocumentService() {

		WebApplicationContext context = getContext();

		return context.getBean("documentService", IDocumentService.class);
	}

	private WebApplicationContext getContext() {

		ServletContext context = getServletContext();
		WebApplicationContext springContext = WebApplicationContextUtils
				.getWebApplicationContext(context);
		return springContext;
	}

	private void print(WebApplicationContext applicationContext) {
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		StringBuilder printBuilder = new StringBuilder(
				"Spring Beans In Context: ");
		;
		for (String beanName : beanNames) {
			printBuilder.append("\n");
			printBuilder.append(" Bean Name: ");
			printBuilder.append(beanName);
			printBuilder.append(" Bean Class: ");
			printBuilder
					.append(applicationContext.getBean(beanName).getClass());
		}
		System.out.println(printBuilder.toString());
	}
}
