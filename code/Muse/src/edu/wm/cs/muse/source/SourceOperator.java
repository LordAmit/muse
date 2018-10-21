package edu.wm.cs.muse.source;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.utility.Utility;

/**
 * The SourceOperator class formats and inserts the string data source markers according to the Source 
 * Schema
 * @author yang
 */

public class SourceOperator {
	
	ArrayList<SourceNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;

	public SourceOperator(ASTRewrite rewriter) {
		this.rewriter = rewriter;
	}
	
	public SourceOperator(ASTRewrite rewriter, ArrayList<SourceNodeChangeContainers> nodeChanges) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
	}
	
	/**
	 * Modifies the ASTRewrite to swap between insertions based on the nodeChanges and returns it.
	 * @return
	 */
		
	public ASTRewrite InsertChanges() {

		for (SourceNodeChangeContainers nodeChange : nodeChanges) {
			
			if (nodeChange.insertionType == 0)
			{
				insertion(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);
			}
				
			else
			{
				insertVariable(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);
			}
		}
		return rewriter;
	}
	
	protected void insertion(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String source = String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
				Utility.COUNTER_GLOBAL);
		Utility.COUNTER_GLOBAL++;
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(source, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
	}

	private void insertVariable(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String variable = String.format("String dataLeAk%d = \"\";", Utility.COUNTER_GLOBAL);
		Statement placeHolder = (Statement)rewriter.createStringPlaceholder(variable, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
	}

}
