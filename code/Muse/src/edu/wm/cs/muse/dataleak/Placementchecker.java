package edu.wm.cs.muse.dataleak;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import edu.wm.cs.muse.Muse;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.JavaSourceFromString;
import edu.wm.cs.muse.mdroid.ASTHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import java.net.URI;

import java.util.Arrays;

/**
 * Placement Checker that checks to see if placement of dataleak occurs in a valid place
 * @author Jeff Petit-Freres
 *
 */
public class Placementchecker {
	String source_code;
	String source_file;
	public Placementchecker(/*String source_code*/) {
		//this.source_code = source_code;
	}

	
	public File getTempFile(CompilationUnit astRoot, ASTRewrite rewriter, String source_file_name) {
		//Muse muse = new Muse();
		File temp_file;
		
		try {
			temp_file = new File("temp_file.java");

			//System.out.println("This is something like creatin a file: "+FileUtility.readSourceFile(source_file_name));
			this.source_file = FileUtility.readSourceFile(source_file_name).toString();
			
			try {
				if (source_file=="") {System.out.println("its null");}
		
			    tempFileWriter(astRoot, rewriter, source_file, temp_file);
				return temp_file;
			} catch (MalformedTreeException e) {
				
				e.printStackTrace();
			} catch (BadLocationException e) {
				
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		return null;
		
	}
  public Boolean check(File temp_file) throws IOException {
	  //have user set java_home variable
	
	System.setProperty("java.home",System.getenv("JAVA_HOME"));
	
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
    String source_code= FileUtils.readFileToString(temp_file);

    JavaFileObject file = new JavaSourceFromString("sample_helloWorld.txt",source_code);
    Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
    CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);
    boolean success = task.call();
    for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
   
    	System.out.println("Code: "+ diagnostic.getCode() + " with message: " + diagnostic.getMessage(null));
    	
    	switch (diagnostic.getCode()){
    		case "compiler.err.already.defined":
    			System.out.println("WARNING DATA LEAK IS ALREADY DEFINED");
    			//return false;
    		//case 
    			
    	}
    }
 
    return true;

 }
  public ASTRewrite tempFileWriter(CompilationUnit root, ASTRewrite rewriter, String source, File file)
			throws MalformedTreeException, BadLocationException, IOException {
		File temp_file = new File("output.txt");
		
		// Applies the edit tree rooted by this edit to the given document.
		// edits.apply(sourceDoc);
	
		Document tempDocument = new Document(source);

		TextEdit tempEdits = rewriter.rewriteAST(tempDocument, null);
		tempEdits.apply(tempDocument);
		FileUtils.writeStringToFile(temp_file, tempDocument.get(), false);
		FileUtils.writeStringToFile(file, tempDocument.get(), false);

		// TaintSinkSchema
		source = FileUtility.readSourceFile(temp_file.getAbsolutePath()).toString();
		rewriter = null;
		root = ASTHelper.getAST(tempDocument.get(), Arguments.getBinariesFolder(), "test/temp/");
		return rewriter = ASTRewrite.create(root.getAST());
	}
}


    




