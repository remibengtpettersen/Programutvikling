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
import model.CameraView;
import model.Cell;
import model.GameOfLife;
import s305080.Gif.Controller.GifPropertiesController;

import java.io.File;
import java.io.IOException;

/**
 * Created by Truls on 27/04/16.
 */
public class GifSaver {

    Cell cell;

    @FXML
    public Canvas canvas;

    private GameOfLife gol;
    private GameOfLife originalGol;
    private CanvasController cController;
    private int width;
    private int height;

    CameraView cView = new CameraView();

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


        cell = cController.getCell().clone();

        cView.boardOffsetX = (int) cController.cView.getCommonOffsetX(originalGol, cell.getSize());
        cView.boardOffsetY = (int) cController.cView.getCommonOffsetY(originalGol, cell.getSize());
        width = (int)cController.getCanvas().getWidth();
        height = (int)cController.getCanvas().getHeight();

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


        cell = cController.getCell().clone();

        cView.boardOffsetX  = (int) (cController.cView.getCommonOffsetX(originalGol, cell.getSize()) + cController.getCanvasPosX(boundingBox[0]));
        cView.boardOffsetY  = (int) (cController.cView.getCommonOffsetY(originalGol, cell.getSize()) + cController.getCanvasPosY(boundingBox[1]));
        System.out.println(boundingBox[0] + " " + boundingBox[1] + " " + boundingBox[2] + " " + boundingBox[3]);
        width = (int) ((boundingBox[2] - boundingBox[0] + 1) * cell.getSize());
        height = (int) ((boundingBox[3] - boundingBox[1] + 1) * cell.getSize());

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
       cView.updateView(gol, cell.getSize(), width, height);
       drawImage();
    }



    private void drawImage() {
        drawBackground();
        for (int x = cView.currViewMinX; x < cView.currViewMaxX ; x++) {
            for (int y = cView.currViewMinY; y < cView.currViewMaxY; y++) {

                if (gol.isCellAlive(x, y))
                    drawCell(x, y);

            }
        }
    }

    private void drawBackground() {

    }

    private void drawCell(int x, int y) {

        int x1 =  (int)(x * cell.getSize() - cView.getCommonOffsetX(gol, cell.getSize())), y1 =  (int)(y * cell.getSize() - cView.getCommonOffsetY(gol, cell.getSize()));
        x1 = (x1 < 0) ? 0 : x1;
        y1 = (y1 < 0) ? 0 : y1;

        int x2 = (int) ((x + 1) * cell.getSize() - cView.getCommonOffsetX(gol, cell.getSize())), y2 = (int)((y + 1) * cell.getSize() - cView.getCommonOffsetY(gol, cell.getSize()));
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
