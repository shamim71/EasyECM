package org.easy.ecm.content.service;

import java.util.Date;

import javax.jcr.nodetype.NodeType;

/**
 * The DocumentMetadata class represent a document meta data information in the
 * Repository. A document contains other information apart from the actual
 * content like the owner information, created or lated modified date, encoding
 * and version information etc.
 * 
 * @author Shamim Ahmmed
 */
public class DocumentMetadata {

	/** The unique identifier in the repository */
	private String identifier;

	/** The full path in the repository */
	private String fullPath;

	/** The name of the file */
	private String fileName;

	/** The creator of the document */
	private String createdBy;

	/** The created time */
	private Date created;

	/** The last modified date time */
	private Date lastModified;

	/** The last modified user */
	private String lastModifiedBy;

	/** The version of the document */
	private String version;

	/** The encoding of the document */
	private String encoding;

	/** The mime-type of the document */
	private String mimeType;

	/** The checked out status of the document */
	private boolean isCheckedOut;

	/** The is the document is version able or not */
	private boolean isVersionable;

	/** The node type of the document */
	private NodeType nodeType;

	/** The size of the document */
	private long size;

	private boolean isFolder;
	
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the fullPath
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * @param fullPath
	 *            the fullPath to set
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
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
	 * @return the lastModified
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified
	 *            the lastModified to set
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy
	 *            the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType
	 *            the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return the nodeType
	 */
	public NodeType getNodeType() {
		return nodeType;
	}

	/**
	 * @param nodeType
	 *            the nodeType to set
	 */
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the isCheckedOut
	 */
	public boolean isCheckedOut() {
		return isCheckedOut;
	}

	/**
	 * @param isCheckedOut
	 *            the isCheckedOut to set
	 */
	public void setCheckedOut(boolean isCheckedOut) {
		this.isCheckedOut = isCheckedOut;
	}

	/**
	 * @return the isVersionable
	 */
	public boolean isVersionable() {
		return isVersionable;
	}

	/**
	 * @param isVersionable
	 *            the isVersionable to set
	 */
	public void setVersionable(boolean isVersionable) {
		this.isVersionable = isVersionable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DocumentMetadata [identifier=" + identifier + ", fullPath="
				+ fullPath + ", fileName=" + fileName + ", createdBy="
				+ createdBy + ", created=" + created + ", lastModified="
				+ lastModified + ", lastModifiedBy=" + lastModifiedBy
				+ ", version=" + version + ", encoding=" + encoding
				+ ", mimeType=" + mimeType + ", isCheckedOut=" + isCheckedOut
				+ ", isVersionable=" + isVersionable + ", nodeType=" + nodeType
				+ "]";
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

}
