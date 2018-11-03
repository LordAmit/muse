package edu.wm.cs.muse.taint;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.muse.source.SourceNodeChangeContainers;
import edu.wm.cs.muse.utility.Utility;;

/**
 * The TaintSchema will traverse the nodes of the AST, and when it reaches a
 * method, it will locate the class(es) the method is in and insert a
 * declaration in the class(es) and a source in the method. Once the schema has
 * completed its course, it will call SinkOperator to insert sinks that reflect
 * the location of the declarations.
 * 
 * @author yang, amit
 */

public class TaintSchema extends ASTVisitor {
	ASTNode parent;
	private ArrayList<TaintNodeChangeContainers> taintNodeChanges;
	private ArrayList<SourceNodeChangeContainers> nodeChanges;
	// for source of taint.
	int index = 0;
	ASTNode outermost_class;

	/**
	 * TaintSchema should use the source node container array since they occur at
	 * the same time and use the same parameters
	 */
	public TaintSchema() {
		taintNodeChanges = new ArrayList<TaintNodeChangeContainers>();
		nodeChanges = new ArrayList<SourceNodeChangeContainers>();
	}

	public ArrayList<TaintNodeChangeContainers> getTaintNodeChanges() {
		return this.taintNodeChanges;
	};

	public ArrayList<SourceNodeChangeContainers> getNodeChanges() {
		return this.nodeChanges;
	};

	public boolean visit(MethodDeclaration node) {
		
		Stack<ASTNode> ancestorStack = new Stack<ASTNode>();

		System.out.println(node.getName());
		// for method body code
//		nodeChanges.add(new SourceNodeChangeContainers(node.getBody(), index, Block.STATEMENTS_PROPERTY, 0));
		parent = node.getParent();

		while (true) {
			if (parent.getNodeType() == ASTNode.TYPE_DECLARATION) {
				ancestorStack.add(parent);
				// for declaraion
//				nodeChanges
//						.add(new SourceNodeChangeContainers(parent, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY, 1));
				parent = parent.getParent();
			}
			if (parent.getParent() == null)
				break;
		}

//		System.out.println(node.getName());
//		System.out.println(parentStack.size());
//
		for (ASTNode ancestorNode : ancestorStack) {
			// for declaration
			nodeChanges.add(
					new SourceNodeChangeContainers(ancestorNode, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY, 1));
			nodeChanges.add(new SourceNodeChangeContainers(node.getBody(), index, Block.STATEMENTS_PROPERTY, 0));
			index++;
			
		}
		

//		ASTNode temp = node.getParent();

		// SourceOperator sourceOp = new SourceOperator(rewriter);
//		Block node = method.getBody();
//		if (node == null) {
//			return true;
//		}
//		int index = 0;

//		
////		for (Object obj : node.statements()) {
////			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
////				index++;
////			}
////		}
//		ASTNode node_parent = node.getParent();
//		ASTNode class_n = node.getParent();
//
//		boolean inAnonymousClass = false;
//		boolean inStaticContext = false;
////		while (node_parent != null && !inAnonymousClass && !inStaticContext) {
//
//			// TODO: check qualified_name
//			if (node_parent.getNodeType() == ASTNode.TYPE_DECLARATION) {
//				class_n = node_parent;
//			}
//			if (node.getNodeType() == ASTNode.METHOD_DECLARATION) {
//				System.out.println("Something");
//				System.out.println(node_parent.getNodeType());
//				System.out.println(ASTNode.METHOD_DECLARATION);
//				System.out.println(node_parent.toString());
//				System.out.println(index);
//			}
//			
//			if (node_parent.getParent().getNodeType() == ASTNode.TYPE_DECLARATION) {
//				System.out.println("This is supposed to work for class TestClass");
//				outermost_class = node_parent;
//				//TODO: use taintOperator to insert at n.getClass()
//			}
//	
//
////					}
//					else {
////						System.out.println("Internal class checked.");
//			// recursively find and insert with taintOperator every outer class
////						for (int num = index; num > 0; num--) {
////							if (temp.getDeclaringClass() != null) {
////								//use taintOperator to insert at class
////								taintNodeChanges.add(new TaintNodeChangeContainers(class_n, index, Block.STATEMENTS_PROPERTY));
////								//use sourceOperator to insert at method
////								nodeChanges.add(new SourceNodeChangeContainers(node, index, Block.STATEMENTS_PROPERTY, 0));
////								
////								//search through children for earlier class
////								class_n = (ASTNode) node_parent.getStructuralProperty(Block.STATEMENTS_PROPERTY);
////								while (class_n.getNodeType() != ASTNode.QUALIFIED_NAME)
////								{
////									class_n = (ASTNode) class_n.getStructuralProperty(Block.STATEMENTS_PROPERTY);
////								}
////								
////								temp = (Class<? extends ASTNode>) temp.getDeclaringClass();
////								//unsure if this is possible ^
////
////							}
////							
////							else {
////								//use taintOperator to insert at class
////								taintNodeChanges.add(new TaintNodeChangeContainers(class_n, index, Block.STATEMENTS_PROPERTY));
////								//use sourceOperator to insert at method
////								nodeChanges.add(new SourceNodeChangeContainers(node, index, Block.STATEMENTS_PROPERTY, 0));
////							}							
////						}
////					}
////				index++;
//			}
//			node_parent = node_parent.getParent();
////		}
////			return true;
////		}
//		index++;
//		ancestorStack.clear();
		return true;
	}

}
