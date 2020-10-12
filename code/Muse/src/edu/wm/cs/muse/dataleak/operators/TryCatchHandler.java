package edu.wm.cs.muse.dataleak.operators;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;

/**
 * Handles the checking of whether an api method call leak throws an exception
 * and appends a try catch block around the method call leak with a catch for 
 * Exception e.  Handles both sinks and sources.
 * 
 * @author Kevin Cortright and Nicholas di Mauro
 *
 */

public class TryCatchHandler {
	
	protected TryCatchHandler() {
		
	}

	/**
	 * Method that returns true if the inserted string contains a method that could throw an error.
	 * Takes the canon name for a class and creates a list of all its methods, then it looks for the first
	 * method within the string insertion and compares that method to the list. If it finds a method that matches,
	 * checks to make sure it doesn't have any exceptions.
	 * 
	 * @param canon_name   the string that contains the class name with package.
	 * @param insertion    the string that will be inserted as a sink/source that contains a method.
	 * @author Nicholas di Mauro
	 *
	 */
	public static boolean stringHasThrows(String canon_name, String insertion) throws ClassNotFoundException{
		Class<?> c = Class.forName(canon_name);
        Method[] allMethods = c.getMethods();
		String first_method = insertion.substring(insertion.indexOf(".")+1, insertion.indexOf("("));
		for(int i=0; i < allMethods.length; i++) {
			if (allMethods[i].toString().contains(first_method)){
				if(allMethods[i].getExceptionTypes().length != 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method that returns true if the inserted string contains a method that contains an exception.
	 * Takes in a string and parses it to get only the parts needed for checking if an exception is
	 * thrown, the class name and package, and the methods called within that package. This method
	 * then takes the class, and checks all the methods invoked by the leak against all the methods
	 * of the class, and if any of them throw an exception, return true, requiring that string be rewritten.
	 * 
	 * @param insertion    the string that will be inserted as a sink/source that contains a method.
	 * @author Kevin Cortright and Nicholas di Mauro
	 *
	 */
	protected boolean stringHasThrows(String insertion) throws ClassNotFoundException {
		String[] stringsInitial = insertion.split("=");
		String newInsertion = "";
		if (stringsInitial.length>1) {
			newInsertion = stringsInitial[1];
		}
		else {
			newInsertion = stringsInitial[0];
		}
		insertion = newInsertion.replaceAll("\\s", "");
		List<String> methods = new ArrayList<String>();
		String canon_name = "";
		String[] strings = insertion.split("\\.");
		for (int i=0; i<strings.length; i++) {
			if (strings[i].contains("(")) {
				methods.add(strings[i].substring(0, strings[i].indexOf("(")));
			}
			else {
				if(canon_name.length() == 0) {
					canon_name+=strings[i];
				}
				else {
					canon_name+=("." + strings[i]);
				}
			}
		}
		Class<?> c = null;
		try {
			c = Class.forName(canon_name);
		}
		catch (ClassNotFoundException e) {
			return false;
		}
		Method[] allMethods = c.getMethods();
		for (int i=0; i<allMethods.length; i++) {
			for (int j=0; j<methods.size();j++) {
				if (allMethods[i].toString().contains(methods.get(j))) {
					if (allMethods[i].getExceptionTypes().length !=0) {
						return true;
					}
				}
			}
		}
		return false; 
		
	} 
	
	/**
	 * Appends a try catch block around the dataleak statement. This method is 
	 * called if the method invoked by the statement throws exceptions.
	 * 
	 * @param statement
	 * @return
	 * @author Kevin Cortright
	 */
	
	protected TryStatement addTryCatch(Statement statement) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("test/output/output.txt"));
			String buffer;
			while ((buffer = br.readLine()) != null) {
			        System.out.println(buffer);
			    }
		}
		catch (Exception e) {
			
		}
		TryStatement tryStatement = statement.getAST().newTryStatement();
		Block tryBody = statement.getAST().newBlock();
		tryBody.statements().add(statement);
		tryStatement.setBody(tryBody);
		CatchClause catchClause = statement.getAST().newCatchClause();
		tryStatement.catchClauses().add(catchClause);
		SingleVariableDeclaration svd = statement.getAST().newSingleVariableDeclaration();
		catchClause.setException(svd);
		svd.setType(statement.getAST().newSimpleType(statement.getAST().newName("Exception")));
		svd.setName(statement.getAST().newSimpleName("e"));
		Block catchBody = statement.getAST().newBlock();
		catchClause.setBody(catchBody);
		return tryStatement;
	}

	
}
