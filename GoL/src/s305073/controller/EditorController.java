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
import model.GameOfLife;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by remibengtpettersen.
 */
public class EditorController {

    private final static int STRIP_TOP_SPACING = 5;
    private final static int SPACING_BETWEEN_GENERATIONS = 8;
    private final static int STRIP_SIZE = 200;

    private GraphicsContext gc;
    private GameOfLife game;
    private Color canvasEditorColor = Color.ALICEBLUE;
    private Color canvasStripColor = Color.WHITE;
    private double canvasWidthToHeightRatio;
    private double stripToEditorWidthRatio;
    private double stripToEditorHeightRatio;
    private List<boolean[][]> generationList;

    @FXML private Canvas canvasEditor;
    @FXML private Canvas canvasStrip;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox vBoxMeta;
    @FXML private RadioButton rbtnCanvasGrid;
    private Color gridLineColor = Color.WHITE;
    private double gridLineSize = 0.5;

    public EditorController() {

        generationList = new ArrayList<>();
    }

    public void initialize(Stage editor){
        setGraphicsContextToEditor();

        game.getCell().setSize(10);

        initializeScrollPane();
        initializeCanvasSizeListener();
        bindEditorCanvasToSize(editor.getScene());
        bindStripCanvasToSize(editor.getScene());

        calculateCanvasToStripRatio();

        renderEditor();
    }

    private void renderEditor() {
        setGraphicsContextToEditor();

        for (int i = 0; i < game.getGrid().length; i++) {
            for (int j = 0; j < game.getGrid()[0].length; j++) {
                if (game.getGrid()[i][j] == true) {
                    fillCellPosition(i, j, Color.BLACK);
                }
            }
        }
    }

    private void setGraphicsContextToEditor() {
        gc = canvasEditor.getGraphicsContext2D();
    }

    private void setGraphicsContextToStrip() {
        gc = canvasStrip.getGraphicsContext2D();
    }

    private void clearPosition(int i, int j) {
        gc.clearRect(i, j, game.getCell().getSize(), game.getCell().getSize());
    }

    private void calculateCanvasToStripRatio() {
        canvasWidthToHeightRatio = canvasEditor.getWidth() / canvasEditor.getHeight();
        stripToEditorWidthRatio = canvasEditor.getWidth() / STRIP_SIZE * canvasWidthToHeightRatio;
        stripToEditorHeightRatio = canvasEditor.getHeight() / STRIP_SIZE;
    }

    private void initializeScrollPane() {
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
    }

    private void initializeCanvasSizeListener() {
        canvasEditor.widthProperty().addListener(event -> {
            setGraphicsContextToEditor();
            setCanvasColor();
            renderEditor();
            gridLinesOnOff();
        });

        canvasEditor.heightProperty().addListener(event -> {
            setGraphicsContextToEditor();
            setCanvasColor();
            renderEditor();
            gridLinesOnOff();
        });

        canvasStrip.widthProperty().addListener(event -> {
            setGraphicsContextToStrip();
            setStripCanvasColor();
        });

        canvasStrip.heightProperty().addListener(event -> {
            setGraphicsContextToStrip();
            setStripCanvasColor();
        });
    }

