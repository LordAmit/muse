package edu.wm.cs.muse;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class TempSchema extends ASTVisitor {

	@Override
	public boolean visit(MethodDeclaration node) {
//		System.out.println(node.toString());
		System.out.println(node.getName());
//		System.out.println(node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION);
		return true;
	}

}
