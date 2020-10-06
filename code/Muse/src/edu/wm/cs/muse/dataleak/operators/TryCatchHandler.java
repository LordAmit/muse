package edu.wm.cs.muse.dataleak.operators;


import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;

public class TryCatchHandler {
	
	protected TryCatchHandler() {
		
	}
	
	protected void addTryCatch(Statement statement) {
		TryStatement tryStatement = statement.getAST().newTryStatement();
		tryStatement.setBody((Block) statement);
		
	}

	
}
