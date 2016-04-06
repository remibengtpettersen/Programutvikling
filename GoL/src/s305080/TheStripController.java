package s305080;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.GameOfLife2D;

/**
 * Created by Truls on 06/04/16.
 */
public class TheStripController {
    private boolean[][] grid;
    double cellSize;

    @FXML
    Canvas canvas;
    GraphicsContext gc;

    private GameOfLife2D gol;

    public TheStripController(){
    //    gc = canvas.getGraphicsContext2D();

    }

    public void updateStrip(){
        gol.setGrid(copy(grid));
        gol.updateRuleGrid();
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        cellSize = canvas.getHeight()/grid[0].length;
        for(int i = 0; i < canvas.getWidth(); i += canvas.getWidth()/20){
            for(int x = 0; x < grid.length; x++){
                for(int y = 0; y < grid[x].length; y++){
                    if(gol.getGrid()[x][y]){
                        gc.fillRect(i + cellSize * x, cellSize * y, cellSize, cellSize);
                    }
                }
            }
            gol.nextGeneration();
        }
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
        gol = new GameOfLife2D(grid.length, grid[0].length);
        gol.setGrid(grid);
        gol.updateRuleGrid();
        cellSize = 150 / grid.length;
        canvas.widthProperty().addListener(e -> {
            updateStrip();
        });

    }
}
