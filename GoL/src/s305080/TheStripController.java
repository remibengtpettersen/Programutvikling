package s305080;

import controller.MasterController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    @FXML
    Canvas canvas;
    GraphicsContext gc;

    private GameOfLife gol;
    private MasterController master;

    public TheStripController(){
    //    gc = canvas.getGraphicsContext2D();

    }

    public void updateStrip(){

        minX = master.getCanvasController().getCurrViewMinX();
        maxX = master.getCanvasController().getCurrViewMaxX();
        minY = master.getCanvasController().getCurrViewMinY();
        maxY = master.getCanvasController().getCurrViewMaxY();

        gol.setGrid(copy(grid));
        gol.updateRuleGrid();

        clearCanvasAndSetColors();

        cellSize = canvas.getHeight()/(maxY - minY);
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
    }

    private void clearCanvasAndSetColors() {
        gc.setFill(master.getCanvasController().getCell().getDeadColor());
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setFill(master.getCanvasController().getCell().getColor());
        gc.setStroke(gc.getFill());
    }

    private boolean[][] copy(boolean[][] array) {

        boolean[][] copiedBoard = new boolean[array.length][array.length];

        for(int i = 0; i < array.length; i++){
            copiedBoard[i] = array[i].clone();
        }
         return copiedBoard;
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
    }
}
