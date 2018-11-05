package edu.wm.cs.muse.taint;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.sink.SinkNodeChangeContainers;
import edu.wm.cs.muse.utility.Utility;

public class TaintSinkOperator {
	
	ArrayList<TaintNodeChangeContainers> fieldChanges;
	ArrayList<SinkNodeChangeContainers> methodChanges;
	ASTRewrite rewriter;

	public TaintSinkOperator(ASTRewrite rewriter, ArrayList<TaintNodeChangeContainers> fieldChanges, ArrayList<SinkNodeChangeContainers> methodChanges) {
		this.rewriter = rewriter;
		this.fieldChanges = fieldChanges;
		this.methodChanges = methodChanges;
		
	}
	
	/**
	 * Modifies compares the parent classes between the two nodeChangeContainers, then
	 * inserts the appropriate number of sinks into the correct methods according to the
	 * number of fields.
	 * 
	 * @return
	 */
	public ASTRewrite InsertChanges() {
		
		for (TaintNodeChangeContainers fieldChanges : fieldChanges) {
			for (SinkNodeChangeContainers methodChanges: methodChanges) {
				if (methodChanges.node == fieldChanges.node)
				{
					insertSink(methodChanges.node, methodChanges.index, fieldChanges.fieldBoys, methodChanges.propertyDescriptor);
				}
			}
		}
		
		return rewriter;
	}

	// for sink insertion
	void insertSink(ASTNode node, int index, ArrayList<FieldDeclaration> fieldBoys, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		for (int i = 0; i < fieldBoys.size(); i++)
		{
			String sink = String.format("android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);", fieldBoys.get(i), index,
					Utility.COUNTER_GLOBAL);
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(sink, ASTNode.EMPTY_STATEMENT);
			listRewrite.insertAt(placeHolder, index, null);
			System.out.println(String.format("leak-%d-%d", fieldBoys.get(i), index));
		}

	}

}
