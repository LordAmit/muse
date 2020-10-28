package edu.wm.cs.muse.dataleak.support;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	public TryCatchHandler() {
		
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
		//Uses the class name to get a class object
		Class<?> c = Class.forName(canon_name);
		//Gets all the methods and puts them into an array.
        Method[] allMethods = c.getMethods();
        //Finds the first method within the insertion string.
		String first_method = insertion.substring(insertion.indexOf(".")+1, insertion.indexOf("("));
		//Checks the allMethods list to see if the insertion string methods are in it and if they
		//throw exceptions.
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
	public boolean stringHasThrows(String insertion) throws ClassNotFoundException {
		String[] stringsInitial = insertion.split("=");
		//checks for the keyword "new" and removes it
		for(int i = 0; i < stringsInitial.length; i++ ) {
			String[] new_split = stringsInitial[i].split("new");
			if (new_split.length > 1) {
				stringsInitial[i] = new_split[1];
			}
		}
		String newInsertion = "";
		//Adds the assignment of the variable to stringsInitial
		if (stringsInitial.length>1) {
			newInsertion = stringsInitial[1];
		}
		else {
			newInsertion = stringsInitial[0];
		}
		//removes whitespace
		insertion = newInsertion.replaceAll("\\s", "");
		//creates lists for methods and canon_names once they are found
		List<String> methods = new ArrayList<String>();
		List<String> canon_names = new ArrayList<String>();
		String canon_name = "";
		String[] strings = insertion.split(",|\\.");
		for (int i=0; i<strings.length; i++) {
			//searches for multiple method calls within the string.
			if (strings[i].contains("(")) {
				methods.add(strings[i].substring(0, strings[i].indexOf("(")));
				//if a method was called, adds the canon_name string to the list
				if(canon_name.length()!=0) {
					//will not add to the list if it is already there
					if(!canon_names.contains(canon_name)) {
						canon_names.add(canon_name);
						canon_name = "";
					}
				}
				//checks to make sure there is no character that is part of a canon_name after "("
				//For example, the line android.util.Log.d(javax.crypto.Cipher.getInstance());
				//would check after ".d(" for the keyword "javax".
				if (strings[i].charAt(strings.length-1) != '(' && !strings[i].contains(")") && !strings[i].contains("\"")) {
					canon_name += strings[i].substring(strings[i].indexOf("(")+1);
				}
			}
			else {
				//adds substrings that contain the class name for methods.
				if(canon_name.length() == 0) {
					canon_name+=strings[i];
				}
				else {
					canon_name+=("." + strings[i]);
				}
			}
		}
		//System.out.println("Methods: " + methods.toString());
		//System.out.println("canon string: " + canon_names.toString());
		Class<?> c = null;
		//looks for methods that are in both the insertion string and the canon class
		//and returns true if the method has an exception.
		for(int k = 0; k < canon_names.size(); k++) {
			try {
				c = Class.forName(canon_names.get(k));
				System.out.println(c);
				Method[] allMethods = c.getMethods();
				for (int i=0; i<allMethods.length; i++) {
					for (int j=0; j<methods.size();j++) {
						if (allMethods[i].toString().contains(methods.get(j)+"(")) {
							//System.out.println("Suspected method: " +  methods.get(j)+"(");
							//System.out.println(allMethods[i]);
							if (allMethods[i].getExceptionTypes().length !=0) {
								return true;
							}
						}
					}
				}
			}
			catch (ClassNotFoundException e) {
				//System.out.println("Class not found: " + e);
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
	
	public TryStatement addTryCatch(Statement statement) {
		//Creates a new tryStatement and body for that statement
		TryStatement tryStatement = statement.getAST().newTryStatement();
		Block tryBody = statement.getAST().newBlock();
		//Adds the statement containing the leak with an exception to the body
		//and sets the body to the tryStatement
		tryBody.statements().add(statement);
		tryStatement.setBody(tryBody);
		//Creates an empty catch clause to assign to tryStatement
		CatchClause catchClause = statement.getAST().newCatchClause();
		tryStatement.catchClauses().add(catchClause);
		//Creates a generic exception for the catch clause and assigns it to tryStatement
		SingleVariableDeclaration svd = statement.getAST().newSingleVariableDeclaration();
		catchClause.setException(svd);
		svd.setType(statement.getAST().newSimpleType(statement.getAST().newName("Exception")));
		svd.setName(statement.getAST().newSimpleName("e"));
		Block catchBody = statement.getAST().newBlock();
		catchClause.setBody(catchBody);
		return tryStatement;
	}

	
}
