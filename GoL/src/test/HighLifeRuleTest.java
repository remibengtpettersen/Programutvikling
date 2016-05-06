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

/**
 */
public class HighLifeRuleTest {

    @org.junit.Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DynamicGameOfLife gol;

    /**
     * Creates a grid to be evolved and a neighbour grid that is pre populated with neighbours count. The neighbour aggregator is tested elsewhere.
     */
    @Before
    public void setUp() throws Exception {

        gol = new DynamicGameOfLife();
        gol.setRule(RuleParser.HIGHLIFE_RULESTRING);

        // Creates a mock grid to be tested
        /*boolean[][] grid = new boolean[][]{
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

        gridBluePrint = new boolean[3][3];
        neighboursBluePrint = new byte[3][3];

        // Constructs classicRule
        rule = new HighLifeRule(grid, neighbours);*/
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testEvolve_ifReplicator_thenCellCountIncreased(){

        // make replicator 3 gen predecessor
        gol.setCellAlive(0,0);
        gol.setCellAlive(1,0);
        gol.setCellAlive(2,0);
        gol.setCellAlive(3,1);
        gol.setCellAlive(3,2);
        gol.setCellAlive(3,3);

        int expectedPredecessorCellCount = 6;
        int expectedReplicatorCellCount = 12;

        assertEquals(expectedPredecessorCellCount, gol.getCellCount());

        // evolve to replicator
        for (int i = 0; i < 3; i++) {
            gol.nextGeneration();
        }

        // check that the replicator is made
        assertEquals(12, gol.getCellCount());

        for (int i = 0; i < 12; i++) {
            gol.nextGeneration();
        }

        // check that the replicator replicated itself after 12 generation
        assertEquals(2*expectedReplicatorCellCount, gol.getCellCount());
    }

    @Test
    public void testIfInputArraysAreNotEqualHaveSameDimensions() throws EvolveException {
        /*Rule testRule = new HighLifeRule(new boolean[1][2], new byte[1][1]);
        expectedException.expect(ArrayIndexOutOfBoundsException.class);
        testRule.evolve();*/
    }

    @Test public void testIfCellIsDeadAndOneLiveNeighbourThenReturnCellWillDie() throws EvolveException {
        /*rule.setGol(gridBluePrint);
        rule.setNeighbours(neighboursBluePrint);

        gridBluePrint[0][0] = true;

        for (int i = 0; i < neighboursBluePrint.length; i++) {
            for (int j = 0; j < neighboursBluePrint[0].length; j++) {
                neighboursBluePrint[i][j] = 1;
            }
        }

        neighboursBluePrint[1][1] = 0;

        rule.evolve();

        assertArrayEquals(new boolean[][]{
                    new boolean[]{false, false, false},
                    new boolean[]{false, false, false},
                    new boolean[]{false, false, false}},rule.getGrid());*/
    }

    @Test public void testIfNeighbourCountHigherThanEight() throws EvolveException {
        /*rule.setGol(gridBluePrint);
        rule.setNeighbours(neighboursBluePrint);

        rule.setGol(gridBluePrint);
        rule.setNeighbours(neighboursBluePrint);

        gridBluePrint[0][0] = true;

        for (int i = 0; i < neighboursBluePrint.length; i++) {
            for (int j = 0; j < neighboursBluePrint[0].length; j++) {
                neighboursBluePrint[i][j] = 1;
            }
        }

        neighboursBluePrint[1][1] = 9;

        expectedException.expect(EvolveException.class);
        rule.evolve();*/
    }

    @Test public void testIfNeighbourCountLowerThanZero() throws EvolveException {

        /*boolean[][] grid = new boolean[3][3];
        byte[][] neighbours = new byte[3][3];

        rule.setGol(grid);
        rule.setNeighbours(neighbours);

        grid[0][0] = true;

        for (int i = 0; i < neighbours.length; i++) {
            for (int j = 0; j <neighbours[0].length; j++) {
                neighbours[i][j] = 1;
            }
        }

        neighbours[1][1] = -1;

        expectedException.expect(EvolveException.class);
        rule.evolve();*/
    }

    /**
     * Runs the evolve method once and checks if the array has evolved correctly
     */
    @Test
    public void testEvolve() throws EvolveException {

       /* rule.evolve();

        Utilities.print2DArray(rule.getGrid());

        assertArrayEquals(new boolean[][]{
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, true, true, false, false},
                new boolean[]{false, true, true, true, false},
                new boolean[]{false, false, true, true, false},
                new boolean[]{false, false, false, false, false}}, rule.getGrid());*/
    }
}