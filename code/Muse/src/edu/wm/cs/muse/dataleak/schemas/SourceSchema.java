package edu.wm.cs.muse.dataleak.schemas;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.muse.dataleak.support.node_containers.SourceNodeChangeContainers;

/**
 * The SourceSchema visits each node the AST tree to find data sources, then inserts a placeholder string 
 * through an SourceOperator.
 * @author yang
 */

public class SourceSchema extends ASTVisitor {
	
	private ArrayList<SourceNodeChangeContainers> nodeChanges;

	public SourceSchema() {
		nodeChanges = new ArrayList<SourceNodeChangeContainers>();
	}
	
	public ArrayList<SourceNodeChangeContainers> getNodeChanges(){
		return this.nodeChanges;
	};
	
	/*
	 * Includes an additional integer param to differentiate between insertion and insertVariable
	 */
	public boolean visit(MethodDeclaration method) {
		// Methods
		Block node = method.getBody();
		if (node == null) {
			return true;
		}
		int index = 0;
		for (Object obj : node.statements()) {
			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
				index++;
			}
		}
		ASTNode n = node.getParent();
		boolean inAnonymousClass = false;
		boolean inStaticContext = false;
		while (n != null && !inAnonymousClass && !inStaticContext) {
			switch (n.getNodeType()) {
			case ASTNode.CATCH_CLAUSE:
				n = n.getParent();
				break;
			case ASTNode.METHOD_DECLARATION:
				try {
					if (Modifier.isStatic(((MethodDeclaration) n).getModifiers())) {
						inStaticContext = true;
					}
				} catch (NullPointerException e) {
				}
				break;
			/*
			 * case ASTNode.BLOCK: int parentIndex = 0; for (Object obj : ((Block)
			 * n).statements()) { if (obj.toString().startsWith("super")) { parentIndex++; }
			 * } insertVariable(n, parentIndex, Block.STATEMENTS_PROPERTY);
			 * insertSource(node, index, Block.STATEMENTS_PROPERTY); break;
			 */
			case ASTNode.TYPE_DECLARATION:
//				insertVariable(n, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
//				insertion(node, index, Block.STATEMENTS_PROPERTY);
				nodeChanges.add(new SourceNodeChangeContainers(n, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY, 1));
				nodeChanges.add(new SourceNodeChangeContainers(node, index, Block.STATEMENTS_PROPERTY, 0));
				try {
					if (Modifier.isStatic(((TypeDeclaration) n).getModifiers())) {
						inStaticContext = true;
					}
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.ANONYMOUS_CLASS_DECLARATION:
//				insertVariable(n, 0, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY);
//				insertion(node, index, Block.STATEMENTS_PROPERTY);
				nodeChanges.add(new SourceNodeChangeContainers(n, 0, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY, 1));
				nodeChanges.add(new SourceNodeChangeContainers(node, index, Block.STATEMENTS_PROPERTY, 0));
				inAnonymousClass = true;
				break;
			}
			n = n.getParent();
		}
		return true;
	}

}
