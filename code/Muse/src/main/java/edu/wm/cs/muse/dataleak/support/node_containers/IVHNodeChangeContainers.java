package edu.wm.cs.muse.dataleak.support.node_containers;

import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Containers to hold information needed for IVHOperator
 * that is promulgated through IVHSchema
 * @author Kevin Cortright
 *
 */
public class IVHNodeChangeContainers {

	public TypeDeclaration node;
	public ChildListPropertyDescriptor nodePropertyDescriptor;
	public TypeDeclaration parent;
	public ChildListPropertyDescriptor parentPropertyDescriptor;
	public boolean parentUsed;
	public boolean parentIsChild;
	
	public IVHNodeChangeContainers (TypeDeclaration node, TypeDeclaration parent, 
			ChildListPropertyDescriptor nodeDescriptor, ChildListPropertyDescriptor parentDescriptor, boolean parentUsed,
			boolean parentIsChild) {
		this.node = node;
		this.parent = parent;
		nodePropertyDescriptor = nodeDescriptor;
		parentPropertyDescriptor = parentDescriptor;
		this.parentUsed = parentUsed;
		this.parentIsChild = parentIsChild;
	}
	
}
