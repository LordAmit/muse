package edu.wm.cs.muse.guiTest;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import edu.wm.cs.muse.gui.*;
import java.io.File;

import org.junit.jupiter.api.Test;

import edu.wm.cs.muse.dataleak.support.FileUtility;

public class GuiTest {
	
	File expectedOutput;
	File processedOutput;

	@Test
	void only_required_fields() {
		expectedOutput = new File("test/output/sample_config_1.txt");
		processedOutput = new File("testconfig.properties");
		GenerateConfig.generateConfig("testconfig", "C:\\Users", "C:\\Users\\Admin\\muse\\mse\\code\\Muse\\Sample_Application\\app\\src", "SCOPESINK", false , "Sample App", "C:\\Users\\Admin\\Downloads\\test", false, "C:\\Users\\Admin\\Downloads\\insertion_log", "C:\\Users\\Admin\\Downloads\\execution_log", false, "sample_source_string", "sample_sink_string", "sample_varDec_string");
		assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));
	}
	
	@Test
	void with_mutation_fields() {
		expectedOutput = new File("test/output/sample_config_2.txt");
		processedOutput = new File("testconfig.properties");
		GenerateConfig.generateConfig("testconfig", "C:\\Users", "C:\\Users\\Admin\\muse\\mse\\code\\Muse\\Sample_Application\\app\\src", "SCOPESINK", true , "Sample App", "C:\\Users\\Admin\\Downloads\\test", false, "C:\\Users\\Admin\\Downloads\\insertion_log", "C:\\Users\\Admin\\Downloads\\execution_log", false, "sample_source_string", "sample_sink_string", "sample_varDec_string");
		assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));
	}
	
	@Test
	void with_log_analyze_fields() {
		expectedOutput = new File("test/output/sample_config_3.txt");
		processedOutput = new File("testconfig.properties");
		GenerateConfig.generateConfig("testconfig", "C:\\Users", "C:\\Users\\Admin\\muse\\mse\\code\\Muse\\Sample_Application\\app\\src", "SCOPESINK", false , "Sample App", "C:\\Users\\Admin\\Downloads\\test", true, "C:\\Users\\Admin\\Downloads\\insertion_log", "C:\\Users\\Admin\\Downloads\\execution_log", false, "sample_source_string", "sample_sink_string", "sample_varDec_string");
		assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));
	}

	@Test
	void with_custom_data_leak_fields() {
		expectedOutput = new File("test/output/sample_config_4.txt");
		processedOutput = new File("testconfig.properties");
		GenerateConfig.generateConfig("testconfig", "C:\\Users", "C:\\Users\\Admin\\muse\\mse\\code\\Muse\\Sample_Application\\app\\src", "SCOPESINK", false , "Sample App", "C:\\Users\\Admin\\Downloads\\test", false, "C:\\Users\\Admin\\Downloads\\insertion_log", "C:\\Users\\Admin\\Downloads\\execution_log", true, "sample_source_string", "sample_sink_string", "sample_varDec_string");
		assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));
	}
	
	@Test
	void with_all_fields() {
		expectedOutput = new File("test/output/sample_config_5.txt");
		processedOutput = new File("testconfig.properties");
		GenerateConfig.generateConfig("testconfig", "C:\\Users", "C:\\Users\\Admin\\muse\\mse\\code\\Muse\\Sample_Application\\app\\src", "SCOPESINK", true , "Sample App", "C:\\Users\\Admin\\Downloads\\test", true, "C:\\Users\\Admin\\Downloads\\insertion_log", "C:\\Users\\Admin\\Downloads\\execution_log", true, "sample_source_string", "sample_sink_string", "sample_varDec_string");
		assertEquals(true, FileUtility.testFileEquality(expectedOutput, processedOutput));
	}
}
