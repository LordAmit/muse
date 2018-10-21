package edu.wm.cs.muse.operators;

import edu.wm.cs.muse.utility.Utility;

/**
 * @author liz
 *
 * This class contains all the information pertaining to the data leak security operator. 
 * It is still very much a work in progress right now. 
 */
public class DataLeak {
	
	private String source = "";
	private String sink = "";
	private String leak = "";
	
	public DataLeak(String source, String sink) {
		this.source = source;
		this.sink = sink;
		leak = String.format(source, Utility.COUNTER_GLOBAL) + "\n" 
	           + String.format(sink, Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL);
	}
	
	public String getSource() {
		return source;
	}
	
	public String getSink() {
		return sink;
	}
	
	public String getLeak() {
		return leak;
	}
			
}
