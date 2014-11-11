package org.easy.ecm.content.service;

import java.io.InputStream;
import java.util.List;

/**
 * The EcmDocument class represent a document object in the Repository. It is
 * behave like document in a normal file system like it belongs to a folder and
 * have content and metadata as well.
 * 
 * @author Shamim Ahmmed
 */
public class EcmDocument {

	/** The name of the parent folder of the document */
	private String parentFolder;

	/** The file name of the document */
	private String fileName;

	/** The content of the file in bytes, not suitable for large content */
	private byte[] content;
	
	/**
	 * The content of the file as Stream basically for when client request as
	 * stream
	 */
	private InputStream inputStream;

	/** The file metadata information */
	private DocumentMetadata metadata;

	private List<EcmDocument> children;
	
	/**
	 * @return the metadata
	 */
	public DocumentMetadata getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata
	 *            the metadata to set
	 */
	public void setMetadata(DocumentMetadata metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the parentFolder
	 */
	public String getParentFolder() {
		return parentFolder;
	}

	/**
	 * @param parentFolder
	 *            the parentFolder to set
	 */
	public void setParentFolder(String parentFolder) {
		this.parentFolder = parentFolder;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}



	public List<EcmDocument> getChildren() {
		return children;
	}

	public void setChildren(List<EcmDocument> children) {
		this.children = children;
	}

}
