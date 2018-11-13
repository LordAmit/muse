package edu.wm.cs.muse.dataleak.support;

import org.eclipse.jdt.core.dom.ASTNode;
import org.omg.CORBA.DynAnyPackage.TypeMismatch;

/**
 * Offers utility operations for Schemas, focusing on ASTNode. 
 * @author Amit Seal Ami
 *
 */
public class SchemaUtility {
	
	public static int getMethodDepthInternalClass(ASTNode node) throws TypeMismatch {
		if(node.getNodeType()!=ASTNode.METHOD_DECLARATION) throw new TypeMismatch();
		int index = -1;
		while(node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION){
			index++;
			node = node.getParent();
		}
		return index;
	}

}
