package test;

import model.GameOfLife;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class GameBoardTest {

    GameOfLife gol;

    @Before
    public void setUp() throws Exception {
        gol = new GameOfLife(535);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateGameBoard(){


        assertEquals(535, gol.getGrid().length, 0.1d);
        assertEquals(535, gol.getNeighbours().length, 0.1d);
    }




}
