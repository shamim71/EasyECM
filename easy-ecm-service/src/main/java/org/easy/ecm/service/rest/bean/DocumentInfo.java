package org.easy.ecm.service.rest.bean;

import java.util.Comparator;
import java.util.Date;

public class DocumentInfo{

	private boolean isFolder;
	
	private String name;
	
	private Date lastModified;
	
	private String downloadLink;

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	
}
