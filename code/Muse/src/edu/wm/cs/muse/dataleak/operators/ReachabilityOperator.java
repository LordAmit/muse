package edu.wm.cs.muse.dataleak.operators;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.ReachabilityNodeChangeContainers;

/**
 * Operates on the list of nodes coming from ReachabilitySchema
 * 
 * @author Amit Seal Ami
 *
 */
public class ReachabilityOperator {
	ArrayList<ReachabilityNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;
	private TryCatchHandler handler = new TryCatchHandler();

	public ReachabilityOperator(ASTRewrite rewriter, ArrayList<ReachabilityNodeChangeContainers> nodeChanges) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
	}

	/**
	 * modifies the ASTRewrite based on the nodeChanges and returns it.
	 * 
	 * @return ASTRewrite modified ASTRewrite
	 */
	public ASTRewrite InsertChanges() {
		for (int i = 0; i < nodeChanges.size(); i++) {

			ReachabilityNodeChangeContainers nodeChange = nodeChanges.get(i);

			System.out.println(String.format(nodeChange.changedSource, Utility.COUNTER_GLOBAL));

			Statement placeHolder = (Statement) rewriter
					.createStringPlaceholder(DataLeak.getLeak(OperatorType.REACHABILITY, Utility.COUNTER_GLOBAL), ASTNode.EMPTY_STATEMENT);
			
			
			/*
			 * Uses the rewriter to create an AST for the SinkSchema to utilize Then
			 * creates a new instance to manipulate the AST The root node then accepts the
			 * schema visitor on the visit The rewriter implements the specified changes
			 * made by the sink operator
			 */
			try {
			if (handler.stringHasThrows(DataLeak.getLeak(OperatorType.REACHABILITY, Utility.COUNTER_GLOBAL))) {
				TryStatement tryPlaceHolder = handler.addTryCatch(placeHolder);
				ListRewrite listRewrite = rewriter.getListRewrite(nodeChange.node, nodeChange.propertyDescriptor);
				listRewrite.insertAt(tryPlaceHolder, nodeChange.index, null);
			}
			else {
				ListRewrite listRewrite = rewriter.getListRewrite(nodeChange.node, nodeChange.propertyDescriptor);
				listRewrite.insertAt(placeHolder, nodeChange.index, null);
			}
			}
			catch (ClassNotFoundException e) {
				
			}
			Utility.COUNTER_GLOBAL++;
			
		}
		return rewriter;
	}
}
