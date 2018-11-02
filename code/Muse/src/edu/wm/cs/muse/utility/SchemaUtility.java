package edu.wm.cs.muse.utility;

import org.eclipse.jdt.core.dom.ASTNode;

public class SchemaUtility {
	public static int getDepthInternalClass(ASTNode node) {
		int index = 0;
		while(node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION){
			index++;
			node = node.getParent();
		}
		return index;
	}

}
