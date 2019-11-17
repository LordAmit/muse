package edu.wm.cs.muse.dataleak.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.wm.cs.muse.dataleak.DataLeak;

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
	private static String appName;
	private static String mutantsFolder;
	private static String Operator;
	private static String leakPath = "src/edu/wm/cs/muse/dataleak/default_leak_strings.txt";
	private static Boolean testmode = false;

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

	public static boolean setLeaks(OperatorType op, String leakPath) {
		try {
			String[] leakStrings = FileUtility.readSourceFile(leakPath).toString().split("\\n");
			// first line read in as leak source string or default leak source string if empty
			if (leakStrings.length > 0 && !leakStrings[0].isEmpty()) {
				DataLeak.setSource(op, leakStrings[0]);
			}
			// second line read in as leak sink string or default leak sink string if empty
			if (leakStrings.length > 1 && !leakStrings[1].isEmpty()) {
				DataLeak.setSink(op, leakStrings[1]);
			}
			// third line read in as variable declaration string or default variable declaration string if empty
			if (leakStrings.length > 2 && !leakStrings[2].isEmpty()) {
				DataLeak.setVariableDeclaration(op, leakStrings[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void setRootPath(String rootPath) {
		Arguments.rootPath = rootPath;
	}

	public static void setTestMode(Boolean mode) {
		testmode = mode;
	}

	public static Boolean getTestMode() {
		return testmode;
	}
	
	
	/**
	 * @return the folder that contains the binaries related to lib4ast
	 */
	public static String getBinariesFolder() {
		return binariesFolder;
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
	 * TAINTSOURCE, TAINTSINK, SCOPESOURCE, SCOPESINK and REACHABILITY
	 */
	public static String getOperator() {
		return Operator;
	}
	
	

}
