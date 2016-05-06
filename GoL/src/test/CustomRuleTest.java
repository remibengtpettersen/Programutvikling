package test;

import model.DynamicGameOfLife;
import model.EvolveException;
import model.rules.RuleParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CustomRule
 */
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
    }

    @Test
    public void testEvolve_ifLWDRuleAndThreeCells_thenGrowToDiamond(){

        // Life Without Death rule (B3/S012345678)
        gol.setRule(RuleParser.LWD_RULESTRING);

        gol.setCellAlive(3, 3);
        gol.setCellAlive(4, 3);
        gol.setCellAlive(5, 3);

        // the diamond has 29 cells
        int expectedCellCount = 29;

        // evolve 5 generations
        for (int i = 0; i < 5; i++) {
            gol.nextGeneration();
        }

        // check if the result matches with expectation
        assertEquals(expectedCellCount, gol.getCellCount());
    }

    @Test
    public void testEvolve_ifSeedsRuleAndTwoCells_thenGrowToTwoSeeds(){

        // Seeds rule (B2/S)
        gol.setRule(RuleParser.SEEDS_RULESTRING);

        // create two cells next to each other
        gol.setCellAlive(3, 3);
        gol.setCellAlive(4, 3);

        // expect that two seed-gliders
        int expectedCellCount = 8;

        // evolve 5 generations
        for (int i = 0; i < 5; i++) {
            gol.nextGeneration();
        }

        // check if the result matches with expectation
        assertEquals(expectedCellCount, gol.getCellCount());
    }

    @Test
    public void testEvolve_ifDiamoebaRuleAndFourByFour_thenGrowToCross(){

        // Diamoeba rule (B35678/S5678)
        gol.setRule(RuleParser.DIAMOEBA_RULESTRING);

        //create a block that is 4 cells wide and 4 cells high
        for (int x = 0; x < 4; x++)
            for(int y = 0; y < 4; y++)
                gol.setCellAlive(x, y);

        // the 4x4 block
        int expectedInitialCellCount = 16;

        // check that the block was created
        assertEquals(expectedInitialCellCount, gol.getCellCount());

        // evolve
        gol.nextGeneration();

        // the cross has a total of 20 cells
        int expectedResultCellCount = 20;

        // check if the result matches with expectation
        assertEquals(expectedResultCellCount, gol.getCellCount());
    }
}