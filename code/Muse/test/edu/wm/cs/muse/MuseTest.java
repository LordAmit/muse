package edu.wm.cs.muse;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;

/*
 * We will be focusing on creating behavior based test cases. AAA pattern, i.e. 
 * Arrange the preconditions
 * Act on test Object
 * Assert the results
 * will be utilized.
 */

/**
 * Unit test file of Muse. 
 * @author Amit Seal Ami, Liz Weech, Yang Zhang
 *
 */
public class MuseTest {
	
	File expectedOutput;
	String content = null;
	String output;
	Muse muse;
	CompilationUnit root;
	Document sourceDoc;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;
	File file = new File("test/test.txt");
	
	@Test
	public void reachability_operation_on_hello_world() {

		try {
			prepare_test_files(OperatorType.REACHABILITY);			
			execute_muse(OperatorType.REACHABILITY);
			assertEquals(true, FileUtils.contentEquals(expectedOutput, processedOutput));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	@Test
	public void source_operation_on_hello_world() {
	
		try {
			prepare_test_files(OperatorType.SOURCE);
			execute_muse(OperatorType.SOURCE);
			
			assertEquals(expectedOutput, processedOutput);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	@Test
	public void sink_operation_on_hello_world() {
	
		try {
			prepare_test_files(OperatorType.SINK);
			execute_muse(OperatorType.SINK);
			
			assertEquals(expectedOutput, processedOutput);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
		@Test
	public void taint_operation_on_hello_world() {
		try {
			
			prepare_test_files(OperatorType.TAINT);
			execute_muse(OperatorType.TAINT);

			assertEquals(expectedOutput, processedOutput);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	private void execute_muse(OperatorType operator) throws BadLocationException, MalformedTreeException, IOException {
		rewriter = ASTRewrite.create(root.getAST());
		sourceDoc = new Document(content);
		muse.operatorExecution(root, rewriter, sourceDoc.get(), file, operator);
		
		processedOutput = file;
	}

	private void prepare_test_files(OperatorType operator) throws FileNotFoundException, IOException {
		switch (operator) {
			case SINK:
				// the input for the sink test is the output from the source operator
				// this is because the sink operator relies on sources already being inserted in the code base
				content = FileUtility.readSourceFile("test/output/sample_hello_world_source.txt").toString();
				expectedOutput = new File("test/output/sample_hello_world_sink.txt");
				
			case REACHABILITY:
				content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
				expectedOutput = new File("test/output/sample_hello_world_reachability.txt");
				
			case SOURCE:
				content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
				expectedOutput = new File("test/output/sample_hello_world_source.txt");
			
			case TAINT:
				// not implemented yet
				// do nothing
			case TAINTSINK: 
				// also not implemented yet
				
		}
		muse = new Muse();
		root = getTestAST(content);
	}
	
    private CompilationUnit getTestAST(String source) {	
		HashMap<String, String> options = new HashMap<String, String>();
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
		parser.setCompilerOptions(options);
		
		parser.setSource(source.toCharArray());
		   
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
			
		return (CompilationUnit) parser.createAST(new NullProgressMonitor());
	}
}
