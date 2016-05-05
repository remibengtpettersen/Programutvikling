package s305061.gif;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lieng.GIFWriter;
import model.DynamicGameOfLife;
import model.GameOfLife;

import java.io.File;
import java.io.IOException;

/**
 * @author Andreas s305061
 * Controller for GIF window.
 * Handles GIF creation with GIFLib, in addition to GUI control
 */
public class GifController {

    @FXML private TextField iterationTextField;
    @FXML private TextField scaleTextField;
    @FXML private ColorPicker colorPicker;
    @FXML private TextField timeTextField;

    @FXML private TextField leftTextField;
    @FXML private TextField topTextField;
    @FXML private TextField rightTextField;
    @FXML private TextField bottomTextField;

    private Stage stage;
    private GameOfLife gol;

    private int iterations = 10;
    private int scale = 10;
    private Color color = Color.BLACK;
    private int timeBetweenFrames = 16;

    private int left = 0;
    private int top = 0;
    private int right = 10;
    private int bottom = 10;

    /**
     * Initialization method.
     * Will set a reference to a game object to be cloned and animated, then automatically crop the board
     *
     * @param gol Reference to a game object
     */
    public void initialize(Stage stage, GameOfLife gol) {

        this.stage = stage;
        this.gol = gol;

        autoCrop();

        pushVariablesToTextFields();
    }

    /**
     * Sets the bounding box to fit around the size of the dynamic grid
     */
    public void autoCrop() {

        // if dynamic game board
        if(gol instanceof DynamicGameOfLife){

            // ensure that the game board is as small as possible
            ((DynamicGameOfLife)gol).fitBoardToPattern();

            // then use the entire game board
            left = 0;
            right = gol.getGridWidth();
            top = 0;
            bottom = gol.getGridHeight();
        }
        // if static game board
        else {

            // get the boundary around the pattern
            int[] boundary = gol.getBoundingBox();

            left = boundary[0];
            right = boundary[1] + 1;
            top = boundary[2];
            bottom = boundary[3] + 1;
        }

        pushBoundaryVariablesToTextFields();
    }

