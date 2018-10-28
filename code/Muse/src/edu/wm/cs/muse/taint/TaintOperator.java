package edu.wm.cs.muse.taint;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.operators.DataLeak;
import edu.wm.cs.muse.utility.OperatorType;
import edu.wm.cs.muse.utility.Utility;

/**
 * The TaintOperator class has two methods, insertDeclaration and insertSource, that will create the
 * appropriate object when given a class or method by the TaintSchema.
 * @author yang
 */

public class TaintOperator {
	
	protected void insertionDeclaration(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {

	}

	
	protected void insertSource() {
		
	}

}
