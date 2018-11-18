package edu.wm.cs.muse.dataleak.schemas;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;;

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
	private ArrayList<SourceNodeChangeContainers> nodeChanges;
	int index = 0;

	/**
	 * TaintSchema should use the source node container array since they occur at
	 * the same time and use the same parameters. The sink aspect will be included via
	 * the TaintSinkSchema
	 */
	public TaintSchema() {
		nodeChanges = new ArrayList<SourceNodeChangeContainers>();
	}

	public ArrayList<SourceNodeChangeContainers> getNodeChanges() {
		return this.nodeChanges;
	};

	//Creates a stack of class nodes that have a corresponding methodDeclaration node
	//in order to correctly insert field declarations in the classes and source strings
	//in the method bodies using the TaintOperator.
	public boolean visit(MethodDeclaration node) {
		
		Stack<ASTNode> ancestorStack = new Stack<ASTNode>();

		System.out.println(node.getName());
		parent = node.getParent();

		while (true) {
			if (parent.getNodeType() == ASTNode.TYPE_DECLARATION) {
				ancestorStack.add(parent);
				parent = parent.getParent();
			}
			if (parent.getParent() == null)
				break;
		}

		for (ASTNode ancestorNode : ancestorStack) {
			// for declaration
			nodeChanges.add(
					new SourceNodeChangeContainers(ancestorNode, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY, 1));
			//for method body
			nodeChanges.add(new SourceNodeChangeContainers(node.getBody(), index, Block.STATEMENTS_PROPERTY, 0));
			index++;
			
		}
		
		return true;
	}

}