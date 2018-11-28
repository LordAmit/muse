package edu.wm.cs.muse.dataleak.operators;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;

/**
 * The TaintOperator class has two methods, insertDeclaration and insertSource,
 * that will create the appropriate object when given a class or method by the
 * TaintSchema.
 * 
 * @author yang, amit
 */

public class TaintOperator {

	ArrayList<SourceNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;

	public TaintOperator(ASTRewrite rewriter, ArrayList<SourceNodeChangeContainers> nodeChanges) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
	}

	/**
	 * Modifies the ASTRewrite to insert a taint declaration and a source string
	 * in the method, then returns it.
	 * 
	 * @return
	 */
	public ASTRewrite InsertChanges() {

		for (SourceNodeChangeContainers nodeChange : nodeChanges) {
			if (nodeChange.insertionType == 0) {
				insertion(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);
			} else {
				insertVariable(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);
			}
		}
		
		return rewriter;

	}

	// for inserting source inside methodBody
	public void insertion(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String source = String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();", Utility.COUNTER_GLOBAL - 1);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(source, ASTNode.EMPTY_STATEMENT);
		//listRewrite.insertAt(placeHolder, index, null);
		listRewrite.insertAt(placeHolder, 0, null);
	}

	// for declaration.
	private void insertVariable(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String variable = String.format("String dataLeAk%d = \"\";", Utility.COUNTER_GLOBAL);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(variable, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
		Utility.COUNTER_GLOBAL++;
	}
	
}
