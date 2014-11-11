/**
 * 
 */
package org.easy.ecm.content.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author bkurtz
 *
 */
public class Main {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			byte[] rawData = readFileAsByte();
			System.out.println(rawData.toString());
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

	}
	public static byte[] readFileAsByte() throws IOException{
		final String fileName = "C:\\Users\\SAhmmed\\projects\\easy-ecm\\easy-ecm-content-service\\src\\test\\resources\\files\\sample.pdf";
		File file = new File(fileName);
		FileInputStream fin = new FileInputStream(file);
		byte fileContent[] = new byte[(int)file.length()];
		fin.read(fileContent);
		return fileContent;
	}

}
