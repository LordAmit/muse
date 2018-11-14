package edu.wm.cs.muse;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.internal.watson.ElementTreeWriter;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.mdroid.ASTHelper;

/*
 * We will be focusing on creating behavior based test cases. AAA pattern, i.e. 
 * Arrange the preconditions
 * Act on test Object
 * Assert the results
 * will be utilized.
 */

/**
 * Unit test file of Muse. 
 * @author Amit Seal Ami
 *
 */
public class MuseTest {
	
	String expectedOutput;
	String content = null;
	String output;
	Muse muse;
	CompilationUnit root;
	Document sourceDoc;
	ASTRewrite rewriter;
	TextEdit edits;
	String processedOutput;
	@Test
	public void reachability_operation_on_hello_world() {

		try {
			prepare_test_files(OperatorType.REACHABILITY);			
			execute_muse(OperatorType.REACHABILITY);
			
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
	

	private void execute_muse(OperatorType operator) throws BadLocationException {
		rewriter = ASTRewrite.create(root.getAST());
		rewriter = muse.operatorExecution(root, rewriter, operator);
		sourceDoc = new Document(content);

		edits = rewriter.rewriteAST(sourceDoc, null);
		// Applies the edit tree rooted by this edit to the given document.
		edits.apply(sourceDoc);
		
		processedOutput = sourceDoc.get();
	}

	private void prepare_test_files(OperatorType operator) throws FileNotFoundException, IOException {
		switch (operator) {
			case SINK:
				// the input for the sink test is the output from the source operator
				// this is because the sink operator relies on sources already being inserted in the code base
				content = FileUtility.readSourceFile("test/output/sample_hello_world_source.txt").toString();
				expectedOutput = FileUtility.readSourceFile("test/output/sample_hello_world_sink.txt").toString();
				
			case REACHABILITY:
				content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
				expectedOutput = FileUtility.readSourceFile("test/output/sample_hello_world_reachability.txt").toString();
				
			case SOURCE:
				content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
				expectedOutput = FileUtility.readSourceFile("test/output/sample_hello_world_source.txt").toString();
			
			case TAINT:
				// not implemented yet
				// do nothing
			case TAINTSINK: 
				// also not implemented yet
				
		}
		Arguments.extractArguments(new File("test/input/runtime_argument.txt"));
		muse = new Muse();
		root = ASTHelper.getAST(content, Arguments.getBinariesFolder(),
				Arguments.getRootPath());
	}
}
