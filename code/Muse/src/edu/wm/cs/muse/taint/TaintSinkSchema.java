package edu.wm.cs.muse.taint;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.muse.source.SourceNodeChangeContainers;
import edu.wm.cs.muse.utility.Utility;;

/**
 * The TaintSinkSchema will traverse the nodes of the rewritten AST for declarations, then correctly implement
 * a sink insertion to all the methods in the class. 
 * 
 * @author yang
 */

public class TaintSinkSchema extends ASTVisitor {
	
	//action flow is: visitors find a node of class type, uses findField to get all
	//the fields of that class that start with "dataLeAk" into an arraylist, (also
	//use .getParent to include any other field declarations, then proceed as usual.
	//A for-loop is used to insert all the sinks into the methods.
	public boolean visit (MethodDeclaration method)
	{
		return true;
	}
	
	public void findField (FieldDeclaration field)
	{
		
	}

}
