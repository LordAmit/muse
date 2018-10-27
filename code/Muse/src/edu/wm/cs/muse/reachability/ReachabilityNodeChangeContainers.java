package edu.wm.cs.muse.reachability;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;

/**
 * It acts as a container for storing node, change, and property descriptor.
 * @author Amit Seal Ami
 *
 */
public class ReachabilityNodeChangeContainers {

	ASTNode node;
	int index;
	ChildListPropertyDescriptor propertyDescriptor;
	String changedSource = null;
	public ReachabilityNodeChangeContainers(ASTNode node, int index,
			ChildListPropertyDescriptor childListPropertyDescriptor, String changedSource) {
		this.node = node;
		this.index = index;
		propertyDescriptor = childListPropertyDescriptor;
		this.changedSource = changedSource;
		
		
	}
}
