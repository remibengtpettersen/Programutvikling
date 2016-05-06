package test;

import model.DynamicGameOfLife;
import model.EvolveException;
import model.StaticGameOfLife;
import model.rules.RuleParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *  * @author Pair programmed.
 *
 * Test class for HighLifeRule
 */
public class HighLifeRuleTest {

    @org.junit.Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DynamicGameOfLife gol;

    /**
     * Verify that test project is correctly configured.
     */
    @Test
    public void testParrot() {
        assertTrue(true);
    }

    /**
     * Creates a DynamicGameOfLife object with a HighLife rule
     */
    @Before
    public void setUp() throws Exception {

        gol = new DynamicGameOfLife();
        gol.setRule("B36/S23");
    }

    @Test
    public void testEvolve_ifReplicator_thenCellCountDoubled(){

        // make replicator 3 gen predecessor
        gol.setCellAlive(0,0);
        gol.setCellAlive(1,0);
        gol.setCellAlive(2,0);
        gol.setCellAlive(3,1);
        gol.setCellAlive(3,2);
        gol.setCellAlive(3,3);

        int expectedPredecessorCellCount = 6;
        int expectedReplicatorCellCount = 12;

        // check the predecessor was made
        assertEquals(expectedPredecessorCellCount, gol.getCellCount());

        // evolve to replicator
        for (int i = 0; i < 3; i++) {
            gol.nextGeneration();
        }

        // check that the replicator is made
        assertEquals(12, gol.getCellCount());

        // evolve 12 generations
        for (int i = 0; i < 12; i++) {
            gol.nextGeneration();
        }

        // check that the replicator replicated itself, doubling the cellcount
        assertEquals(2*expectedReplicatorCellCount, gol.getCellCount());
    }

    @Test
    public void testEvolve_ifRPentomino_thenDiesOut(){

        // make R-pentomino
        gol.setCellAlive(0,1);
        gol.setCellAlive(1,0);
        gol.setCellAlive(1,1);
        gol.setCellAlive(1,2);
        gol.setCellAlive(2,0);

        int expectedRPentominoCellCount = 5;

        // check that the R-pentamino was made
        assertEquals(expectedRPentominoCellCount, gol.getCellCount());

        int expectedCellcountAfter9Generations = 0;

        for (int i = 0; i < 9; i++) {
            gol.nextGeneration();
        }

        // check that the R-pentamino died out
        assertEquals(expectedCellcountAfter9Generations, gol.getCellCount());
    }

    @Test
    public void testToString_ifRuleSet_thenRuleReturn(){

        String expectedString = "B36/S23";

        assertEquals(expectedString, gol.getRule().toString());
    }

}