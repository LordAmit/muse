package edu.wm.cs.muse.gui;

import java.io.File;
import java.io.FileWriter;

public class GenerateConfig {
	
	public static void generateConfig(String config_name, String lib4ast, String app_src, String operator, boolean mutate , String app_name, String destination_folder, boolean log_analyze, String insertion_log, String execution_log, boolean custom_data_leak, String source, String sink, String varDec)
	{
		// Creates a config file if one doesn't already exist
        try { 
        	File myObj = new File(config_name + ".properties");
            if (myObj.createNewFile())
        	{
            	System.out.println("Modifying New File");
        	} else {
        		System.out.println("Modifying Existing File");
        	}
        } catch(Exception e)
        {  }
        
        // Forms the config text file as a string to write onto the .properties file later
        String config_text = "lib4ast: "+lib4ast+"\n"
        		+ "appSrc: " + app_src + "\n" + 
        		"operatorType: " + operator + "\n";
        
        if (mutate)
        {
        	config_text += "\n" + 
            		"//REQUIRED FOR MUTATE\n" + 
            		"appName: " + app_name + "\n" + 
            		"output: " + destination_folder + "\n";
        }
        
        if (log_analyze)
        {
        	config_text += "\n" + 
            		"//REQUIRED FOR LOGANALYZE\n" + 
            		"insertionLog: "+ insertion_log +"\n" + 
            		"executionLog: "+ execution_log +"\n";
        }
        
        if (custom_data_leak)
        {
        	config_text += "\n" + 
            		"//REQUIRED FOR CUSTOM LEAKS\n" + 
            		"source: "+source+"\n" + 
            		"sink: "+sink+"\n" + 
            		"varDec: " +varDec;
        }
        		
        // Writes to the .properties file
        try {
        	FileWriter myWriter = new FileWriter(config_name + ".properties");
            myWriter.write(config_text);
            myWriter.close();
        } catch (Exception e)
        {  }
        
	}
}
