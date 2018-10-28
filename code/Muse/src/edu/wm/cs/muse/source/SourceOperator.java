package edu.wm.cs.muse.source;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.utility.OperatorType;
import edu.wm.cs.muse.utility.Utility;
import edu.wm.cs.muse.operators.DataLeak;

/**
 * The SourceOperator class formats and inserts the string data source markers according to the Source 
 * Schema
 * @author yang
 */

public class SourceOperator {
	
	ArrayList<SourceNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;

//	public SourceOperator(ASTRewrite rewriter) {
//		this.rewriter = rewriter;
//	}
	
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
	
	public void insertion(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		// old source code
//		String source = String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
//				Utility.COUNTER_GLOBAL);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(DataLeak.getSource(OperatorType.SOURCE, Utility.COUNTER_GLOBAL), ASTNode.EMPTY_STATEMENT);
		Utility.COUNTER_GLOBAL++;
		listRewrite.insertAt(placeHolder, index, null);
	}

	private void insertVariable(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String variable = String.format("String dataLeAk%d = \"\";", Utility.COUNTER_GLOBAL);
		Statement placeHolder = (Statement)rewriter.createStringPlaceholder(variable, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
	}

}
