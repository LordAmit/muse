package edu.wm.cs.muse.schemasTest;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;

import edu.wm.cs.muse.dataleak.schemas.ScopeSinkSchema;
import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;
import edu.wm.cs.muse.dataleak.support.node_containers.TaintNodeChangeContainers;

public class ScopeSinkSchemaStub extends ScopeSinkSchema{
	ASTNode parent;
	ASTNode classRetainer = null;
	private ArrayList<TaintNodeChangeContainers> taintNodeChanges;
	private ArrayList<SinkNodeChangeContainers> nodeChanges;
	int index = 0;
	int methodIndex = 0;
	ArrayList<FieldDeclaration> fieldHolder = new ArrayList<FieldDeclaration>();
	ArrayList<FieldDeclaration> previousFieldHolder = new ArrayList<FieldDeclaration>();
	int methodCounter = 0;
	int sinkCounter = 0;
	int subClassCount = -1;
	int dataLeakCount=0;
	ArrayList<String> methodNames = new ArrayList<>();
	public ScopeSinkSchemaStub() {
		super();
	}
	
	@Override
	public boolean visit(MethodDeclaration method) {
		
		if (Modifier.isStatic(method.getModifiers())) {
			return true;
		}
		parent = method.getParent();
		

		methodNames.add(method.getName().toString().toUpperCase());
		this.methodCounter++;
		if (parent.getNodeType() == ASTNode.TYPE_DECLARATION) {
			this.sinkCounter++;
//
//			nodeChanges.add(new SinkNodeChangeContainers(parent, Utility.COUNTER_GLOBAL_TSINK++, throwaway,
//					Block.STATEMENTS_PROPERTY, method.getBody(), 0));
//			// get parent's fields with findField
//			parent = parent.getParent();
//
		}

		return true;
	}
	
	public int returnMethodCounter() {
		return methodCounter;
	}
	
	public int returnSinkCounter() {
		return sinkCounter;
	}
	public ArrayList<String> returnMethodNames() {
		return methodNames;
	}
	
	@Override
	public boolean visit(FieldDeclaration field) {

		// The getParent loop was found unnecessary, getParent will always find a TYPE_DECLARATION
				
				parent = field.getParent();

//				if (parent.getNodeType() == ASTNode.TYPE_DECLARATION && parent.getParent() == null) {
				// some class segment was completed before this one. This is a new class chain
				if (parent == classRetainer) {
					// check for strings of the declaration "String dataLeAk%d"

					if (field.toString().contains("dataLeAk")
							&& field.toString().substring(0, 15).compareTo("String dataLeAk") == 0) {
						fieldHolder.add(field);
						previousFieldHolder.add(field);
						dataLeakCount++;
					}

					ArrayList<FieldDeclaration> fieldDecl = new ArrayList<FieldDeclaration>(fieldHolder);

					//taintNodeChanges.add(new TaintNodeChangeContainers(parent, fieldDecl, index, Block.STATEMENTS_PROPERTY, 0));
					// keep track of outer classes
					fieldHolder.clear();
					classRetainer = parent;
				}

				// it has switched to a subclass, must push all fields to keep correct hierarchy

				// A new FieldDeclaration ArrayList called previousFieldHolder was made in order
				// to add all the sink strings from an earlier outer class into the subclass. It
				// will add the same field as fieldBoys(now called fieldHolder) as it traverses
				// the tree, but does NOT get cleared if the parent of the method is in the same
				// class as the previous method.
				if (parent != classRetainer) {
					System.out.println(parent);
					subClassCount++;

					if (classRetainer != null) {

						if (field.toString().contains("dataLeAk")
								&& field.toString().substring(0, 15).compareTo("String dataLeAk") == 0) {
							previousFieldHolder.add(field);
							dataLeakCount++;
						}
						classRetainer = parent;
					}

					if (classRetainer == null) {

						if (field.toString().contains("dataLeAk")
								&& field.toString().substring(0, 15).compareTo("String dataLeAk") == 0) {
							previousFieldHolder.add(field);
							//dataLeakCount++;
						}
						classRetainer = parent;
					}

					ArrayList<FieldDeclaration> fieldDecl = new ArrayList<FieldDeclaration>(fieldHolder);


					for (int fieldCount = 0; fieldCount < previousFieldHolder.size(); fieldCount++) {
						fieldHolder.add(previousFieldHolder.get(fieldCount));
					}
				}

				index++;

				return true;
			}
	
	public int returnSubClassCount() {
		return subClassCount;
	}
	
	public int returndataLeakCount() {
		return dataLeakCount;
	}

}

