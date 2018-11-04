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

import edu.wm.cs.muse.mdroid.ASTHelper;
import edu.wm.cs.muse.utility.Arguments;
import edu.wm.cs.muse.utility.FileUtility;
import edu.wm.cs.muse.utility.OperatorType;

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
	Muse muse;CompilationUnit root;
	Document sourceDoc;
	ASTRewrite rewriter;
	TextEdit edits;
	String processedOutput;
	@Test
	public void reachability_operation_on_hello_world() {
	
		try {
			arrange_reachability();			
			
			act_reachability();
			
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
			arrange_source();			
			
			act_source();
			
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
	

	private void act_reachability() throws BadLocationException {
		rewriter = ASTRewrite.create(root.getAST());
		rewriter = muse.operatorExecution(root, rewriter, OperatorType.REACHABILITY);
		sourceDoc = new Document(content);

		edits = rewriter.rewriteAST(sourceDoc, null);
		// Applies the edit tree rooted by this edit to the given document.
		edits.apply(sourceDoc);
		
		processedOutput = sourceDoc.get();
	}

	private void arrange_reachability() throws FileNotFoundException, IOException {
		content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
		expectedOutput = FileUtility.readSourceFile("test/output/sample_hello_world_reachability.txt").toString();
		Arguments.extractArguments(new File("test/input/runtime_argument.txt"));
		muse = new Muse();
		root = ASTHelper.getAST(content, Arguments.getBinariesFolder(),
				Arguments.getRootPath());
	}
	
	private void act_source() throws BadLocationException {
		rewriter = ASTRewrite.create(root.getAST());
		rewriter = muse.operatorExecution(root, rewriter, OperatorType.SOURCE);
		sourceDoc = new Document(content);

		edits = rewriter.rewriteAST(sourceDoc, null);
		// Applies the edit tree rooted by this edit to the given document.
		edits.apply(sourceDoc);
		
		processedOutput = sourceDoc.get();
	}
	
	private void arrange_source() throws FileNotFoundException, IOException {
		content = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
		expectedOutput = FileUtility.readSourceFile("test/output/sample_hello_world_reachability.txt").toString();
		Arguments.extractArguments(new File("test/input/runtime_argument.txt"));
		muse = new Muse();
		root = ASTHelper.getAST(content, Arguments.getBinariesFolder(),
				Arguments.getRootPath());
	}
	

}
