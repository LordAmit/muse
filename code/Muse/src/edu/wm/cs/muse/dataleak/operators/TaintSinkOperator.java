package edu.wm.cs.muse.dataleak.operators;

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;

/**
 * The SinkOperator class formats and inserts the string-based sink markers according to the Sink Schema
 * @author Yang Zhang
 */

public class TaintSinkOperator {
	ArrayList<SinkNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;
	HashMap<Integer, Integer> repeatCounts = new HashMap<Integer, Integer>();

	public TaintSinkOperator(ASTRewrite rewriter) {
		this.rewriter = rewriter;
	}
	
	public TaintSinkOperator(ASTRewrite rewriter, ArrayList<SinkNodeChangeContainers> nodeChanges) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
	}
	
	/**
	 * Modifies the ASTRewrite to swap between insertions based on the nodeChanges and returns it.
	 * @return
	 */
	public ASTRewrite InsertChanges() {

		for (SinkNodeChangeContainers nodeChange : nodeChanges) {
		
			if (nodeChange.insertionType == 0)
			{
				insertSink(nodeChange.node, nodeChange.index, nodeChange.count, nodeChange.propertyDescriptor, nodeChange.method);
			}
			
			else
			{
				insertSource(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor, nodeChange.count);
			}
		}
		return rewriter;
	}
	
	void insertSink(ASTNode node, int index, int count, ChildListPropertyDescriptor nodeProperty,
			ASTNode method) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		int cur = repeatCounts.containsKey(count) ? repeatCounts.get(count) : -1;
		repeatCounts.put(count, cur + 1);
		
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(DataLeak.getSink(OperatorType.SINK, count, repeatCounts.get(count)), ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
		String methodName = ((MethodDeclaration) method).getName().toString();
		String className = "";
		method = method.getParent();
		while (method != null) {
			if (method.getNodeType() == ASTNode.TYPE_DECLARATION) {
				className = ((TypeDeclaration) method).getName().toString();
				break;
			} else if (method.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
				className = "1";
				break;
			}
			method = method.getParent();
		}
		System.out.println(String.format("leak-%d-%d: %s.%s", count, repeatCounts.get(count), className, methodName));
	}

	void insertSource(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty, int count) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(DataLeak.getSource(OperatorType.SINK, count), ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
	}

}
