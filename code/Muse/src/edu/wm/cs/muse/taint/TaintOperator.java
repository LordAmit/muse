package edu.wm.cs.muse.taint;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.operators.DataLeak;
import edu.wm.cs.muse.reachability.ReachabilityNodeChangeContainers;
import edu.wm.cs.muse.sink.SinkNodeChangeContainers;
import edu.wm.cs.muse.source.SourceNodeChangeContainers;
import edu.wm.cs.muse.utility.OperatorType;
import edu.wm.cs.muse.utility.Utility;

/**
 * The TaintOperator class has two methods, insertDeclaration and insertSource, that will create the
 * appropriate object when given a class or method by the TaintSchema.
 * @author yang
 */

public class TaintOperator {
	
	ArrayList<TaintNodeChangeContainers> taintNodeChanges;
	ASTRewrite rewriter;
	
	public TaintOperator(ASTRewrite rewriter, ArrayList<TaintNodeChangeContainers> nodeChanges) {
		this.rewriter = rewriter;
		this.taintNodeChanges = nodeChanges;
	}
	
	/**
	 * Modifies the ASTRewrite to insert a taint and then returns it.
	 * @return
	 */
	public ASTRewrite InsertChanges() {

		for (TaintNodeChangeContainers taintNodeChange : taintNodeChanges) {
		
			ListRewrite listRewrite = rewriter.getListRewrite(taintNodeChange.node, taintNodeChange.propertyDescriptor);
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(DataLeak.getSource(OperatorType.TAINT, Utility.COUNTER_GLOBAL), ASTNode.EMPTY_STATEMENT);
			Utility.COUNTER_GLOBAL++;
			listRewrite.insertAt(placeHolder, taintNodeChange.index, null);
			
		}
		return rewriter;
	}

}
