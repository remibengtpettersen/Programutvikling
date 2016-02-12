package test;

import model.ClassicRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class ClassicRuleTest {

    private ClassicRule classicRule;

    @Before
    public void setUp() throws Exception {
        classicRule = new ClassicRule();
    }

    @After
    public void tearDown() throws Exception {

    }
    
    @Test
    public void testEvolve() throws Exception {

        //Checks if the cell should live
        assertTrue(classicRule.evolve(3, false));
        assertTrue(classicRule.evolve(2, true));

        //Checks if the cell should die
        assertFalse(classicRule.evolve(2, false));
        assertFalse(classicRule.evolve(4, true));
    }

}