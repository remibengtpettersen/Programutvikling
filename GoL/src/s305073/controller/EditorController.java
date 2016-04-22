package s305073.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import model.Cell;
import model.GameOfLife;

/**
 * Created by remibengtpettersen.
 */
public class EditorController {

    @FXML private Canvas canvas;
    @FXML private Canvas strip;
    private GameOfLife gol;
    private Cell cell;
    double size;

    private GraphicsContext gc;

    public EditorController() {
        cell = new Cell();
        cell.setSize(10);
        cell.setSpacing(0.1);
        size = cell.getSize();
    }

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
    }

    public void initializeController() {
        gol.getBoundingBox();

        int y_1 = getPatternMinRowCoordinate();
        int y_2 = getPatternMaxRowCoordinate();
        int x_1 = getPatternMinColumnCoordinate();
        int x_2 = getPatternMaxColumnCoordinate();

        int cell_x = (int)(canvas.getWidth() / cell.getSize());
        int cell_y = (int)(canvas.getHeight() / cell.getSize());

        gc.setFill(Color.RED);
        gc.fillRect(y_1, x_1, y_2 - y_1, x_2 - x_1);

        System.out.println(x_1 + " " + x_2 + " " + y_1 + " " + y_2 + " ");
    }

    public void getDeepCopyGol(GameOfLife gol) {
        this.gol = gol;
    }

    public int getPatternMinRowCoordinate() {
        return gol.getBoundingBox()[0];
    }

    public int getPatternMaxRowCoordinate() {
        return gol.getBoundingBox()[1];
    }

    public int getPatternMinColumnCoordinate() {
        return gol.getBoundingBox()[2];
    }

    public int getPatternMaxColumnCoordinate() {
        return gol.getBoundingBox()[3];
    }

    public double getSizePatternY() {
        return (getPatternMaxRowCoordinate() - getPatternMinRowCoordinate() + 1) * cell.getSize();
    }

    public double getSizePatternX() {
        return (getPatternMaxColumnCoordinate() - getPatternMinColumnCoordinate() + 1) * cell.getSize();
    }
}