package edu.wm.cs.muse.dataleak.support;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.omg.CORBA.DynAnyPackage.TypeMismatch;
import com.sun.jdi;
/**
 * Offers utility operations for Schemas, focusing on ASTNode. 
 * @author Amit Seal Ami
 *
 */
public class SchemaOperatorUtility {
	
	public static int getMethodDepthInternalClass(ASTNode node) throws InvalidTypeException {
		if(node.getNodeType()!=ASTNode.METHOD_DECLARATION) throw new TypeMismatch();
		int index = -1;
		while(node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION){
			index++;
			node = node.getParent();
		}
		return index;
	}
	
	public static String getClassNameOfMethod(ASTNode method) {
		String className = "";
		method = method.getParent();
		while (method != null) {
			if (method.getNodeType() == ASTNode.TYPE_DECLARATION) {
				className = ((TypeDeclaration) method).getName().toString();
				break;
			} else if (method.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
				className = "1";
				break;
			}
			method = method.getParent();
		}
		return className;
	}
	
}
