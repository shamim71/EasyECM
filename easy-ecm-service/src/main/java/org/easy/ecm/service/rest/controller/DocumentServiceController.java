package org.easy.ecm.service.rest.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.nodetype.NodeType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.content.service.DocumentMetadata;
import org.easy.ecm.content.service.EcmDocument;
import org.easy.ecm.content.service.IDocumentService;
import org.easy.ecm.content.service.repository.WorkSpaceSessionFactory;
import org.easy.ecm.service.rest.bean.DocumentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easy.ecm.service.Constants;

/**
 * This class is responsible for providing user information such as list of
 * users, manipulating a repository user or removing user from the repository
 * registry. All the above mentioned services are provided via rest interfaces.
 * 
 * @author Shamim Ahmmed
 * 
 */
@Controller
public class DocumentServiceController extends BaseController {

	static final Logger logger = LoggerFactory
			.getLogger(DocumentServiceController.class);
	private final static String DOWNLOAD_URL = "/service/file/content?";

	/** Workspace session factory providing acquiring repository session */

	@Autowired
	WorkSpaceSessionFactory sessionFactory;

	@Autowired
	IDocumentService documentService;

	@org.springframework.beans.factory.annotation.Value("${easy.ecm.jcr.admin.login}" )
	private String login;
	
	@org.springframework.beans.factory.annotation.Value("${easy.ecm.jcr.admin.password}" )
	private String pass;
	
	private Session getSession(final String login, final String password) {
		Session session = null;
		try {
			session = this.sessionFactory.getCurrentSession(login, password,
					Constants.DEFAULT_WORKSPACE);
		} catch (EcmException e1) {
			return null;
		}
		return session;
	}

