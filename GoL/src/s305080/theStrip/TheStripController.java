package s305080.theStrip;

import controller.MasterController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import model.CameraView;
import model.Cell;
import model.GameOfLife;

/**
 * Created by Truls on 06/04/16.
 */
public class TheStripController {


    double cellSize;


    Cell originalCell;

    @FXML
    public Canvas canvas;
    GraphicsContext gc;

    private  GameOfLife gol;
    private GameOfLife originalGol;
    private MasterController master;

    private int width;
    CameraView cView = new CameraView();
    private CameraView originalCView;

    public TheStripController(){
    //    gc = canvas.getGraphicsContext2D();

    }

    /**
     * Updates the Strip with the information from the current board
     */
    public void updateStrip(){

        gol = originalGol.clone();

        System.out.println(gol.getRule().toString() + " " + originalGol.getRule().toString());
        System.out.println("Are they the same? " + gol.getRule().toString().equals(originalGol.getRule().toString()));
        if(!gol.getRule().toString().equals(originalGol.getRule().toString())) //for some reason this always returns false :/
        {
            gol.setRule(originalGol.getRule().toString());
            System.out.println("changed rule");
        }

        cellSize = canvas.getHeight()/(master.getCanvasController().getCanvas().getHeight() / originalCell.getSize());
        cView.boardOffsetX = (int) (originalCView.getCommonOffsetX(originalGol, originalCell.getSize()) * cellSize / originalCell.getSize());
        cView.boardOffsetY = (int) (originalCView.getCommonOffsetY(originalGol, originalCell.getSize()) * cellSize / originalCell.getSize());
        width = (int) (canvas.getHeight() *
                                master.getCanvasController().getCanvas().getWidth() /
                                master.getCanvasController().getCanvas().getHeight());

        Affine xform = new Affine();
        double tx = 0;
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
        cView.updateView(gol, cellSize, width, (int)canvas.getHeight());
        renderLife();

    }

    private void renderLife() {
        gc.setFill(originalCell.getDeadColor());
        gc.fillRect(0, 0, width, canvas.getHeight());
        gc.setFill(originalCell.getColor());
        for (int x = cView.currViewMinX; x < cView.currViewMaxY; x++) {
            for (int y = cView.currViewMinY; y < cView.currViewMaxY; y++) {

                if (gol.isCellAlive(x, y))
                    drawCell(x, y);

            }
        }
    }

    private void drawCell(int x, int y) {
        double x1 = x * cellSize - cView.getCommonOffsetX(gol, cellSize);
        double xWidth = (x1 < 0) ? cellSize - cellSize * originalCell.getSpacingFactor() + x1 : cellSize - cellSize * originalCell.getSpacingFactor();
        gc.fillRect((x1 < 0) ? 0 : x1, y * cellSize - cView.getCommonOffsetY(gol, cellSize), xWidth, cellSize - cellSize * originalCell.getSpacingFactor());
    }


    private void clearCanvasAndSetColors() {
        gc.setFill(originalCell.getDeadColor());
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setFill(originalCell.getColor());
        gc.setStroke(gc.getFill());
    }


    public void setGol(GameOfLife gol) {
        gc = canvas.getGraphicsContext2D();
        this.originalGol = gol;
        canvas.heightProperty().addListener(e -> {
            updateStrip();
        });

    }

    public void setMaster(MasterController master) {
        this.master = master;
        originalCell = master.getCanvasController().getCell();
        originalCView = master.getCanvasController().getCameraView();
    }

}
