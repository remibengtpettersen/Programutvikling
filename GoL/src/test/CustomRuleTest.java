package test;

import model.rules.ClassicRule;
import model.rules.CustomRule;
import model.rules.Rule2D;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tools.Utilities;

import static org.junit.Assert.*;

/**
 */
public class CustomRuleTest {

    private Rule2D rule;

    /**
     * Creates a grid to be evolved and a neighbour grid that is pre populated with neighbours count. The neighbour aggregator is tested elsewhere.
     */
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
        rule = new CustomRule(grid, neighbours, "b3/s23");
    }

    /**
     * Runs the evolve method once and checks if the array has evolved correctly
     */
    @Test
    public void testEvolve(){

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