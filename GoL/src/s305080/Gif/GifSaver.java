package s305080.Gif;

import controller.MasterController;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import lieng.GIFWriter;
import model.Cell;
import model.DynamicGameOfLife;
import model.GameOfLife;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Truls on 27/04/16.
 */
public class GifSaver {

    double cellSize;

    int minX;
    int maxX;
    int minY;
    int maxY;

    Cell cell;

    @FXML
    public Canvas canvas;

    private GameOfLife gol;
    private GameOfLife originalGol;
    private MasterController master;
    private double offsetX;
    private double offsetY;
    private int width;
    private int height;
    private GIFWriter gifWriter;
    private int frameNr;
    private int iterations;

    public void saveToGifBeta(MasterController master) throws IOException {
        this.master = master;
        originalGol = master.getCanvasController().gol;
        gol = originalGol.clone();
        iterations = getIterations();

        if (!gol.getRule().toString().equals(originalGol.getRule().toString())) //for some reason this always returns false :/
        {
            gol.setRule(originalGol.getRule().toString());
            System.out.println("changed rule");
        }
        offsetX = master.getCanvasController().getCommonOffsetX();
        offsetY = master.getCanvasController().getCommonOffsetY();
        width = (int)master.getCanvasController().getCanvas().getWidth();
        height = (int)master.getCanvasController().getCanvas().getHeight();
        cellSize = master.getCanvasController().cell.getSize();

        createGif();

    }

    private void createGif() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose save directory");

        File file = fileChooser.showSaveDialog(master.stage);
        if (file == null){
            return;
        }
        String path = file.toString();
        if (!path.endsWith(".gif")){
            path = path + ".gif";
        }

        gifWriter = new GIFWriter(width, height, path, 10);

        frameNr = 0;
        cell = master.getCanvasController().cell;
        drawNextImage();

    }

    private void drawNextImage() throws IOException {
       renderCanvas();
        if(frameNr > 100){
            gifWriter.close();
            return;
        }
        gol.nextGeneration();
        frameNr++;
        gifWriter.insertAndProceed();
        drawNextImage();
    }

   private void renderCanvas() {
       updateView();
       drawImage();
    }

    private void updateView() {
        minX = (int) (getCommonOffsetX() / cellSize);
        maxX = (int) ((getCommonOffsetX() + width) / cellSize) + 1;
        if (maxX > gol.getGridWidth())
            maxX = gol.getGridWidth();

        minY = (int) (getCommonOffsetY() / cellSize);
        maxY = (int) ((getCommonOffsetY() + height) / cellSize) + 1;
        if (maxY > gol.getGridHeight())
            maxY = gol.getGridHeight();

        if (minY < 0)
            minY = 0;
        if (minX < 0)
            minX = 0;
    }

    private void drawImage() {
        drawBackground();
        for (int x = minX + 1; x < maxX - 1 ; x++) {
            for (int y = minY + 1; y < maxY - 1 ; y++) {

                if (gol.isCellAlive(x, y))
                    drawCell(x, y);

            }
        }
    }

    private void drawBackground() {

    }

    private void drawCell(int x, int y) {
        gifWriter.fillRect(
                (int)(x * cellSize - getCommonOffsetX()), (int)((x + 1) * cellSize - getCommonOffsetX()),
                (int)(y * cellSize - getCommonOffsetY()), (int)((y + 1) * cellSize - getCommonOffsetY()),
                new java.awt.Color(
                        (int) (255 * cell.getColor().getRed()),
                        (int) (255 * cell.getColor().getGreen()),
                        (int) (255 * cell.getColor().getBlue())));

    }

    public double getCommonOffsetX() {
        return offsetX + gol.getOffsetX() * cellSize;
    }

    public double getCommonOffsetY() {
        return offsetY + gol.getOffsetY() * cellSize;
    }

    public int getIterations() {




        return 0;
    }
}
