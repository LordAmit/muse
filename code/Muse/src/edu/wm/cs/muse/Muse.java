package edu.wm.cs.muse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import edu.wm.cs.muse.dataleak.operators.ReachabilityOperator;
import edu.wm.cs.muse.dataleak.operators.SinkOperator;
import edu.wm.cs.muse.dataleak.operators.SourceOperator;
import edu.wm.cs.muse.dataleak.operators.TaintOperator;
import edu.wm.cs.muse.dataleak.operators.TaintSinkOperator;
import edu.wm.cs.muse.dataleak.schemas.ReachabilitySchema;
import edu.wm.cs.muse.dataleak.schemas.SinkSchema;
import edu.wm.cs.muse.dataleak.schemas.SourceSchema;
import edu.wm.cs.muse.dataleak.schemas.TaintSchema;
import edu.wm.cs.muse.dataleak.schemas.TaintSinkSchema;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.mdroid.ASTHelper;

/**
 *
 * @author Richard Bonett
 * @since October 12, 2017
 */
public class Muse {

	ASTRewrite rewriter;

	public void runMuse(String[] args) {
		// Usage Error
		if (args.length != 4) {
			printArgumentError();
			return;
		}

		Arguments.extractArguments(args);

		FileUtility.setMutantsDirectory();

		System.out.println(Arguments.getRootPath());

		Collection<File> files = FileUtils.listFiles(new File(Arguments.getRootPath()), TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);

		for (File file : files) {
			try {
				if (file.getName().endsWith(".java")
						&& file.getCanonicalPath().contains(Arguments.getAppName().replace(".", "/"))
						&& !file.getName().contains("EmmaInstrumentation.java")
						&& !file.getName().contains("FinishListener.java")
						&& !file.getName().contains("InstrumentedActivity.java")
						&& !file.getName().contains("SMSInstrumentedReceiver.java")) {

					// System.out.println("PROCESSING: " + file.getAbsolutePath());
					String source = FileUtility.readSourceFile(file.getAbsolutePath()).toString();

					// Creates an abstract syntax tree.
					CompilationUnit root = ASTHelper.getAST(source, Arguments.getBinariesFolder(),
							Arguments.getRootPath());

					// Creates a new instance for describing manipulations of the given AST.
					rewriter = ASTRewrite.create(root.getAST());

					// Accepts the given visitor on a visit of the current node, which is root here.
					// rewriter also records the required edits necessary
					/*
					 * this is commented out for adopting new changes
					 */
//					root.accept(new ReachabilityVisitor(rewriter));
//					reachabilityExecution(file, source, root);
//					ReachabilitySchema reachabilitySchema = new ReachabilitySchema();
//					root.accept(reachabilitySchema);
//					ReachabilityOperator operator = new ReachabilityOperator(rewriter,
//							reachabilitySchema.getNodeChanges());
//					rewriter = operator.InsertChanges();
//					rewriter = reachabilityExecution(root, rewriter);
					rewriter = operatorExecution(root, rewriter, OperatorType.TAINT);
//					rewriter = tempExecution(root, rewriter);
					File temp_file = new File("test/temp/temp_file.java");

//					Document sourceDoc = new Document(source);
//
//					TextEdit edits = rewriter.rewriteAST(sourceDoc, null);
//					// Applies the edit tree rooted by this edit to the given document.
//					edits.apply(sourceDoc);
//					FileUtils.writeStringToFile(file, sourceDoc.get(), false);
//					rewriter = null;
					//TaintSchema
					Document tempDocument = new Document(source);
					TextEdit tempEdits = rewriter.rewriteAST(tempDocument, null);
					tempEdits.apply(tempDocument);
					FileUtils.writeStringToFile(temp_file, tempDocument.get(), false);
					FileUtils.writeStringToFile(file, tempDocument.get(), false);
					//TaintSinkSchema
					source = FileUtility.readSourceFile(temp_file.getAbsolutePath()).toString();
					rewriter = null;
//					root = ASTHelper.getAST(tempDocument.get(), Arguments.getBinariesFolder(),
//								Arguments.getRootPath());
					
					root = ASTHelper.getAST(tempDocument.get(), Arguments.getBinariesFolder(), "test/temp/");
					rewriter = ASTRewrite.create(root.getAST());
					rewriter = operatorExecution(root, rewriter, OperatorType.TAINTSINK);
					Files.delete(temp_file.toPath());

					try {
						applyChangesToFile(file, source);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();

					}
					
					// copies the code from temp_file to the correct folder and file
					// taint should be inserting into, then deletes the temp_file.
//					Path finishedTaint = temp_file.toPath();
//					Path mutantsFolder = Paths.get(file.toString());
//					Files.copy(finishedTaint, mutantsFolder, StandardCopyOption.REPLACE_EXISTING);

//					source = readSourceFile(file.getAbsolutePath()).toString();
//					root = ASTHelper.getAST(source, binariesFolder, rootPath);

					/*
					 * Uses the rewriter to create an AST for the SinkSchema to utilize Then creates
					 * a new instance to manipulate the AST The root node then accepts the schema
					 * visitor on the visit The rewriter implements the specified changes made by
					 * the sink operator
					 */
//					SinkSchema sinkScheme = new SinkSchema();
//					rewriter = ASTRewrite.create(root.getAST());
//					root.accept(new SinkSchema());
//					SinkOperator sinkOp = new SinkOperator(rewriter, sinkScheme.getNodeChanges());
//					rewriter = sinkOp.InsertChanges();

					// sourceDoc = new Document(source);
					// edits = rewriter.rewriteAST(sourceDoc, null);
					// edits.apply(sourceDoc);
					// FileUtils.writeStringToFile(file, sourceDoc.get(), false);
					// rewriter = null;
				}
			} catch (IOException e) {
				System.err
						.println(String.format("ERROR PROCESSING \"%s\": %s", file.getAbsolutePath(), e.getMessage()));
				return;
//			} catch (MalformedTreeException | BadLocationException e) {
			} catch (BadLocationException e) {
				System.err.println("ERROR EDITING AST: " + e.getMessage());
				return;
			}
		}
	}

