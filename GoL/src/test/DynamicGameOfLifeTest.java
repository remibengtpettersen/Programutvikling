package test;

import model.DynamicGameOfLife;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
        gol.setCellAlive(0, 1);
        gol.setCellAlive(0, 2);

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
        assertEquals("111", gol.toString());

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
}