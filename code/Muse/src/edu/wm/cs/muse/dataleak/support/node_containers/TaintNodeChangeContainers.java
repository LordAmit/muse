package edu.wm.cs.muse.dataleak.support.node_containers;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

/**
 * A container for storing nodes, index, property descriptor, and field declaration arrays 
 * for taint operators.
 * @author yang
 *
 */

public class TaintNodeChangeContainers {
	
	public ASTNode node;
	public int index;
	public ChildListPropertyDescriptor propertyDescriptor;
	public int count;
	public ArrayList<FieldDeclaration> fieldBoys;
	
	//Uses the class node to check which fields go with which methods, then adds all the sink-fields
	//to those methods
	public TaintNodeChangeContainers(ASTNode node, ArrayList<FieldDeclaration> fieldBoys, int index, 
			ChildListPropertyDescriptor childListPropertyDescriptor, int count)
	{
		this.node = node;
		this.index = index;
		propertyDescriptor = childListPropertyDescriptor;
		this.count = count;
		this.fieldBoys = fieldBoys;
	}

}