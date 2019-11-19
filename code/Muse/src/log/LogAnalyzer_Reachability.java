package log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.Document;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;

/**
 * Reachability log analyzer requires two string contents. Log and Source.
 * Based on the log file, it removes the unused log sinks and only keeps the true positive logs.
 * 
 * @author Ian Wolff
 *
 */
public class LogAnalyzer_Reachability {
	//RuntimeLogs.txt
	static String testString;
	//ModifiedFile.txt
	static String sourceString;
	private CommandLine cmd = null;
	private static Properties prop;
	private static OperatorType op;
	private static File mod_file_path;
	private static File [] mod_files;
	private static File mutant_file_path;
	private static File [] mutated_files;

	/**
	 * Iterates through the modified file directory and compares the occurrence of
	 * "dataLeak" in the file and the runtime log to remove false positive data leaks.
	 * Then alters the files in mutants folder with the respective changes.
	 * @param args
	 * @throws IOException
	 * @author Yang Zhang
	 */
	public void runLogAnalysis(String[] args) throws FileNotFoundException, IOException {
		for (File mod_file : mod_files) {
			try {
				if (mod_file.getName().endsWith(".txt")) {
					sourceString = FileUtility.readSourceFile(mod_file.getAbsolutePath()).toString();
					LogAnalyzer_Reachability log = new LogAnalyzer_Reachability();
					String new_analyzed_file = log.removeUnusedIndicesFromSource(LogAnalyzer_Reachability.sourceString,
							log.getIndicesFromLogs(LogAnalyzer_Reachability.testString));
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
	 * Extracts indices of true positive data leaks from the log file
	 * 
	 * @param string source code contents from formatted text file with new lines
	 * @return set of indices based on true positive leaks
	 * @author Amit Seal Ami
	 *
	 */
	public Set<Integer> getIndicesFromLogs(String string) {
		Set<Integer> indices = new HashSet<Integer>();
		String[] lines = string.split("\n");
		for (String line : lines) {
			if (line.contains("leak-")) {
				System.out.println(line);
				indices.add(Integer.parseInt(line.split("leak-")[1].split(":")[0]));
			}
		}
		return indices;
	}

	/**Analyzes the source string and based on input, removes false positive sources 
	 * and sinks for the reachability operator.
	 * 
	 * @param string           String content of the source file
	 * @param indicesFromLog   extracted indices of dataleaks from log file
	 * @return String content  of file that only contains true positive for
	 *         reachability
	 * @author Amit Seal Ami, Ian Wolff
	 */
	public String removeUnusedIndicesFromSource(String string, Set<Integer> indicesFromLog) {
		String[] lines = string.split("\n");
		String outputLines = "";
		boolean addThrowAwayLine = false;		
		// rawsink and rawSource separate the substrings before and after the "%d" placeholder
		String[] rawSource = DataLeak.getRawSource(op).split("%d");
		String[] rawSink = DataLeak.getRawSink(op).split("%d");
		
		for (String line : lines) {
			// removes sinks that do not appear in the log
			if (line.contains(rawSink[0])) {
				// isolates the index from the leak string and removes any leftover whitespace
				String placeholderVal = line.replace(rawSink[0], "").split(rawSink[1])[0].trim();
				if (indicesFromLog.contains(Integer.parseInt(placeholderVal))) {
					outputLines += line + "\n";
					addThrowAwayLine = true;
				}
			// removes sources that do not appear in the log
			} else if (line.contains(rawSource[0])) {
				if (addThrowAwayLine) {
					outputLines += line + "\n";
					addThrowAwayLine = false;
				}
			// outputs line regularly
			} else {
				outputLines += line += "\n";
			}
		}
		return outputLines;
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
		//any non option arguments are passed in 
		Arguments.extractArguments(args[0]);
		try (InputStream input = new FileInputStream(args[0])) {
			prop = new Properties();
			prop.load(input);		
		} catch (IOException e) {
			printArgumentError();
			return;
		}
		if (prop.getProperty("logPath") == null) {
			printArgumentError();
			return;
		} else if (prop.getProperty("appSrc") == null) {
			printArgumentError();
			return;
		} else if (prop.getProperty("output") == null) {
			printArgumentError();
			return;
		} else if (prop.getProperty("operatorType") == null) {
			printArgumentError();
			return;
		}
		//path to log file from Muse for input
		testString = FileUtility.readSourceFile(prop.getProperty("logPath")).toString();
		//path to modified file with inserted leaks for input
		mod_file_path = new File(prop.getProperty("appSrc"));
		mod_files = mod_file_path.listFiles();
		//path to mutant folder directory for output
		mutant_file_path = new File(prop.getProperty("output"));
		mutated_files = mutant_file_path.listFiles();	
		op = Arguments.getOperatorEnumType(prop.getProperty("operatorType"));
	}
	
	private static void printArgumentError() {
		System.out.println("******* ERROR: INCORRECT USAGE *******");
	}

	public static void main(String[] args) throws IOException {
		prepareArguments(args);
		new LogAnalyzer_Reachability().runLogAnalysis(args);

	}

}