	/**
	 * Converts all modifications recorded by this rewriter into an object
	 * representing the the corresponding text edits to the source of a ITypeRoot
	 * from which the AST was created from. The type root's source itself is not
	 * modified by this method call.
	 * 
	 * @author Amit Seal Ami
	 * @param file   is file where it will be written
	 * @param source is the content of source
	 * @throws BadLocationException
	 * @throws IOException
	 */
	private void applyChangesToFile(File file, String source) throws BadLocationException, IOException {
		Document sourceDoc = new Document(source);

		TextEdit edits = rewriter.rewriteAST(sourceDoc, null);
		// Applies the edit tree rooted by this edit to the given document.
		edits.apply(sourceDoc);
		FileUtils.writeStringToFile(file, sourceDoc.get(), false);
		rewriter = null;
	}

	/**
	 *  executes the specified operator's modifications to the AST
	 * 
	 * @param root is the compilation unit based root of the AST
	 * @param rewriter ASTRewrite object that holds the changes to the AST
	 * @param operatorType is the type of operator being executed: sink, source, reachability, or taint
	 */
	public ASTRewrite operatorExecution(CompilationUnit root, ASTRewrite rewriter, OperatorType operatorType) {
		switch (operatorType) {
			case SINK:
				SinkSchema sinkSchema = new SinkSchema();
				root.accept(sinkSchema);
				SinkOperator sinkOperator = new SinkOperator(rewriter, sinkSchema.getNodeChanges());
				rewriter = sinkOperator.InsertChanges();
				return rewriter;
				
			case SOURCE: 
				SourceSchema sourceSchema = new SourceSchema();
				root.accept(sourceSchema);
				SourceOperator sourceOperator = new SourceOperator(rewriter, sourceSchema.getNodeChanges());
				rewriter = sourceOperator.InsertChanges();
				return rewriter;
				
			case REACHABILITY: 
				ReachabilitySchema reachabilitySchema = new ReachabilitySchema();
				root.accept(reachabilitySchema);
				ReachabilityOperator reachabilityOperator = new ReachabilityOperator(rewriter, reachabilitySchema.getNodeChanges());
				rewriter = reachabilityOperator.InsertChanges();
				return rewriter;
				
			case TAINT: 
				TaintSchema taintSchema = new TaintSchema();
				root.accept(taintSchema);
				TaintOperator taintOperator = new TaintOperator(rewriter, taintSchema.getNodeChanges());
				rewriter = taintOperator.InsertChanges();
				return rewriter;
			
			case TAINTSINK:
				TaintSinkSchema taintSinkSchema = new TaintSinkSchema();
				root.accept(taintSinkSchema);
				TaintSinkOperator operator = new TaintSinkOperator(rewriter, taintSinkSchema.getFieldNodeChanges(),
						taintSinkSchema.getMethodNodeChanges());
				rewriter = operator.InsertChanges();
				return rewriter;
				
			default: 
				return null;
				
		}
	}
	
	//method to merge the Taint and TaintSink cases into one Taint case
	public ASTRewrite taintExecution(CompilationUnit root, ASTRewrite rewriter) {
		
		
		return rewriter;
	}

	public ASTRewrite tempExecution(CompilationUnit root, ASTRewrite rewriter) {

		TempSchema tempSchema = new TempSchema();
		root.accept(tempSchema);
//		TaintOperator operator = new TaintOperator(rewriter, tempSchema.getTaintNodeChanges());
//		rewriter = operator.InsertChanges();
		return rewriter;
	}

	private void printArgumentError() {
		System.out.println("******* ERROR: INCORRECT USAGE *******");
		System.out.println("Argument List:");
		System.out.println("1. Binaries path");
		System.out.println("2. App Source Code path");
		System.out.println("3. App Name");
		System.out.println("4. Mutants path");
	}

//	private StringBuffer readSourceFile(String filePath) throws FileNotFoundException, IOException {
//		StringBuffer source = new StringBuffer();
//		BufferedReader reader = new BufferedReader(new FileReader(filePath));
//		String line = null;
//
//		while ((line = reader.readLine()) != null) {
//			source.append(line).append("\n");
//		}
//		reader.close();
//		return source;
//	}

	public static void main(String[] args) {
		new Muse().runMuse(args);
	}
}
