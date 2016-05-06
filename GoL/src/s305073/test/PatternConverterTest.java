package s305073.test;

import model.DynamicGameOfLife;
import model.GameOfLife;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import s305073.model.PatternConverter;

import static org.junit.Assert.*;

/**
 * Created by remibengtpettersen on 06.05.2016 at 11.47.
 */
public class PatternConverterTest {

    private GameOfLife gol;

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testRleConverter_ifGridOneByOneAndCellAlive_thenReturnOccupied() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set alive
        gol.setCellAlive(0, 0);

        // assert if output is "o"
        assertEquals("o!", PatternConverter.ConvertPatternToRle(gol));
    }

    @Test
    public void testRleConverter_ifGridTwoByTwoAllAlive_thenReturnOccupied() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set all values true
        gol.setCellAlive(0, 0);
        gol.setCellAlive(0, 1);
        gol.setCellAlive(1, 0);
        gol.setCellAlive(1, 1);

        // assert pattern is correct
        assertEquals("2o$2o!", PatternConverter.ConvertPatternToRle(gol));
    }

    @Test
    public void testRleConverter_ifGridTwoByTwoALiveTopRightCorner_thenReturnOneOccupied() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set top right corner in grid true
        gol.setCellAlive(0, 1);

        // assert pattern is correct
        assertEquals("o!", PatternConverter.ConvertPatternToRle(gol));
    }

    /*
    #C This is a glider.
    x = 3, y = 3
    bo$2bo$3o!
    */
    @Test
    public void testRleConverter_ifGlider_thenReturnGliderInRLE() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set glider
        gol.setCellAlive(0, 1);
        gol.setCellAlive(1, 2);
        gol.setCellAlive(2, 0);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(2, 2);

        // assert pattern is correct
        assertEquals("bo$2bo$3o!", PatternConverter.ConvertPatternToRle(gol));
    }

    @Test
    public void testRleConverter_ifDoubleVerticalGliderWithLineBetween_thenReturnGliderInRLE() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set first glider
        gol.setCellAlive(0, 1);
        gol.setCellAlive(1, 2);
        gol.setCellAlive(2, 0);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(2, 2);

        // set second glider
        gol.setCellAlive(4, 1);
        gol.setCellAlive(5, 2);
        gol.setCellAlive(6, 0);
        gol.setCellAlive(6, 1);
        gol.setCellAlive(6, 2);

        // assert pattern is correct
        assertEquals("bo$2bo$3o$3b$bo$2bo$3o!", PatternConverter.ConvertPatternToRle(gol));
    }

    @Test
    public void testRleConverter_ifDoubleHorizontalGliderWithSixLinesBetween_thenReturnGliderInRLE() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set first glider
        gol.setCellAlive(0, 1);
        gol.setCellAlive(1, 2);
        gol.setCellAlive(2, 0);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(2, 2);

        // set second glider
        gol.setCellAlive(0, 8);
        gol.setCellAlive(1, 9);
        gol.setCellAlive(2, 7);
        gol.setCellAlive(2, 8);
        gol.setCellAlive(2, 9);

        // assert pattern is correct
        assertEquals("bo6bo$2bo6bo$3o4b3o!", PatternConverter.ConvertPatternToRle(gol));
    }

    @Test
    public void testGetPatternSize_ifWidthTen_thenReturnTen() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set first glider
        gol.setCellAlive(0, 1);
        gol.setCellAlive(1, 2);
        gol.setCellAlive(2, 0);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(2, 2);

        // set second glider
        gol.setCellAlive(0, 8);
        gol.setCellAlive(1, 9);
        gol.setCellAlive(2, 7);
        gol.setCellAlive(2, 8);
        gol.setCellAlive(2, 9);

        int width = PatternConverter.getPatternSize(gol)[1];

        assertEquals(10, width);
    }

    @Test
    public void testGetPatternSize_ifWidthThree_thenReturnThree() {
        // instantiate gol
        gol = new DynamicGameOfLife();

        // set first glider
        gol.setCellAlive(0, 1);
        gol.setCellAlive(1, 2);
        gol.setCellAlive(2, 0);
        gol.setCellAlive(2, 1);
        gol.setCellAlive(2, 2);

        // set second glider
        gol.setCellAlive(0, 8);
        gol.setCellAlive(1, 9);
        gol.setCellAlive(2, 7);
        gol.setCellAlive(2, 8);
        gol.setCellAlive(2, 9);

        int width = PatternConverter.getPatternSize(gol)[0];

        assertEquals(3, width);
    }

}