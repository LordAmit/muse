package edu.wm.cs.muse.dataleak;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;

/**
 * @author liz
 *
 *         This class contains all the information pertaining to the data leak
 *         security operator. More specifically, it contains the sink and source
 *         for each sink, source, and reachability operator schema.
 * 
 *         Sample data leak formats: Declaration: String dataLeak{{ IDENTIFIER
 *         }}; Source: dataLeak{{ IDENTIFIER }} =
 *         java.util.Calendar.getInstance().getTimeZone().getDisplayName();
 *         Sink: android.util.Log.d("leak-{{ IDENTIFIER }}", dataLeak{{
 *         IDENTIFIER }}); Hop: dataLeak{{ IDENTIFIER }} = dataLeak{{ IDENTIFIER
 *         }};
 * 
 */
public class DataLeak {

	// source and sink strings used by the reachability operator schema
	private static String reachabilitySource = "String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";
	private static String reachabilitySink = "Object throwawayLeAk%d = android.util.Log.d(\"leak-%d\", dataLeAk%d);";
	private static String complexReachabilitySource = "String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";
	private static String complexReachabilitySink = "android.util.Log.d(\"leak-%d\", dataLeAkPath%d);";
	private static String taintSourceSource = "dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";
	private static String taintSourceSink = "android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);";
	private static String taintSinkSource =  "final String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";
	private static String taintSinkSink = "android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);";
	private static String scopeSource = "Object scope_LeAk%d = android.util.Log.d(\"scope-leak-%d\", dataLeAk%d);";
	private static String scopeSink = "android.util.Log.d(\"leak-%s-%s\", dataLeAk%s);";
	
	public static void setSource(OperatorType op, String source) {
		if (op == OperatorType.TAINTSOURCE) {
			taintSourceSource = source;
		}
		else if (op == OperatorType.TAINTSINK) {
			taintSinkSource = source;
		}
		else if (op == OperatorType.SCOPESOURCE) {
			scopeSource = source;
		}
		else if (op == OperatorType.REACHABILITY) {
			reachabilitySource = source;
		}
		else if (op == OperatorType.COMPLEXREACHABILITY) {
			complexReachabilitySource = source;
		}
		else {
			throw new IllegalArgumentException("Type must be valid Operator.");
		}
	}
	
	public static void setSink(OperatorType op, String sink) {
		if (op == OperatorType.TAINTSOURCE) {
			taintSourceSink =sink;
		}
		else if (op == OperatorType.TAINTSINK) {
			taintSinkSink = sink;
		}
		else if (op == OperatorType.SCOPESINK) {
			scopeSink = sink;
		}
		else if (op == OperatorType.REACHABILITY) {
			reachabilitySink = sink;
		}
		else if (op == OperatorType.COMPLEXREACHABILITY) {
			complexReachabilitySink = sink;
		}
		else {
			throw new IllegalArgumentException("Type must be valid Operator.");
		}
	}
	
	/**
	 * Formats the source string and returns the correct source string based on the
	 * operator type specified.
	 * 
	 * @param op         is the operator type
	 * @param identifier is an instance of the global counter utility and is used to
	 *                   identify the source string.
	 * @returns the appropriate source for the operator type specified.
	 */
	public static String getSource(OperatorType op, int identifier) {
		if (op == OperatorType.TAINTSOURCE) {
			return String.format(taintSourceSource, identifier);
		}
		else if (op == OperatorType.TAINTSINK) {
			return String.format(taintSinkSource, identifier);
		}
		else if (op == OperatorType.SCOPESOURCE) {
			return String.format(scopeSource, identifier);
		}
		else if (op == OperatorType.REACHABILITY) {
			return String.format(reachabilitySource, identifier);
		}
		else if (op == OperatorType.COMPLEXREACHABILITY) {
			return String.format(complexReachabilitySource, identifier);
		}
		return null;
	}
	
	public static String getSource(OperatorType op) {
		if (op == OperatorType.TAINTSOURCE) {
			return taintSourceSource;
		}
		else if (op == OperatorType.TAINTSINK) {
			return taintSinkSource;
		}
		else if (op == OperatorType.SCOPESOURCE) {
			return scopeSource;
		}
		else if (op == OperatorType.REACHABILITY) {
			return reachabilitySource;
		}
		else if (op == OperatorType.COMPLEXREACHABILITY) {
			return complexReachabilitySource;
		}
		return null;
	}

