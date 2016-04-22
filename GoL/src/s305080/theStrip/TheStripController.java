package s305080.theStrip;

import controller.MasterController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import model.Cell;
import model.GameOfLife;

/**
 * Created by Truls on 06/04/16.
 */
public class TheStripController {

    private boolean[][] grid;
    double cellSize;

    int minX;
    int maxX;
    int minY;
    int maxY;

    Cell cell;

    @FXML
    public Canvas canvas;
    GraphicsContext gc;

    private GameOfLife gol;
    private MasterController master;

    public TheStripController(){
    //    gc = canvas.getGraphicsContext2D();

    }

    /**
     * Updates the Strip with the information from the current board
     */
    public void updateStrip(){

        minX = master.getCanvasController().getCurrViewMinX();
        maxX = master.getCanvasController().getCurrViewMaxX();
        minY = master.getCanvasController().getCurrViewMinY();
        maxY = master.getCanvasController().getCurrViewMaxY();

        gol.deepCopyOnSet(grid);

        if(gol.getRule().toString().equals(master.getCanvasController().dGol.getRule().toString())) //for some reason this always returns false :/
            gol.updateRuleGrid();
        else
            gol.setRule(master.getCanvasController().dGol.getRule().toString()); System.out.println("changed rule");


        cellSize = canvas.getHeight()/(maxY - minY);



        Affine xform = new Affine();
        double tx = 0;
        xform.setTx(tx);
        gc.setTransform(xform);

        clearCanvasAndSetColors();

        for(int i = 0; i < canvas.getWidth() / ((maxX - minX) * cellSize); i++){
            xform.setTx(tx);
            gc.setTransform(xform);
            gol.nextGeneration();

            for(int x = minX; x < maxX; x++){
                for(int y = minY; y < maxY; y++){
                    if(gol.getGrid()[x][y]){
                        gc.fillRect(cellSize * x - minX * cellSize, cellSize * y - minY * cellSize, cellSize * 0.9, cellSize * 0.9);
                    }
                }
            }
            gc.strokeLine(0,0,0,canvas.getHeight());
            tx += (maxX - minX) * cellSize;
        }

        /*
        for(int i = 0; i < canvas.getWidth(); i += (maxX - minX) * cellSize){
            for(int x = minX; x < maxX; x++){
                for(int y = minY; y < maxY; y++){
                    if(dGol.getGrid()[x][y]){
                        gc.fillRect(i + cellSize * x - minX * cellSize, cellSize * y - minY * cellSize, cellSize * 0.9, cellSize * 0.9);
                    }
                }
            }
            dGol.nextGeneration();
            gc.strokeLine(i,0,i,canvas.getHeight());
        }
        */

    }

    private void clearCanvasAndSetColors() {
        gc.setFill(cell.getDeadColor());
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setFill(cell.getColor());
        gc.setStroke(gc.getFill());
    }


    public void setGrid(boolean[][] grid) {
        gc = canvas.getGraphicsContext2D();
        this.grid = grid;
        gol = new GameOfLife(grid.length, grid[0].length);
        gol.setGrid(grid);
        gol.updateRuleGrid();
        cellSize = 150 / grid.length;
        canvas.heightProperty().addListener(e -> {
            updateStrip();
        });

    }

    public void setMaster(MasterController master) {
        this.master = master;
        cell = master.getCanvasController().cell;
    }
}
