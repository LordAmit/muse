package log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.Document;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;

/**
 * Sinks log analyzer requires two string contents. Log and Source.
 * Based on the log file, it removes the unused log sinks and only keeps the true positive logs.
 * It can be used with the ScopeSink and TaintSink operators
 * @author Amit Seal Ami
 * 
 */
public class LogAnalyzer_Sinks {
	//RuntimeLogs.txt
	static String testString;
	//ModifiedFile.txt
	static String sourceString;
	private static Properties prop;
	private static OperatorType op;
	
	/**
	 * Iterates through the modified file directory to remove false positive data leaks.
	 * Then alters the files in mutants folder with the respective changes.
	 * @param args
	 * @throws IOException
	 * @author Yang Zhang
	 */
	public void runLogAnalysis(String[] args) throws Exception {
		if (args.length != 1) {
			printArgumentError();
			return;
		}
		

		//any non option arguments are passed in 
		Arguments.extractArguments(args[0]);
		
		try (InputStream input = new FileInputStream(args[0])) {
			prop = new Properties();
			prop.load(input);		
		} catch (IOException e) {
			printArgumentError();
			return;
		}
		
		//path to log file from Muse for input
		if (prop.getProperty("logPath") == null) {
			printArgumentError();
			return;
		}
		testString = FileUtility.readSourceFile(prop.getProperty("logPath")).toString();

		
		//path to modified file with inserted leaks for input
		if (prop.getProperty("appSrc") == null) {
			printArgumentError();
			return;
		}
		File mod_file_path = new File(prop.getProperty("appSrc"));
		File [] mod_files = mod_file_path.listFiles();
		
		//path to mutant folder directory for output
		if (prop.getProperty("output") == null) {
			printArgumentError();
			return;
		}
		File mutant_file_path = new File(prop.getProperty("output"));
		File [] mutated_files = mutant_file_path.listFiles();
		
		if (prop.getProperty("operatorType") == null) {
			printArgumentError();
			return;
		}
		op = Arguments.getOperatorEnumType(prop.getProperty("operatorType"));


		for (File mod_file : mod_files) {
			try {
				if (mod_file.getName().endsWith(".txt")) {
					sourceString = FileUtility.readSourceFile(mod_file.getAbsolutePath()).toString();
					String new_analyzed_file = analyzeSourceString(sourceString, getLogMaps(testString));
					System.out.println(new_analyzed_file);
					
					//traverse mutants folder to replace the existing modified code
					//the mutant folder filepath should link straight to the directory 
					//containing the mutated files being analyzed.
					String originalName = mod_file.getName().replaceAll(".txt", ".java");
					for (File mutated_file : mutated_files) {
						if (mutated_file.getName().equals(originalName) == true) 
						{
							Document sourceDoc = new Document(new_analyzed_file);
							FileUtils.writeStringToFile(mutated_file, sourceDoc.get(), false);
						}
					}
				}

			} catch (IOException e) {
				System.err.println(String.format("ERROR PROCESSING \"%s\": %s", mod_file.getAbsolutePath(), e.getMessage()));
				return;
			}
		}
	}

	/**
	 * Analyze source string, based on input, non true positive sinks for taintSink or scopeSink. 
	 * @param string contains the source code in one string, with multiple lines.
	 * @param maps {@link log.LogAnalyzer_Sinks#getLogMaps(String) maps} contains the maps of source and sinks
	 * @return modified source code.
	 * @throws Exception 
	 * @author Amit Seal Ami
	 */
	public static String analyzeSourceString(String string, Map<Integer, Set<Integer>> maps) throws Exception {
		if(string.length()<10) {
			throw new Exception("Give me proper source string; separated by new lines.");
		}
		String[] lines = string.split("\n");
		String outputLines = "";
		String[] rawSource = DataLeak.getRawSource(op).split("%d");
		String[] rawSink = DataLeak.getRawSink(op).split("%d");
		String[] rawVarDec = DataLeak.getVariableDeclaration(op).split("%d");

		for (String line : lines) {
			// removes sinks that do not appear in the log
			if (line.trim().startsWith(rawSink[0])) {
				//isolate the "%d-%d" placeholder string and split it into two indices
				String[] placeholderVals = line.replace(rawSink[0],"").split(rawSink[2])[0].split("-");
				//remove whitespace
				Integer source = Integer.parseInt(placeholderVals[0].trim());
				Integer sink = Integer.parseInt(placeholderVals[1].trim());
				if (maps.get(source) != null && maps.get(source).contains(sink)) {
					outputLines += line + "\n";
				}
			} 
			// removes sources that do not appear in the log
			else if (line.trim().startsWith(rawSource[0])) {
				//isolate the "%d" placeholder string
				String placeholderVal = line.replace(rawSource[0],"").split("=")[0];
				Integer source = Integer.parseInt(placeholderVal.trim());
				if (maps.containsKey(source)) {
					outputLines += line + "\n";
				}
			}
			// removes variable declarations that not appear in the log
			else if (line.trim().startsWith(rawVarDec[0])) {
				//isolate the "%d" placeholder string
				String placeholderVal = line.replace(rawVarDec[0],"").split(rawVarDec[1])[0];
				Integer source = Integer.parseInt(placeholderVal.trim());
				if (maps.containsKey(source)) {
					outputLines += line + "\n";
				}
			}
			else {
				outputLines += line + "\n";
			}
		}
		return outputLines;
	}
	
	/**
	 * getLogMaps returns a mapping for each source, along with list of its sinks.
	 * @param allLogs receives all logs in a single string
	 * @return mapping from source to list of sinks
	 * @author Amit Seal Ami
	 */
	public static Map<Integer, Set<Integer>> getLogMaps(String allLogs) {
		String[] lines = allLogs.split("\n");
		String[] rawSink = DataLeak.getRawSink(op).split("%d",4);
		Map<Integer, Set<Integer>> maps = new HashMap<Integer, Set<Integer>>();
		for (String line : lines) {
			if (line.contains("leak-")) {
				System.out.println(line);
				String[] source_sink = line.split("leak-")[1].split(":")[0].split("-");
				Integer source = Integer.parseInt(source_sink[0]);
				Integer sink = Integer.parseInt(source_sink[1]);
				if (!maps.containsKey(source)) {
					maps.put(source, new HashSet<Integer>());
				}
				Set<Integer> values = maps.get(source);
				values.add(sink);
				maps.put(source, values);
			}
		}
//		System.out.println(maps);
		return maps;
	}
	
	private void printArgumentError() {
		System.out.println("******* ERROR: INCORRECT USAGE *******");
		System.out.println("Argument List:");
		System.out.println("1. Runtime Logs File");
		System.out.println("2. Modified Files Directory");
		System.out.println("3. Mutants path");
	}

	public static void main(String[] args) throws Exception {
		new LogAnalyzer_Sinks().runLogAnalysis(args);
	}
}
