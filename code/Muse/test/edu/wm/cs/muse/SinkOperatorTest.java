package edu.wm.cs.muse;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;

import edu.wm.cs.muse.dataleak.operators.SinkOperator;
import edu.wm.cs.muse.dataleak.operators.TaintOperator;
import edu.wm.cs.muse.dataleak.schemas.SinkSchema;
import edu.wm.cs.muse.dataleak.schemas.TaintSchema;
import edu.wm.cs.muse.dataleak.support.Arguments;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers.INSERTION_TYPE;
import edu.wm.cs.muse.mdroid.ASTHelper;

/**
 * 
 * @author Scott Murphy
 * 
 * Test class to test functonality of the Sink Operator
 */

public class SinkOperatorTest {

	private ASTRewrite rewriter;
	private String source;
	private CompilationUnit root;
	private MethodDeclaration method;
	private Block node;
	private ArrayList<SinkNodeChangeContainers> nodeChanges;
	private SinkOperator sinkOperator;
	private SinkSchema sinkSchema;
	private SinkNodeChangeContainers container;
  

  @Before
  public void init() {
    try {
      source = FileUtility.readSourceFile("test/input/sample_helloWorld.txt").toString();
    }
    catch (IOException e) {
      System.err.println(String.format("ERROR PROCESSING \"%s\": %s", "test/input/sample_multilevelclass.txt", e.getMessage()));
			return;
    }

    nodeChanges = new ArrayList<SinkNodeChangeContainers>();
    root = ASTHelper.getAST(source, Arguments.getBinariesFolder(), Arguments.getRootPath());
	rewriter = ASTRewrite.create(root.getAST());
	sinkSchema = new SinkSchema();
	root.accept(sinkSchema);
  }
  
  /*
  @Test
  public void insertion_at_null_declaration_node() {
		//create a null container with declaration insertion type
		SinkNodeChangeContainers nullDeclarationNode = new SinkNodeChangeContainers(node, 0, 0, Block.STATEMENTS_PROPERTY, method, 2);
		nodeChanges.add(nullDeclarationNode);
		sinkOperator = new SinkOperator(rewriter, nodeChanges);
		String output = sinkOperator.InsertChanges().toString();
		System.out.println(output);
		//check that the returned rewriter is  equal to the original rewriter
		//assertEquals(rewriter.toString(),output);
	}
	*/
  
  @Test
	public void insert_declaration_nodeChange() {
		nodeChanges.add(createNodeChanges("int methodA(){", 0));	
		sinkOperator = new SinkOperator(rewriter, nodeChanges);
		String output = sinkOperator.InsertChanges().toString();
		//accesses the first output line where an insertion should occur
		String outputAtInsertion = output.split("\\n")[4];
		
		//first insertion point should reflect insertion
		String expectedOutput = "	 [inserted: ;";
		assertEquals(expectedOutput, outputAtInsertion);
	}



  	/**
	 * This method creates a single node change to be passed to the operator. 
	 * 
	 * @param inputString: the string that will passed to the parser as a source
	 * @param insertionType: either METHOD_BODY or DECLARATION description
	 * @return a new SourceNodeChangeContainers containing the block and insertion type
	 */
	private SinkNodeChangeContainers createNodeChanges(String inputString, int insertionType) {
		AST testAST = root.getAST();
		//create a temporary parser to generate an AST from the test input
	    ASTParser tempParser = ASTParser.newParser(AST.JLS8);
	    //set the content for the method body from the test input
	    tempParser.setSource(inputString.toCharArray());
	    tempParser.setKind(ASTParser.K_STATEMENTS);
	    tempParser.setResolveBindings(true);
	    tempParser.setBindingsRecovery(true);
	    ASTNode block = tempParser.createAST(new NullProgressMonitor());
	    block = ASTNode.copySubtree(testAST, block);
	    //create a new container with the test input and insertion type
		SinkNodeChangeContainers newContainer = new SinkNodeChangeContainers(block, 0, 0, Block.STATEMENTS_PROPERTY, block, insertionType);
		System.out.println(block.toString());
		return newContainer;
	}

}