package edu.wm.cs.muse.taint;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import edu.wm.cs.muse.source.SourceOperator;;

public class TaintSchema {
	
	/**
	 * The TaintSchema will traverse the nodes of the AST, and when it reaches a method, it will locate
	 * the class(es) the method is in and insert a declaration in the class(es) and a source in the method.
	 * Once the schema has completed its course, it will call SinkOperator to insert sinks that reflect
	 * the location of the declarations.
	 * @author yang
	 */
	
	//ASTRewrite rewriter;

	public TaintSchema(ASTRewrite rewriter) {
		//this.rewriter = rewriter;
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(MethodDeclaration method) {
		
		Class<? extends ASTNode> temp = method.getClass();
		
		//SourceOperator sourceOp = new SourceOperator(rewriter);
		Block node = method.getBody();
		if (node == null) {
			return true;
		}
		int index = 0;
//		for (Object obj : node.statements()) {
//			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
//				index++;
//			}
//		}
		ASTNode n = node.getParent();
		boolean inAnonymousClass = false;
		boolean inStaticContext = false;
		
		while (n != null && !inAnonymousClass && !inStaticContext) {
			
			if (n.getNodeType() == ASTNode.METHOD_DECLARATION) {
					if (n.getClass().getDeclaringClass() == null) {
						temp = n.getClass();
						//we've reached the very outermost class
						//use taintOperator to insert at n.getClass()
					}
					else {
						//recursively find and insert with taintOperator every outer class
						for (int num = index; num > 0; num--) {
							if (temp.getDeclaringClass() != null) {
								temp = (Class<? extends ASTNode>) temp.getDeclaringClass();
								//unsure if this is possible ^
								//use taintOperator to insert at temp.getClass()
							}
							
							else {
								//use taintOperator to insert at temp.getClass()
							}							
						}
					}
				//sourceOp.insertion(node, index, Block.STATEMENTS_PROPERTY);
				index++;
			}
			n = n.getParent();
		}
		return true;
	}

}
