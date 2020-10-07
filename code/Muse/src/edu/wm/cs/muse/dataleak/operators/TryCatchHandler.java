package edu.wm.cs.muse.dataleak.operators;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;

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
	private boolean rewrite(String insertion) throws ClassNotFoundException {
		String[] stringsInitial = insertion.split("=");
		String newInsertion = stringsInitial[1];
		insertion = newInsertion.replaceAll("\\s", "");
		List<String> methods = new ArrayList<String>();
		String canon_name = null;
		String[] strings = insertion.split(".");
		for (int i=0; i<strings.length; i++) {
			if (strings[i].contains("()")) {
				methods.add(strings[i]);
			}
			else {
				canon_name+=strings[i];
			}
		}
		for (int i=0; i<methods.size(); i++) {
			methods.set(i, methods.get(i).substring(0, methods.get(i).indexOf("(")));
		}
		Class<?> c = Class.forName(canon_name);
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
	
	protected void addTryCatch(Statement statement) {
		TryStatement tryStatement = statement.getAST().newTryStatement();
		tryStatement.setBody((Block) statement);
		
	}

	
}
