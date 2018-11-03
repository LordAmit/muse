package edu.wm.cs.muse.taint;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.operators.DataLeak;
import edu.wm.cs.muse.source.SourceNodeChangeContainers;
import edu.wm.cs.muse.utility.OperatorType;
import edu.wm.cs.muse.utility.Utility;

/**
 * The TaintOperator class has two methods, insertDeclaration and insertSource,
 * that will create the appropriate object when given a class or method by the
 * TaintSchema.
 * 
 * @author yang
 */

public class TaintOperator {

	ArrayList<TaintNodeChangeContainers> taintNodeChanges;
	ArrayList<SourceNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;

	public TaintOperator(ASTRewrite rewriter, ArrayList<SourceNodeChangeContainers> nodeChanges,
			ArrayList<TaintNodeChangeContainers> taintNodeChanges) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
		this.taintNodeChanges = taintNodeChanges;
	}

	/**
	 * Modifies the ASTRewrite to insert a taint and then returns it.
	 * 
	 * @return
	 */
	public ASTRewrite InsertChanges() {

//		for (TaintNodeChangeContainers taintNodeChange : taintNodeChanges) {
//		
//			ListRewrite listRewrite = rewriter.getListRewrite(taintNodeChange.node, taintNodeChange.propertyDescriptor);
//			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(DataLeak.getSource(OperatorType.TAINT, Utility.COUNTER_GLOBAL), ASTNode.EMPTY_STATEMENT);
//			Utility.COUNTER_GLOBAL++;
//			listRewrite.insertAt(placeHolder, taintNodeChange.index, null);
//			
//		}
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
		String source = String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();", index);
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
		Utility.COUNTER_GLOBAL += 1;
	}

}