	/**
	 * Formats the sink string and returns the correct sink string based on the
	 * operator type specified.
	 * 
	 * Note that there is no sink defined for the source operator schema, and null
	 * is returned if the schema operator type is specified. Additionally, the sink
	 * operator requires two identifiers to format its sink.
	 * 
	 * @param op               is the operator type
	 * @param sourceIdentifier the identifier for the source
	 * @param sinkIdentifier   the identifier for the sink
	 * @returns the appropriate sink for the operator type specified.
	 */
	public static String getSink(OperatorType op, int sourceIdentifier, int sinkIdentifier) {
		if (op == OperatorType.TAINTSINK) {
			return String.format(taintSinkSink, sourceIdentifier, sinkIdentifier, sourceIdentifier);
		}
		else if (op == OperatorType.TAINTSOURCE) {
			return String.format(taintSourceSink, sourceIdentifier, sinkIdentifier, sourceIdentifier);
		}
		else if (op == OperatorType.REACHABILITY) {
			return String.format(reachabilitySink, sinkIdentifier, sinkIdentifier, sinkIdentifier);
		}

		return null;
	}
	
	public static String getSink(OperatorType op) {
		if (op == OperatorType.TAINTSINK) {
			return taintSinkSink;
		}
		else if (op == OperatorType.TAINTSOURCE) {
			return taintSourceSink;
		}
		else if (op == OperatorType.SCOPESINK) {
			return scopeSink;
		}
		else if (op == OperatorType.REACHABILITY) {
			return reachabilitySink;
		}
		else if (op == OperatorType.COMPLEXREACHABILITY) {
			return complexReachabilitySink;
		}
		return null;
	}
	
	/**
	 * Formats the leak string and returns it. Currently only used by the
	 * reachability operator schema.
	 * 
	 * @param identifier an instance of the global counter utility used to identify
	 *                   the leak string
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * 
	 * @returns the string version of a data leak as used by the reachability
	 *          operator schema.
	 */
	public static String getLeak(int identifier) {
		return String.format(reachabilitySource, identifier) + "\n"
				+ String.format(reachabilitySink, identifier, identifier, identifier);
	}

	/**
	 * Formats the leak string and returns it. Accepts only OperatorType Reachability and ComplexReachabilityOperator
	 * 
	 * @param identifier an instance of the global counter utility used to identify
	 *                   the leak string
	 * @param type       OperatorType
	 * 
	 * @returns the string version of a data leak as used by the reachability
	 *          operator schema.
	 */

	public static String getLeak(OperatorType type, int identifier) {
		if (type == OperatorType.REACHABILITY)
			return String.format(reachabilitySource, identifier) + "\n"
					+ String.format(reachabilitySink, identifier, identifier, identifier);
		else if (type == OperatorType.COMPLEXREACHABILITY) {

			String[] paths = new String[] {
					"String[] leakArRay%d = new String[] {\"n/a\", dataLeAk%d};\n"
							+ "String dataLeAkPath%d = leakArRay%d[leakArRay%d.length - 1];",
					"java.util.HashMap<String, java.util.HashMap<String, String>> leakMaP%d = new java.util.HashMap<String, java.util.HashMap<String, String>>();\n"
							+ "leakMaP%d.put(\"test\", new java.util.HashMap<String, String>());\n"
							+ "leakMaP%d.get(\"test\").put(\"test\", dataLeAk%d);\n"
							+ "String dataLeAkPath%d = leakMaP%d.get(\"test\").get(\"test\");",
					"StringBuffer leakBuFFer%d = new StringBuffer();" + "for (char chAr%d : dataLeAk%d.toCharArray()) {"
							+ "leakBuFFer%d.append(chAr%d);" + "}" + "String dataLeAkPath%d = leakBuFFer%d.toString();",
					"String dataLeAkPath%d;" + "try {" + "throw new Exception(dataLeAk%d);"
							+ "} catch (Exception leakErRor%d) {" + "dataLeAkPath%d = leakErRor%d.getMessage();"
							+ "}" };
			String source = String.format(
					"String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();", identifier);
			String sink = String.format("android.util.Log.d(\"leak-%d\", dataLeAkPath%d);", identifier, identifier);
			String leak = source + "\n" + String.format(paths[identifier % paths.length], identifier, identifier,
					identifier, identifier, identifier, identifier, identifier) + "\n" + sink;
			return leak;

		}
		else {
			throw new IllegalArgumentException("Type must be Operator.REACHABILITY or Operator.COMPLEXREACHABILITY ");
		}
	}

	/**
	 * Hopping logic that adds a level of misdirection that certain security
	 * analysis tools can't follow.
	 * 
	 * @returns the "hop" that results in one dataleak string being set equal to
	 *          another.
	 */
	public static String getHop(int identifierOne, int identifierTwo) {
		return String.format("String dataLeAk%d = dataLeAk%d", identifierOne, identifierTwo);
	}

}
