package edu.wm.cs.muse.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Utility {
	public static int COUNTER_GLOBAL=0;
	
//	/**
//	 * reads a text file using and returns the contents
//	 * @param filePath of the file
//	 * @return StringBuffer that contains the text contents of file
//	 * @throws FileNotFoundException
//	 * @throws IOException
//	 */
//	public static StringBuffer readSourceFile(String filePath) throws FileNotFoundException, IOException {
//		StringBuffer source = new StringBuffer();
//		BufferedReader reader = new BufferedReader(new FileReader(filePath));
//		String line = null;
//
//		while ((line = reader.readLine()) != null) {
//			source.append(line).append("\n");
//		}
//		reader.close();
//		return source;
//	}
}
