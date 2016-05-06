package s305080.Gif;

import controller.CanvasController;
import javafx.application.Platform;
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
import tools.MessageBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Used to save game of life patterns to a gif file
 */
public class GifSaver {

    Cell cell;

    @FXML
    public Canvas canvas;
    Parent root;
    private Stage stage;


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

    /**
     * Saves game of life simulation to a gif the same size as the canvas.
     * @param cController contains the GameOfLife class and information about the size of the gif.
     * @throws IOException if something wrong happened with the image stream.
     */
    public void saveCanvasAsGif(CanvasController cController) throws IOException {
        this.cController = cController;

        originalGol = cController.getGol();
        gol = originalGol.clone();

        // les user decide length and speed of gif
        if (!collectUserRequest()){
            return;
        }

        // stores the cell used in canvasController
        cell = cController.getCell().clone();

        // old common offset to be used as offset in gif
        cView.boardOffsetX = cController.getCameraView().getCommonOffsetX(originalGol, cell.getSize());
        cView.boardOffsetY = cController.getCameraView().getCommonOffsetY(originalGol, cell.getSize());

        // gif shall have same measures as canvas
        width = (int)cController.getCanvas().getWidth();
        height = (int)cController.getCanvas().getHeight();

        createGif();

    }

    /**
     * Saves game of life simulation to a gif the same size as the selected area on the canvas.
     * @param cController contains the GameOfLife class and information about the size of the gif.
     * @param boundingBox the coordinates of the upper left and bottom right cells.
     * @throws IOException if something wrong happened with the image stream.
     */
    public void saveGifFromSelectedArea(CanvasController cController, int[] boundingBox) throws IOException {
        this.cController = cController;

        originalGol = cController.getGol();
        gol = originalGol.clone();

        // les user decide length and speed of gif
        if (!collectUserRequest()){
            return;
        }

        // stores the cell used in canvasController
        cell = cController.getCell().clone();

        // old common offset to be used as offset in gif
        cView.boardOffsetX  = (int) (cController.getCameraView().getCommonOffsetX(originalGol, cell.getSize()) + cController.getCanvasPosX(boundingBox[0]));
        cView.boardOffsetY  = (int) (cController.getCameraView().getCommonOffsetY(originalGol, cell.getSize()) + cController.getCanvasPosY(boundingBox[1]));
        System.out.println(boundingBox[0] + " " + boundingBox[1] + " " + boundingBox[2] + " " + boundingBox[3]);

        // shall have the same dimensions as selected area
        width = (int) ((boundingBox[2] - boundingBox[0] + 1) * cell.getSize());
        height = (int) ((boundingBox[3] - boundingBox[1] + 1) * cell.getSize());

        createGif();
    }


    /**
     * Creates a gif of the simulation game of life
     * @throws IOException if something wrong happened with the image stream.
     */
    private void createGif() throws IOException {

        // lets the user decide where to save the gif
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose save directory");
        File file = fileChooser.showSaveDialog(cController.getMasterController().getStage());

        // quits if user canceled or didn't choose location
        if (file == null){
            return;
        }

        // makes sure the file ends with ".gif"
        String path = file.toString();
        if (!path.endsWith(".gif")){
            path = path + ".gif";
        }

        // initiates the gifWriter
        gifWriter = new GIFWriter(width, height, path, 1000/framerate);

        // to count the frames
        frameNr = 0;

        // sets the correct background color

        // creates a thread to make the actual gif to prevent the gifWriter from halting the program
        Thread t = new Thread(()->{
            try {
                drawNextImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                MessageBox.close();
                MessageBox.alert("The gif is done");
            });

        });
        t.start();

        MessageBox.alert("Working on gif, we will notify you when it's done...");

    }

    /**
     * Draws one frame in the gif, and starts the drawing of the next frame
     * @throws IOException if something wrong happened with the image stream.
     */
    private void drawNextImage() throws IOException {

        // creates the image
       renderGif();

        // checks if enough frames
        if(frameNr >= iterations){
            // closes gif
            gifWriter.close();
            // stops recursion
            return;
        }

        // evolves game of life as far as the user wants to
        for (int i = 0; i < gPerIteration; i++) {
            gol.nextGeneration();
        }

        // counting frames
        frameNr++;

        // prepares gifWriter for next image
        gifWriter.insertAndProceed();

        // draws next image
        drawNextImage();
    }

    /**
     * creates one image in the gif
     */
   private void renderGif() {
       // calculate wich cells are inside the view
       cView.updateView(gol, cell.getSize(), width, height);

       // draws the cells inside the view
       drawImage();
    }


    /**
     * draws the cells inside the view
     */
    private void drawImage() {

        drawBackground();

        // draws the live cells
        for (int x = cView.currViewMinX; x < cView.currViewMaxX ; x++) {
            for (int y = cView.currViewMinY; y < cView.currViewMaxY; y++) {

                if (gol.isCellAlive(x, y))
                    drawCell(x, y);

            }
        }
    }

    /**
     * fills the image with the background color
     */
    private void drawBackground() {
        gifWriter.fillRect(0, width - 1, 0, height - 1, new Color(
                (int) (255 * cell.getDeadColor().getRed()),
                (int) (255 * cell.getDeadColor().getGreen()),
                (int) (255 * cell.getDeadColor().getBlue())));
    }

    // draws the cell at (x, y)
    private void drawCell(int x, int y) {

        // stores teh minimum x and y value to fill
        int x1 =  (int)(x * cell.getSize() - cView.getCommonOffsetX(gol, cell.getSize())), y1 =  (int)(y * cell.getSize() - cView.getCommonOffsetY(gol, cell.getSize()));

        // makes sure the values is inside the gif
        x1 = (x1 < 0) ? 0 : x1;
        y1 = (y1 < 0) ? 0 : y1;

        // stores the maximum x and y values to fill
        int x2 = (int) ((x + 1) * cell.getSize() - cView.getCommonOffsetX(gol, cell.getSize())), y2 = (int)((y + 1) * cell.getSize() - cView.getCommonOffsetY(gol, cell.getSize()));

        // makes sure the values are inside the gif
        x2 = (x2 >= width) ? width - 1 : x2;
        y2 = (y2 >= height) ? height - 1 : y2;

        gifWriter.fillRect(
                x1 , x2,
                y1 , y2,
                new Color(
                        (int) (255 * cell.getColor().getRed()),
                        (int) (255 * cell.getColor().getGreen()),
                        (int) (255 * cell.getColor().getBlue())));

    }

    /**
     * Lets the user type inn length and speed of gif
     * @return false if user canceled
     */
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

    /**
     * Sets the variables needed to make the gif
     * @param iterations
     * @param gPerIteration
     * @param framerate
     */
    public void setUserRequest(int iterations, int gPerIteration, int framerate) {
        this.iterations = iterations;
        this.gPerIteration = gPerIteration;
        this.framerate = framerate;
    }

    /**
     * Closes the stage
     */
    public void closeWindow() {
        stage.close();
    }
}
