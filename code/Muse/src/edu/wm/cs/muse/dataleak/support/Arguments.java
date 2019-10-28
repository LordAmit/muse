package edu.wm.cs.muse.dataleak.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class is introduced for better handling of arguments, and adding
 * documentation to each parameter.
 * 
 * @author Amit Seal Ami
 */
/**
 * @author Amit Seal Ami
 *
 */
public class Arguments {
	private static String binariesFolder;
	private static String rootPath;
	private static String leakPath;
	private static String appName;
	private static String mutantsFolder;
	private static String Operator;

	/**
	 * private constructor makes sure that no constructor can ever be used.
	 */
	@SuppressWarnings("unused")
	private Arguments() {

	}

	/**
	 * This extracts and assigns arguments to binariesFolder, rootPath, appName,
	 * mutantsFolder
	 * 
	 * @param args contains the arguments provided through command line
	 */
	public Arguments(String args[]) {

	}

	public static void extractArguments(String[] args) {
		binariesFolder = args[0];
		rootPath = args[1];
		appName = args[2];
		mutantsFolder = args[3];
		Operator = args[4];
		leakPath = args[5];
	}

	public static void extractArguments(File file) {
		try {
			String contentString = FileUtility.readSourceFile(file.getAbsolutePath()).toString();
			extractArguments(contentString.split(" "));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setRootPath(String rootPath) {
		Arguments.rootPath = rootPath;
	}


	/**
	 * @return the folder that contains the binaries related to lib4ast
	 */
	public static String getBinariesFolder() {
        //binariesFolder = "C:\\Users\\Ian\\Downloads\\MDroidPlus-master\\libs4ast";
		return binariesFolder;
	}
	
	/**
	 * @return the path where the leak string file resides.
	 */
	public static String getLeakPath() {
		leakPath = "C:\\Users\\Ian\\Downloads\\leak.txt";
		return leakPath;
	}


	/**
	 * @return the path where the source files reside. Source files may be under
	 *         sub-directories in this root path
	 */
	public static String getRootPath() {
		return rootPath;
	}

	/**
	 * @return the name of the app. Is used for creating folder under Mutants Folder
	 *         for app and for other purposes.
	 */
	public static String getAppName() {
		return appName;
	}

	/**
	 * @return returns the path of the folder where the mutated source files will be
	 *         kept.
	 */
	public static String getMutantsFolder() {
		return mutantsFolder;
	}
	
	/**
	 * @return operator specified by the argument
	 * Acceptable options are: 
	 * SOURCE, SINK, TAINT, TAINTSINK and REACHABILITY
	 */
	public static String getOperator() {
		return Operator;
	}
	
	

}
