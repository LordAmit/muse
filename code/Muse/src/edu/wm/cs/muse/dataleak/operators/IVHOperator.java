package edu.wm.cs.muse.dataleak.operators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.dataleak.DataLeak;
import edu.wm.cs.muse.dataleak.support.OperatorType;
import edu.wm.cs.muse.dataleak.support.Placementchecker;
import edu.wm.cs.muse.dataleak.support.node_containers.IVHNodeChangeContainers;

/**
 * Iterates through every subclass identified by IVHSchema and appends
 * the subclass with a source containing non-sensitive information and 
 * a sink in every method to retrieve that source and the source injected
 * into the superclass. The superclass is given a source containing sensitive 
 * information and a public method which returns the sensitive source.
 * @author Kevin Cortright
 *
 */
public class IVHOperator {
	
	ArrayList<IVHNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;
	private TryCatchHandler handler = new TryCatchHandler();
	File temp_file;
	Placementchecker checker = new Placementchecker();
	String source_file;
	
	public IVHOperator (ASTRewrite rewriter, ArrayList<IVHNodeChangeContainers> nodeChanges, String source_file) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
		this.source_file = source_file;
	}
	
	/**
	 * Iterates through all the subclass nodes and appends them
	 * and the associated superclass node as needed.
	 * 
	 * @return ASTRewrite
	 */
	public ASTRewrite InsertChanges() {
		for (IVHNodeChangeContainers container: nodeChanges) {
			try {
				InsertSources(container.node, container.nodePropertyDescriptor, false);
				//If the superclass node has already been set up by another
				//subclass, do not set it up
				if (!container.parentUsed) {
					InsertSources(container.parent, container.parentPropertyDescriptor, true);
					InsertMethod(container.parent);
				}
				InsertSinks(container.node);
			}
			catch (Exception e) {
				
			}
		}
		if (!(temp_file ==null)) {
			temp_file.delete();
		}
		return rewriter;
	}
	
	/**
	 * Insert the method declaration and associated return statement
	 * into the superclass
	 * @param node
	 */
	public void InsertMethod(TypeDeclaration node) {
		MethodDeclaration method = node.getAST().newMethodDeclaration();
		method.setName(node.getAST().newSimpleName("dataLeakGetter"));
		Block block = node.getAST().newBlock();
		block.statements().add("return dataLeak;");
		method.setBody(block);

		Type type;
		//TODO give the type String to the method as a return type
		//and give it public
		//method.setReturnType(type);
		node.bodyDeclarations().add(method);
		
	}
	
	/**
	 * Insert the sources and if superclass, insert sensitive source,
	 * if subclass, insert non-sensitive source
	 * @param node
	 * @param descriptor
	 * @param isParent
	 * @throws ClassNotFoundException
	 */
	public void InsertSources(TypeDeclaration node, ChildListPropertyDescriptor descriptor, boolean isParent) throws ClassNotFoundException {
		ListRewrite childListRewrite = rewriter.getListRewrite(node, descriptor);
		String source = "";
		if (isParent) {
			source = DataLeak.getSource(OperatorType.IVH, 0);
		}
		else {
			source = DataLeak.getVariableDeclaration(OperatorType.IVH);
		}
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(source, ASTNode.EMPTY_STATEMENT);
		if (handler.stringHasThrows(source)) {
			TryStatement tryPlaceHolder = handler.addTryCatch(placeHolder);
			childListRewrite.insertAt(tryPlaceHolder, 0, null);
			if (!(childListRewrite.getParent().getRoot() instanceof Block)) {
				temp_file = checker.getTempFile((CompilationUnit)childListRewrite.getParent().getRoot(), rewriter, source_file);
				try {
					if (!checker.check(temp_file))
						childListRewrite.remove(tryPlaceHolder,null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			childListRewrite.insertAt(placeHolder, 0, null);
			if (!(childListRewrite.getParent().getRoot() instanceof Block)) {
				temp_file = checker.getTempFile((CompilationUnit)childListRewrite.getParent().getRoot(), rewriter, source_file);
				try {
					if (!checker.check(temp_file))
						childListRewrite.remove(placeHolder,null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Inserts sinks in all methods of the subclass
	 * @param node
	 * @throws ClassNotFoundException
	 */
	public void InsertSinks(TypeDeclaration node) throws ClassNotFoundException {
		ListRewrite listRewrite = null;
		MethodDeclaration[] method = node.getMethods();
		for (int i=0; i<method.length; i++) {
			try {
				listRewrite = rewriter.getListRewrite(method[i], Block.STATEMENTS_PROPERTY);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			String sink = DataLeak.getSink(OperatorType.IVH, 0, 0);
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(sink, ASTNode.EMPTY_STATEMENT);
			if (handler.stringHasThrows(sink)) {
				TryStatement tryPlaceHolder = handler.addTryCatch(placeHolder);
				listRewrite.insertAt(tryPlaceHolder, 0, null);
				if (!(listRewrite.getParent().getRoot() instanceof Block)) {
					temp_file = checker.getTempFile((CompilationUnit) listRewrite.getParent().getRoot(), rewriter, source_file);
					try {
						if (!checker.check(temp_file)) {
							listRewrite.remove(tryPlaceHolder, null);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else {
				listRewrite.insertAt(placeHolder, 0, null);
				if (!(listRewrite.getParent().getRoot() instanceof Block)) {
					temp_file = checker.getTempFile((CompilationUnit) listRewrite.getParent().getRoot(), rewriter, source_file);
					try {
						if (!checker.check(temp_file)) {
							listRewrite.remove(placeHolder, null);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.print("Leak in: " + method[i].getName().toString());
			
		}
	}
	
}
