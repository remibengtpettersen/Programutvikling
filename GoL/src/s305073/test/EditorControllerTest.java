package s305073.test;

import model.DynamicGameOfLife;
import model.GameOfLife;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import s305073.controller.EditorController;

import static org.junit.Assert.*;

public class EditorControllerTest {

    private EditorController editorController;
    private GameOfLife gol;

    @Before
    public void setup() {
        editorController = new EditorController();
        gol = new DynamicGameOfLife();
    }

    @After
    public void tearDown() {

    }

    /**
     * Checks that test project is configured correctly if pass.
     */
    @Test
    public void testParrot() {
        assertTrue(true);
    }

    @Test
    public void testBoundingBoxReturnsCorrectWidth() {
        gol.changeCellState(0, 0);
        gol.changeCellState(0, 1);
        gol.changeCellState(0, 2);

        int[] patternSize = gol.getBoundingBox();

        assertEquals(3, patternSize[3] - patternSize[2] + 1);
    }

    @Test
    public void testBoundingBoxReturnsCorrectHeight() {
        gol.changeCellState(0, 0);
        gol.changeCellState(1, 0);
        gol.changeCellState(2, 0);

        int[] patternSize = gol.getBoundingBox();

        assertEquals(3, patternSize[1] - patternSize[0] + 1);
    }
}