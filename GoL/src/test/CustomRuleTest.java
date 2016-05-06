package test;

import model.DynamicGameOfLife;
import model.EvolveException;
import model.GameOfLife;
import model.StaticGameOfLife;
import model.rules.CustomRule;
import model.rules.Rule;
import model.rules.RuleParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tools.Utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomRuleTest {

    private DynamicGameOfLife gol;

    @Test
    public void testParrot() {
        assertTrue(true);
    }

    /**
     * Creates a grid to be evolved and a neighbour grid that is pre populated with neighbours count. The neighbour aggregator is tested elsewhere.
     */
    @Before
    public void setUp() throws Exception {

        gol = new DynamicGameOfLife();

        // Creates a mock grid to be tested
        /*boolean[][] grid = new boolean[][]{
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
                new byte[]{0, 1, 1, 1, 0}};*/

        // Constructs classicRule
        //rule = new CustomRule(grid, neighbours, "b3/s23");
    }

    @After
    public void tearDown(){

        gol = null;
    }

    /**
     * Runs the evolve method once and checks if the array has evolved correctly
     */
    @Test
    public void testEvolve() throws EvolveException {

        //rule.evolve();

        /*Utilities.print2DArray(rule.getGrid());

        assertArrayEquals(new boolean[][]{
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, true, true, true, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false}}, rule.getGrid());*/
    }

    @Test
    public void testEvolve_ifLWDRuleAndThreeCells_thenGrowToDiamond(){

        gol.setRule(RuleParser.LWD_RULESTRING);

        gol.setCellAlive(3, 3);
        gol.setCellAlive(4, 3);
        gol.setCellAlive(5, 3);

        int expectedCellCount = 29;

        for (int i = 0; i < 5; i++) {
            gol.nextGeneration();
        }

        assertEquals(expectedCellCount, gol.getCellCount());
    }

    @Test
    public void testEvolve_ifSeedsRuleAndTwoCells_thenGrowToTwoSeeds(){

        gol.setRule(RuleParser.SEEDS_RULESTRING);

        gol.setCellAlive(3, 3);
        gol.setCellAlive(4, 3);

        int expectedCellCount = 8;

        for (int i = 0; i < 5; i++) {
            gol.nextGeneration();
        }

        assertEquals(expectedCellCount, gol.getCellCount());
    }

    @Test
    public void testEvolve_ifDiamoebaRuleAndFourByFour_thenGrowToCross(){

        gol.setRule(RuleParser.DIAMOEBA_RULESTRING);

        int expectedCellCount = 20;

        for (int x = 0; x < 4; x++)
            for(int y = 0; y < 4; y++)
                gol.setCellAlive(x, y);

        gol.nextGeneration();

        assertEquals(expectedCellCount, gol.getCellCount());
    }

}