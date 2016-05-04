package s305080.Gif;

import controller.CanvasController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lieng.GIFWriter;
import model.Cell;
import model.GameOfLife;
import s305080.Gif.Controller.GifPropertiesController;

import java.io.File;
import java.io.IOException;

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
    private CanvasController cController;
    private double offsetX;
    private double offsetY;
    private int width;
    private int height;

    private GIFWriter gifWriter;
    private int frameNr;
    private int iterations = 0;
    private int gPerIteration;
    private int framerate;
    private GifPropertiesController gifPropertiesController;
    Parent root;
    private Stage stage;


    public void saveToGifBeta(CanvasController cController) throws IOException {
        this.cController = cController;

        originalGol = cController.gol;
        gol = originalGol.clone();

        if (!collectUserRequest()){
            return;
        }

        if (!gol.getRule().toString().equals(originalGol.getRule().toString())) //for some reason this always returns false :/
        {
            gol.setRule(originalGol.getRule().toString());
            System.out.println("changed rule");
        }
        offsetX = cController.getCommonOffsetX();
        offsetY = cController.getCommonOffsetY();
        width = (int)cController.getCanvas().getWidth();
        height = (int)cController.getCanvas().getHeight();
        cellSize = cController.getCell().getSize();

        createGif();

    }

    public void saveToGifAlfa(CanvasController cController, int[] boundingBox) throws IOException {
        this.cController = cController;

        originalGol = cController.gol;
        gol = originalGol.clone();

        if (!collectUserRequest()){
            return;
        }

        if (!gol.getRule().toString().equals(originalGol.getRule().toString())) //for some reason this always returns false :/
        {
            gol.setRule(originalGol.getRule().toString());
            System.out.println("changed rule");
        }

        offsetX = cController.getCommonOffsetX() + cController.getCanvasPosX(boundingBox[0]);
        offsetY = cController.getCommonOffsetY() + cController.getCanvasPosY(boundingBox[1]);
        cellSize = cController.getCell().getSize();
        System.out.println(boundingBox[0] + " " + boundingBox[1] + " " + boundingBox[2] + " " + boundingBox[3]);
        width = (int) ((boundingBox[2] - boundingBox[0] + 1) * cellSize);
        height = (int) ((boundingBox[3] - boundingBox[1] + 1) * cellSize);

        createGif();
    }





    private void createGif() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose save directory");

        File file = fileChooser.showSaveDialog(cController.masterController.stage);
        if (file == null){
            return;
        }
        String path = file.toString();
        if (!path.endsWith(".gif")){
            path = path + ".gif";
        }

        gifWriter = new GIFWriter(width, height, path, 1000/framerate);

        frameNr = 0;
        cell = cController.getCell();

        new Thread(()->{
            try {
                drawNextImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void drawNextImage() throws IOException {
       renderCanvas();
        if(frameNr > iterations){
            gifWriter.close();
            return;
        }
        for (int i = 0; i < gPerIteration; i++) {
            gol.nextGeneration();
        }
        frameNr++;
        gifWriter.insertAndProceed();

        System.out.println("Done with image nr. " + frameNr);

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
        for (int x = minX; x < maxX ; x++) {
            for (int y = minY; y < maxY; y++) {

                if (gol.isCellAlive(x, y))
                    drawCell(x, y);

            }
        }
    }

    private void drawBackground() {

    }

    private void drawCell(int x, int y) {

        int x1 =  (int)(x * cellSize - getCommonOffsetX()), y1 =  (int)(y * cellSize - getCommonOffsetY());
        x1 = (x1 < 0) ? 0 : x1;
        y1 = (y1 < 0) ? 0 : y1;

        int x2 = (int) ((x + 1) * cellSize - getCommonOffsetX()), y2 = (int)((y + 1) * cellSize - getCommonOffsetY());
        x2 = (x2 >= width) ? width - 1 : x2;
        y2 = (y2 >= height) ? height - 1 : y2;

        gifWriter.fillRect(
                x1 , x2,
                y1 , y2,
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

    private boolean collectUserRequest() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/GifData.fxml"));

        try {
            root = loader.load();

            gifPropertiesController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        stage = new Stage();

        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        gifPropertiesController.setParent(this);
        stage.showAndWait();

        if (iterations == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public void setUserRequest(int iterations, int gPerIteration, int framerate) {
        this.iterations = iterations;
        this.gPerIteration = gPerIteration;
        this.framerate = framerate;
    }

    public void closeWindow() {
        stage.close();
    }
}
