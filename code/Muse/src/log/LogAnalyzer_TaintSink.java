package log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TaintSink log analyzer requires two string contents. Log and Source.
 * Based on the log file, it removes the unused log sinks and only keeps the true positive logs.
 * @author Amit Seal Ami
 * 
 */
public class LogAnalyzer_TaintSink {
	
	static String sourceSample = "package com.example.amit.helloworld;\n" + "\n"
			+ "import android.support.v7.app.AppCompatActivity;\n" + "import android.os.Bundle;\n"
			+ "import android.view.View;\n" + "import android.widget.Button;\n" + "\n"
			+ "public class MainActivity extends AppCompatActivity {\n" + "\n" + "\n" + "\n"
			+ "    String dataLeAk28 = \"28\";\n" + "\n" + "    String dataLeAk27 = \"27\";\n" + "\n"
			+ "    String dataLeAk26 = \"26\";\n" + "\n" + "    String dataLeAk25 = \"25\";\n" + "\n"
			+ "    String dataLeAk24 = \"24\";\n" + "\n" + "    int intA = 0;\n" + "\n"
			+ "    public int methodA(View v) {\n" + "        android.util.Log.d(\"leak\", \"methodA start\");\n" + "\n"
			+ "        dataLeAk24 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "        android.util.Log.d(\"leak-24-12\", dataLeAk24);\n"
			+ "        android.util.Log.d(\"leak-25-12\", dataLeAk25);\n"
			+ "        android.util.Log.d(\"leak-26-12\", dataLeAk26);\n"
			+ "        android.util.Log.d(\"leak-27-12\", dataLeAk27);\n"
			+ "        android.util.Log.d(\"leak-28-12\", dataLeAk28);\n"
			+ "        android.util.Log.d(\"leak\", \"methodA Ends\");\n" + "        intA = 2;\n"
			+ "        return 1;\n" + "    }\n" + "\n" + "    int intB = 0;\n" + "\n" + "    int methodB() {\n"
			+ "        dataLeAk25 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "        android.util.Log.d(\"leak-24-13\", dataLeAk24);\n"
			+ "        android.util.Log.d(\"leak-25-13\", dataLeAk25);\n"
			+ "        android.util.Log.d(\"leak-26-13\", dataLeAk26);\n"
			+ "        android.util.Log.d(\"leak-27-13\", dataLeAk27);\n"
			+ "        android.util.Log.d(\"leak-28-13\", dataLeAk28);\n"
			+ "        android.util.Log.d(\"leak\", \"methodB Ends\");\n" + "        intB = 3;\n"
			+ "        return 0;\n" + "    }\n" + "    int methodC(){\n"
			+ "        dataLeAk26 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "        android.util.Log.d(\"leak-24-14\", dataLeAk24);\n"
			+ "        android.util.Log.d(\"leak-25-14\", dataLeAk25);\n"
			+ "        android.util.Log.d(\"leak-26-14\", dataLeAk26);\n"
			+ "        android.util.Log.d(\"leak-27-14\", dataLeAk27);\n"
			+ "        android.util.Log.d(\"leak-28-14\", dataLeAk28);\n"
			+ "        android.util.Log.d(\"leak\", \"methodC Ends\");\n" + "        return 1;\n" + "    }\n" + "\n"
			+ "    public void button_click(View view) {\n"
			+ "        android.util.Log.d(\"leak\", \"button_click starts\");\n" + "\n"
			+ "        dataLeAk27 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "        android.util.Log.d(\"leak-24-15\", dataLeAk24);\n"
			+ "        android.util.Log.d(\"leak-25-15\", dataLeAk25);\n"
			+ "        android.util.Log.d(\"leak-26-15\", dataLeAk26);\n"
			+ "        android.util.Log.d(\"leak-27-15\", dataLeAk27);\n"
			+ "        android.util.Log.d(\"leak-28-15\", dataLeAk28);\n"
			+ "        android.util.Log.d(\"hey\", \"hello\");\n" + "        methodA(view);\n"
			+ "        android.util.Log.d(\"leak\", \"button_click ends\");\n" + "    }\n" + "\n" + "    @Override\n"
			+ "    protected void onCreate(Bundle savedInstanceState) {\n"
			+ "        dataLeAk28 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();\n"
			+ "        android.util.Log.d(\"leak-24-16\", dataLeAk24);\n"
			+ "        android.util.Log.d(\"leak-25-16\", dataLeAk25);\n"
			+ "        android.util.Log.d(\"leak-26-16\", dataLeAk26);\n"
			+ "        android.util.Log.d(\"leak-27-16\", dataLeAk27);\n"
			+ "        android.util.Log.d(\"leak-28-16\", dataLeAk28);\n"
			+ "        super.onCreate(savedInstanceState);\n" + "        setContentView(R.layout.activity_main);\n"
			+ "        android.util.Log.d(\"leak\", \"onCreate Ends\");\n" + "    }\n" + "}\n";
	static String logSample = "2018-12-05 10:49:38.219 6195-6195/? D/leak-24-16: 24\n"
			+ "2018-12-05 10:49:38.219 6195-6195/? D/leak-25-16: 25\n"
			+ "2018-12-05 10:49:38.219 6195-6195/? D/leak-26-16: 26\n"
			+ "2018-12-05 10:49:38.219 6195-6195/? D/leak-27-16: 27\n"
			+ "2018-12-05 10:49:38.219 6195-6195/? D/leak-28-16: GMT+00:00\n"
			+ "2018-12-05 10:49:38.332 6195-6195/? D/leak: onCreate Ends\n"
			+ "2018-12-05 10:49:46.018 6257-6257/com.example.amit.helloworld D/leak-24-16: 24\n"
			+ "2018-12-05 10:49:46.018 6257-6257/com.example.amit.helloworld D/leak-25-16: 25\n"
			+ "2018-12-05 10:49:46.018 6257-6257/com.example.amit.helloworld D/leak-26-16: 26\n"
			+ "2018-12-05 10:49:46.018 6257-6257/com.example.amit.helloworld D/leak-27-16: 27\n"
			+ "2018-12-05 10:49:46.018 6257-6257/com.example.amit.helloworld D/leak-28-16: GMT+00:00\n"
			+ "2018-12-05 10:49:46.094 6257-6257/com.example.amit.helloworld D/leak: onCreate Ends\n"
			+ "2018-12-05 10:49:53.779 6257-6257/com.example.amit.helloworld D/leak: button_click starts\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-24-15: 24\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-25-15: 25\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-26-15: 26\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-27-15: GMT+00:00\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-28-15: GMT+00:00\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak: methodA start\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-24-12: GMT+00:00\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-25-12: 25\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-26-12: 26\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-27-12: GMT+00:00\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak-28-12: GMT+00:00\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak: methodA Ends\n"
			+ "2018-12-05 10:49:53.780 6257-6257/com.example.amit.helloworld D/leak: button_click ends\n";

	/**
	 * Analyze source string, based on input, non true positive sinks for taintSink. 
	 * @param string contains the source code in one string, with multiple lines.
	 * @param maps {@link log.LogAnalyzer_TaintSink#getLogMaps(String) maps} contains the maps of source and sinks
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

	public static void main(String[] args) throws Exception {
		System.out.println(analyzeSourceString(sourceSample, getLogMaps(logSample)));
	}
}
