package test;

import model.DynamicGameOfLife;
import model.GameOfLife;
import model.StaticGameOfLife;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 */
public class ClassicRuleTest {

    @org.junit.Rule
    public ExpectedException expectedException = ExpectedException.none();

    private GameOfLife gol;

    @Test
    public void testEvolve_ifBlinker_thenRotate(){

        gol = new StaticGameOfLife(5,5);

        // create blinker
        gol.setCellAlive(1, 2);
        gol.setCellAlive(2, 2);
        gol.setCellAlive(3, 2);

        // horizontal
        String expectedBeforeEvolve = "00000 00000 01110 00000 00000";

        // vertical, with expanded board
        String expectedAfterEvolve= "00000 00100 00100 00100 00000";

        // check that the blinker was made
        assertEquals(expectedBeforeEvolve, gol.toString());

        // evolve
        gol.nextGeneration();

        // check that the blinker evolved correctly, and rotated 90 degrees
        assertEquals(expectedAfterEvolve, gol.toString());

        // evolve again
        gol.nextGeneration();

        // check that the blinker rotated another 90 degrees, and becoming equal to the initial blinker
        assertEquals(expectedBeforeEvolve, gol.toString());
    }

    @Test
    public void testEvolve_ifBlock_thenStayStill(){

        gol = new StaticGameOfLife(4,4);

        // create block
        gol.setCellAlive(1, 1);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(1, 2);
        gol.setCellAlive(2, 2);

        // expect the block to stay the same
        String expectedBeforeAndAfterEvolve = "0000 0110 0110 0000";

        // check is the block was made
        assertEquals(expectedBeforeAndAfterEvolve, gol.toString());

        // evolve
        gol.nextGeneration();

        // check if the block is the same
        assertEquals(expectedBeforeAndAfterEvolve, gol.toString());
    }

    @Test
    public void testEvolve_ifRPentomino_thenGrow(){

        gol = new DynamicGameOfLife();

        // make R-pentomino
        gol.setCellAlive(0,1);
        gol.setCellAlive(1,0);
        gol.setCellAlive(1,1);
        gol.setCellAlive(1,2);
        gol.setCellAlive(2,0);

        assertEquals(5, gol.getCellCount());

        for (int i = 0; i < 20; i++) {
            gol.nextGeneration();
        }

        assertEquals(32, gol.getCellCount());
    }

    @Test
    public void testToString_ifClassicRuleSet_thenClassicRuleReturn(){

        gol = new DynamicGameOfLife();

        String expectedString = "B3/S23";

        assertEquals(expectedString, gol.getRule().toString());
    }


}