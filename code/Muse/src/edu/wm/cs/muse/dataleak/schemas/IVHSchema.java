package edu.wm.cs.muse.dataleak.schemas;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.muse.dataleak.support.node_containers.IVHNodeChangeContainers;

/**
 * Visits every class in the file and determines which classes
 * are subclasses of previously visit classes. Will add any pairs
 * of subclass and superclass to a list of IVHNodeChangeContainers
 * that contain information needed for IVHOperator to perform its tasks.
 * Any class that does not meet the requirements will be added to a
 * running list of classes to be checked against as superclasses for 
 * future subclasses.
 * @author Kevin Cortright
 *
 */
public class IVHSchema extends ASTVisitor{
	
	private ArrayList<IVHNodeChangeContainers> nodeChanges;
	private ArrayList<TypeDeclaration> previousClasses;
	private ArrayList<TypeDeclaration> usedSuperClasses;
	private ArrayList<TypeDeclaration> usedSubClasses;
	
	public IVHSchema() {
		nodeChanges = new ArrayList <IVHNodeChangeContainers>();
		previousClasses = new ArrayList <TypeDeclaration>();
		usedSuperClasses = new ArrayList <TypeDeclaration>();
		usedSubClasses = new ArrayList <TypeDeclaration>();
	}
	
	public ArrayList<IVHNodeChangeContainers> getNodeChanges() {
		return nodeChanges;
	}

	/**
	 * Visits every Class in the file to perform needed
	 * checks to validate classes that fit requirements
	 */
	public boolean visit(TypeDeclaration node) {
		//Checks for class and interface and returns if interface,
		//as we only care about classes
		if (node.isInterface()) {
			return false;
		}
		
		//Check if this class has a superclass, and if not, return,
		//we will use the subclass to work with the superclass
		if (node.getSuperclassType()==null||node.getMethods().length == 0) {
			previousClasses.add(node);
			return false;
		}
		
		//Need at least one method in the subclass to be non-static
		boolean hasNonStatic = false;
		for (int i=0; i<node.getMethods().length;i++) {
			if (!node.getMethods()[i].modifiers().toString().equals("[static]")) {
				hasNonStatic = true;
				break;
			}
		}
		
		if (!hasNonStatic) {
			return false;
		}
		//Find the parent of the parent of the current node
		Type superclass = node.getSuperclassType();
		TypeDeclaration parentClass = null;
		for (TypeDeclaration parent: previousClasses) {
			if (parent.getName().toString().equals(superclass.toString())) {
				parentClass = parent;
			}
		}
		
		//Searches for the parent class amongst the previously used child classes
		TypeDeclaration parentClassPreviousSubClass = null;
		for (TypeDeclaration parent: usedSubClasses) {
			if (parentClass.getName().toString().equals(parent.getName().toString())) {
				parentClassPreviousSubClass = parent;
			}
		}
		
		//Return if no superclass found
		if (parentClass == null) {
			System.out.println("Superclass not found.");
			return false;
		}
		
		//If there's a child class that is the parent class, create true boolean for ivh operator
		boolean isSub = false;
		if (!(parentClassPreviousSubClass == null)) {
			isSub = true;
		}
		nodeChanges.add(new IVHNodeChangeContainers(node, parentClass,
				TypeDeclaration.BODY_DECLARATIONS_PROPERTY, TypeDeclaration.BODY_DECLARATIONS_PROPERTY,
				usedSuperClasses.contains(parentClass), isSub));
		
		//add classes to lists for future checks
		usedSuperClasses.add(parentClass);
		previousClasses.add(node);
		usedSubClasses.add(node);
		return true;
	}
	
}
