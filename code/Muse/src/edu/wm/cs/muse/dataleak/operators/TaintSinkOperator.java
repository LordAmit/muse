package edu.wm.cs.muse.dataleak.operators;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.support.SchemaOperatorUtility;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.TaintNodeChangeContainers;

/**
 * The TaintSinkOperator class will insert the sink aspect of the taint mutation
 * scheme on the modified file through comparisons of the parent classes between
 * the field and methods.
 * 
 * @author Yang Zhang, Amit Seal Ami
 */
public class TaintSinkOperator {

	ArrayList<TaintNodeChangeContainers> fieldChanges;
	ArrayList<SinkNodeChangeContainers> methodChanges;
	ASTRewrite rewriter;

	public TaintSinkOperator(ASTRewrite rewriter, ArrayList<TaintNodeChangeContainers> fieldChanges,
			ArrayList<SinkNodeChangeContainers> methodChanges) {
		this.rewriter = rewriter;
		this.fieldChanges = fieldChanges;
		this.methodChanges = methodChanges;

	}

	/**
	 * Modifies compares the parent classes between the two nodeChangeContainers,
	 * then inserts the appropriate number of sinks into the correct methods
	 * according to the number of fields.
	 * 
	 * @return
	 */
	public ASTRewrite InsertChanges() {

		for (TaintNodeChangeContainers fieldChanges : fieldChanges) {
			for (SinkNodeChangeContainers methodChanges : methodChanges) {
				if (methodChanges.node == fieldChanges.node) {
					insertSink((Block) methodChanges.method, methodChanges.index, fieldChanges.fieldBoys,
							methodChanges.propertyDescriptor);
				}
			}
		}

		return rewriter;
	}

	// for sink insertion
	void insertSink(Block node, int index, ArrayList<FieldDeclaration> fieldBoys,
			ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = null;
		if (node == null)
			return;
		for (int i = 0; i < fieldBoys.size(); i++) {
			try {
				listRewrite = rewriter.getListRewrite(node, nodeProperty);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			int index_equal = fieldBoys.get(i).toString().indexOf("=");
			String tempString = fieldBoys.get(i).toString().substring(15, index_equal);
			tempString = tempString.trim();
			MethodDeclaration methodNode = (MethodDeclaration) node.getParent();
			System.out.println(String.format("leak-%s-%s: %s.%s", tempString, index, SchemaOperatorUtility.getClassNameOfMethod(node),methodNode.getName()));
			String sink = String.format("android.util.Log.d(\"leak-%s-%s\", dataLeAk%s);", tempString, index,
					tempString);
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(sink, ASTNode.EMPTY_STATEMENT);
			int placement = 0;
			for (Object obj : node.statements()) {
				if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
					placement++;
				}
			}
			listRewrite.insertAt(placeHolder, placement, null);
		}

	}

}
