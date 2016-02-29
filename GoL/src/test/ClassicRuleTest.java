package test;

import model.rules.ClassicRule;
import model.rules.Rule2D;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tools.Utilities;

import static org.junit.Assert.*;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class ClassicRuleTest {

    private Rule2D rule;

    @Before
    public void setUp() throws Exception {

        // Creates a mock grid to be tested
        boolean[][] grid = new boolean[][]{
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, true, false, false},
                new boolean[]{false, false, true, false, false},
                new boolean[]{false, false, true, false, false},
                new boolean[]{false, false, false, false, false}};

        // Creates a neighbour count array manually
        byte[][] neighbours = new byte[][]{
                new byte[]{0, 1, 1, 1, 0},
                new byte[]{0, 2, 1, 2, 0},
                new byte[]{0, 3, 2, 3, 0},
                new byte[]{0, 2, 1, 2, 0},
                new byte[]{0, 1, 1, 1, 0}};

        // Constructs classicRule
        rule = new ClassicRule(grid, neighbours);
    }

    @After
    public void tearDown() throws Exception {

    }
    
    @Test
    public void testEvolve() throws Exception {

        rule.evolve();

        Utilities.print2DArray(rule.getGrid());

        assertArrayEquals(new boolean[][]{
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, true, true, true, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false}}, rule.getGrid());
    }

}