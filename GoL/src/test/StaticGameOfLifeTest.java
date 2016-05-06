package test;

import model.StaticGameOfLife;
import org.junit.*;


import static org.junit.Assert.*;

/**
 * @author Pair programmed.
 *
 * Test class for Game of Life static game board.
 */
public class StaticGameOfLifeTest {

    private StaticGameOfLife gol;

    /**
     * Verify that test project is correctly configured.
     */
    @Test
    public void testParrot() {
        assertTrue(true);
    }

    @Test
    public void testCreateGameBoard_ifFiveByFiveGrid_thenReturnWidthAndHeightEqualToFive(){
        StaticGameOfLife staticGameofLife = new StaticGameOfLife(5, 5);
        assertEquals(5, staticGameofLife.getGrid().length);
        assertEquals(5, staticGameofLife.getGrid()[0].length);
        assertEquals(5, staticGameofLife.getNeighbours().length);
        assertEquals(5, staticGameofLife.getNeighbours()[0].length);
    }

    @Test
    public void testAggregateNeighbours_ifAllNeighboursDead_thenReturnNeighbours() throws Exception {
        // instantiate gol
        gol = new StaticGameOfLife(3, 3);

        // aggregate neighbours - column 0 to 2
       String neighbours= gol.getAggregatedNeighbours();

        // assert if all zero
        assertEquals("000 000 000", neighbours);
    }

    @Test
    public void testAggregateNeighbours_ifOneAlive_thenReturnOneNeighbour() throws Exception {
        // instantiate gol
        gol = new StaticGameOfLife(3, 3);

        // set alive cell
        gol.setCellAlive(1, 1);

        // aggregate neighbours - column 0 to 2
        String neighbours= gol.getAggregatedNeighbours();

        // assert equal
        assertEquals("111 101 111", neighbours);
    }

