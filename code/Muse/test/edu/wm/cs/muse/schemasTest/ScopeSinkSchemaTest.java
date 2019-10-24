package edu.wm.cs.muse.schemasTest;

import static org.junit.Assert.assertEquals;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import edu.wm.cs.muse.Muse;
import edu.wm.cs.muse.dataleak.schemas.ScopeSourceSchema;
import edu.wm.cs.muse.dataleak.schemas.ScopeSinkSchema;
import edu.wm.cs.muse.dataleak.support.*;

//These test cases will focus on the TaintSinkSchema method
public class ScopeSinkSchemaTest{

	File expectedOutput;
	String content = null;
	Muse muse;
	CompilationUnit root;
	Document sourceDoc;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;
	File output = new File("test/output/schemastestoutput/output.txt");
	
	/**
	 * Test Case: Checks to see that all methods are being traversed
	 * 
	 * Method under test: visit
	 * 
	 * Correct Behavior: 6 changes should be found
	 */
	@Test
	public void number_of_methods() {
		try {
			prepare_test_files(OperatorType.SCOPESINK);

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		}
		ScopeSinkSchemaStub testTaintSinkSchema = new ScopeSinkSchemaStub();
		root.accept(testTaintSinkSchema);	
		sourceDoc = new Document(content);
		assertEquals(6,testTaintSinkSchema.returnMethodCounter());

	}
	
	
	/**
	 * Test Case: Checks to see if the correct number of sinks are being inputted
	 * 
	 * Method under test: visit
	 * 
	 * Correct Behavior: 6 changes should be found
	 */
	@Test
	public void number_of_Sinks() {
		try {
			prepare_test_files(OperatorType.SCOPESINK);

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		}
		ScopeSinkSchemaStub testTaintSinkSchema = new ScopeSinkSchemaStub();
		root.accept(testTaintSinkSchema);	
		sourceDoc = new Document(content);
		assertEquals(6,testTaintSinkSchema.returnSinkCounter());

	}
	
	
	/**
	 * Test Case: Checks to see if the correct methods are being returned
	 * 
	 * Method under test: visit
	 * 
	 * Correct Behavior: list of methods should be returned
	 */
	@Test
	public void correct_method_calls() {
		ArrayList<String> correctList = new ArrayList<>();
		correctList.add("METHODA");
		correctList.add("METHODB");
		correctList.add("METHODCONEA");
		correctList.add("METHODCONEB");
		correctList.add("METHODCTWOA");
		correctList.add("METHODCTWOB");
		
		try {
			prepare_test_files(OperatorType.SCOPESINK);

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		}
		ScopeSinkSchemaStub testTaintSinkSchema = new ScopeSinkSchemaStub();
		root.accept(testTaintSinkSchema);	
		sourceDoc = new Document(content);
		assertEquals(correctList,testTaintSinkSchema.returnMethodNames());
	}
	
	/**
	 * Test case: checks to see if subclasses are being traversed properly
	 * 
	 * Method under test: visit
	 * 
	 * Correct behavior: 2 changes should be returned
	 */
	@Test
	public void number_of_subClasses() {
		
		
		try {
			prepare_test_files(OperatorType.SCOPESINK);

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		}
		ScopeSinkSchemaStub testTaintSinkSchema = new ScopeSinkSchemaStub();
		root.accept(testTaintSinkSchema);	
		sourceDoc = new Document(content);
		assertEquals(2,testTaintSinkSchema.returnSubClassCount());
	}
	
	/**
	 * Test Case: checks to see if there is a correct number of dataleaks
	 * 
	 * Method under test: visit
	 * 
	 * Correct Behavior: 11 leaks should be returned
	 */
	@Test
	public void number_of_dataLeaks() {
		
		
		try {
			prepare_test_files(OperatorType.SCOPESINK);

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		}
		ScopeSinkSchemaStub testTaintSinkSchema = new ScopeSinkSchemaStub();
		root.accept(testTaintSinkSchema);	
		sourceDoc = new Document(content);
		assertEquals(11,testTaintSinkSchema.returndataLeakCount());
	}
	
	/**
	 * Prepares test files
	 * @param operator
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void prepare_test_files(OperatorType operator) throws FileNotFoundException, IOException {
		Utility.COUNTER_GLOBAL = 0;
		output = new File("test/output/schemastestoutput/output.txt");
	
		this.content = FileUtility.readSourceFile("test/output/sample_multilevelclass_taint.txt").toString();
		expectedOutput = new File("test/output/sample_multilevelclass_taint.txt");
	
		
		
		this.muse = new Muse();
		this.root = getTestAST(content);
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