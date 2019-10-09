package edu.wm.cs.muse.schemasTest;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

import edu.wm.cs.muse.dataleak.schemas.TaintSinkSchema;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.TaintNodeChangeContainers;

public class TaintSinkSchemaStub extends TaintSinkSchema{
	ASTNode parent;
	ASTNode classRetainer = null;
	private ArrayList<TaintNodeChangeContainers> taintNodeChanges;
	private ArrayList<SinkNodeChangeContainers> nodeChanges;
	int index = 0;
	int methodIndex = 0;
	ArrayList<FieldDeclaration> fieldHolder = new ArrayList<FieldDeclaration>();
	ArrayList<FieldDeclaration> previousFieldHolder = new ArrayList<FieldDeclaration>();

	
	public TaintSinkSchemaStub() {
		super();
	}
	
	@Override
	public boolean visit(MethodDeclaration method) {
		if (Modifier.isStatic(method.getModifiers())) {
			return true;
		}
		parent = method.getParent();
		int throwaway = 0;

		if (parent.getNodeType() == ASTNode.TYPE_DECLARATION) {

			nodeChanges.add(new SinkNodeChangeContainers(parent, Utility.COUNTER_GLOBAL_TSINK++, throwaway,
					Block.STATEMENTS_PROPERTY, method.getBody(), 0));
			// get parent's fields with findField
			parent = parent.getParent();

		}

		return true;
	}

}