	@RequestMapping(method = GET, value = "/file/content")
	public void downloadFileContent(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String path)
			throws IOException {

		final String apiKey = request.getParameter("api_key");
		final String apiSecreet = request.getParameter("api_secreet");

		logger.info("Downloading Document with path :" + path);

		EcmDocument document;
		try {
			Session ses = getSession(apiKey, apiSecreet);
			documentService.setSession(ses);

			document = documentService.retrieveDocumentAsStream(path);
			/** Prepare response */
			response.setContentType(document.getMetadata().getMimeType());
			response.setHeader("Content-Disposition", "filename="
					+ document.getMetadata().getFileName());
			/** Send content to Browser */

			InputStream inputStream = null;

			inputStream = document.getInputStream();
			byte[] buffer = new byte[1024];
			int bytesRead = 0;

			do {
				bytesRead = inputStream.read(buffer);
				if (bytesRead == -1)
					break;
				response.getOutputStream().write(buffer, 0, bytesRead);
				response.getOutputStream().flush();
			} while (true);

			response.getOutputStream().flush();

		} catch (EcmException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(method = GET, value = "/f")
	public void downloadFileContentByPath(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String p)
			throws IOException {

		logger.debug("Downloading file, "+ p);
		
		
		final String referrer = request.getHeader("referer"); 
		System.out.println("Referrer: "+ referrer + ", File: "+p );
		EcmDocument document;
		
		
		try {
			Session ses = getSession(login, pass);
			documentService.setSession(ses);

			document = documentService.retrieveDocumentAsStream(p);

			/** Prepare response */
			response.setContentType(document.getMetadata().getMimeType());
			response.setHeader("Content-Disposition", "filename="
					+ document.getMetadata().getFileName());
			/**Set content size */
			response.setContentLength((int )document.getMetadata().getSize());
			/** Send content to Browser */
			
			InputStream inputStream = null;

			inputStream = document.getInputStream();
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			int totalBytes = 0;
			do {
				bytesRead = inputStream.read(buffer);
				if (bytesRead == -1)
					break;
				totalBytes = totalBytes + bytesRead;
				response.getOutputStream().write(buffer, 0, bytesRead);
				response.getOutputStream().flush();
			} while (true);
			
			inputStream.close();
			
			response.flushBuffer();

		} catch (EcmException e) {
			e.printStackTrace();
		}



	}
	
/*	@RequestMapping(value = "/files/{file_name}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getFile(@PathVariable("file_name") String fileName) {
		
		File f = new File("");
	    return new FileSystemResource(f); 
	}*/
	@RequestMapping(method = RequestMethod.DELETE, value = "/file/content")
	public @ResponseBody
	String deleteFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String path) throws IOException {

		final String apiKey = request.getParameter("api_key");
		final String apiSecreet = request.getParameter("api_secreet");

		logger.debug("Deleting Document with path :" + path);
		try {
			Session ses = getSession(apiKey, apiSecreet);
			documentService.setSession(ses);

			documentService.removeNode(path);

		} catch (EcmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK";
	}

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

	@RequestMapping(method = RequestMethod.GET, value = "/file/list")
	public @ResponseBody
	List<DocumentInfo> listContents(HttpServletResponse httpResponse,
			HttpServletRequest httpRequest,
			@RequestParam(required = false) String path,
			@RequestParam String api_key, @RequestParam String api_secreet) {

		List<DocumentInfo> docs = new ArrayList<>();
		try {

			final String params = "api_key=" + getUrlEncodedValue(api_key)
					+ "&api_secreet=" + getUrlEncodedValue(api_secreet)
					+ "&path=";

			Session ses = getSession(api_key, api_secreet);
			documentService.setSession(ses);

			List<DocumentMetadata> metaData = documentService.listNode(path);
			for (DocumentMetadata meta : metaData) {
				DocumentInfo info = new DocumentInfo();
				info.setFolder(meta.isFolder());
				info.setName(meta.getFileName());
				info.setLastModified(meta.getLastModified());

				final String parameters = params
						+ getUrlEncodedValue(meta.getFullPath());

				final String callBackPath = httpRequest.getContextPath()
						+ DOWNLOAD_URL + parameters;

				URL url = new URL(httpRequest.getScheme(),
						httpRequest.getServerName(),
						httpRequest.getServerPort(), callBackPath);

				info.setDownloadLink(url.toString());
				docs.add(info);
			}

		} catch (EcmException | MalformedURLException e) {

			e.printStackTrace();
		}
	 Collections.sort(docs, new Comparator<DocumentInfo>() {

			@Override
			public int compare(DocumentInfo o1, DocumentInfo o2) {
				if(o1 == null || o2 == null || o1.getLastModified() == null || o2.getLastModified() == null){
					return 0;
				}
				if(o1.getLastModified().after(o2.getLastModified())){
					return -1;
				}
				if(o1.getLastModified().before(o2.getLastModified())){
					return 1;
				}
				return 0;
			}
		});
		return docs;
	}

	private boolean isFolder(Node node) throws RepositoryException {
		if (node.isNodeType(NodeType.NT_FOLDER)) {
			return true;
		}
		return false;
	}

	private boolean isFile(Node node) throws RepositoryException {
		if (node.isNodeType(NodeType.NT_FILE)) {
			return true;
		}
		return false;
	}

	/** Recursively outputs the contents of the given node. */
	private void dump(Node node) throws RepositoryException {
		// First output the node path
		System.out.println(node.getPath() + " " + node.getName());
		// Skip the virtual (and large!) jcr:system subtree
		if (node.getName().equals("jcr:system")) {
			return;
		}

		// Then output the properties
		PropertyIterator properties = node.getProperties();
		while (properties.hasNext()) {
			Property property = properties.nextProperty();
			if (property.getDefinition().isMultiple()) {
				// A multi-valued property, print all values
				Value[] values = property.getValues();
				for (int i = 0; i < values.length; i++) {
					System.out.println(property.getPath() + " = "
							+ values[i].getString());
				}
			} else {
				// A single-valued property
				System.out.println(property.getPath() + " = "
						+ property.getString());
			}
		}

		// Finally output all the child nodes recursively
		NodeIterator nodes = node.getNodes();
		while (nodes.hasNext()) {
			Node child = nodes.nextNode();
			PropertyIterator p = child.getProperties();
			while (p.hasNext()) {
				Property pp = p.nextProperty();

				// System.out.println(pp.getPath() + " = " + pp.getString());
			}
			if (isFile(child)) {
				System.out.println("File: " + child.getPath());
			} else if (isFolder(child)) {
				System.out.println("Folder: " + child.getPath());
			} else {
				System.out.println("Others: " + child.getPath());
			}
			// dump(nodes.nextNode());
		}
	}

	/**
	 * @return the sessionFactory
	 */
	public WorkSpaceSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(WorkSpaceSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
