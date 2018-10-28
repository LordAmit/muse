package edu.wm.cs.muse.taint;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

/**
 * A container for storing nodes, index, property descriptor, and insertion type for taint operators.
 * @author yang
 *
 */

public class TaintNodeChangeContainers {
	
	ASTNode node;
	int index;
	ChildListPropertyDescriptor propertyDescriptor;
	int count;
	ASTNode method;
	
	public TaintNodeChangeContainers(ASTNode node, int index, 
			ChildListPropertyDescriptor childListPropertyDescriptor)
	{
		this.node = node;
		this.index = index;
		propertyDescriptor = childListPropertyDescriptor;
	}

}