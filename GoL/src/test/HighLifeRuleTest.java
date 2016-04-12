package test;

import model.rules.HighLifeRule;
import model.rules.Rule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tools.Utilities;

import static org.junit.Assert.*;

/**
 */
public class HighLifeRuleTest {

    private Rule rule;

    /**
     * Creates a grid to be evolved and a neighbour grid that is pre populated with neighbours count. The neighbour aggregator is tested elsewhere.
     */
    @Before
    public void setUp() throws Exception {

        // Creates a mock grid to be tested
        boolean[][] grid = new boolean[][]{
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, true, true, false, false},
                new boolean[]{false, true, false, true, false},
                new boolean[]{false, false, true, true, false},
                new boolean[]{false, false, false, false, false}};

        // Creates a neighbour count array manually
        byte[][] neighbours = new byte[][]{
                new byte[]{1, 2, 2, 1, 0},
                new byte[]{2, 2, 3, 2, 1},
                new byte[]{2, 3, 6, 3, 2},
                new byte[]{1, 2, 3, 2, 2},
                new byte[]{0, 1, 2, 2, 1}};

        // Constructs classicRule
        rule = new HighLifeRule(grid, neighbours);
    }

    @After
    public void tearDown() throws Exception {

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
                new boolean[]{false, true, true, false, false},
                new boolean[]{false, true, true, true, false},
                new boolean[]{false, false, true, true, false},
                new boolean[]{false, false, false, false, false}}, rule.getGrid());
    }

}