package edu.wm.cs.muse.utility;

/**
 * @author amit
 * This class is introduced for better handling of arguments, and adding documentation to each parameter.
 */
public class Arguments {
	private static String binariesFolder;
	private static String rootPath;
	private static String appName;
	private static String mutantsFolder;

	/**
	 * This extracts and assigns arguments to binariesFolder, rootPath, appName, mutantsFolder
	 * @param args contains the arguments provided through command line
	 */
	public static void extractArguments(String[] args) {
		binariesFolder = args[0];
		rootPath = args[1];
		appName = args[2];
		mutantsFolder = args[3];
	}
	
	public static void setRootPath(String rootPath) {
		Arguments.rootPath = rootPath;
	}

	/**
	 * @return the folder that contains the binaries related to lib4ast
	 */
	public static String getBinariesFolder() {
		return binariesFolder;
	}

	
	/**
	 * @return the path where the source files reside. Source files may be under sub-directories in this root path
	 */
	public static String getRootPath() {
		return rootPath;
	}
	
	/**
	 * @return the name of the app. Is used for creating folder under Mutants Folder for app and for other purposes.
	 */
	public static String getAppName() {
		return appName;
	}

	/**
	 * @return returns the path of the folder where the mutated source files will be kept.
	 */
	public static String getMutantsFolder() {
		return mutantsFolder;
	}
	

}
