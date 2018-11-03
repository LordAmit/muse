package edu.wm.cs.muse.source;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

/**
 * A container for storing nodes, index, property descriptor, and insertion type for sources.
 * @author yang
 *
 */

public class SourceNodeChangeContainers {
	
	public ASTNode node;
	public int index;
	public ChildListPropertyDescriptor propertyDescriptor;
	public int count;
	public ASTNode method;
	public int insertionType;
	
	public SourceNodeChangeContainers(ASTNode node, int index, 
			ChildListPropertyDescriptor childListPropertyDescriptor, int insertionType)
	{
		this.node = node;
		this.index = index;
		propertyDescriptor = childListPropertyDescriptor;
		this.insertionType = insertionType;
	}

}
