package test;

import model.DynamicGameOfLife;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Pair programmed.
 *
 * Test class for Game of Life dynamic game board.
 */

public class DynamicGameOfLifeTest {

    private DynamicGameOfLife gol;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * Verify that test project is correctly configured.
     */
    @Test
    public void testParrot() {
        assertTrue(true);
    }

    @Test
    public void testFitToPattern_ifPatternIsLessThenGrid_thenFitGridToPattern() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set pattern
        gol.setCellAlive(1, 1);
        gol.setCellAlive(1, 5);
        gol.setCellAlive(5, 1);
        gol.setCellAlive(5, 5);

        // verify pattern size
        assertEquals(6, gol.getGridWidth());
        assertEquals(6, gol.getGridHeight());

        // remove pattern
        gol.setCellDead(1, 1);
        gol.setCellDead(1, 5);
        gol.setCellDead(5, 1);
        gol.setCellDead(5, 5);

        // verify size is unchanged
        assertEquals(6, gol.getGridWidth());
        assertEquals(6, gol.getGridHeight());

        // fit to pattern
        gol.fitBoardToPattern();

        // assert size is changed
        assertEquals(3, gol.getGridWidth());
        assertEquals(3, gol.getGridHeight());
    }

    @Test
    public void testClone_ifClonedAndIncrementOneGeneration_thenGridIsDifferent() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set alive cells (Blinker)
        gol.setCellAlive(0, 0);
        gol.setCellAlive(1, 0);
        gol.setCellAlive(2, 0);

        assertEquals("1 1 1", gol.toString());

        // instantiate clone reference
        DynamicGameOfLife clone;

        // clone
        clone = gol.clone();

        // increment one generation
        clone.nextGeneration();

        // assert blinker has changed in clone
        assertEquals("000 000 111 000 000", clone.toString());
    }

    @Test
    public void testClearGrid_ifCleared_thenReturnEmptyGrid() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set alive cells (Blinker)
        gol.setCellAlive(0, 0);
        gol.setCellAlive(1, 0);
        gol.setCellAlive(2, 0);

        // assert grid is populated
        assertEquals("1 1 1", gol.toString());

        // assert width and height
        assertEquals(3, gol.getGridWidth());
        assertEquals(1, gol.getGridHeight());

        // clear grid
        gol.clearGrid();

        // assert grid is empty
        assertFalse(gol.isCellAlive(0, 0));
    }

    @Test
    public void testDecreaseXLeft_ifCellIsRemovedLeft_thenDecreaseLeft() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set alive cells (Blinker)
        gol.setCellAlive(0, 0);
        gol.setCellAlive(1, 0);
        gol.setCellAlive(2, 0);
        gol.setCellAlive(3, 0);
        gol.setCellAlive(4, 0);

        //assert pattern width
        assertEquals(5, gol.getGridWidth());

        // remove cells to the left
        gol.setCellDead(0, 0);
        gol.setCellDead(1, 0);
        gol.setCellDead(2, 0);
        gol.setCellDead(3, 0);

        // fit to pattern
        gol.fitBoardToPattern();

        // assert size changed
        assertEquals(3, gol.getGridWidth());
    }

    @Test
    public void testDecreaseYTop_ifCellIsRemovedTop_thenDecreaseTop() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set alive cells (Blinker)
        gol.setCellAlive(0, 0);
        gol.setCellAlive(0, 1);
        gol.setCellAlive(0, 2);
        gol.setCellAlive(0, 3);
        gol.setCellAlive(0, 4);

        //assert pattern width
        assertEquals(5, gol.getGridHeight());

        // remove cells to the left
        gol.setCellDead(0, 0);
        gol.setCellDead(0, 1);
        gol.setCellDead(0, 2);
        gol.setCellDead(0, 3);

        // fit to pattern
        gol.fitBoardToPattern();

        // assert size changed
        assertEquals(3, gol.getGridWidth());
    }

    @Test
    public void testExtractPattern_ifOneCellAlive_thenReturnArrayContainingOneCell() throws Exception {
        // set up blue print array
        boolean[][] bluePrint = new boolean[1][1];
        bluePrint[0][0] = true;

        // set one alive cell
        gol.setCellAlive(1, 1);

        // create empty test array
        boolean[][] testArray;

        // extract pattern
        testArray = gol.extractPattern();

        // assert that arrays are equal
        assertArrayEquals(bluePrint, testArray);
    }

    @Test
    public void testExtractPattern_ifGlider_thenReturnGliderInAThreeByThreeArray() {
        // set up blue print array
        boolean[][] bluePrint = new boolean[3][3];
        bluePrint[0][1] = true;
        bluePrint[1][2] = true;
        bluePrint[2][0] = true;
        bluePrint[2][1] = true;
        bluePrint[2][2] = true;

        // set glider in grid
        gol.setCellAlive(5, 5);
        gol.setCellAlive(6, 6);
        gol.setCellAlive(7, 4);
        gol.setCellAlive(7, 5);
        gol.setCellAlive(7, 6);

        // create empty test array
        boolean[][] testArray;

        // extract pattern
        testArray = gol.extractPattern();

        // assert that extracted glider is equal to blue print
        assertArrayEquals(bluePrint, testArray);
    }

    @Test
    public void testExtractPattern_ifTwoGlidersVertically_thenReturnGliderInAThreeBySix() {
        // set up blue print array
        boolean[][] bluePrint = new boolean[6][3];

        // first glider
        bluePrint[0][1] = true;
        bluePrint[1][2] = true;
        bluePrint[2][0] = true;
        bluePrint[2][1] = true;
        bluePrint[2][2] = true;

        // second glider
        bluePrint[3][1] = true;
        bluePrint[4][2] = true;
        bluePrint[5][0] = true;
        bluePrint[5][1] = true;
        bluePrint[5][2] = true;

        // set glider in grid
        gol.setCellAlive(9, 13);
        gol.setCellAlive(10, 14);
        gol.setCellAlive(11, 12);
        gol.setCellAlive(11, 13);
        gol.setCellAlive(11, 14);

        gol.setCellAlive(12, 13);
        gol.setCellAlive(13, 14);
        gol.setCellAlive(14, 12);
        gol.setCellAlive(14, 13);
        gol.setCellAlive(14, 14);

        // create empty test array
        boolean[][] testArray;

        // extract pattern
        testArray = gol.extractPattern();

        // assert that extracted glider is equal to blue print
        assertArrayEquals(bluePrint, testArray);
    }

    @Test
    public void testExtractPattern_ifTwoGlidersHorizontally_thenReturnGliderInAThreeBySix() {
        // set up blue print array
        boolean[][] bluePrint = new boolean[3][6];

        // first glider
        bluePrint[0][1] = true;
        bluePrint[1][2] = true;
        bluePrint[2][0] = true;
        bluePrint[2][1] = true;
        bluePrint[2][2] = true;

        // second glider
        bluePrint[0][4] = true;
        bluePrint[1][5] = true;
        bluePrint[2][3] = true;
        bluePrint[2][4] = true;
        bluePrint[2][5] = true;

        // set glider in grid
        gol.setCellAlive(12, 10);
        gol.setCellAlive(13, 11);
        gol.setCellAlive(14, 9);
        gol.setCellAlive(14, 10);
        gol.setCellAlive(14, 11);

        gol.setCellAlive(12, 13);
        gol.setCellAlive(13, 14);
        gol.setCellAlive(14, 12);
        gol.setCellAlive(14, 13);
        gol.setCellAlive(14, 14);

        // create empty test array
        boolean[][] testArray;

        // extract pattern
        testArray = gol.extractPattern();

        // assert that extracted glider is equal to blue print
        assertArrayEquals(bluePrint, testArray);
    }
}