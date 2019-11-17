package log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.Document;

import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;

/**
 * ScopeSink log analyzer requires two string contents. Log and Source.
 * Based on the log file, it removes the unused log sinks and only keeps the true positive logs.
 * @author Amit Seal Ami
 * 
 */
public class LogAnalyzer_ScopeSink {
	//RuntimeLogs.txt
	static String testString;
	//ModifiedFile.txt
	static String sourceString;
	private CommandLine cmd = null;
	
	/**
	 * Iterates through the modified file directory to remove false positive data leaks.
	 * Then alters the files in mutants folder with the respective changes.
	 * @param args
	 * @throws IOException
	 * @author Yang Zhang
	 */
	public void runLogAnalysis(String[] args) throws Exception {
		Options options = new Options();
		//adding an option flag that can be used on command line
		options.addOption("d", "dataleak", true, "Run LogAnalyzer with a custom data leak file");

		CommandLineParser parser = new DefaultParser();

		//parse the command line input
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e1) {
			e1.printStackTrace();
			return;
		}

		///////Add control flow based on the option flag parsed here
			
		//sets the leakPath to the file specified
		if (cmd.hasOption("d")) {
			System.out.println("DataLeak set");
			Arguments.setLeaks(OperatorType.SCOPESINK, cmd.getOptionValue("d"));
		}	
		
		///////
		
		// Usage Error, check length of remaining arguments
		if (cmd.getArgs().length != 3) {
			printArgumentError();
			return;
		}
		
		args = cmd.getArgs();
			
		testString = FileUtility.readSourceFile(args[0].toString()).toString();
		//modified files directory
		File mod_file_path = new File(args[1].toString());
		File [] mod_files = mod_file_path.listFiles();
		
		//mutant folder directory
		File mutant_file_path = new File(args[2].toString());
		File [] mutated_files = mutant_file_path.listFiles();

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
	 * Analyze source string, based on input, non true positive sinks for scopeSink. 
	 * @param string contains the source code in one string, with multiple lines.
	 * @param maps {@link log.LogAnalyzer_ScopeSink#getLogMaps(String) maps} contains the maps of source and sinks
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

		for (String line : lines) {

			if (line.contains("leak-")) {

				String[] source_sink = line.split("leak-")[1].split("\"")[0].split("-");
				Integer source = Integer.parseInt(source_sink[0]);
				Integer sink = Integer.parseInt(source_sink[1]);
				if (!maps.get(source).contains(sink)) {
					continue;
				}else {
					outputLines += line + "\n";
				}
			} else {
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

		Map<Integer, Set<Integer>> maps = new HashMap<Integer, Set<Integer>>();
		for (String line : lines) {
			if (line.contains("leak-")) {
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
		new LogAnalyzer_ScopeSink().runLogAnalysis(args);
	}
}