    private void setStripCanvasColor() {
        setGraphicsContextToStrip();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasEditor.getWidth(), canvasEditor.getHeight());
    }


    private void setCanvasColor() {
        setGraphicsContextToEditor();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasEditor.getWidth(), canvasEditor.getHeight());
    }

    private void bindEditorCanvasToSize(Scene scene) {
        System.out.println(vBoxMeta.getWidth());
        canvasEditor.widthProperty().bind(scene.widthProperty().subtract(200));
        canvasEditor.heightProperty().bind(scene.heightProperty().subtract(220));
    }

    private void bindStripCanvasToSize(Scene scene) {
        canvasStrip.widthProperty().bind(scene.widthProperty());
        canvasStrip.heightProperty().bind(scene.heightProperty());
    }

    @FXML
    public void closeEditor(ActionEvent actionEvent) {
    }

    public void getDeepCopyGol(GameOfLife gameOfLife) {
        try {
            this.game = (GameOfLife) gameOfLife.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void setGridLines() {
        setGraphicsContextToEditor();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.5);

        for (int i = 0; i < canvasEditor.getWidth(); i += game.getCell().getSize()) {
            for (int j = 0; j < canvasEditor.getHeight(); j +=game.getCell().getSize()) {
                gc.strokeLine(i, 0, i, canvasEditor.getHeight());
                gc.strokeLine(0, i, canvasEditor.getWidth(), i);
            }
        }
    }

    private void fillCellPosition(int x_coordinate, int y_coordinate, Color color) {
        gc.setFill(color);

        gc.fillRect(x_coordinate * game.getCell().getSize(),
                y_coordinate * game.getCell().getSize(),
                game.getCell().getSize() * game.getCell().getSpacing(),
                game.getCell().getSize() * game.getCell().getSpacing());
    }

    private void fillStripPosition(int x_coordinate, int y_coordinate, Color color) {
        gc.setFill(color);

        gc.fillRect(x_coordinate * game.getCell().getSize(),
                y_coordinate * game.getCell().getSize(),
                game.getCell().getSize() * game.getCell().getSpacing(),
                game.getCell().getSize() * game.getCell().getSpacing());
    }

    private double stripHeight() {
        return canvasStrip.getHeight();
    }

    private double stripWidth() {
        return canvasStrip.getHeight() * canvasWidthToHeightRatio;
    }

    public void mouseClicked(MouseEvent event) {
        int gridLocation_x = (int)event.getX() / (int) game.getCell().getSize();
        int gridLocation_y = (int)event.getY() / (int) game.getCell().getSize();

        game.getGrid()[gridLocation_x][gridLocation_y] = true;

        setGraphicsContextToEditor();
        fillCellPosition(gridLocation_x, gridLocation_y, Color.BLACK);
    }

    public void mouseDragged(MouseEvent event) {
        int gridLocation_x = (int)event.getX() / (int) game.getCell().getSize();
        int gridLocation_y = (int)event.getY() / (int) game.getCell().getSize();

        setGraphicsContextToEditor();
        fillCellPosition(gridLocation_x, gridLocation_y, Color.BLACK);
    }

    @FXML
    public void updateStrip(ActionEvent actionEvent) {
        Affine xForm = new Affine();
        double tx = 1;

        setGraphicsContextToStrip();

        for (int i = 0; i < 20 ; i++) {
            xForm.setTx(tx);
            gc.setTransform(xForm);
            tx += 300;
        }
        xForm.setTx(0.0);
        gc.setTransform(xForm);
    }

    private void renderStrip() {
        setGraphicsContextToStrip();

        for (int i = 0; i < game.getGrid().length; i++) {
            for (int j = 0; j < game.getGrid()[0].length; j++) {
                if (game.getGrid()[i][j] == true) {
                    fillCellPosition(i, j, Color.BLACK);
                }
            }
        }
    }

    public void triggerGridLines(ActionEvent actionEvent) {
        gridLinesOnOff();
    }

    private void gridLinesOnOff() {
        if (rbtnCanvasGrid.isSelected()) {
            setGridLines();
        }
        else {
            clearGridLines();
        }
    }

    private void clearGridLines() {
        setGraphicsContextToEditor();
        gc.setStroke(gridLineColor);
        gc.setLineWidth(gridLineSize);

        for (int i = 0; i < canvasEditor.getWidth(); i += game.getCell().getSize()) {
            for (int j = 0; j < canvasEditor.getHeight(); j +=game.getCell().getSize()) {
                gc.strokeLine(i, 0, i, canvasEditor.getHeight());
                gc.strokeLine(0, i, canvasEditor.getWidth(), i);
            }
        }
    }

    @FXML
    public void savePatternToFile(ActionEvent actionEvent) {
    }
}
