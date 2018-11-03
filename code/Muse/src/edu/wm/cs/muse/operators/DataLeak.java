package edu.wm.cs.muse.operators;

import edu.wm.cs.muse.utility.OperatorType;

/**
 * @author liz
 *
 * This class contains all the information pertaining to the data leak security operator. More specifically, it contains
 * the sink and source for each sink, source, and reachability operator schema.  
 * 
 * Sample data leak formats:
 *   Declaration: String dataLeak{{ IDENTIFIER }};
 *   Source: dataLeak{{ IDENTIFIER }} = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
 *   Sink: android.util.Log.d(“leak-{{ IDENTIFIER }}”, dataLeak{{ IDENTIFIER }});
 *   Hop: dataLeak{{ IDENTIFIER }} = dataLeak{{ IDENTIFIER }};
 *   
 */
public class DataLeak {
	
	// source and sink strings used by the reachability operator schema
	private static String reachabilitySource = "String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";
	private static String reachabilitySink = "Object throwawayLeAk%d = android.util.Log.d(\"leak-%d\", dataLeAk%d);";
	
	
	/**
	 * Formats the source string and returns the correct source string based on the operator type specified.
	 * 
	 * @param op is the operator type
	 * @param identifier is an instance of the global counter utility and is used to identify the source string. 
	 * @returns the appropriate source for the operator type specified.
	 */
	public static String getSource(OperatorType op, int identifier) {
		if (op == OperatorType.SINK) {
			return String.format("final String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
                                 identifier);
		}
		if (op == OperatorType.SOURCE) {
			return String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
                   identifier);
		}
		if (op == OperatorType.TAINT) {
			return String.format("Object tainted_LeAk%d = android.util.Log.d(\\\"taint-leak-%d\\\", dataLeAk%d);",
	                   identifier);
		}
		if (op == OperatorType.REACHABILITY) {
			return reachabilitySource;
		}
		return null;
	}
	
	/**
	 * Formats the sink string and returns the correct sink string based on the operator type specified.
	 * 
	 * Note that there is no sink defined for the source operator schema, and null is returned if the schema operator type 
	 * is specified. Additionally, the sink operator requires two identifiers to format its sink.
	 *  
	 * @param op is the operator type
	 * @param sourceIdentifier the identifier for the source
	 * @param sinkIdentifier the identifier for the sink 
	 * @returns the appropriate sink for the operator type specified. 
	 */
	public static String getSink(OperatorType op, int sourceIdentifier, int sinkIdentifier) {
		if (op == OperatorType.SINK) {
			return String.format("android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);", sourceIdentifier, sinkIdentifier, sourceIdentifier);
		}
		if (op == OperatorType.REACHABILITY) {
			return reachabilitySink;
		}
		return null;
	}
	
	/**
	 * Formats the leak string and returns it. Currently only used by the reachability operator schema. 
	 * 
	 * @param identifier an instance of the global counter utility used to identify the leak string
	 * 
	 * @returns the string version of a data leak as used by the reachability operator schema. 
	 */
	public static String getLeak(int identifier) {
		return String.format(reachabilitySource, identifier) + "\n" 
		           + String.format(reachabilitySink, identifier, identifier, identifier);
	}
	
	/**
	 * Hopping logic that adds a level of misdirection that certain security analysis tools can't follow.  
	 * 
	 * @returns the "hop" that results in one dataleak string being set equal to another. 
	 */
	public static String getHop(int identifierOne, int identifierTwo) {
		return String.format("String dataLeAk%d = dataLeAk%d", identifierOne, identifierTwo);
	}
			
}
