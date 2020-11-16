package edu.wm.cs.muse.gui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Interceptor{

    public interface Block {
        void call() throws Exception;
    }	
    
    /*
     * Helper class. This class intercepts all System.out.Println statements
     * and redirects them to a desired location. We use this to capture the
     * runtime output from Muse, and redirect it to the GUI TextArea.
     */
    public static String copyOut(Block block) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(bos, true);
        PrintStream oldStream = System.out;
        System.setOut(printStream);
        try {
            block.call();
        }
        finally {
            System.setOut(oldStream);
        }
        return bos.toString();
    }
}