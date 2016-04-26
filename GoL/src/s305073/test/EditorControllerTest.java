package s305073.test;

import javafx.scene.paint.Color;
import model.Cell;
import model.DynamicGameOfLife;
import model.GameOfLife;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import s305073.controller.EditorController;

import static org.junit.Assert.*;

/**
 * Created by remibengtpettersen on 13.04.2016.
 */
public class EditorControllerTest {


    @Test
    public void test_ifDiagonalLine_ThenReturnCoordinates() {
        EditorController editorController = new EditorController();
        DynamicGameOfLife gol = new DynamicGameOfLife();

        //gol.getGrid().add()
        /*gol.getGrid()[1][2] = true;
        gol.getGrid()[2][3] = true;
        gol.getGrid()[3][4] = true;
        gol.getGrid()[4][5] = true;*/

        editorController.getDeepCopyGol(gol);

        boolean[][] bluePrint = new boolean[7][7];

        bluePrint[1][2] = true;
        bluePrint[2][3] = true;
        bluePrint[3][4] = true;
        bluePrint[4][5] = true;


    }

    @Test
    public void test_SizeofPattern() {
        EditorController editorController = new EditorController();
        GameOfLife gol = new GameOfLife(7, 7);
        Cell cell = new Cell(Color.BLACK, Color.WHITE);
        cell.setSize(10);

        gol.getGrid()[1][2] = true;
        gol.getGrid()[2][3] = true;
        gol.getGrid()[3][4] = true;
        gol.getGrid()[4][5] = true;

        //editorController.getDeepCopyGol(gol);

        double size = 4 * cell.getSize();

    }
}