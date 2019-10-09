package edu.wm.cs.muse.schemasTest;

import static org.junit.Assert.assertEquals;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import edu.wm.cs.muse.Muse;
import edu.wm.cs.muse.dataleak.schemas.TaintSchema;
import edu.wm.cs.muse.dataleak.support.*;

//These test cases will focus on the TaintSinkSchema method
public class TaintSinkSchemaTest{

	File expectedOutput;
	String content = null;
	Muse muse;
	CompilationUnit root;
	Document sourceDoc;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;
	File output = new File("test/output/schemastestoutput/output.txt");
//	@Test
//	public visit_test() {
//		TaintSinkSchema TaintSinkTest = new TaintSinkSchema();
//		prepare_test_files(OperatorType.TaintSink);
//		runTaintSinkSchema(root, rewriter, sourceDoc.get(), output, OperatorType.TaintSink);
//		
//	}
	private void prepare_test_files(OperatorType operator) throws FileNotFoundException, IOException {
		Utility.COUNTER_GLOBAL = 0;
		output = new File("test/output/schemastestoutput/output.txt");
	
		this.content = FileUtility.readSourceFile("test/input/sample_multilevelclass.txt").toString();
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
	@Test
	public void Taint_Sink_Schema_run_test() {
		try {
			prepare_test_files(OperatorType.TAINTSINK);

		} catch (IOException e) {
			e.printStackTrace();

		} catch (MalformedTreeException e) {
			e.printStackTrace();

		}
		
		TaintSchema taintSchema_ts = new TaintSchema();
		
		root.accept(taintSchema_ts);
		//rewriter = ASTRewrite.create(root.getAST());	
		sourceDoc = new Document(content);
		assertEquals(true,false);

	}
}