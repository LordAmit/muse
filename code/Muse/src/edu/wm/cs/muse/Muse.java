package edu.wm.cs.muse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import edu.wm.cs.muse.mdroid.ASTHelper;
import edu.wm.cs.muse.utility.Arguments;
import edu.wm.cs.muse.utility.FileUtility;
import edu.wm.cs.muse.utility.Utility;
import edu.wm.cs.muse.visitors.ReachabilityVisitor;
import edu.wm.cs.muse.visitors.SinkVisitor;

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
		// Getting arguments
//		String binariesFolder = args[0];
//		String rootPath = args[1];
//		String appName = args[2];
//		String mutantsFolder = args[3];

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
					
					//Creates an abstract syntax tree.
					CompilationUnit root = ASTHelper.getAST(source, Arguments.getBinariesFolder(), Arguments.getRootPath());

					//Creates a new instance for describing manipulations of the given AST.
					rewriter = ASTRewrite.create(root.getAST());
					
					//Accepts the given visitor on a visit of the current node, which is root here.
					root.accept(new ReachabilityVisitor(rewriter));

					Document sourceDoc = new Document(source);
					TextEdit edits = rewriter.rewriteAST(sourceDoc, null);
					edits.apply(sourceDoc);
					FileUtils.writeStringToFile(file, sourceDoc.get(), false);
					rewriter = null;

//					source = readSourceFile(file.getAbsolutePath()).toString();
//					root = ASTHelper.getAST(source, binariesFolder, rootPath);
//					rewriter = ASTRewrite.create(root.getAST());
//					root.accept(new SinkVisitor(rewriter));

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