    @Test
    public void testNextGeneration_ifDeadCellWithNoNeighbours_thenStayDead() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifDeadCellWithOneNeighbours_thenStayDead() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive neighbour
        gol.setCellAlive(2, 2);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(1, 1));
    }

    @Test
    public void testNextGeneration_ifDeadCellWithTwoNeighbours_thenStayDead() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive neighbours
        gol.setCellAlive(2, 1);
        gol.setCellAlive(2, 2);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(1, 1));
    }

    @Test
    public void testNextGeneration_ifDeadCellWithThreeNeighbours_thenComeAlive() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive neighbours
        gol.setCellAlive(1, 2);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(2, 2);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertTrue(gol.isCellAlive(1, 1));
    }

    @Test
    public void testNextGeneration_ifDeadCellWithFourNeighbours_thenStayDead() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifDeadCellWithFiveNeighbours_thenStayDead() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);
        gol.setCellAlive(3, 3);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifDeadCellWithSixNeighbours_thenStayDead() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);
        gol.setCellAlive(3, 3);
        gol.setCellAlive(2, 3);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifDeadCellWithSevenNeighbours_thenStayDead() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);
        gol.setCellAlive(3, 3);
        gol.setCellAlive(2, 3);
        gol.setCellAlive(1, 3);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifDeadCellWithEightNeighbours_thenStayDead() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);
        gol.setCellAlive(3, 3);
        gol.setCellAlive(2, 3);
        gol.setCellAlive(1, 3);
        gol.setCellAlive(1, 2);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifAliveCellWithOneNeighbour_thenDie() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set self alive
        gol.setCellAlive(2, 2);

        // set alive neighbour
        gol.setCellAlive(1, 1);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifAliveCellWithTwoNeighbour_thenNoChange() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set self alive
        gol.setCellAlive(2, 2);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertTrue(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifAliveCellWithThreeNeighbour_thenNoChange() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set self alive
        gol.setCellAlive(2, 2);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);

        // increment one generation
        gol.nextGeneration();

        // assert if alive
        assertTrue(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifAliveCellWithFourNeighbour_thenDie() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set self alive
        gol.setCellAlive(2, 2);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifAliveCellWithFiveNeighbour_thenDie() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set self alive
        gol.setCellAlive(2, 2);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);
        gol.setCellAlive(2, 3);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifAliveCellWithSixNeighbour_thenDie() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set self alive
        gol.setCellAlive(2, 2);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);
        gol.setCellAlive(2, 3);
        gol.setCellAlive(1, 3);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifAliveCellWithSevenNeighbour_thenDie() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set self alive
        gol.setCellAlive(2, 2);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);
        gol.setCellAlive(3, 3);
        gol.setCellAlive(2, 3);
        gol.setCellAlive(1, 3);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testNextGeneration_ifAliveCellWithEightNeighbour_thenDie() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set self alive
        gol.setCellAlive(2, 2);

        // set alive neighbours
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);
        gol.setCellAlive(3, 2);
        gol.setCellAlive(3, 3);
        gol.setCellAlive(2, 3);
        gol.setCellAlive(1, 3);
        gol.setCellAlive(1, 2);

        // increment one generation
        gol.nextGeneration();

        // assert if dead
        assertFalse(gol.isCellAlive(2, 2));
    }

    @Test
    public void testClearGird_ifClearGrid_thenEmptyGameBoard() {
        // instantiate gol
        gol = new StaticGameOfLife(3, 3);

        // clear
        gol.clearGrid();

        // loop through grid
        for (int x = 0; x < gol.getGrid().length; x++) {
            for (int y = 0; y < gol.getGrid()[0].length; y++) {
                // assert if all cells are false
                assertFalse(gol.isCellAlive(x, y));
            }
        }
    }

    @Test
    public void testClearGird_ifClearGrid_thenEmptyNeighboursGrid() {
        // instantiate gol
        gol = new StaticGameOfLife(3, 3);

        // clear
        gol.clearGrid();

        // loop through grid
        for (int x = 0; x < gol.getGrid().length; x++) {
            for (int y = 0; y < gol.getGrid()[0].length; y++) {
                // assert if all cells are false
                assertEquals(0, gol.getNeighboursAt(x, y));
            }
        }
    }

    @Test
    public void testGetBoundingBox_ifHorizontalBlinker_thenReturnWidthThree() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set horizontal blinker
        gol.setCellAlive(2, 1);
        gol.setCellAlive(2, 2);
        gol.setCellAlive(2, 3);

        // assert if width is three
        assertEquals(3, gol.getBoundingBox()[3] - gol.getBoundingBox()[2] + 1);
    }

    @Test
    public void testGetBoundingBox_ifHorizontalBlinker_thenReturnHeightOne() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set horizontal blinker
        gol.setCellAlive(2, 1);
        gol.setCellAlive(2, 2);
        gol.setCellAlive(2, 3);

        // assert if length is one
        assertEquals(1, gol.getBoundingBox()[1] - gol.getBoundingBox()[0] + 1);
    }

    @Test
    public void testGetBoundingBox_ifVerticalBlinker_thenReturnWidthOne() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set vertical blinker
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);

        // assert if width is one
        assertEquals(1, gol.getBoundingBox()[3] - gol.getBoundingBox()[2] + 1);
    }

    @Test
    public void testGetBoundingBox_ifVerticalBlinker_thenReturnHeightThree() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set vertical blinker
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(3, 1);

        // assert if height is three
        assertEquals(3, gol.getBoundingBox()[1] - gol.getBoundingBox()[0] + 1);
    }

    @Test
    public void testResetNeighboursAt_ifAlive_thenReturnZero() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive cell
        gol.setCellAlive(1, 1);

        // aggregate neighbours
        gol.nextGeneration();

        // reset position (2, 2)
        gol.resetNeighboursAt(2, 2);

        // assert reset
        assertEquals(0, gol.getNeighboursAt(2, 2));
    }

    @Test
    public void testSetCellDead_ifAlive_thenDie() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive cell
        gol.setCellAlive(1, 1);

        // assert is alive
        assertTrue(gol.isCellAlive(1, 1));

        // set cell dead
        gol.setCellDead(1, 1);

        // assert is dead
        assertFalse(gol.isCellAlive(1, 1));
    }

    @Test
    public void testClone_ifClonedAndNextGeneration_thenDifferent() {
        // instantiate gol
        gol = new StaticGameOfLife(5, 5);

        // set alive cells
        gol.setCellAlive(1, 2);
        gol.setCellAlive(2, 2);
        gol.setCellAlive(3, 2);

        // create and instantiate
        StaticGameOfLife clone = new StaticGameOfLife(5, 5);

        // clone
        clone = gol.clone();

        // increment one generation
        clone.nextGeneration();

        // assert blinker has changed in clone
        assertTrue(gol.isCellAlive(1, 2) != clone.isCellAlive(1, 2));
        assertTrue(gol.isCellAlive(2, 1) != clone.isCellAlive(2, 1));
        assertTrue(gol.isCellAlive(2, 2) == clone.isCellAlive(2, 2));
        assertTrue(gol.isCellAlive(2, 3) != clone.isCellAlive(2, 3));
        assertTrue(gol.isCellAlive(3, 2) != clone.isCellAlive(3, 2));
    }
}