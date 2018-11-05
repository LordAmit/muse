package edu.wm.cs.muse.taint;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.operators.DataLeak;
import edu.wm.cs.muse.source.SourceNodeChangeContainers;
import edu.wm.cs.muse.utility.OperatorType;
import edu.wm.cs.muse.utility.Utility;

/**
 * The TaintOperator class has two methods, insertDeclaration and insertSource,
 * that will create the appropriate object when given a class or method by the
 * TaintSchema.
 * 
 * @author yang, amit
 */

public class TaintOperator {

//	ArrayList<TaintNodeChangeContainers> taintNodeChanges;
	ArrayList<SourceNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;

	public TaintOperator(ASTRewrite rewriter, ArrayList<SourceNodeChangeContainers> nodeChanges) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
		
		//need array to keep track of sink data as changes are made with source and declaration.
//		taintNodeChanges = new ArrayList<TaintNodeChangeContainers>();
	}

	/**
	 * Modifies the ASTRewrite to insert a taint and then returns it.
	 * 
	 * @return
	 */
	public ASTRewrite InsertChanges() {

//		for (TaintNodeChangeContainers taintNodeChange : taintNodeChanges) {
//		
//			ListRewrite listRewrite = rewriter.getListRewrite(taintNodeChange.node, taintNodeChange.propertyDescriptor);
//			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(DataLeak.getSource(OperatorType.TAINT, Utility.COUNTER_GLOBAL), ASTNode.EMPTY_STATEMENT);
//			Utility.COUNTER_GLOBAL++;
//			listRewrite.insertAt(placeHolder, taintNodeChange.index, null);
//			
//		}
		for (SourceNodeChangeContainers nodeChange : nodeChanges) {
			if (nodeChange.insertionType == 0) {
				insertion(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);
			} else {
				insertVariable(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);
			}
		}
		
		return rewriter;

	}

	// for inserting source inside methodBody
	public void insertion(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String source = String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();", index);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(source, ASTNode.EMPTY_STATEMENT);
		//listRewrite.insertAt(placeHolder, index, null);
		listRewrite.insertAt(placeHolder, 0, null);
	}

	// for declaration.
	private void insertVariable(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String variable = String.format("String dataLeAk%d = \"\";", Utility.COUNTER_GLOBAL);
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(variable, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
		Utility.COUNTER_GLOBAL += 1;
	}
	
	//sink must only be done after both declaration and source inserts, since it relies on both the index
	//and the global counter.
	
	//package the stuff you need from declaration, then when source comes along you send it to a container
	//which is then used by another function to do sink.
	//sink is basically declaration's number of times and utility counter + source's placement
	
	
//	public ASTRewrite InsertSinkChanges() {
//
//		for (SourceNodeChangeContainers nodeChange : nodeChanges) {
//			int i = 0;
//			if (nodeChange.insertionType == 0) {
//				insertSink(nodeChange.node, nodeChange.index, global.get(i), nodeChange.propertyDescriptor);
//				i++;
//			}
//		}
//		return rewriter;
//
//	}
//	
//	// for sink insertion
//	void insertSink(ASTNode node, int index, int global_counter, ChildListPropertyDescriptor nodeProperty) {
//		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
//		String sink = String.format("android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);", global_counter, index,
//				Utility.COUNTER_GLOBAL);
//		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(sink, ASTNode.EMPTY_STATEMENT);
//		listRewrite.insertAt(placeHolder, index, null);
//		System.out.println(String.format("leak-%d-%d", global_counter, index));
//	}

}
