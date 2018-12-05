package edu.wm.cs.muse.dataleak.operators;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.TaintNodeChangeContainers;

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
					insertSink(methodChanges.method, methodChanges.index, fieldChanges.fieldBoys,
							methodChanges.propertyDescriptor);
				}
			}
		}

		return rewriter;
	}

	// for sink insertion
	void insertSink(ASTNode node, int index, ArrayList<FieldDeclaration> fieldBoys,
			ChildListPropertyDescriptor nodeProperty) {
		for (int i = 0; i < fieldBoys.size(); i++) {
//			System.out.println("index: " + index);
//			if (index == 5)
//				System.out.println("stop");
//			if (index == 6)
//				System.out.println("stop");

			ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
			int index_equal = fieldBoys.get(i).toString().indexOf("=");
			String tempString = fieldBoys.get(i).toString().substring(15, index_equal);
			tempString = tempString.trim();

//			System.out.println(tempString + "tempstring" + fieldBoys.get(i));
			String sink = String.format("android.util.Log.d(\"leak-%s-%s\", dataLeAk%s);", tempString, index,
					tempString);
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(sink, ASTNode.EMPTY_STATEMENT);

			listRewrite.insertAt(placeHolder, 1, null);
//			System.out.println(String.format("leak-%d-%d", fieldBoys.get(i).toString().substring(16), index));
		}

	}

}
