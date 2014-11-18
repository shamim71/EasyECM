package org.easy.ecm.content.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Binary;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFactory;


/**
 * The AbstractDocumentService class is the base class of all document service related subclass
 * and it keeps the Repository Session and other utility function that is need in subclass.
 *
 * @author Shamim Ahmmed
 */

public abstract class AbstractDocumentService {
	
	/** The repository session object to access the content repository */
	protected Session session;
	
	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}
	
	/**
	 * Convert the byte array to Binary object
	 * 
	 * @param data the data to be converted
	 * @return the converted Binary object
	 * 
	 * @throws UnsupportedRepositoryOperationException
	 * @throws RepositoryException
	 */
	protected Binary convertToBinary(byte[] data) throws UnsupportedRepositoryOperationException, RepositoryException{
		ValueFactory vf = this.session.getValueFactory();
		Binary b = vf.createBinary(new ByteArrayInputStream(data));
		return b;
	}
	
	protected Binary convertToBinary(InputStream inputStream) throws UnsupportedRepositoryOperationException, RepositoryException{
		ValueFactory vf = this.session.getValueFactory();
		Binary b = vf.createBinary(inputStream);
		return b;
	}
	
	/**
	 * Convert the Binary object to byte array
	 * 
	 * @param b the Binary object
	 * @return the generated byte array
	 * 
	 * @throws RepositoryException
	 * @throws IOException
	 */
	protected byte[] convertToByteArray(Binary b) throws RepositoryException, IOException{
		InputStream dataInputStream = b.getStream();
		ByteArrayOutputStream bArrayStream = new ByteArrayOutputStream();
		while(true){
			int data = dataInputStream.read();
			/** If the returned value -1 then it reach end of stream */
			if(data == -1){
				break;
			}
			bArrayStream.write(data);
		}
		b.dispose();
		
		return bArrayStream.toByteArray();
	}
}