    /**
     * Called when the "Create GIF" button is clicked. Will attempt to create a GIF based on input.
     */
    @FXML
    public void createGif() {

        pullVariablesFromTextFields();

        // open save dialog
        File file = chooseSavePath();

        if(file != null) {

            String path = file.toString();

            try {
                startWriteGolSequenceToGIF(gol, iterations, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Opens a fileChooser window, returns the file path selected by user
     * @return File path
     */
    private File chooseSavePath(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("unnamed");
        fileChooser.setTitle("Save GIF File");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("GIF File", "*.gif"));

        return fileChooser.showSaveDialog(stage);
    }

    /**
     * Sets all text in text fields to reflect their respective variables
     */
    private void pushVariablesToTextFields(){

        iterationTextField.setText("" + iterations);
        scaleTextField.setText("" + scale);
        colorPicker.setValue(color);
        timeTextField.setText("" + timeBetweenFrames);

        pushBoundaryVariablesToTextFields();
    }

    /**
     * Sets the boundary related text fields to reflect their respective variables
     */
    private void pushBoundaryVariablesToTextFields(){

        leftTextField.setText(""+left);
        topTextField.setText(""+top);
        rightTextField.setText(""+right);
        bottomTextField.setText(""+bottom);
    }

    /**
     * Sets all variables to reflect their respective text fields
     */
    private void pullVariablesFromTextFields(){

        iterations = parseTextToInt(iterationTextField.getText());
        scale = parseTextToInt(scaleTextField.getText());
        color = colorPicker.getValue();
        setTimeBetweenFrames(parseTextToInt(timeTextField.getText()));

        left = parseTextToInt(leftTextField.getText());
        top = parseTextToInt(topTextField.getText());
        right = parseTextToInt(rightTextField.getText());
        bottom = parseTextToInt(bottomTextField.getText());
    }

    /**
     * Converts a javafx.scene.paint.Color to a java.awt.Color
     *
     * @param fxColor javafx.scene.paint.Color
     * @return java.awt.Color
     */
    private java.awt.Color FxColorToAwtColor(javafx.scene.paint.Color fxColor){

        int red = (int)(fxColor.getRed()*255);
        int green = (int)(fxColor.getGreen()*255);
        int blue = (int)(fxColor.getBlue()*255);

        return new java.awt.Color(red, green, blue);
    }

    /**
     * Parses text to int if possible. If not, 0 will be returned
     *
     * @param string Text to be parsed
     * @return Integer parsed from text, or 0
     */
    private int parseTextToInt(String string){

        try{
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e){
            return 0;
        }
    }

    /**
     * Initiates the recursive GIF creation
     *
     * @param originalGol The game object to be cloned
     * @param iterations Number of iterations to evolve, also number of frames to be added to gif
     * @throws IOException IO exception, thrown by GIFLib
     */
    private void startWriteGolSequenceToGIF(GameOfLife originalGol, int iterations, String path) throws IOException {

        GameOfLife clonedGol = originalGol.clone();

        GIFWriter gifWriter = new GIFWriter(getGifWidth(), getGifHeight(), path, timeBetweenFrames);

        writeGoLSequenceToGIF(gifWriter, clonedGol, iterations);
    }

    /**
     * Recursive GIF creation. Every repetition will add a frame for the current generation,
     * then recursively proceed to the next frame and generation.
     *
     * @param writer GIFWriter from GIFLib
     * @param game The cloned game class to be animated
     * @param counter Counter to determine when to stop recursive method. Initially set as number of generations/frames
     * @throws IOException IO exception, thrown by GIFLib
     */
    private void writeGoLSequenceToGIF(GIFWriter writer, GameOfLife game, int counter) throws IOException {

        // condition to end recursion
        if(counter <= 0){
            writer.close();
        } else {

            // add new image (frame) to gif
            writer.createNextImage();

            // converts javafx.scene.pain.Color to java.awt.Color
            java.awt.Color awtColor = FxColorToAwtColor(color);

            // these are 0 and 0 the first repetition (reset in clone() method),
            // then they will increase according to the movement of the pattern (if dynamic game board)
            int offsetX = game.getOffsetX();
            int offsetY = game.getOffsetY();

            // draw current generation of the game board to current image in writer
            for (int gameX = left + offsetX; gameX <= right + offsetX; gameX++)
                for (int gameY = top + offsetY; gameY <= bottom + offsetY; gameY++){

                    if(game.isCellAlive(gameX, gameY)){

                        int gifX = (gameX - left - offsetX) * scale;
                        int gifY = (gameY - top - offsetY) * scale;

                        int gifMaxX = gifX + scale - 1;
                        int gifMaxY = gifY + scale - 1;

                        try {
                            writer.fillRect(gifX, gifMaxX, gifY, gifMaxY, awtColor);
                        } catch (ArrayIndexOutOfBoundsException ignored){
                        }
                    }
                }

            // insert image to GIF sequence via writer
            writer.insertCurrentImage();

            // nextGeneration call to game
            game.nextGeneration();

            // recursive call to writeGoLSequenceToGIF
            writeGoLSequenceToGIF(writer, game, --counter);
        }
    }

    //region getters
    /**
     * Gets the width of the bounding box set through GUI or autoCrop()
     *
     * @return Width (number of cells)
     */
    private int getBoundaryWidth(){ return right-left; }

    /**
     * Gets the height of the bounding box set through GUI or autoCrop()
     *
     * @return Height (number of cells)
     */
    private int getBoundaryHeight(){ return bottom-top; }

    /**
     * Gets the width of the GIF file to be made
     *
     * @return Width (number of pixels)
     */
    private int getGifWidth(){ return getBoundaryWidth()*scale; }

    /**
     * Gets the height of the GIF file to be made
     *
     * @return Height (number of pixels)
     */
    private int getGifHeight(){ return getBoundaryHeight()*scale; }
    //endregion

    //region setters
    /**
     * Sets time delay between each iteration or frame of the GIF to be made
     *
     * @param timeBetweenFrames Time delay in milliseconds
     */
    private void setTimeBetweenFrames(int timeBetweenFrames){
        this.timeBetweenFrames = timeBetweenFrames;
    }
    //endregion
}