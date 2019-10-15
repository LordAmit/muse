package edu.wm.cs.muse;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import edu.wm.cs.muse.dataleak.schemas.TaintSchema;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;

/**
 * @author Ian Wolff
 */

public class MuseTaintSchemaTest {

  public enum ComponentType {
		STATICMETHOD, SWITCH, TRY, TRYMETHOD, SWITCHMETHOD, PRIVATE, PROTECTED
	} 
	
	String content = null;
	Muse muse;
	CompilationUnit root;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;
	TaintSchema taintSchema;

  @Test
  public void taint_operation_on_multilevelclass_static() {
    try {      
      prepare_test_files(ComponentType.STATICMETHOD);
      execute_muse_taint();
      ArrayList<SourceNodeChangeContainers> taintChanges = taintSchema.getNodeChanges();

      assertEquals(10, taintChanges.size());
      
    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  @Test
  public void taint_operation_on_multilevelclass_switch() {
    try {      
      prepare_test_files(ComponentType.SWITCH);
      execute_muse_taint();
      ArrayList<SourceNodeChangeContainers> taintChanges = taintSchema.getNodeChanges();

      assertEquals(24, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  @Test
  public void taint_operation_on_multilevelclass_try() {
    try {      
      prepare_test_files(ComponentType.TRY);
      execute_muse_taint();
      ArrayList<SourceNodeChangeContainers> taintChanges = taintSchema.getNodeChanges();

      assertEquals(24, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  @Test
  public void taint_operation_on_multilevelclass_try_method() {
    try {      
      prepare_test_files(ComponentType.TRYMETHOD);
      execute_muse_taint();
      ArrayList<SourceNodeChangeContainers> taintChanges = taintSchema.getNodeChanges();

      assertEquals(24, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  @Test
  public void taint_operation_on_multilevelclass_switch_method() {
    try {      
      prepare_test_files(ComponentType.SWITCHMETHOD);
      execute_muse_taint();
      ArrayList<SourceNodeChangeContainers> taintChanges = taintSchema.getNodeChanges();

      assertEquals(36, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
}
  
  @Test
  public void taint_operation_on_multilevelclass_private_method() {
    try {      
      prepare_test_files(ComponentType.PRIVATE);
      execute_muse_taint();
      ArrayList<SourceNodeChangeContainers> taintChanges = taintSchema.getNodeChanges();

      assertEquals(10, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void taint_operation_on_multilevelclass_protected_method() {
    try {      
      prepare_test_files(ComponentType.PROTECTED);
      execute_muse_taint();
      ArrayList<SourceNodeChangeContainers> taintChanges = taintSchema.getNodeChanges();

      assertEquals(12, taintChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }
  
  private void execute_muse_taint() throws BadLocationException, MalformedTreeException, IOException {
	taintSchema = new TaintSchema();
    rewriter = ASTRewrite.create(root.getAST());
  
    root.accept(taintSchema);
  }

  private void prepare_test_files(ComponentType component) throws FileNotFoundException, IOException {
    Utility.COUNTER_GLOBAL = 0;

    switch (component) {
    case STATICMETHOD:
    	content = FileUtility.readSourceFile("test/input/taint_sample_static_multilevelclass.txt").toString();
    	//content = FileUtility.readSourceFile("test/input/sink_sample_static_method.txt").toString();
      break;

    case SWITCH:
      content = FileUtility.readSourceFile("test/input/taint_sample_switch_multilevelclass.txt").toString();
      break;
      
    case SWITCHMETHOD:
      content = FileUtility.readSourceFile("test/input/taint_sample_switch_method_multilevelclass.txt").toString();
      break;

    case TRY:
      content = FileUtility.readSourceFile("test/input/taint_sample_try_multilevelclass.txt").toString();
      break;
    
    case TRYMETHOD:
      content = FileUtility.readSourceFile("test/input/taint_sample_try_method_multilevelclass.txt").toString();
      break;
      
    case PRIVATE:
        content = FileUtility.readSourceFile("test/input/taint_sample_private_multilevelclass.txt").toString();
        break;
        
    case PROTECTED:
        content = FileUtility.readSourceFile("test/input/taint_sample_protected_multilevelclass.txt").toString();
        break;
    }

    muse = new Muse();
		root = getTestAST(content);
  }

  //Taken directly from muse test
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