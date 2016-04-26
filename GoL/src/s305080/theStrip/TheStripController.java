package s305080.theStrip;

import controller.MasterController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import model.Cell;
import model.DynamicGameOfLife;
import model.GameOfLife;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Truls on 06/04/16.
 */
public class TheStripController {

    private ArrayList<ArrayList<AtomicBoolean>> grid;
    double cellSize;

    int minX;
    int maxX;
    int minY;
    int maxY;

    Cell cell;

    @FXML
    public Canvas canvas;
    GraphicsContext gc;

    private DynamicGameOfLife gol;
    private MasterController master;
    private double offsetX;
    private double offsetY;
    private double commonOffsetX;
    private double width;

    public TheStripController(){
    //    gc = canvas.getGraphicsContext2D();

    }

    /**
     * Updates the Strip with the information from the current board
     */
    public void updateStrip(){

        gol.deepCopyOnSet(grid);

        if(!gol.getRule().toString().equals(master.getCanvasController().gol.getRule().toString())) //for some reason this always returns false :/
        {
            gol.setRule(master.getCanvasController().gol.getRule().toString());
            System.out.println("changed rule");
        }

        cellSize = canvas.getHeight()/(master.getCanvasController().getCanvas().getHeight() / cell.getSize());
        offsetX = master.getCanvasController().getCommonOffsetX()*cellSize/cell.getSize();
        offsetY = master.getCanvasController().getCommonOffsetY()*cellSize/cell.getSize();
        width = canvas.getHeight() *
                master.getCanvasController().getCanvas().getWidth() /
                master.getCanvasController().getCanvas().getHeight();

        Affine xform = new Affine();
        double tx = 0;
        xform.setTx(tx);
        gc.setTransform(xform);

        clearCanvasAndSetColors();

        while (gc.getTransform().getTx() < canvas.getWidth()){
            xform.setTx(tx);
            gc.setTransform(xform);

            renderCanvas();
            gol.nextGeneration();

            gc.strokeLine(0,0,0,canvas.getHeight());
            tx += width;
        }

        /*
        for(int i = 0; i < canvas.getWidth(); i += (maxX - minX) * cellSize){
            for(int x = minX; x < maxX; x++){
                for(int y = minY; y < maxY; y++){
                    if(gol.getGrid()[x][y]){
                        gc.fillRect(i + cellSize * x - minX * cellSize, cellSize * y - minY * cellSize, cellSize * 0.9, cellSize * 0.9);
                    }
                }
            }
            gol.nextGeneration();
            gc.strokeLine(i,0,i,canvas.getHeight());
        }
        */

    }

    private void renderCanvas() {
        updateView();
        renderLife();

    }

    private void updateView() {
        minX = (int) (getCommonOffsetX() / cellSize);
        maxX = (int) ((getCommonOffsetX() + width) / cellSize) + 1;
        if (maxX > gol.getGridWidth())
            maxX = gol.getGridWidth();

        minY = (int) (getCommonOffsetY() / cellSize);
        maxY = (int) ((getCommonOffsetY() + canvas.getHeight()) / cellSize) + 1;
        if (maxY > gol.getGridHeight())
            maxY = gol.getGridHeight();

        if (minY < 0)
            minY = 0;
        if (minX < 0)
            minX = 0;
    }

    private void renderLife() {
        gc.setFill(cell.getDeadColor());
        gc.fillRect(0, 0, width, canvas.getHeight());
        gc.setFill(cell.getColor());
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {

                if (gol.isCellAlive(x, y))
                    drawCell(x, y);

            }
        }
    }

    private void drawCell(int x, int y) {
        double x1 = x * cellSize - getCommonOffsetX();
        double xWidth = (x1 < 0) ? cellSize - cellSize * cell.getSpacing() + x1 : cellSize - cellSize * cell.getSpacing();
        gc.fillRect((x1 < 0) ? 0 : x1, y * cellSize - getCommonOffsetY(), xWidth, cellSize - cellSize * cell.getSpacing());
    }


    private void clearCanvasAndSetColors() {
        gc.setFill(cell.getDeadColor());
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setFill(cell.getColor());
        gc.setStroke(gc.getFill());
    }


    public void setGrid(ArrayList<ArrayList<AtomicBoolean>> grid) {
        gc = canvas.getGraphicsContext2D();
        this.grid = grid;
        gol = new DynamicGameOfLife();
        canvas.heightProperty().addListener(e -> {
            updateStrip();
        });

    }

    public void setMaster(MasterController master) {
        this.master = master;
        cell = master.getCanvasController().cell;
    }

    public double getCommonOffsetX() {
        return offsetX + gol.getOffsetX() * cellSize;
    }

    public double getCommonOffsetY() {
        return offsetY + gol.getOffsetY() * cellSize;
    }
}
