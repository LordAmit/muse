package edu.wm.cs.muse.taint;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import edu.wm.cs.muse.source.SourceNodeChangeContainers;
import edu.wm.cs.muse.source.SourceOperator;
import edu.wm.cs.muse.utility.Utility;;

public class TaintSchema {
	
	/**
	 * The TaintSchema will traverse the nodes of the AST, and when it reaches a method, it will locate
	 * the class(es) the method is in and insert a declaration in the class(es) and a source in the method.
	 * Once the schema has completed its course, it will call SinkOperator to insert sinks that reflect
	 * the location of the declarations.
	 * @author yang
	 */

	private ArrayList<TaintNodeChangeContainers> taintNodeChanges;
	private ArrayList<SourceNodeChangeContainers> nodeChanges;

	/**
	 * TaintSchema should use the source node container array since they occur at the same time and 
	 * use the same parameters
	 */
	public TaintSchema() {
		taintNodeChanges = new ArrayList<TaintNodeChangeContainers>();
		nodeChanges = new ArrayList<SourceNodeChangeContainers>();
	}

	public ArrayList<TaintNodeChangeContainers> getTaintNodeChanges1(){
		return this.taintNodeChanges;
	};
	
	public ArrayList<SourceNodeChangeContainers> getNodeChanges(){
		return this.nodeChanges;
	};
	
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
		ASTNode class_n = node.getParent();
		boolean inAnonymousClass = false;
		boolean inStaticContext = false;
		
		while (n != null && !inAnonymousClass && !inStaticContext) {
			
			if (n.getNodeType() == ASTNode.QUALIFIED_NAME) {
				class_n = n;
			}
			
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
								//use taintOperator to insert at class
								taintNodeChanges.add(new TaintNodeChangeContainers(class_n, index, Block.STATEMENTS_PROPERTY));
								//use sourceOperator to insert at method
								nodeChanges.add(new SourceNodeChangeContainers(node, index, Block.STATEMENTS_PROPERTY, 0));
								
								//search through children for earlier class
								class_n = (ASTNode) n.getStructuralProperty(Block.STATEMENTS_PROPERTY);
								while (class_n.getNodeType() != ASTNode.QUALIFIED_NAME)
								{
									class_n = (ASTNode) class_n.getStructuralProperty(Block.STATEMENTS_PROPERTY);
								}
								
								temp = (Class<? extends ASTNode>) temp.getDeclaringClass();
								//unsure if this is possible ^

							}
							
							else {
								//use taintOperator to insert at class
								taintNodeChanges.add(new TaintNodeChangeContainers(class_n, index, Block.STATEMENTS_PROPERTY));
								//use sourceOperator to insert at method
								nodeChanges.add(new SourceNodeChangeContainers(node, index, Block.STATEMENTS_PROPERTY, 0));
							}							
						}
					}
				index++;
			}
			n = n.getParent();
		}
		return true;
	}

}
