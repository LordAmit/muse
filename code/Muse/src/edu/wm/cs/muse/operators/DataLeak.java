package edu.wm.cs.muse.operators;

import edu.wm.cs.muse.utility.OperatorType;

/**
 * @author liz
 *
 * This class contains all the information pertaining to the data leak security operator. More specifically, it contains
 * the sink and source for each sink, source, and reachability operator schema.  
 */
public class DataLeak {
	
	// source and sink strings used by the reachability operator schema
	private static String reachabilitySource = "String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";
	private static String reachabilitySink = "Object throwawayLeAk%d = android.util.Log.d(\"leak-%d\", dataLeAk%d);";
	
	
	/**
	 * The count parameter is an instance of the global counter utility and is used to format the source string. 
	 * 
	 * @returns the appropriate source for the operator type specified.
	 */
	public static String getSource(OperatorType op, int count) {
		if (op == OperatorType.SINK) {
			return String.format("final String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
                                 count);
		}
		if (op == OperatorType.SOURCE) {
			return String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
                   count);
		}
		if (op == OperatorType.REACHABILITY) {
			return reachabilitySource;
		}
		return null;
	}
	
	/**
	 * Note that there is no sink defined for the source operator schema, and null is returned if the schema operator type 
	 * is specified. Additionally, the sink operator requires two counters to format its sink.
	 *  
	 * @returns the appropriate sink for the operator type specified. 
	 */
	public static String getSink(OperatorType op, int sourceCounter, int sinkCounter) {
		if (op == OperatorType.SINK) {
			return String.format("android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);", sourceCounter, sinkCounter, sourceCounter);
		}
		if (op == OperatorType.REACHABILITY) {
			return reachabilitySink;
		}
		return null;
	}
	
	/**
	 * The count parameter is an instance of the global counter utility used to format the leak string. 
	 * 
	 * @returns the string version of a data leak as used by the reachability operator schema. 
	 */
	public static String getLeak(int count) {
		return String.format(reachabilitySource, count) + "\n" 
		           + String.format(reachabilitySink, count, count, count);
	}
			
}
