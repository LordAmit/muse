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
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import edu.wm.cs.muse.dataleak.schemas.SourceSchema;
import edu.wm.cs.muse.dataleak.schemas.SinkSchema;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;

/**
 * 
 * @author Scott Murphy
 * 
 * Class to test the functionality of Sink Schema
 * All sink schemas run with 3 data leaks already in place
 */

public class MuseSinkSchemaTest {

  public enum ComponentType {
		STATICMETHOD, SWITCH, TRY, TRYMETHOD, SWITCHMETHOD
	} 
	
	String content = null;
	Muse muse;
	CompilationUnit root;
	ASTRewrite rewriter;
	TextEdit edits;
	File processedOutput;
	SinkSchema sinkSchema;


  @Test
  public void sink_operation_on_hello_world_static() {
    try {      
      prepare_test_files(ComponentType.STATICMETHOD);
      execute_muse_sink();
      ArrayList<SinkNodeChangeContainers> sinkChanges = sinkSchema.getNodeChanges();

      assertEquals(6, sinkChanges.size());
      
    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  @Test
  public void sink_operation_on_hello_world_switch() {
    try {      
      prepare_test_files(ComponentType.SWITCH);
      execute_muse_sink();
      ArrayList<SinkNodeChangeContainers> sinkChanges = sinkSchema.getNodeChanges();

      assertEquals(9, sinkChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  @Test
  public void sink_operation_on_hello_world_try() {
    try {      
      prepare_test_files(ComponentType.TRY);
      execute_muse_sink();
      ArrayList<SinkNodeChangeContainers> sinkChanges = sinkSchema.getNodeChanges();

      assertEquals(9, sinkChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  @Test
  public void sink_operation_on_hello_world_try_method() {
    try {      
      prepare_test_files(ComponentType.TRYMETHOD);
      execute_muse_sink();
      ArrayList<SinkNodeChangeContainers> sinkChanges = sinkSchema.getNodeChanges();

      assertEquals(12, sinkChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  @Test
  public void sink_operation_on_hello_world_switch_method() {
    try {      
      prepare_test_files(ComponentType.SWITCHMETHOD);
      execute_muse_sink();
      ArrayList<SinkNodeChangeContainers> sinkChanges = sinkSchema.getNodeChanges();

      assertEquals(15, sinkChanges.size());

    } catch (IOException e) {
      e.printStackTrace();

    } catch (MalformedTreeException e) {
      e.printStackTrace();

    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  
  }

  private void execute_muse_sink() throws BadLocationException, MalformedTreeException, IOException {
    sinkSchema = new SinkSchema();
    rewriter = ASTRewrite.create(root.getAST());
  
    root.accept(sinkSchema);
  }

  private void prepare_test_files(ComponentType component) throws FileNotFoundException, IOException {
    Utility.COUNTER_GLOBAL = 0;

    switch (component) {
    case STATICMETHOD:
      content = FileUtility.readSourceFile("test/input/sink_sample_static_method.txt").toString();
      break;

    case SWITCH:
      content = FileUtility.readSourceFile("test/input/sink_sample_switch.txt").toString();
      break;
      
    case SWITCHMETHOD:
      content = FileUtility.readSourceFile("test/input/sink_sample_switch_method.txt").toString();
      break;

    case TRY:
      content = FileUtility.readSourceFile("test/input/sink_sample_try.txt").toString();
      break;
    
    case TRYMETHOD:
      content = FileUtility.readSourceFile("test/input/sink_sample_try_method.txt").toString();
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