package edu.wm.cs.muse.dataleak.support;

public class TypeMismatchException extends Exception {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 842241097234226782L;

	public TypeMismatchException() {
    	super();
    }

    public TypeMismatchException(String message)
    {
       super(message);
    }
}
