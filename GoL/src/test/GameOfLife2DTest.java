package test;

import model.GameOfLife2D;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Andreas on 29.02.2016.
 */
public class GameOfLife2DTest {

    GameOfLife2D gol;

    @Before
    public void setUp() throws Exception {
        gol = new GameOfLife2D(5, 5);
        gol.setGrid(new boolean[][]{
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, true, true, true, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false}});
        gol.aggregateNeighbours();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateGameBoard(){
        assertEquals(5, gol.getGrid().length, 0.1d);
        assertEquals(5, gol.getNeighbours().length, 0.1d);
    }

    @Test
    public void testNextGeneration() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testAggregateNeighbours() throws Exception {
        assertArrayEquals(new byte[][]{
                new byte[]{0, 0, 0, 0, 0},
                new byte[]{1, 2, 3, 2, 1},
                new byte[]{1, 1, 2, 1, 1},
                new byte[]{1, 2, 3, 2, 1},
                new byte[]{0, 0, 0, 0, 0}}, gol.getNeighbours());
    }

    @Test
    public void testGetNeighbours() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testGetGrid() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testSetGrid() throws Exception {
        assertTrue(false);
    }
}