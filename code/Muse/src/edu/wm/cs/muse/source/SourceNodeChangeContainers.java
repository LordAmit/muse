package edu.wm.cs.muse.source;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

public class SourceNodeChangeContainers {
	
	ASTNode node;
	int index;
	ChildListPropertyDescriptor propertyDescriptor;
	int count;
	ASTNode method;
	int insertionType;
	
	public SourceNodeChangeContainers(ASTNode node, int index, 
			ChildListPropertyDescriptor childListPropertyDescriptor, int insertionType)
	{
		this.node = node;
		this.index = index;
		propertyDescriptor = childListPropertyDescriptor;
		this.insertionType = insertionType;
	}

}
