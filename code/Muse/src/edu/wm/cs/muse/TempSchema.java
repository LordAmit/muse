package edu.wm.cs.muse;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.omg.CORBA.DynAnyPackage.TypeMismatch;

import edu.wm.cs.muse.taint.TaintSchema;
import edu.wm.cs.muse.utility.SchemaUtility;

public class TempSchema extends ASTVisitor {

	@Override
	public boolean visit(MethodDeclaration node) {
//		System.out.println(node.toString());
		try {
			System.out.println(node.getName() +" " +SchemaUtility.getMethodDepthInternalClass(node));
		} catch (TypeMismatch e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println(node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION);
		return true;
	}

}
