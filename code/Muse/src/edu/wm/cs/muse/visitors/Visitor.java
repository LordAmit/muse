package edu.wm.cs.muse.visitors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
//import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
//import org.eclipse.jdt.core.dom.MethodDeclaration;
//import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

abstract public class Visitor extends ASTVisitor {
	ASTRewrite rewriter;

	public Visitor(ASTRewrite rewriter) {

		this.rewriter = rewriter;
	}
	
	//An abstract class made to house the common characteristics of all visitors
	
	abstract protected void insertion(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty);
	
	abstract public boolean visit(TypeDeclaration node);

	abstract public boolean visit(AnonymousClassDeclaration node);
	
	abstract public boolean visit(MethodDeclaration method);
	
}
