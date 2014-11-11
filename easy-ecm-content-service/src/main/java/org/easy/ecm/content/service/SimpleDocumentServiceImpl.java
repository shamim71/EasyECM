package org.easy.ecm.content.service;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;

import org.easy.ecm.common.exception.EcmException;
import org.easy.ecm.common.exception.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The SimpleDocumentServiceImpl class is the implementation of
 * <code>IDocumentService</code> interface. The class implementation based on
 * Java Content Repository and Apache Jack Rabbit implementation. It implements
 * the basic content related operation defined by interface.
 * 
 * @author Shamim Ahmmed
 */

@Service
public class SimpleDocumentServiceImpl extends AbstractDocumentService
		implements IDocumentService {

	/** Logger handler */
	final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getSimpleName());

	/**
	 * Create a new folder under the root folder of workspace connected by the
	 * session.
	 * 
	 * @param folderName
	 *            the name of the folder
	 * 
	 * @throws EcmException
	 *             if an error occurs.
	 */
	public Node createFolder(String folderName) throws EcmException {
		logger.debug("Creating folder: " + folderName + " under root folder.");
		Node node = null;
		try {
			/** Get the root folder */
			Node rootNode = this.session.getRootNode();

			node = this.getFolder(rootNode, folderName);
			if (node != null) {
				throw new EcmException("Node," + folderName + " already exist",
						ErrorCodes.REPOSITROY_ERR_NODE_EXIST);
			}
			/** Add the folder */
			node = rootNode.addNode(folderName, NodeType.NT_FOLDER);

			/** finally save the changes */
			this.session.save();

		} catch (RepositoryException e) {
			throw new EcmException("Fail to create folder.", e,
					ErrorCodes.REPOSITROY_ERR_FAIL_TO_CREATE_FOLDER);
		}
		logger.debug("A new folder: " + folderName
				+ " has been created under root folder.");
		return node;
	}

	/**
	 * Get the <code>Node</code> instance with the specified path provided
	 * 
	 * @param nodePath
	 *            the full path of the node
	 * 
	 * @return the generated node instance
	 * @throws EcmException
	 *             if there is any error
	 */
	public Node getNode(String nodePath) throws EcmException {
		logger.debug("Retrieving folder: " + nodePath + " ");
		Node node = null;

		this.validateNodePath(nodePath);

		try {
			node = this.session.getNode(nodePath);

		} catch (PathNotFoundException e) {
			logger.error("Node path does not exist or invalid: " + nodePath);

		} catch (RepositoryException e) {
			throw new EcmException("Fail to load node.", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);

		}
		logger.debug("a node instance has been created using path : "
				+ nodePath + ".");

		return node;
	}

	/**
	 * Get the Child node instance from the parent node
	 * 
	 * @param pNode
	 *            the parent Node
	 * @param childName
	 *            the name of the child Node
	 * 
	 * @return the generated child node instance
	 * @throws EcmException
	 *             if there is any error
	 */
	public Node getFolder(Node pNode, String childName) throws EcmException {
		Node node = null;

		try {
			node = pNode.getNode(childName);

		} catch (PathNotFoundException e) {
			logger.error("Node path does not exist or invalid: " + childName);

		} catch (RepositoryException e) {
			throw new EcmException("Fail to load node.", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);
		}
		return node;
	}

	/**
	 * Create a folder under a parent folder with the folder name specified
	 * 
	 * @param parentPath
	 *            the parent folder path
	 * @param folderName
	 *            the name of the child folder
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public Node createFolder(String parentPath, String folderName)
			throws EcmException {
		logger.debug("Creating folder: " + folderName + " under folder "
				+ parentPath);
		Node node = null;
		try {
			/** Get the root folder */
			Node pNode = this.getNode(parentPath);

			/** Creating folder */
			node = this.createFolder(pNode, folderName);

		} catch (EcmException e) {
			throw e;
		}
		logger.debug("A new folder: " + folderName
				+ " has been created under parent folder, " + parentPath);
		return node;
	}

	/**
	 * Create a folder under a parent folder <code>Node</code> with the folder
	 * name specified
	 * 
	 * @param pNode
	 *            the parent folder Node
	 * @param folderName
	 *            the name of the child folder
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public Node createFolder(Node pNode, String folderName) throws EcmException {
		Node node = null;
		try {

			/** Validate if the parent node is exist */
			this.validateNodeInstance(pNode);

			/** Check if the folder is exist */
			node = this.getFolder(pNode, folderName);
			if (node != null) {
				throw new EcmException("Folder,'" + folderName
						+ "' already exist",
						ErrorCodes.REPOSITROY_ERR_NODE_EXIST);
			}
			/** Add the folder */
			node = pNode.addNode(folderName, NodeType.NT_FOLDER);

			/** finally save the changes */
			this.session.save();

		} catch (RepositoryException e) {
			throw new EcmException("Fail to create folder.", e,
					ErrorCodes.REPOSITROY_ERR_FAIL_TO_CREATE_FOLDER);
		}
		return node;
	}

	/**
	 * Add an non version-able <code>EcmDocument</code> into the folder name
	 * specified
	 * 
	 * @param folderPath
	 *            the name of the folder
	 * @param document
	 *            the EcmDocument need to add
	 * 
	 * @throws EcmException
	 *             if an error occurs.
	 */
	public void addDocumentToFolder(String folderPath, EcmDocument document)
			throws EcmException {
		logger.debug("Adding document to folder:" + folderPath);
		try {
			Node parentNode = this.getNode(folderPath);

			this.validateNodeInstance(parentNode);

			Node fileNode = parentNode.addNode(document.getMetadata()
					.getFileName(), NodeType.NT_FILE);
			
			this.createFileContentNode(fileNode, document);

			this.session.save();

		} catch (RepositoryException e) {
			throw new EcmException("Fail to add document to the folder,"
					+ folderPath, e, ErrorCodes.REPOSITROY_ERR_GENERIC);
		}
		logger.debug("A file has been added to the folder:" + folderPath);

	}

	@Override
	public void updateDocumentToFolder(String folderPath, EcmDocument document)
			throws EcmException {
		logger.debug("Updating document to folder:" + folderPath);
		try {
			Node fileNode = this.getNode(folderPath + "/"
					+ document.getFileName());

			this.updateFileContentNode(fileNode, document);

			this.session.save();

		} catch (RepositoryException e) {
			throw new EcmException("Fail to update document to the folder,"
					+ folderPath, e, ErrorCodes.REPOSITROY_ERR_GENERIC);
		}

	}

	/**
	 * Retrieve the <code>EcmDocument</code> provided by file full path
	 * 
	 * @param fullFilePath
	 *            the name of the file path
	 * 
	 * @return the generated <code>EcmDocument</code> object
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public EcmDocument retrieveDocument(String fullFilePath)
			throws EcmException {
		return this.readDocument(fullFilePath, 1);
	}

	/**
	 * Retrieve the <code>EcmDocument</code> provided by file full path. The
	 * content should be available as <code>InputStream</code>
	 * 
	 * @param fullFilePath
	 *            the name of the file path
	 * 
	 * @return the generated <code>EcmDocument</code> object
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public EcmDocument retrieveDocumentAsStream(String fullFilePath)
			throws EcmException {
		return this.readDocument(fullFilePath, 0);
	}

	/**
	 * Read the <code>EcmDocument</code> from the repository either as a stream
	 * or as a byte array.
	 * 
	 * @param fullFilePath
	 *            the path of the document
	 * @param readType
	 *            the type of method to read, 0 means Stream reading, 1 means
	 *            byte array reading
	 * 
	 * @return the generated <code>EcmDocument</code> object
	 * @throws EcmException
	 *             if there is any error
	 */
	private EcmDocument readDocument(String fullFilePath, int readType)
			throws EcmException {
		logger.debug("Loading document using file path ," + fullFilePath);
		
	
		/** Validate file path */
		this.validateNodePath(fullFilePath);

		Node node = this.getNode(fullFilePath);
		
		return readDocumentByNode(node, readType);

	}
	private EcmDocument readDocumentByNode(Node node, int readType)
			throws EcmException {
		EcmDocument doc = null;
		try {
			if (node == null) {
				return doc;
			}
			doc = new EcmDocument();

			Node resNode = node.getNode(Property.JCR_CONTENT);
			if (resNode == null) {
				return doc;
			}
			/** Load the document meta data */
			DocumentMetadata meta = this.loadDocumentMetadata(resNode);

			meta.setFileName(node.getName());
			meta.setIdentifier(node.getIdentifier());

			String createdBy = node.getProperty(Property.JCR_CREATED_BY)
					.getString();
			Calendar createdDate = node.getProperty(Property.JCR_CREATED)
					.getDate();

			meta.setCreated(createdDate.getTime());
			meta.setCreatedBy(createdBy);
			meta.setCheckedOut(node.isCheckedOut());
			meta.setFullPath(node.getPath());

			doc.setMetadata(meta);
			doc.setFileName(meta.getFileName());
			doc.setParentFolder(node.getParent().getName());

			/** Load the file content */
			Property dataProperty = resNode.getProperty(Property.JCR_DATA);
			if (dataProperty != null && readType == 0) {
				doc.setInputStream(dataProperty.getBinary().getStream());
				doc.getMetadata().setSize(dataProperty.getBinary().getSize());
			} else if (dataProperty != null && readType == 1) {
				byte[] contents = convertToByteArray(dataProperty.getBinary());
				doc.setContent(contents);
				doc.getMetadata().setSize(dataProperty.getBinary().getSize());
			}

		} catch (PathNotFoundException e) {
			throw new EcmException("Fail to read document from repository.", e,
					ErrorCodes.REPOSITROY_ERR_INVALID_PATH);

		} catch (RepositoryException e) {
			throw new EcmException("Fail to read document from repository.", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);

		} catch (IOException e) {
			throw new EcmException("Fail to read document from repository.", e,
					ErrorCodes.REPOSITROY_ERR_FAIL_TO_READ_CONTENT);
		}

		return doc;
	}
	/**
	 * Load the document <code>DocumentMetadata</code> object from the
	 * repository
	 * 
	 * @param nContent
	 *            the content <code>Node</code>
	 * 
	 * @return the generated <code>DocumentMetadata</code> object
	 * 
	 * @throws PathNotFoundException
	 *             if any property is missing
	 * @throws RepositoryException
	 *             if there is any repository error
	 */
	private DocumentMetadata loadDocumentMetadata(Node nContent)
			throws PathNotFoundException, RepositoryException {
		DocumentMetadata meta = new DocumentMetadata();

		String mimeType = nContent.getProperty(Property.JCR_MIMETYPE)
				.getString();
		String encoding = nContent.getProperty(Property.JCR_ENCODING)
				.getString();

		String lastModifiedBy = nContent.getProperty(
				Property.JCR_LAST_MODIFIED_BY).getString();
		Calendar lastModified = nContent
				.getProperty(Property.JCR_LAST_MODIFIED).getDate();

		meta.setEncoding(encoding);
		meta.setMimeType(mimeType);
		meta.setLastModified(lastModified.getTime());
		meta.setLastModifiedBy(lastModifiedBy);

		return meta;
	}

	/**
	 * Remove the specified node from the workspace
	 * 
	 * @param nodePath
	 *            the path of the node to be removed
	 * 
	 * @throws EcmException
	 *             if any error occurred
	 * 
	 */
	public void removeNode(String nodePath) throws EcmException {
		Node node = null;

		/** Load the node */
		node = this.getNode(nodePath);

		/** Validate if the node is exist or not */
		this.validateNodeInstance(node);

		try {
			node.remove();
			this.session.save();

		} catch (AccessDeniedException e) {
			throw new EcmException("Fail to remove node.", e,
					ErrorCodes.REPOSITROY_ERR_ACCESS_DENIED);

		} catch (VersionException e) {
			throw new EcmException("Fail to remove node.", e,
					ErrorCodes.REPOSITROY_ERR_VERSION_ERROR);

		} catch (LockException e) {
			throw new EcmException("Fail to remove node.", e,
					ErrorCodes.REPOSITROY_ERR_VERSION_ERROR);

		} catch (ConstraintViolationException e) {
			throw new EcmException("Fail to remove node.", e,
					ErrorCodes.REPOSITROY_ERR_CONSTRAINT_VIOLATION);

		} catch (RepositoryException e) {
			throw new EcmException("Fail to remove node.", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);
		}

	}

	/**
	 * Add a new version-able <code>EcmDocument</code> under the parent folder
	 * name provided
	 * 
	 * @param pFolderPath
	 *            the name of the parent folder
	 * @param document
	 *            the <code>EcmDocument</code> object
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public void addNewVersionableDocumentToFolder(String pFolderPath,
			EcmDocument document) throws EcmException {
		logger.debug("Adding versionable document to folder:" + pFolderPath);
		try {
			Node parentNode = this.getNode(pFolderPath);

			Node fileNode = parentNode.addNode(document.getMetadata()
					.getFileName(), NodeType.NT_FILE);
			fileNode.addMixin(NodeType.MIX_VERSIONABLE);

			this.createFileContentNode(fileNode, document);

			this.session.save();

			/** Get the version manager */
			VersionManager vm = this.session.getWorkspace().getVersionManager();

			/** Check in the changes as a new version */
			vm.checkin(fileNode.getPath());
			this.session.save();

		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		logger.debug("A document has been added to the folder:" + pFolderPath);

	}

	/**
	 * Create a file content node with the nod and document provided
	 * 
	 * @param fileNode
	 *            the file node
	 * @param document
	 *            the document object
	 * 
	 * @throws ItemExistsException
	 *             if the item not exist
	 * @throws PathNotFoundException
	 *             if file path is not valid
	 * @throws NoSuchNodeTypeException
	 *             if it is not a valid document type
	 * @throws LockException
	 *             if the node is locked
	 * @throws VersionException
	 *             the version graph is wrong
	 * @throws ConstraintViolationException
	 *             if violate the node constraints
	 * @throws RepositoryException
	 *             if other repository exception occurs
	 */
	private void createFileContentNode(Node fileNode, EcmDocument document)
			throws ItemExistsException, PathNotFoundException,
			NoSuchNodeTypeException, LockException, VersionException,
			ConstraintViolationException, RepositoryException {
		Node resNode = fileNode.addNode(Property.JCR_CONTENT,
				NodeType.NT_RESOURCE);
		
		this.updateResouceNodePropertyAsStream(fileNode, resNode, document);
	}

	/**
	 * Update a node with the file node and document provided
	 * 
	 * @param fileNode
	 *            the node instance
	 * @param document
	 *            the document instance
	 * 
	 * @throws ItemExistsException
	 *             if the item not exist
	 * @throws PathNotFoundException
	 *             if file path is not valid
	 * @throws NoSuchNodeTypeException
	 *             if it is not a valid document type
	 * @throws LockException
	 *             if the node is locked
	 * @throws VersionException
	 *             the version graph is wrong
	 * @throws ConstraintViolationException
	 *             if violate the node constraints
	 * @throws RepositoryException
	 *             if other repository exception occurs
	 */
	private void updateFileContentNode(Node fileNode, EcmDocument document)
			throws ItemExistsException, PathNotFoundException,
			NoSuchNodeTypeException, LockException, VersionException,
			ConstraintViolationException, RepositoryException {

		Node resNode = fileNode.getNode(Property.JCR_CONTENT);

		this.updateResouceNodePropertyAsStream(fileNode, resNode, document);
	}

	/**
	 * @param resNode
	 * @param document
	 * @throws ValueFormatException
	 * @throws VersionException
	 * @throws LockException
	 * @throws ConstraintViolationException
	 * @throws UnsupportedRepositoryOperationException
	 * @throws RepositoryException
	 */
	private void updateResouceNodePropertyxxx(Node resNode, EcmDocument document)
			throws ValueFormatException, VersionException, LockException,
			ConstraintViolationException,
			UnsupportedRepositoryOperationException, RepositoryException {

		resNode.setProperty(Property.JCR_MIMETYPE, document.getMetadata()
				.getMimeType());
		resNode.setProperty(Property.JCR_ENCODING, document.getMetadata()
				.getEncoding());
		resNode.setProperty(Property.JCR_DATA,
				convertToBinary(document.getContent()));

		/** Set last updated date time */
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		resNode.setProperty(Property.JCR_LAST_MODIFIED, now);

		/** Update the user information */
		String userName = this.getSession().getUserID();
		resNode.setProperty(Property.JCR_LAST_MODIFIED_BY, userName);
	}

	private void updateResouceNodePropertyAsStream(Node fileNode, Node resNode,
			EcmDocument document) throws ValueFormatException,
			VersionException, LockException, ConstraintViolationException,
			UnsupportedRepositoryOperationException, RepositoryException {

		resNode.setProperty(Property.JCR_MIMETYPE, document.getMetadata()
				.getMimeType());
		resNode.setProperty(Property.JCR_ENCODING, document.getMetadata()
				.getEncoding());
		resNode.setProperty(Property.JCR_DATA,
				convertToBinary(document.getInputStream()));

		/** Set last updated date time */
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		resNode.setProperty(Property.JCR_LAST_MODIFIED, now);

		/** Update the user information */
		String userName = this.getSession().getUserID();
		resNode.setProperty(Property.JCR_LAST_MODIFIED_BY, userName);
		
		/** Update document identifier */
		if(document.getMetadata() != null){
			document.getMetadata().setIdentifier(fileNode.getIdentifier());
		}
	}

	/**
	 * Add a new version of existing document provided by file path and the
	 * document that need to be added
	 * 
	 * @param filePath
	 *            the file path of the document
	 * @param document
	 *            the document provided as <code>EcmDocument</code> object
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public void addDocumentVersion(String filePath, EcmDocument document)
			throws EcmException {
		logger.debug("Adding a version of document: " + filePath);
		try {

			/** Check if the node already exist */
			Node fileNode = this.getNode(filePath);

			final boolean isCheckOut = this.isNodeCheckedOut(fileNode);
			if (isCheckOut) {
				throw new EcmException("The file, " + filePath
						+ " is already checked out.");
			}
			/** Get the version manager */
			VersionManager vm = this.session.getWorkspace().getVersionManager();

			/** Check out the new created node */
			vm.checkout(fileNode.getPath());
			this.session.save();

			this.updateFileContentNode(fileNode, document);

			this.session.save();

			/** Check in the changes as a new version */
			vm.checkin(fileNode.getPath());

		} catch (RepositoryException e) {
			throw new EcmException("Fail to create a new version of document",
					e);
		}
		logger.debug("A document has been added to the folder:" + filePath);

	}

	/**
	 * Check out a node using the provided file path
	 * 
	 * @param filePath
	 *            the full path of the document
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public void checkOutNode(String filePath) throws EcmException {
		logger.debug("Checking out document: " + filePath);
		try {

			/** Validate the node path */
			this.validateNodePath(filePath);

			/** Check if the node already exist */
			Node fileNode = this.getNode(filePath);

			/** Validate the node */
			this.validateNodeInstance(fileNode);

			final boolean isCheckOut = this.isNodeCheckedOut(fileNode);
			if (isCheckOut) {
				throw new EcmException("The file, " + filePath
						+ " is already checked out.");
			}
			/** Get the version manager */
			VersionManager vm = this.session.getWorkspace().getVersionManager();

			/** Check out the new created node */
			vm.checkout(fileNode.getPath());
			this.session.save();

		} catch (UnsupportedRepositoryOperationException e) {
			throw new EcmException(
					"Fail to check out node, check is not supported", e);
		} catch (RepositoryException e) {
			throw new EcmException("Fail to check out node", e);
		}
		logger.debug("A node successfully checked out using path :" + filePath);

	}

	/**
	 * Check in a node using the provided file path
	 * 
	 * @param filePath
	 *            the full path of the document
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public void checkInNode(String filePath) throws EcmException {
		logger.debug("Checking out document: " + filePath);
		try {

			/** Validate the node path */
			this.validateNodePath(filePath);

			/** Check if the node already exist */
			Node fileNode = this.getNode(filePath);

			/** Validate the node */
			this.validateNodeInstance(fileNode);

			final boolean isCheckOut = this.isNodeCheckedOut(fileNode);
			if (!isCheckOut) {
				throw new EcmException("The node, " + filePath
						+ " is not checked out.");
			}
			/** Get the version manager */
			VersionManager vm = this.session.getWorkspace().getVersionManager();

			/** Check out the new created node */
			vm.checkin(fileNode.getPath());
			this.session.save();

		} catch (UnsupportedRepositoryOperationException e) {
			throw new EcmException(
					"Fail to check out node, check is not supported", e);
		} catch (RepositoryException e) {
			throw new EcmException("Fail to check out node", e);
		}
		logger.debug("A node successfully checked out using path :" + filePath);

	}

	/**
	 * Check whether the provided node path is checked out or not
	 * 
	 * @param nodePath
	 *            the path of the document
	 * 
	 * @return true if it is checked out otherwise return false
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public boolean isNodeCheckedOut(String nodePath) throws EcmException {
		logger.debug("Checking if the node is checked out, provided path :"
				+ nodePath);

		boolean isNodeCheckedOut = false;

		try {

			/** Validate the node path */
			this.validateNodePath(nodePath);

			/** Check if the node already exist */
			Node node = this.getNode(nodePath);

			if (node == null) {
				throw new EcmException(
						"Fail to create node instance using path, " + nodePath);
			}

			isNodeCheckedOut = this.isNodeCheckedOut(node);

		} catch (EcmException e) {
			throw e;
		}

		logger.debug("Checking out if the node is checked out using path :"
				+ nodePath + " is done.");

		return isNodeCheckedOut;
	}

	/**
	 * Check whether the node is checked out or not
	 * 
	 * @param node
	 * @return
	 * @throws EcmException
	 */
	public boolean isNodeCheckedOut(Node node) throws EcmException {
		logger.debug("Checking if the node is checked out");

		boolean isNodeCheckedOut = false;

		try {

			/** Validate the node */
			this.validateNodeInstance(node);

			/** Get the version manager */
			VersionManager vm = this.session.getWorkspace().getVersionManager();

			isNodeCheckedOut = vm.isCheckedOut(node.getPath());

		} catch (RepositoryException e) {
			throw new EcmException(
					"Fail to check if the is checked out or not", e);
		}

		logger.debug("Checking out if the node is checked out is done.");

		return isNodeCheckedOut;
	}

	/**
	 * Validate whether the provided node path is empty or not
	 * 
	 * @param node
	 * @throws EcmException
	 */
	private void validateNodePath(String nodePath) throws EcmException {
		if (nodePath == null || nodePath.equals("")) {
			throw new EcmException("The provided node path is empty.");
		}
	}

	/**
	 * Validate whether the provided node is empty or not
	 * 
	 * @param node
	 * @throws EcmException
	 */
	private void validateNodeInstance(Node node) throws EcmException {
		if (node == null) {
			throw new EcmException("The provided node instance is empty.");
		}
	}

	/**
	 * Return the <code>List</code> that contains this
	 * <code>DocumentMetadata</code>.
	 * 
	 * @return the <code>List</code> that contains this
	 *         <code>DocumentMetadata</code>.
	 * 
	 * @throws EcmException
	 *             if an error occurs.
	 */
	public List<DocumentMetadata> getVersionHistory(String filePath)
			throws EcmException {
		logger.debug("Loading document version history using path : "
				+ filePath);
		List<DocumentMetadata> list = new ArrayList<DocumentMetadata>();
		try {

			/** Validate the node path */
			this.validateNodePath(filePath);

			/** Get the version manager */
			VersionManager vm = this.session.getWorkspace().getVersionManager();

			VersionHistory history = vm.getVersionHistory(filePath);

			VersionIterator iterator = history.getAllVersions();
			iterator.skip(1);
			while (iterator.hasNext()) {
				Version version = iterator.nextVersion();
				DocumentMetadata meta = this.loadFileVersionMetadata(version);
				list.add(meta);
			}
		} catch (UnsupportedRepositoryOperationException e) {
			throw new EcmException(
					"Fail to retrieve version history, check is not supported",
					e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
		} catch (RepositoryException e) {
			throw new EcmException("Fail to retrieve version history", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);
		}
		logger.debug("Version history loaded for file :" + filePath);

		return list;
	}

	/**
	 * Load a node with version specified
	 * 
	 * @param version
	 *            the version instance
	 * @return the Node object
	 * @throws RepositoryException
	 *             if there is any error during retrieval
	 */
	private Node loadDocumentContentNode(Version version)
			throws RepositoryException {
		Node node = null;
		NodeIterator nodeIterator = version.getNodes();
		if (nodeIterator.hasNext()) {
			Node versionedNode = nodeIterator.nextNode();
			NodeIterator niter = versionedNode.getNodes();
			if (niter.hasNext()) {
				node = niter.nextNode();
			}
		}
		return node;
	}

	/**
	 * Load the <code>DocumentMetadata</code> of the version of a node specified
	 * 
	 * @param version
	 *            the version of the node
	 * @return the instance of <code>DocumentMetadata</code>
	 * 
	 * @throws PathNotFoundException
	 *             if the path is wrong
	 * @throws RepositoryException
	 *             if the repository is invalid
	 */
	private DocumentMetadata loadFileVersionMetadata(Version version)
			throws PathNotFoundException, RepositoryException {
		DocumentMetadata meta = new DocumentMetadata();

		meta.setVersion(version.getName());
		meta.setCreated(version.getCreated().getTime());
		meta.setIdentifier(version.getIdentifier());

		/** Load the content of the node */
		Node nContent = this.loadDocumentContentNode(version);

		String mimeType = nContent.getProperty(Property.JCR_MIMETYPE)
				.getString();
		String encoding = nContent.getProperty(Property.JCR_ENCODING)
				.getString();

		meta.setEncoding(encoding);
		meta.setMimeType(mimeType);

		return meta;
	}

	/**
	 * Load the version of file name and version no specified
	 * 
	 * @param filePath
	 *            the full file path
	 * @param versionNo
	 *            version no of the document
	 * @return the generated Version instance
	 * 
	 * @throws RepositoryException
	 *             if there is any problem during loading Version
	 */
	private Version getDocumentVersion(String filePath, String versionNo)
			throws RepositoryException {
		Version version = null;

		/** Get the version manager */
		VersionManager vm = this.session.getWorkspace().getVersionManager();

		VersionHistory history = vm.getVersionHistory(filePath);

		/**
		 * Get the version iterator and find the exact version, we always need
		 * to skip the first node because this not a real version , just like a
		 * head node.
		 */
		VersionIterator iterator = history.getAllVersions();
		iterator.skip(1);

		while (iterator.hasNext()) {
			Version v = iterator.nextVersion();
			if (v.getName().equals(versionNo)) {
				version = v;
				break;
			}
		}
		return version;
	}

	/**
	 * Load the <code>EcmDocument</code> provided by file path and the version
	 * no
	 * 
	 * @param filePath
	 *            the full file path of the document
	 * @param versionNo
	 *            the version which need to load
	 * 
	 * @return the generated <code>EcmDocument</code>
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public EcmDocument loadDocumentVersion(String filePath, String versionNo)
			throws EcmException {
		logger.debug("Loading version, " + versionNo + "  of document :"
				+ filePath);
		EcmDocument doc = null;
		try {

			/** Validate the node path */
			this.validateNodePath(filePath);

			Version version = this.getDocumentVersion(filePath, versionNo);
			if (version == null) {
				throw new EcmException("The document version," + versionNo
						+ " is not available in repository",
						ErrorCodes.REPOSITROY_ERR_VERSION_NOT_EXIST);
			}
			doc = loadDocumentVersion(version.getIdentifier());

		} catch (UnsupportedRepositoryOperationException e) {
			throw new EcmException(
					"Fail to retrieve version history, check is not supported",
					e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
		} catch (RepositoryException e) {
			throw new EcmException("Fail to retrieve version history", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);
		}
		return doc;
	}

	/**
	 * Load the <code>EcmDocument</code> provided by version no
	 * 
	 * @param versionIdentifier
	 *            the version no of the document
	 * 
	 * @return the generated <code>EcmDocument</code>
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public EcmDocument loadDocumentVersion(String versionIdentifier)
			throws EcmException {
		logger.debug("Loading document version by identifier:"
				+ versionIdentifier);
		EcmDocument doc = null;
		try {
			Node node = this.session.getNodeByIdentifier(versionIdentifier);
			if (node == null) {
				return doc;
			}
			doc = new EcmDocument();

			Version v = (Version) node;
			DocumentMetadata metaData = this.loadFileVersionMetadata(v);
			doc.setMetadata(metaData);

			Node nContent = this.loadDocumentContentNode(v);

			Property dataProperty = nContent.getProperty(Property.JCR_DATA);
			if (dataProperty != null) {
				byte[] contents = convertToByteArray(dataProperty.getBinary());
				doc.setContent(contents);
			}

		} catch (RepositoryException e) {
			throw new EcmException("Fail to retrieve document version", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);

		} catch (IOException e) {
			throw new EcmException("Fail to read document from repository.", e,
					ErrorCodes.REPOSITROY_ERR_FAIL_TO_READ_CONTENT);
		}

		logger.debug("Version loaded:" + versionIdentifier);
		return doc;
	}

	/**
	 * Restore a document version to a specified version no
	 * 
	 * @param filePath
	 *            the full file path of the document to be restored
	 * @param versionNo
	 *            the version no to be restored
	 * 
	 * @throws EcmException
	 *             if there is error
	 */
	public void restoreDocumentVersion(String filePath, String versionNo)
			throws EcmException {
		logger.debug("Restoring version, " + versionNo + "  of document :"
				+ filePath);

		try {

			/** Validate the node path */
			this.validateNodePath(filePath);

			Version version = this.getDocumentVersion(filePath, versionNo);
			if (version == null) {
				throw new EcmException("The document version," + versionNo
						+ " is not available in repository",
						ErrorCodes.REPOSITROY_ERR_VERSION_NOT_EXIST);
			}

			/** Restore the version via VersionManager */
			VersionManager vm = this.session.getWorkspace().getVersionManager();
			vm.restore(version, true);

		} catch (UnsupportedRepositoryOperationException e) {
			throw new EcmException(
					"Fail to retrieve version history, check is not supported",
					e, ErrorCodes.REPOSITROY_ERR_OPERATION_NOT_SUPPORTED);
		} catch (RepositoryException e) {
			throw new EcmException("Fail to retrieve version history", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);
		}

	}

	@Override
	public List<EcmDocument> readWorkspace() throws EcmException {

		/** Get the root folder */
		try {
			Node rootNode = this.session.getRootNode();
			System.out.println(rootNode.getPath());
			listChildren("=", rootNode, System.out);
		} catch (RepositoryException e) {

			e.printStackTrace();
		}

		return null;
	}

	private void listChildren(String indent, Node node, PrintStream out)
			throws RepositoryException {
		out.println(indent + node.getName() + "");
		out.println(node.getPath());
		NodeIterator ni = node.getNodes();
		while (ni.hasNext()) {
			listChildren(indent + "  ", ni.nextNode(), out);
		}
	}

	@Override
	public List<DocumentMetadata> listNode(String path) throws EcmException {
		Node node = getNode(path);
		NodeIterator nodes;
		List<DocumentMetadata> childreen = new ArrayList<DocumentMetadata>();
		try {
			nodes = node.getNodes();
			while (nodes.hasNext()) {

				Node child = nodes.nextNode();

				DocumentMetadata meta = new DocumentMetadata();
				if (isFile(child)) {
					Node resNode = child.getNode(Property.JCR_CONTENT);
					DocumentMetadata contentMetaData = null;
					if (resNode != null) {
						contentMetaData = this.loadDocumentMetadata(resNode);
						/** Load the file content */
						Property dataProperty = resNode
								.getProperty(Property.JCR_DATA);
						if (dataProperty != null) {
							long size = dataProperty.getBinary().getSize();
							meta.setSize(size);
						}
						if (contentMetaData != null) {
							meta.setMimeType(contentMetaData.getMimeType());
							meta.setEncoding(contentMetaData.getEncoding());
							meta.setLastModified(contentMetaData
									.getLastModified());
							meta.setLastModifiedBy(contentMetaData
									.getLastModifiedBy());
						}

					}
				}

				/** Load the document meta data */

				meta.setFileName(child.getName());
				meta.setIdentifier(child.getIdentifier());
				meta.setFullPath(child.getPath());
				
				if (child.getProperty(Property.JCR_CREATED_BY) != null) {
					String createdBy = child.getProperty(
							Property.JCR_CREATED_BY).getString();
					meta.setCreatedBy(createdBy);
				}
				if (child.getProperty(Property.JCR_CREATED) != null) {
					Calendar createdDate = child.getProperty(
							Property.JCR_CREATED).getDate();

					meta.setCreated(createdDate.getTime());
				}

				meta.setCheckedOut(child.isCheckedOut());

				meta.setFolder(false);
				if (isFolder(child)) {
					meta.setFolder(true);
				}

				childreen.add(meta);
			}
		} catch (RepositoryException e) {
			throw new EcmException("Fail to list node content ", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);
		}

		return childreen;
	}

	private boolean isFile(Node node) throws RepositoryException {
		if (node.isNodeType(NodeType.NT_FILE)) {
			return true;
		}
		return false;
	}

	private boolean isFolder(Node node) throws RepositoryException {
		if (node.isNodeType(NodeType.NT_FOLDER)) {
			return true;
		}
		return false;
	}

	@Override
	public EcmDocument loadDocumentByIdentifier(String identifier)
			throws EcmException {
		Node node;
		try {
			node = this.session.getNodeByIdentifier(identifier);
			
			if (node == null) {
				return null;
			}
			EcmDocument document = readDocumentByNode(node, 0);
			
			return document;
			
		} catch (ItemNotFoundException e) {
			throw new EcmException("Fail to read document from repository.", e,
					ErrorCodes.REPOSITROY_ERR_INVALID_PATH);
		} catch (RepositoryException e) {
			throw new EcmException("Fail to read document from repository.", e,
					ErrorCodes.REPOSITROY_ERR_GENERIC);
		}

	}

}
