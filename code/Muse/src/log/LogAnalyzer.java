package log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.wm.cs.muse.dataleak.support.FileUtility;

/**
 * Log Analyzer helps to detect and prepare Source files only containing true
 * positives
 * 
 * @author Amit Seal Ami
 *
 */
public class LogAnalyzer {
	//RuntimeLogs.txt
	static String testString;
	//ModifiedFile.txt
	static String sourceString;

	/**
	 * Extracts indices of true positive data leaks
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
				indices.add(Integer.parseInt(line.split("leak-")[1].split(":")[0]));
			}
		}
		return indices;
	}

	/**
	 * @param string         String content of the source file
	 * @param indicesFromLog extracted indices of dataleaks from log file
	 * @return String content of file that only contains true positive for
	 *         reachability
	 * @author Amit Seal Ami
	 */
	public String removeUnusedIndicesFromSource(String string, Set<Integer> indicesFromLog) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		String[] lines = string.split("\n");
		String outputLines = "";
		boolean addThrowAwayLine = false;
		for (String line : lines) {
			if (line.contains("String dataLeAk")) {
				int index = Integer.parseInt(line.split("dataLeAk")[1].split(" =")[0]);
				if (indicesFromLog.contains(index)) {
					outputLines += line + "\n";
					addThrowAwayLine = true;
				}
			} else if (line.contains("Object throwawayLeAk")) {
				if (addThrowAwayLine) {
					outputLines += line + "\n";
					addThrowAwayLine = false;
				}
			} else {
				outputLines += line += "\n";
			}
		}
		return outputLines;
	}

	public static void main(String[] args) throws IOException, IOException {
		testString = FileUtility.readSourceFile("src/log/RuntimeLogs.txt").toString();
		sourceString = FileUtility.readSourceFile("src/log/ModifiedFile.txt").toString();
		LogAnalyzer log = new LogAnalyzer();

		System.out.println(log.removeUnusedIndicesFromSource(LogAnalyzer.sourceString,
				log.getIndicesFromLogs(LogAnalyzer.testString)));

	}

}
