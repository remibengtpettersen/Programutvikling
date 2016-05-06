package test;

import model.DynamicGameOfLife;
import model.EvolveException;
import model.StaticGameOfLife;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 */
@Deprecated
public class ClassicRuleTest {

    @org.junit.Rule
    public ExpectedException expectedException = ExpectedException.none();


    private boolean[][] gridBluePrint;
    private byte[][] neighboursBluePrint;

    private StaticGameOfLife gol;
    //private ClassicRule rule;

    /**
     * Creates a grid to be evolved and a neighbour grid that is pre populated with neighbours count. The neighbour aggregator is tested elsewhere.
     */
    @Before
    public void setUp(){

        gol = new StaticGameOfLife(5,5);

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
                new byte[]{0, 1, 1, 1, 0}};

        // Constructs classicRule
        rule = new ClassicRule(grid, neighbours);

        gridBluePrint = new boolean[3][3];
        neighboursBluePrint = new byte[3][3];*/
    }

    @Test
    public void testEvolve_ifBlinker_thenRotate(){

        // create blinker
        gol.setCellAlive(2, 3);
        gol.setCellAlive(3, 3);
        gol.setCellAlive(4, 3);

        // horizontal
        String expectedBeforeEvolve = "000 000 111 000 000";

        // vertical, with expanded board
        //String expectedAfterEvolve= "000 000 111 000 000";

        // check that the blinker was made
        //assertEquals(expectedBeforeEvolve, gol.toString());

        // evolve
        gol.nextGeneration();

        // check that the blinker evolved correctly
        //assertEquals(expectedAfterEvolve, gol.toString());
    }

    @Test
    public void testEvolve_ifBlock_thenStayStill(){

        // create block
        gol.setCellAlive(0, 0);
        gol.setCellAlive(1, 0);
        gol.setCellAlive(0, 1);
        gol.setCellAlive(1, 1);

        //String expectedBeforeEvolve = ""
    }

//    @Test public void testEvolve_IfCellIsDeadAndOneLiveNeighbour_ThenReturnCellWillDie() throws EvolveException {
//
//        gol.setCellAlive(0,0);
//
//        for (int x = 0; x < gol.getGridWidth(); x++) {
//            for (int y = 0; y < gol.getGridHeight(); y++) {
//
//            }
//        }
//
//        gridBluePrint[0][1] = true;
//
//        for (int i = 0; i < neighboursBluePrint.length; i++) {
//            for (int j = 0; j < neighboursBluePrint[0].length; j++) {
//                neighboursBluePrint[i][j] = 1;
//            }
//        }
//
//        neighboursBluePrint[1][1] = 0;
//
//        /*rule.evolve();
//
//        assertArrayEquals(new boolean[][]{
//                new boolean[]{false, false, false},
//                new boolean[]{false, false, false},
//                new boolean[]{false, false, false}},rule.getGrid());*/
//    }
//
//    @Test public void testIfNeighbourCountHigherThanEight() throws EvolveException {
//        /*rule.setGol(gridBluePrint);
//        rule.setNeighbours(neighboursBluePrint);
//
//        rule.setGol(gridBluePrint);
//        rule.setNeighbours(neighboursBluePrint);
//
//        gridBluePrint[0][1] = true;
//
//        for (int i = 0; i < neighboursBluePrint.length; i++) {
//            for (int j = 0; j < neighboursBluePrint[0].length; j++) {
//                neighboursBluePrint[i][j] = 1;
//            }
//        }
//
//        neighboursBluePrint[1][1] = 9;
//
//        expectedException.expect(EvolveException.class);
//        rule.evolve();*/
//    }
//
//    @Test public void testIfNeighbourCountLowerThanZero() throws EvolveException {
//
//        /*boolean[][] grid = new boolean[3][3];
//        byte[][] neighbours = new byte[3][3];
//
//        rule.setGol(grid);
//        rule.setNeighbours(neighbours);
//
//        for (int i = 0; i < neighbours.length; i++) {
//            for (int j = 0; j <neighbours[0].length; j++) {
//                neighbours[i][j] = 1;
//            }
//        }
//
//        neighbours[1][1] = -1;
//
//        expectedException.expect(EvolveException.class);
//        rule.evolve();*/
//    }
//
//    /**
//     * Runs the evolve method once and checks if the array has evolved correctly
//     */
//    @Test
//    public void testEvolveBlinker() throws EvolveException {
//
//        /*rule.evolve();
//
//        assertArrayEquals(new boolean[][]{
//                new boolean[]{false, false, false, false, false},
//                new boolean[]{false, false, false, false, false},
//                new boolean[]{false, true, true, true, false},
//                new boolean[]{false, false, false, false, false},
//                new boolean[]{false, false, false, false, false}}, rule.getGrid());*/
//    }

}