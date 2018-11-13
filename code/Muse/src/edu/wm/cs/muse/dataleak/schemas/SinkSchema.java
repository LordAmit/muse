package edu.wm.cs.muse.dataleak.schemas;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.wm.cs.muse.dataleak.support.Utility;
import edu.wm.cs.muse.dataleak.support.node_containers.SinkNodeChangeContainers;

/**
 * The SinkSchema visits each node the AST tree to find data sinks, then calls on SinkOperator to insert a
 * string that indicates the occurrence.
 * @author yang
 */

public class SinkSchema extends ASTVisitor {

	private ArrayList<SinkNodeChangeContainers> nodeChanges;

	public SinkSchema() {
		nodeChanges = new ArrayList<SinkNodeChangeContainers>();
	}
	
	public ArrayList<SinkNodeChangeContainers> getNodeChanges(){
		return this.nodeChanges;
	};
	
	Pattern variablePattern = Pattern.compile("(.*String dataLeAk)(\\d+).*"); // the pattern to search for
	
/*
 * Includes an additional integer param to differentiate between insertSource and insertSink
 */
	public boolean visit(MethodDeclaration method) {
		// Methods
		int count = 0;
		int index = 0;
		Block node = method.getBody();
		if (node == null) {
			return true;
		}
		for (Object obj : node.statements()) {
			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")
					|| obj.toString().startsWith("dataLeAk")) {
				index++;
			}
		}

		ASTNode n = node.getParent();
		boolean inAnonymousClass = false;
		boolean inStaticContext = false;
		while (n != null && !inAnonymousClass && !inStaticContext) {
			switch (n.getNodeType()) {
			case ASTNode.METHOD_DECLARATION:
				try {
					inStaticContext = Modifier.isStatic(((MethodDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.FIELD_DECLARATION:
				try {
					inStaticContext = Modifier.isStatic(((FieldDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;

			case ASTNode.TYPE_DECLARATION:
				for (Object field : ((TypeDeclaration) n).bodyDeclarations()) {
					if (((BodyDeclaration) field).getNodeType() == ASTNode.FIELD_DECLARATION) {
						Matcher matcher = variablePattern.matcher(field.toString());
						if (matcher.find() && field.toString().trim().startsWith("String dataLeAk")) {
							count = Integer.valueOf(matcher.group(2));
//							nodeChanges.add(new SinkNodeChangeContainers(node, index, count, TypeDeclaration.BODY_DECLARATIONS_PROPERTY, method, 0));
							nodeChanges.add(new SinkNodeChangeContainers(node, index, count, Block.STATEMENTS_PROPERTY, method, 0));
//							op.insertSink(node, index, count, Block.STATEMENTS_PROPERTY, method);
						}
					}
				}
				try {
					inStaticContext = Modifier.isStatic(((TypeDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.ANONYMOUS_CLASS_DECLARATION:
				for (Object field : ((AnonymousClassDeclaration) n).bodyDeclarations()) {
					if (((BodyDeclaration) field).getNodeType() == ASTNode.FIELD_DECLARATION) {
						Matcher matcher = variablePattern.matcher(field.toString());
						if (matcher.find() && field.toString().trim().startsWith("String dataLeAk")) {
							count = Integer.valueOf(matcher.group(2));
//							op.insertSink(node, index, count, Block.STATEMENTS_PROPERTY, method);
							nodeChanges.add(new SinkNodeChangeContainers(node, index, count, Block.STATEMENTS_PROPERTY, method, 0));
						}
					}
				}
				inAnonymousClass = true;
				break;
			}
			n = n.getParent();
		}
		while (inAnonymousClass && n != null && !inStaticContext) {
			switch (n.getNodeType()) {
			case ASTNode.CATCH_CLAUSE:
				n = n.getParent();
				break;
			case ASTNode.METHOD_DECLARATION:
				try {
					inStaticContext = Modifier.isStatic(((MethodDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;
				
			case ASTNode.FIELD_DECLARATION:
				try {
					inStaticContext = Modifier.isStatic(((FieldDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;

			case ASTNode.TYPE_DECLARATION:
				Utility.COUNTER_GLOBAL++;
//				op.insertSource(n, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
//				op.insertSink(node, index, Utility.COUNTER_GLOBAL, Block.STATEMENTS_PROPERTY, method);
				nodeChanges.add(new SinkNodeChangeContainers(n, 0, Utility.COUNTER_GLOBAL, TypeDeclaration.BODY_DECLARATIONS_PROPERTY, method, 1));
				nodeChanges.add(new SinkNodeChangeContainers(node, index, Utility.COUNTER_GLOBAL, Block.STATEMENTS_PROPERTY, method, 0));
				try {
					inStaticContext = Modifier.isStatic(((TypeDeclaration) n).getModifiers());
				} catch (NullPointerException e) {
				}
				break;
			case ASTNode.ANONYMOUS_CLASS_DECLARATION:
				Utility.COUNTER_GLOBAL++;
//				op.insertSource(n, 0, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY);
//				op.insertSink(node, index, Utility.COUNTER_GLOBAL, Block.STATEMENTS_PROPERTY, method);
				nodeChanges.add(new SinkNodeChangeContainers(n, 0, Utility.COUNTER_GLOBAL, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY, method, 1));
				nodeChanges.add(new SinkNodeChangeContainers(node, index, Utility.COUNTER_GLOBAL, Block.STATEMENTS_PROPERTY, method, 0));
				break;
			}
			n = n.getParent();
		}
		return true;
	}
	
}
