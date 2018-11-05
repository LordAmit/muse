package edu.wm.cs.muse.taint;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import edu.wm.cs.muse.sink.SinkNodeChangeContainers;

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
		
		return rewriter;
	}

	// for inserting sink inside methodBody
	public void insertion(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {

	}

}
