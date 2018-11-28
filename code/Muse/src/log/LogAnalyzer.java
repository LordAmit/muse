package log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Log Analyzer helps to detect and prepare Source files only containing true
 * positives
 * 
 * @author Amit Seal Ami
 *
 */
public class LogAnalyzer {
	static String testString = "11-28 16:31:53.569 18932-18932/com.example.amit.helloworld D/leak-18: GMT+00:00\n"
			+ "11-28 16:31:53.599 18932-18932/com.example.amit.helloworld D/leak-22: GMT+00:00\n"
			+ "11-28 16:32:11.114 18932-18932/com.example.amit.helloworld D/leak-21: GMT+00:00\n"
			+ "11-28 16:32:11.114 18932-18932/com.example.amit.helloworld D/leak-19: GMT+00:00\n"
			+ "11-28 16:32:12.745 18932-18932/com.example.amit.helloworld D/leak-21: GMT+00:00\n"
			+ "11-28 16:32:12.745 18932-18932/com.example.amit.helloworld D/leak-19: GMT+00:00\n";
	static String sourceString = "package com.example.amit.helloworld;\n" + "\n"
			+ "import android.support.v7.app.AppCompatActivity;\n" + "import android.os.Bundle;\n"
			+ "import android.view.View;\n" + "import android.widget.Button;\n" + "\n"
			+ "public class MainActivity extends AppCompatActivity {\n" + "\n"
			+ "    String dataLeAk18 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "    Object throwawayLeAk18 = android.util.Log.d(\"leak-18\", dataLeAk18);\n" + "\n"
			+ "    int intA = 0;\n" + "\n" + "    public int methodA(View v) {\n"
			+ "        String dataLeAk19 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "        Object throwawayLeAk19 = android.util.Log.d(\"leak-19\", dataLeAk19);\n" + "        intA = 2;\n"
			+ "        return 1;\n" + "    }\n" + "\n" + "    int intB = 0;\n" + "\n" + "    int methodB() {\n"
			+ "        String dataLeAk20 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "        Object throwawayLeAk20 = android.util.Log.d(\"leak-20\", dataLeAk20);\n" + "        intB = 3;\n"
			+ "        return 0;\n" + "    }\n" + "\n" + "    public void button_click(View view) {\n"
			+ "        String dataLeAk21 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "        Object throwawayLeAk21 = android.util.Log.d(\"leak-21\", dataLeAk21);\n"
			+ "        android.util.Log.d(\"hey\", \"hello\");\n" + "        methodA(view);\n" + "    }\n" + "\n"
			+ "    @Override\n" + "    protected void onCreate(Bundle savedInstanceState) {\n"
			+ "        super.onCreate(savedInstanceState);\n"
			+ "        String dataLeAk22 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "        Object throwawayLeAk22 = android.util.Log.d(\"leak-22\", dataLeAk22);\n"
			+ "        setContentView(R.layout.activity_main);\n" + "    }\n" + "}\n";

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

	public static void main(String[] args) {
		LogAnalyzer log = new LogAnalyzer();

		System.out.println(log.removeUnusedIndicesFromSource(LogAnalyzer.sourceString,
				log.getIndicesFromLogs(LogAnalyzer.testString)));

	}

}
