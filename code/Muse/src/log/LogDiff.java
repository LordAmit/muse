package log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;


public class LogDiff {
	
		//RuntimeLogs.txt
		static String museLogString;
		static String crashLogString;
		static String resultLogString;
		static File resultLog;
		
		private static Properties prop;

		/**
		 * Makes two sets.  Adds the leaks of each log to the two sets. 
		 * Removes the leaks from the crashlog set from the muselog set.
		 * Iterates theought the difference set to make a string of the leaks from the difference set separated by \n
		 * Writes this result string to the specified path.
		 * @param args
		 * @throws IOException
		 * @author Yang Zhang
		 */
		public static void runLogAnalysis(String[] args) throws IOException {
				Set<String> museLogIndeces = logToSet(LogDiff.museLogString);
				Set<String> museCrashIndeces = logToSet(LogDiff.crashLogString);
				museLogIndeces.retainAll(museCrashIndeces);
				String resultLogString = "In file: " + resultLog.getName() + " \n";
				Iterator<String> iterator = museLogIndeces.iterator();
				while(iterator.hasNext()){
				  String element = (String) iterator.next();
				  resultLogString += element + " \n";
				}
				FileUtils.writeStringToFile(resultLog, resultLogString, false);
		}
		/**
		 * converts logstring to set of leak ids
		 * @param string source code contents from formatted text file with new lines
		 * @return set of indices based on true positive leaks
		 * @author Amit Seal Ami
		 *
		 */
		public static Set<String> logToSet(String string) {
			Set<String> leaks = new HashSet<String>();
			String[] lines = string.split("\n");
			for (String line : lines) {
				if (line.contains("leak-")) {
					leaks.add(line.split(" ")[0]);
				}
			}
			return leaks;
		}
		/**
		 * Reads in arguments from a properties file and raises an exception if any are missing.
		 * @param args
		 * @throws FileNotFoundException
		 * @throws IOException
		 */
		private static void prepareArguments(String[] args) throws FileNotFoundException, IOException {
			
			if (args.length != 1) {
				printArgumentError();
				return;
			}
			System.out.println("here");
			//any non option arguments are passed in 
			Arguments.extractArguments(args[0]);
			try (InputStream input = new FileInputStream(args[0])) {
				prop = new Properties();
				prop.load(input);		
			} catch (IOException e) {
				printArgumentError();
				return;
			}
			if (prop.getProperty("museLogPath") == null || prop.getProperty("museLogPath").length() == 0) {
				printArgumentError();
				return;
			} else if (prop.getProperty("crashLogPath") == null || prop.getProperty("crashLogPath").length() == 0) {
				printArgumentError();
				return;
			} else if (prop.getProperty("resultLogPath") == null || prop.getProperty("resultLogPath").length() == 0) {
				printArgumentError();
				return;
			}
			
			//path to log file from Muse for input
			museLogString = FileUtility.readSourceFile(prop.getProperty("museLogPath")).toString();
			crashLogString = FileUtility.readSourceFile(prop.getProperty("crashLogPath")).toString();
			resultLog = new File(prop.getProperty("resultLogPath"));
		}
		
		private static void printArgumentError() {
			System.out.println("******* ERROR: INCORRECT USAGE *******");
		}

		public static void main(String[] args) throws IOException {
			prepareArguments(args);
			runLogAnalysis(args);

		}

}
