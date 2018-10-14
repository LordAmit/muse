package edu.wm.cs.muse.visitors;

//import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.utility.Utility;

public class ReachabilityVisitor extends ASTVisitor{
	
	ASTRewrite rewriter;
	public ReachabilityVisitor(ASTRewrite rewriter) {
		this.rewriter = rewriter;
	}
	
	protected void insertDataLeak(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
//		AST ast = node.getAST();
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);

		String source = "String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";
		String sink = "Object throwawayLeAk%d = android.util.Log.d(\"leak-%d\", dataLeAk%d);";
		String leak = String.format(source, Utility.COUNTER_GLOBAL) + "\n" + String.format(sink, Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL);
		Utility.COUNTER_GLOBAL++;
		
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(leak, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);
	}

	public boolean visit(TypeDeclaration node) {
		// Classes and Interfaces
		if (node.isInterface()) {
			return false;
		}
		String loc = node.getName().toString() + ".<init>";
		System.out.println(String.format("leak-%d: <%s>", Utility.COUNTER_GLOBAL, loc));
		insertDataLeak(node, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		return true;
	}

	public boolean visit(AnonymousClassDeclaration node) {
		// Anonymous classes
		String loc = "1.<init>";
		System.out.println(String.format("leak-%d: <%s>", Utility.COUNTER_GLOBAL, loc));
		insertDataLeak(node, 0, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY);
		return true;
	}

	public boolean visit(Block node) {
		// Blocks
		int index = 0;
		for (Object obj : node.statements()) {
			if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
				index = 1;
			}
		}
		String className = "";
		String methodName = "<init>";
		ASTNode trace = node.getParent();
		while (trace != null) {
			if (trace.getNodeType() == ASTNode.TYPE_DECLARATION) {
				className = ((TypeDeclaration) trace).getName().toString();
				break;
			} else if (trace.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
				className = "1";
				break;
			} else if (trace.getNodeType() == ASTNode.METHOD_DECLARATION) {
				methodName = ((MethodDeclaration) trace).getName().toString();
			}
			trace = trace.getParent();
		}
		String loc = className + "." + methodName;
		System.out.println(String.format("leak-%d: %s", Utility.COUNTER_GLOBAL, loc));
		insertDataLeak(node, index, Block.STATEMENTS_PROPERTY);
		return true;
	}
}
