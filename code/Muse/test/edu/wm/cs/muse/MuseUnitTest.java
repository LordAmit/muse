package edu.wm.cs.muse;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.wm.cs.muse.dataleak.support.OperatorType;


/**
 * 
 * @author Scott Murphy
 * 
 *         These Unit Tests are meant to test helper functionality of Muse.java
 *         methods
 * 
 */

public class MuseUnitTest {

    Muse muse;

    @Test
    public void operator_type_SOURCE() {
        prepareTest();
        assertEquals(true, muse.getOperatorType("SOURCE").equals(OperatorType.SOURCE));

    }

    @Test
    public void operator_type_SINK() {
        prepareTest();
        assertEquals(true, muse.getOperatorType("SINK").equals(OperatorType.SINK));
    }

    @Test
    public void operator_type_TAINT() {
        prepareTest();
        assertEquals(true, muse.getOperatorType("TAINT").equals(OperatorType.TAINT));
    }

    @Test
    public void operator_type_TAINTSINK() {
        prepareTest();
        assertEquals(true, muse.getOperatorType("TAINTSINK").equals(OperatorType.TAINTSINK));
    }

    @Test
    public void operator_type_REACHABILITY() {
        prepareTest();
        assertEquals(true, muse.getOperatorType("REACHABILITY").equals(OperatorType.REACHABILITY));
    }

    @Test
    public void operator_type_COMPLEXREACHABILITY() {
        prepareTest();
        assertEquals(true, muse.getOperatorType("COMPLEXREACHABILITY").equals(OperatorType.COMPLEXREACHABILITY));
    }

    private void prepareTest() {
        muse = new Muse();

    }

}
