package edu.wm.cs.muse.sink;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

/**
 * A container for storing nodes, index, property descriptor, count, method and insertion type for sinks.
 * @author yang
 *
 */

public class SinkNodeChangeContainers {
	
	ASTNode node;
	int index;
	ChildListPropertyDescriptor propertyDescriptor;
	int count;
	ASTNode method;
	int insertion;
	
	public SinkNodeChangeContainers(ASTNode node, int index, int count,
			ChildListPropertyDescriptor childListPropertyDescriptor, ASTNode method, int insertion)
	{
		this.node = node;
		this.index = index;
		this.count = count;
		propertyDescriptor = childListPropertyDescriptor;
		this.method = method;
		this.insertion = insertion;
	}
}
