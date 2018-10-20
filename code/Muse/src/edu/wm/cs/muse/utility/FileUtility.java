package edu.wm.cs.muse.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 *  File utility contains different utility methods used to read and manipulate files and directories
 * @author amit
 *
 */
public class FileUtility {
	
	public static int COUNTER_GLOBAL = 0;

	/**
	 * reads a text file and returns the contents through StringBuffer
	 * 
	 * @param filePath of the file
	 * @return StringBuffer that contains the text contents of file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static StringBuffer readSourceFile(String filePath) throws FileNotFoundException, IOException {
		StringBuffer source = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = null;

		while ((line = reader.readLine()) != null) {
			source.append(line).append("\n");
		}
		reader.close();
		return source;
	}
	/**
	 * 
	 * sets up the directory where the mutated source codes will be kept
	 */
	public static void setMutantsDirectory() {

		try {
			String newRoot = Arguments.getMutantsFolder() + File.separator + Arguments.getAppName();
			if (new File(newRoot).exists()) {
				FileUtils.deleteDirectory(new File(newRoot));
			}
			FileUtils.copyDirectory(new File(Arguments.getRootPath()), new File(newRoot));
			Arguments.setRootPath(newRoot);
		} catch (IOException e) {
			return;
		}

	}
}