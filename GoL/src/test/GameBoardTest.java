package test;

import model.GameBoard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class GameBoardTest {

    GameBoard gameBoard;

    @Before
    public void setUp() throws Exception {
        gameBoard = new GameBoard();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateGameBoard(){

        gameBoard.createGameBoard(535);

        assertEquals(535, gameBoard.grid.length, 0.1d);
        assertEquals(535, gameBoard.neighbours.length, 0.1d);
    }



}