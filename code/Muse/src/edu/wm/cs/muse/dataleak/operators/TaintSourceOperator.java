package edu.wm.cs.muse.dataleak.operators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.Placementchecker;
import edu.wm.cs.muse.dataleak.support.TryCatchHandler;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers.INSERTION_TYPE;

/**
 * The TaintSourceOperator class has three methods, InsertChanges, insertInMethodBody,
 * and insertVariable Declaration. InsertChanges allows for the other two methods to
 * be called depending on the Insertion Type called for in SourceNodeChangeContainers.
 * 
 * @author Yang Zhang
 */

public class TaintSourceOperator {

	ArrayList<SourceNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;
	Placementchecker checker = new Placementchecker();
	File temp_file;
	String source_file;
	private TryCatchHandler handler = new TryCatchHandler();

	public TaintSourceOperator(ASTRewrite rewriter, ArrayList<SourceNodeChangeContainers> nodeChanges, String source_file) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
		this.source_file = source_file;
	}

	/**
	 * Modifies the ASTRewrite to swap between insertions based on the nodeChanges
	 * and returns it.
	 * 
	 * @return ASTRewrite modified ASTRewrite
	 */
	public ASTRewrite InsertChanges() {
//		Utility.COUNTER_GLOBAL = 0;

		for (SourceNodeChangeContainers nodeChange : nodeChanges) {

			// if (nodeChange.insertionType == 0)
			if (nodeChange.type == INSERTION_TYPE.METHOD_BODY) {
				try {
					insertInMethodBody(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else {
				try {
					insertVariableDeclaration(nodeChange.node, nodeChange.index, nodeChange.propertyDescriptor);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (!(temp_file ==null)) {
			temp_file.delete();
		}
		return rewriter;
	}

	public void insertInMethodBody(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) throws ClassNotFoundException {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		ASTNode placeHolder;
		if (handler.stringHasThrows(DataLeak.getSource(OperatorType.TAINTSOURCE, Utility.COUNTER_GLOBAL))) {
			placeHolder = handler.addTryCatch((Statement) rewriter.createStringPlaceholder(
					DataLeak.getSource(OperatorType.TAINTSOURCE, Utility.COUNTER_GLOBAL), ASTNode.EMPTY_STATEMENT), rewriter);
		}
		else {
			placeHolder = (Statement) rewriter.createStringPlaceholder(
					DataLeak.getSource(OperatorType.TAINTSOURCE, Utility.COUNTER_GLOBAL), ASTNode.EMPTY_STATEMENT);
		}
		Utility.COUNTER_GLOBAL++;
		listRewrite.insertAt(placeHolder, index, null);
		if (!(listRewrite.getParent().getRoot() instanceof Block)) {
			temp_file = checker.getTempFile((CompilationUnit)listRewrite.getParent().getRoot(), rewriter, source_file);
			try {
				if (!checker.check(temp_file))
					listRewrite.remove(placeHolder,null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void insertVariableDeclaration(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) throws ClassNotFoundException {
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		ASTNode placeHolder;
		String variable = String.format(DataLeak.getVariableDeclaration(OperatorType.TAINTSOURCE), Utility.COUNTER_GLOBAL, 
				Utility.COUNTER_GLOBAL);
		if (handler.stringHasThrows(variable)) {
			placeHolder = handler.addTryCatch((Statement) rewriter.createStringPlaceholder(variable, ASTNode.EMPTY_STATEMENT), rewriter);
		}
		else {
			placeHolder = (Statement) rewriter.createStringPlaceholder(variable, ASTNode.EMPTY_STATEMENT);
		}
		listRewrite.insertAt(placeHolder, index, null);
		if (!(listRewrite.getParent().getRoot() instanceof Block)) {
			temp_file = checker.getTempFile((CompilationUnit)listRewrite.getParent().getRoot(), rewriter, source_file);
			try {
				if (!checker.check(temp_file))
					listRewrite.remove(placeHolder,null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}