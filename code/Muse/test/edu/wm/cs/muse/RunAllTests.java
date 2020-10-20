package edu.wm.cs.muse;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.wm.cs.muse.operatorsTest.*;
import edu.wm.cs.muse.placementCheckerTest.PlacementCheckerTest;
import edu.wm.cs.muse.guiTest.*;
import edu.wm.cs.muse.schemasTest.*;

@RunWith(Suite.class)
@SuiteClasses({ 
		MuseTest.class, // General Test
		
        ScopeSourceOperatorTest.class, // Operator Tests
        TaintSinkOperatorTest.class,   // *
        TaintSourceOperatorTest.class, // *
        
        PlacementCheckerTest.class, // PlacementChecker Test
        
        MuseComplexReachabilitySchemaTest.class, // Schema Tests
        MuseReachabilitySchemaTest.class,        // *
        ScopeSinkSchemaTest.class,               // *
        ScopeSourceSchemaTest.class,             // *
        TaintSinkSchemaTest.class,               // *
        TaintSourceSchemaTest.class,             // *
        
        GuiTest.class // Gui Test
        })
public class RunAllTests {

}
