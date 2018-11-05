package edu.wm.cs.muse.taint;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.muse.sink.SinkNodeChangeContainers;
import edu.wm.cs.muse.source.SourceNodeChangeContainers;
import edu.wm.cs.muse.utility.Utility;;

/**
 * The TaintSinkSchema will traverse the nodes of the rewritten AST for declarations, then correctly implement
 * a sink insertion to all the methods in the class. 
 * 
 * @author yang
 */

public class TaintSinkSchema extends ASTVisitor {
	ASTNode parent;
	ASTNode classRetainer = null;
	private ArrayList<TaintNodeChangeContainers> taintNodeChanges;
	private ArrayList<SinkNodeChangeContainers> nodeChanges;
	int index = 0;
	ArrayList<FieldDeclaration> fieldBoys = new ArrayList<FieldDeclaration>();
	
	public TaintSinkSchema() {
		taintNodeChanges = new ArrayList<TaintNodeChangeContainers>();
		nodeChanges = new ArrayList<SinkNodeChangeContainers>();
	}
	
	public ArrayList<TaintNodeChangeContainers> getFieldNodeChanges() {
		return this.taintNodeChanges;
	};
	
	public ArrayList<SinkNodeChangeContainers> getMethodNodeChanges() {
		return this.nodeChanges;
	};
	
	//action flow is: this method visitor will catch all the methods, then take note 
	//of the classes they belong in. This will be used to compare with field visit
	//and insert a sink for every field declaration in every method where the classes
	//match up.
	public boolean visit (MethodDeclaration method)
	{
		parent = method.getParent();
		int throwaway = 0;

		if (parent.getNodeType() == ASTNode.TYPE_DECLARATION) {
				
			nodeChanges.add(new SinkNodeChangeContainers(parent, index, throwaway, Block.STATEMENTS_PROPERTY, method.getBody(), 0));
			// get parent's fields with findField
			parent = parent.getParent();
		}
		return true;
	}
		
	
	//use field visit to get a array of fields for each class. If it is in a
	//subclass, add all fields in the class before it to the array. Pass to
	//the taintNodeChanges container, then in conjunction with the methods part
	//insert the sinks
	public boolean visit (FieldDeclaration field)
	{
		parent = field.getParent();
		
		if (parent.getNodeType() == ASTNode.TYPE_DECLARATION && parent.getParent() == null)
		{
			//some class segment was completed before this one. This is a new class chain
			if (classRetainer != null)
			{
				//check for strings of the declaration "String dataLeAk%d"
				fieldBoys.add(field);
				taintNodeChanges.add(new TaintNodeChangeContainers(parent, fieldBoys, index, Block.STATEMENTS_PROPERTY, 0));
				//keep track of outer classes
				fieldBoys.clear();
				classRetainer = parent;
			}
			
			//check for strings of the declaration "String dataLeAk%d"
			fieldBoys.add(field);
			classRetainer = parent;
		}

		if (parent.getNodeType() == ASTNode.TYPE_DECLARATION && parent.getParent() != null) 
		{
			//it has switched to a subclass, must push all fields to keep correct hierarchy
			if (parent != classRetainer)
			{
				taintNodeChanges.add(new TaintNodeChangeContainers(classRetainer, fieldBoys, index, Block.STATEMENTS_PROPERTY, 0));
				classRetainer = parent;
			}
			
			else
			{
				//check for strings of the declaration "String dataLeAk%d"
				fieldBoys.add(field);
				//gets the fields, then compounds with outer class fields if in a 
				//subclass
				System.out.println(parent.toString().substring(0, 16));
				System.out.println(field.toString().substring(0, 16));
				//parent = parent.getParent();
			}
		}
		
		index++;
		
		return true;
	}
}
