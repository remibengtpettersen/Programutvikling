package test;

import javafx.scene.paint.Color;
import model.Cell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testing class for Cell
 */
public class CellTest {

    private Cell cell;

    @Before
    public void setUp() throws Exception {

        cell = new Cell();
    }

    @Test
    public void testCalculateGhostColor() throws Exception {

        // set initial color to be red
        Color initialColor = new Color(1, 0, 0, 1);
        cell.setColor(initialColor);

        // set initial dead/background color to be blue
        Color initialDeadColor = new Color(0, 0, 1, 1);
        cell.setDeadColor(initialDeadColor);

        // we expect we will get the average color
        double expectedGhostRed = initialColor.getRed()/2;
        double expectedGhostBlue = initialDeadColor.getBlue()/2;

        // check if we get the average color
        assertEquals(expectedGhostRed, cell.getGhostColor().getRed(), 0.0 );
        assertEquals(expectedGhostBlue, cell.getGhostColor().getBlue(), 0.0 );
    }

    @Test
    public void testSetSize(){

        double expectedResultMinimum = 0.1;
        double expectedResultMaximum = 100;

        // attempt to set size below minimum
        cell.setSize(0);

        // check if cell size was clamped to minimum limit
        assertEquals(expectedResultMinimum, cell.getSize(), 0.0);

        // attempt to set size over maximum
        cell.setSize(101);

        // check if cell size was clamped to maximum limit
        assertEquals(expectedResultMaximum, cell.getSize(), 0.0);
    }

}