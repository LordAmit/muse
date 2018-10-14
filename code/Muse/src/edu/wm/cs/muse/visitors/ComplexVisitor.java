package edu.wm.cs.muse.visitors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import edu.wm.cs.muse.utility.Utility;

class ComplexVisitor extends ReachabilityVisitor {

	public ComplexVisitor(ASTRewrite rewriter) {
		super(rewriter);
		// TODO Auto-generated constructor stub
	}

	String[] paths = new String[] {
			"String[] leakArRay%d = new String[] {\"n/a\", dataLeAk%d};\n"
					+ "String dataLeAkPath%d = leakArRay%d[leakArRay%d.length - 1];",
			"java.util.HashMap<String, java.util.HashMap<String, String>> leakMaP%d = new java.util.HashMap<String, java.util.HashMap<String, String>>();\n"
					+ "leakMaP%d.put(\"test\", new java.util.HashMap<String, String>());\n"
					+ "leakMaP%d.get(\"test\").put(\"test\", dataLeAk%d);\n"
					+ "String dataLeAkPath%d = leakMaP%d.get(\"test\").get(\"test\");",
			"StringBuffer leakBuFFer%d = new StringBuffer();" + "for (char chAr%d : dataLeAk%d.toCharArray()) {"
					+ "leakBuFFer%d.append(chAr%d);" + "}" + "String dataLeAkPath%d = leakBuFFer%d.toString();",
			"String dataLeAkPath%d;" + "try {" + "throw new Exception(dataLeAk%d);"
					+ "} catch (Exception leakErRor%d) {" + "dataLeAkPath%d = leakErRor%d.getMessage();" + "}" };

	@Override
	protected void insertDataLeak(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
//		AST ast = node.getAST();
		ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
		String source = String.format(
				"String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();",
				Utility.COUNTER_GLOBAL);
		String sink = String.format("android.util.Log.d(\"leak-%d\", dataLeAkPath%d);", Utility.COUNTER_GLOBAL,
				Utility.COUNTER_GLOBAL);
		String leak = source + "\n"
				+ String.format(paths[Utility.COUNTER_GLOBAL % paths.length], Utility.COUNTER_GLOBAL,
						Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL,
						Utility.COUNTER_GLOBAL, Utility.COUNTER_GLOBAL)
				+ "\n" + sink;
		Utility.COUNTER_GLOBAL++;
		Statement placeHolder = (Statement) rewriter.createStringPlaceholder(leak, ASTNode.EMPTY_STATEMENT);
		listRewrite.insertAt(placeHolder, index, null);

	}

	public boolean visit(TypeDeclaration node) {
		// Classes and Interfaces
		if (node.isInterface()) {
			return false;
		}
		return true;
	}

	public boolean visit(AnonymousClassDeclaration node) {
		// Anonymous classes
		return true;
	}
}