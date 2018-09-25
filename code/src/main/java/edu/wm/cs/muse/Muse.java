package edu.wm.cs.muse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import edu.wm.cs.mplus.detectors.code.visitors.ASTHelper;

/**
 *
 * @author Richard Bonett
 * @since October 12, 2017
 */
public class Muse {
	
	int counter = 0;
	ASTRewrite rewriter;
	
	public void runMuse(String[] args) {
		//Usage Error
		if(args.length != 4){
			System.out.println("******* ERROR: INCORRECT USAGE *******");
			System.out.println("Argument List:");
			System.out.println("1. Binaries path");
			System.out.println("2. App Source Code path");
			System.out.println("3. App Name");
			System.out.println("4. Mutants path");
			return;
		}
		
		//Getting arguments
		String binariesFolder = args[0];
		String rootPath = args[1];
		String appName = args[2];
		String mutantsFolder = args[3];
		
		try {
			String newRoot = mutantsFolder + File.separator + appName;
			if (new File(newRoot).exists()) {
				FileUtils.deleteDirectory(new File(newRoot));
			}
			FileUtils.copyDirectory(new File(rootPath), new File(newRoot));
			rootPath = newRoot;
		} catch (IOException e) {
			return;
		}
	
		
		System.out.println(rootPath);
		Collection<File> files = FileUtils.listFiles(new File(rootPath), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for (File file : files) {
			try {
				if(file.getName().endsWith(".java") && file.getCanonicalPath().contains(appName.replace(".", "/")) && !file.getName().contains("EmmaInstrumentation.java") && !file.getName().contains("FinishListener.java") && !file.getName().contains("InstrumentedActivity.java") && !file.getName().contains("SMSInstrumentedReceiver.java")){
					//System.out.println("PROCESSING: " + file.getAbsolutePath());
					String source = readSourceFile(file.getAbsolutePath()).toString();
					CompilationUnit root = ASTHelper.getAST(source, binariesFolder, rootPath);
					rewriter = ASTRewrite.create(root.getAST());
					root.accept(new SourceVisitor());
					
					Document sourceDoc = new Document(source);
					TextEdit edits = rewriter.rewriteAST(sourceDoc, null);
					edits.apply(sourceDoc);
					FileUtils.writeStringToFile(file, sourceDoc.get(), false);
					rewriter = null;
					
					source = readSourceFile(file.getAbsolutePath()).toString();
					root = ASTHelper.getAST(source, binariesFolder, rootPath);
					rewriter = ASTRewrite.create(root.getAST());
					root.accept(new SinkVisitor());
					
					//sourceDoc = new Document(source);
					//edits = rewriter.rewriteAST(sourceDoc, null);
					//edits.apply(sourceDoc);
					//FileUtils.writeStringToFile(file, sourceDoc.get(), false);
					//rewriter = null;
				}
			} catch (IOException e) {
				System.err.println(String.format("ERROR PROCESSING \"%s\": %s", file.getAbsolutePath(), e.getMessage()));
				return;
			} catch (MalformedTreeException | BadLocationException e) {
				System.err.println("ERROR EDITING AST: " + e.getMessage());
				return;
			} 
		}
	}

	
	private StringBuffer readSourceFile(String filePath) throws FileNotFoundException, IOException {
		StringBuffer source = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = null;

		while((line = reader.readLine()) != null){
			source.append(line).append("\n");
		}
		reader.close();
		return source;
	}
	
	private class ReachabilityVisitor extends ASTVisitor {
		protected void insertDataLeak(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
			AST ast = node.getAST();
			ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);

			String source = "String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();";
			String sink = "Object throwawayLeAk%d = android.util.Log.d(\"leak-%d\", dataLeAk%d);";
			String leak = String.format(source, counter) + "\n" + String.format(sink, counter, counter, counter);
			counter++;
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(leak, ASTNode.EMPTY_STATEMENT);
			listRewrite.insertAt(placeHolder, index, null);
		}
		
		
		public boolean visit(TypeDeclaration node) {
			// Classes and Interfaces
			if (node.isInterface()) {
				return false;
			}
			String loc = node.getName().toString() + ".<init>";
			System.out.println(String.format("leak-%d: <%s>", counter, loc));
			insertDataLeak(node, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
			return true;
		}
		
		public boolean visit(AnonymousClassDeclaration node) {
			// Anonymous classes
			String loc = "1.<init>";
			System.out.println(String.format("leak-%d: <%s>", counter, loc));
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
				}
				else if (trace.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
					className = "1";
					break;
				}
				else if (trace.getNodeType() == ASTNode.METHOD_DECLARATION) {
					methodName = ((MethodDeclaration) trace).getName().toString();
				}
				trace = trace.getParent();
			}
			String loc = className + "." + methodName;
			System.out.println(String.format("leak-%d: %s", counter, loc));
			insertDataLeak(node, index, Block.STATEMENTS_PROPERTY);
			return true;
		}
	}
	
	
	private class ComplexVisitor extends ReachabilityVisitor {
		
		String[] paths = new String[] {
				"String[] leakArRay%d = new String[] {\"n/a\", dataLeAk%d};\n"
				+ "String dataLeAkPath%d = leakArRay%d[leakArRay%d.length - 1];",
				"java.util.HashMap<String, java.util.HashMap<String, String>> leakMaP%d = new java.util.HashMap<String, java.util.HashMap<String, String>>();\n"
				+ "leakMaP%d.put(\"test\", new java.util.HashMap<String, String>());\n"
				+ "leakMaP%d.get(\"test\").put(\"test\", dataLeAk%d);\n"
				+ "String dataLeAkPath%d = leakMaP%d.get(\"test\").get(\"test\");",
			    "StringBuffer leakBuFFer%d = new StringBuffer();"
			    + "for (char chAr%d : dataLeAk%d.toCharArray()) {"
			    + "leakBuFFer%d.append(chAr%d);"
			    + "}"
			    + "String dataLeAkPath%d = leakBuFFer%d.toString();",
			    "String dataLeAkPath%d;"
			    + "try {"
				+ "throw new Exception(dataLeAk%d);"
			    + "} catch (Exception leakErRor%d) {"
				+ "dataLeAkPath%d = leakErRor%d.getMessage();"
			    + "}"
		};
		
		@Override
		protected void insertDataLeak(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
			AST ast = node.getAST();
			ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
			String source = String.format("String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();", counter);
			String sink = String.format("android.util.Log.d(\"leak-%d\", dataLeAkPath%d);", counter, counter);
			String leak =  source + "\n" + String.format(paths[counter % paths.length], counter, counter, counter, counter, counter, counter, counter) + "\n" + sink;
			counter++;
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

	
	private class SourceVisitor extends ASTVisitor {		
		private void insertSource(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
			ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
			String source = String.format("dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();", counter);
			counter++;
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(source, ASTNode.EMPTY_STATEMENT);
			listRewrite.insertAt(placeHolder, index, null);
		}
		
		private void insertVariable(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
			ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
			String variable = String.format("String dataLeAk%d = \"\";", counter);
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(variable, ASTNode.EMPTY_STATEMENT);
			listRewrite.insertAt(placeHolder, index, null);
		}
	
		
		public boolean visit(MethodDeclaration method) {
			// Methods
			Block node = method.getBody();
			if (node == null) {
				return true;
			}
			int index = 0;
			for (Object obj : node.statements()) {
				if (obj.toString().startsWith("super") || obj.toString().startsWith("this(")) {
					index++;
				}
			}
			ASTNode n = node.getParent();
			boolean inAnonymousClass = false;
			boolean inStaticContext = false;
			while (n != null && !inAnonymousClass && !inStaticContext) {
				switch (n.getNodeType()) {
				case ASTNode.CATCH_CLAUSE:
					n = n.getParent();
					break;
				case ASTNode.METHOD_DECLARATION:
					try {
						if (Modifier.isStatic(((MethodDeclaration) n).getModifiers())) {
							inStaticContext = true;
						}
					}
					catch (NullPointerException e) {}
					break;
				/*case ASTNode.BLOCK:
					int parentIndex = 0;
					for (Object obj : ((Block) n).statements()) {
						if (obj.toString().startsWith("super")) {
							parentIndex++;
						}
					}
					insertVariable(n, parentIndex, Block.STATEMENTS_PROPERTY);
					insertSource(node, index, Block.STATEMENTS_PROPERTY);
					break;*/
				case ASTNode.TYPE_DECLARATION:
					insertVariable(n, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
					insertSource(node, index, Block.STATEMENTS_PROPERTY);
					try {
						if (Modifier.isStatic(((TypeDeclaration) n).getModifiers())) {
							inStaticContext = true;
						}
					}
					catch (NullPointerException e) {}
					break;
				case ASTNode.ANONYMOUS_CLASS_DECLARATION:
					insertVariable(n, 0, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY);
					insertSource(node, index, Block.STATEMENTS_PROPERTY);
					inAnonymousClass = true;
					break;
				}
				n = n.getParent();	
			}
			return true;
		}
	}
	
	
	private class SinkVisitor extends ASTVisitor {	
		Pattern variablePattern = Pattern.compile("(.*String dataLeAk)(\\d+).*");   // the pattern to search for
		HashMap<Integer, Integer> repeatCounts = new HashMap<Integer, Integer>();
	    
		private void insertSink(ASTNode node, int index, int count, ChildListPropertyDescriptor nodeProperty, ASTNode method) {
			ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
			int cur = repeatCounts.containsKey(count) ? repeatCounts.get(count) : -1;
			repeatCounts.put(count, cur + 1);
			String sink = String.format("android.util.Log.d(\"leak-%d-%d\", dataLeAk%d);", count, repeatCounts.get(count), count);
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(sink, ASTNode.EMPTY_STATEMENT);
			listRewrite.insertAt(placeHolder, index, null);
			String methodName = ((MethodDeclaration) method).getName().toString();
			String className = "";
			method = method.getParent();
			while (method != null) {
				if (method.getNodeType() == ASTNode.TYPE_DECLARATION) {
					className = ((TypeDeclaration) method).getName().toString();
					break;
				}
				else if (method.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
					className = "1";
					break;
				}
				method = method.getParent();
			}
			System.out.println(String.format("leak-%d-%d: %s.%s", count, repeatCounts.get(count), className, methodName));
		}
		
		private void insertSource(ASTNode node, int index, ChildListPropertyDescriptor nodeProperty) {
			ListRewrite listRewrite = rewriter.getListRewrite(node, nodeProperty);
			String source = String.format("final String dataLeAk%d = java.util.Calendar.getInstance().getTimeZone().getDisplayName();", counter);
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(source, ASTNode.EMPTY_STATEMENT);
			listRewrite.insertAt(placeHolder, index, null);
		}
	
		
		public boolean visit(MethodDeclaration method) {
			// Methods
			int count = 0;
			int index = 0;
			Block node = method.getBody();
			if (node == null) {
				return true;
			}
			for (Object obj : node.statements() ) {
				if (obj.toString().startsWith("super") || obj.toString().startsWith("this(") || obj.toString().startsWith("dataLeAk")) {
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
					} catch (NullPointerException e) {}
					break;
				case ASTNode.FIELD_DECLARATION:
					try {
						inStaticContext = Modifier.isStatic(((FieldDeclaration) n).getModifiers());
					} catch (NullPointerException e) {}
					break;
				/*case ASTNode.BLOCK:
					for (Object stmt : ((Block) n).statements()) {
						Matcher matcher = variablePattern.matcher(stmt.toString());
						if (matcher.find()) {
							count = Integer.valueOf(matcher.group(1));
							insertSink(node, index, count, Block.STATEMENTS_PROPERTY);
						}
					}
					break;*/
				case ASTNode.TYPE_DECLARATION:
					for (Object field : ((TypeDeclaration) n).bodyDeclarations()) {
						if (((BodyDeclaration) field).getNodeType() == ASTNode.FIELD_DECLARATION) {
							Matcher matcher = variablePattern.matcher(field.toString());
							if (matcher.find() && field.toString().trim().startsWith("String dataLeAk")) {
								count = Integer.valueOf(matcher.group(2));
								insertSink(node, index, count, Block.STATEMENTS_PROPERTY, method);
							}
						}
					}
					try {
						inStaticContext = Modifier.isStatic(((TypeDeclaration) n).getModifiers());
					} catch (NullPointerException e) {}
					break;
				case ASTNode.ANONYMOUS_CLASS_DECLARATION:
					for (Object field : ((AnonymousClassDeclaration) n).bodyDeclarations()) {
						if (((BodyDeclaration) field).getNodeType() == ASTNode.FIELD_DECLARATION) {
							Matcher matcher = variablePattern.matcher(field.toString());
							if (matcher.find() && field.toString().trim().startsWith("String dataLeAk")) {
								count = Integer.valueOf(matcher.group(2));
								insertSink(node, index, count, Block.STATEMENTS_PROPERTY, method);
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
					} catch (NullPointerException e) {}
					break;
				case ASTNode.FIELD_DECLARATION:
					try {
						inStaticContext = Modifier.isStatic(((FieldDeclaration) n).getModifiers());
					} catch (NullPointerException e) {}
					break;
				/*case ASTNode.BLOCK:
					int parentIndex = 0;
					for (Object obj : ((Block) n).statements()) {
						if (obj.toString().startsWith("super") || obj.toString().contains("dataLeAk")) {
							parentIndex++;
						}
					}
					counter++;
					insertSource(n, parentIndex, Block.STATEMENTS_PROPERTY);
					insertSink(node, index, counter, Block.STATEMENTS_PROPERTY);
					break;*/
				case ASTNode.TYPE_DECLARATION:
					counter++;
					insertSource(n, 0, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
					insertSink(node, index, counter, Block.STATEMENTS_PROPERTY, method);
					try {
						inStaticContext = Modifier.isStatic(((TypeDeclaration) n).getModifiers());
					} catch (NullPointerException e) {}
					break;
				case ASTNode.ANONYMOUS_CLASS_DECLARATION:
					counter++;
					insertSource(n, 0, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY);
					insertSink(node, index, counter, Block.STATEMENTS_PROPERTY, method);
					break;
				}
				n = n.getParent();	
			}
			return true;
		}
	}
	
	public static void main(String[] args) {
		new Muse().runMuse(args);
	}
}

