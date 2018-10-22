package edu.wm.cs.muse.reachability;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;

import edu.wm.cs.muse.utility.Utility;

/**
 * This operates on the list of nodes coming from ReachabilitySchema
 * 
 * @author amit
 *
 */
public class ReachabilityOperator {
	ArrayList<ReachabilityNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;

	public ReachabilityOperator(ASTRewrite rewriter, ArrayList<ReachabilityNodeChangeContainers> nodeChanges) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
	}

	/**
	 * modifies the ASTRewrite based on the nodeChanges and returns it.
	 * 
	 * @return
	 */
	public ASTRewrite InsertChanges() {
		for (int i = 0; i < nodeChanges.size(); i++) {

			ReachabilityNodeChangeContainers nodeChange = nodeChanges.get(i);

			String source = "String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";
			String sink = "Object throwawayLeAk%d = android.util.Log.d(\"leak-%d\", dataLeAk%d);";
			String leak = String.format(source, Utility.COUNTER_GLOBAL) + "\n"
					+ String.format(sink, Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL);
			
			System.out.println(String.format(nodeChange.changedSource, Utility.COUNTER_GLOBAL));

			Utility.COUNTER_GLOBAL++;

			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(leak, ASTNode.EMPTY_STATEMENT);

			ListRewrite listRewrite = rewriter.getListRewrite(nodeChange.node, nodeChange.propertyDescriptor);
			listRewrite.insertAt(placeHolder, nodeChange.index, null);
		}
		return rewriter;
	}
}
