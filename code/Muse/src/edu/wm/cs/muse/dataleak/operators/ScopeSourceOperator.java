package edu.wm.cs.muse.dataleak.operators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import edu.wm.cs.muse.Muse;

import edu.wm.cs.muse.dataleak.Placementchecker;
import edu.wm.cs.muse.dataleak.support.FileUtility;
import edu.wm.cs.muse.dataleak.support.SchemaOperatorUtility;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers.INSERTION_TYPE;

/**
 * The TaintOperator class has two methods, insertDeclaration and insertSource,
 * that will create the appropriate object when given a class or method by the
 * TaintSchema.
 * 
 * @author Yang Zhang, Amit Seal Ami
 */

public class ScopeSourceOperator {
	Placementchecker checker = new Placementchecker();
	File temp_file;
	String source_file;
	

	ArrayList<SourceNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;

	public ScopeSourceOperator(ASTRewrite rewriter, ArrayList<SourceNodeChangeContainers> nodeChanges, String source_file) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
		this.source_file = source_file;
	}

	/**
	 * Modifies the ASTRewrite to insert a taint declaration and a source string in
	 * the method, then returns it.
	 * 
	 * @return
	 */
	public ASTRewrite InsertChanges() {

		for (SourceNodeChangeContainers nodeChange : nodeChanges) {
			// if (nodeChange.insertionType == 0 && nodeChange.node != null ) {
			if (nodeChange.type == INSERTION_TYPE.METHOD_BODY && nodeChange.node != null) {
				insertInMethodBody((Block) nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);

			} else {
				if (nodeChange.node != null) {
					insertVariableDeclaration(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);
					
				}
			}

		}

		
		return rewriter;

	}

	// for inserting source inside methodBody
	public void insertInMethodBody(Block node, int index, ChildListPropertyDescriptor nodeProperty) {
		int placement = 0;
		for (Object obj : node.statements()) {
			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
				placement++;
			}
		}
		int identifier = Utility.COUNTER_GLOBAL - 1;

		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String source = String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
				identifier);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(source, ASTNode.EMPTY_STATEMENT);
		// listRewrite.insertAt(placeHolder, index, null);
		listRewrite.insertAt(placeHolder, placement, null);
//		temp_file = checker.getTempFile((CompilationUnit)listRewrite.getParent().getRoot(), rewriter, source_file);
//		try {
//			if (!checker.check(temp_file))
//				listRewrite.remove(placeHolder,null);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
			

	}

	// for declaration.
	private void insertVariableDeclaration(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {

		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String variable = String.format("String dataLeAk%d = \"%d\";", Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(variable, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
//		temp_file = checker.getTempFile((CompilationUnit)listRewrite.getParent().getRoot(), rewriter, source_file);
//		try {
//			if (!checker.check(temp_file)) {
//				System.out.println("Removing");
//				listRewrite.remove(placeHolder,null);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		Utility.COUNTER_GLOBAL++;


	}

}
