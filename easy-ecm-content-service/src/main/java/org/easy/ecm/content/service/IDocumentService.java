package org.easy.ecm.content.service;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;

import org.easy.ecm.common.exception.EcmException;

/**
 * The IDocumentService is the interface with outside service with the Content
 * Repository. It provide most of the document service operations into the
 * repository such as create folder into the repository, store, retrieve and
 * update document, get the document version history, check in and check out a
 * document from the repository.
 * 
 * 
 * @author Shamim Ahmmed
 */
public interface IDocumentService {

	/**
	 * Set the <code>Session</code> instance to the implemented class
	 * 
	 * @param session
	 *            the JCR Session
	 */
	void setSession(Session session);

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
	public Node getNode(String folderPath) throws EcmException;

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
			throws EcmException;

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
	public Node createFolder(Node pNode, String folderName) throws EcmException;

	/**
	 * Create a folder with the folder name specified
	 * 
	 * @param folderName
	 *            the name of the child folder
	 * @throws EcmException
	 *             if there is any error
	 */
	public Node createFolder(String folderName) throws EcmException;

	/**
	 * Remove the specified node from the workspace
	 * 
	 * @param nodePath
	 *            the path of the node to be removed
	 * 
	 * @throws EcmException
	 *             if any error occurred
	 */
	public void removeNode(String nodePath) throws EcmException;

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
			throws EcmException;

	public void updateDocumentToFolder(String folderPath, EcmDocument document)
			throws EcmException;
	
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
			throws EcmException;

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
			EcmDocument document) throws EcmException;

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
			throws EcmException;

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
	public boolean isNodeCheckedOut(String nodePath) throws EcmException;

	/**
	 * Check out a node using the provided file path
	 * 
	 * @param filePath
	 *            the full path of the document
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public void checkOutNode(String filePath) throws EcmException;

	/**
	 * Check in a node using the provided file path
	 * 
	 * @param filePath
	 *            the full path of the document
	 * 
	 * @throws EcmException
	 *             if there is any error
	 */
	public void checkInNode(String filePath) throws EcmException;

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
			throws EcmException;

	/**
	 * Load the <code>EcmDocument</code> provided by version identifier
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
			throws EcmException;

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
			throws EcmException;

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
			throws EcmException;
	
	public EcmDocument retrieveDocumentAsStream(String fullFilePath) throws EcmException ;
	
	public EcmDocument loadDocumentByIdentifier(String identifier) throws EcmException ;
	
	/**
	 * Read the workspace recursively. It does not load the content;
	 * 
	 * 
	 * @return all workspace child nodes.
	 */
	public List<EcmDocument> readWorkspace() throws EcmException;
	
	public List<DocumentMetadata> listNode(final String path)throws EcmException;
}
