package test;

import model.GameOfLife;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by Andreas on 29.02.2016.
 */
public class GameOfLifeTest {

    static GameOfLife gol;
    static boolean [][] grid;

    @BeforeClass
    public static void setUp() throws Exception {
        grid = new boolean[][]{
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, true, true, true, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false}};

        gol = new GameOfLife(5, 5);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateGameBoard(){
        assertEquals(5, gol.getGrid().length);
        assertEquals(5, gol.getNeighbours().length);

    }

    @Test
    public void testAggregateNeighbours() throws Exception {

        gol.setGrid(grid);
        gol.aggregateNeighbours();
        assertArrayEquals(new byte[][]{
                new byte[]{0, 0, 0, 0, 0},
                new byte[]{1, 2, 3, 2, 1},
                new byte[]{1, 1, 2, 1, 1},
                new byte[]{1, 2, 3, 2, 1},
                new byte[]{0, 0, 0, 0, 0}}, gol.getNeighbours());
    }
}