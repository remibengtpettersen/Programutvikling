package s305061.gif;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lieng.GIFWriter;
import model.GameOfLife;

import java.awt.*;
import java.io.IOException;

/**
 * @author Andreas s305061
 * GifPropertiesController for GIF window.
 * Handles GIF creation with GIFLib, in addition to GUI control
 */
public class GifController {

    @FXML private TextField iterationTextField;
    @FXML private TextField scaleTextField;
    @FXML private TextField fileNameTextField;
    @FXML private TextField timeTextField;

    @FXML private TextField leftTextField;
    @FXML private TextField topTextField;
    @FXML private TextField rightTextField;
    @FXML private TextField bottomTextField;

    private GameOfLife gol;

    private int iterations = 10;
    private int scale = 10;
    private String fileName = "unnamed.gif";
    private int timeBetweenFrames = 16;

    private int left = 0;
    private int top = 0;
    private int right = 10;
    private int bottom = 10;

    /**
     * Gets the width of the bounding box set through GUI or autoCrop()
     * @return Width (number of cells)
     */
    private int getBoundaryWidth(){ return right-left; }

    /**
     * Gets the height of the bounding box set through GUI or autoCrop()
     * @return Height (number of cells)
     */
    private int getBoundaryHeight(){ return bottom-top; }

    /**
     * Gets the width of the GIF file to be made
     * @return Width (number of pixels)
     */
    private int getGifWidth(){ return getBoundaryWidth()*scale; }

    /**
     * Gets the height of the GIF file to be made
     * @return Height (number of pixels)
     */
    private int getGifHeight(){ return getBoundaryHeight()*scale; }

    /**
     * Sets time delay between each iteration or frame of the GIF to be made
     * @param timeBetweenFrames Time delay in milliseconds
     */
    private void setTimeBetweenFrames(int timeBetweenFrames){
        this.timeBetweenFrames = timeBetweenFrames;
    }

    /**
     * Sets the filename of the GIF to be made.
     * If input filename doesn't end with the .gif file extension, it will be added
     * @param fileName Filename
     */
    private void setFileName(String fileName){

        if(!fileName.endsWith(".gif")){
          fileName += ".gif";
        }

        this.fileName = fileName;
    }

    /**
     * Initialization method.
     * Will set a reference to a game object to be cloned and animated, then automatically crop the board
     * @param gol Reference to a game object
     */
    public void initialize(GameOfLife gol) {

        this.gol = gol;

        autoCrop();

        pushVariablesToTextFields(false);
    }

    /**
     * Sets the bounding box to fit around the size of the dynamic grid
     */
    public void autoCrop() {

        gol.fitBoardToPattern();

        left = 0;
        right = gol.getGridWidth();
        top = 0;
        bottom = gol.getGridHeight();

        pushVariablesToTextFields(true);
    }

    /**
     * Called when the "Create GIF" button is clicked. Will attempt to create a GIF based on input.
     */
    @FXML
    public void createGif() {

        pullVariablesFromTextFields();

        try {
            startWriteGolSequenceToGIF(gol, iterations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets all text in text fields to reflect their respective variables
     * @param boundaryOnly If true, only the boundary related variables will be set
     */
    private void pushVariablesToTextFields(boolean boundaryOnly){

        if(!boundaryOnly) {
            iterationTextField.setText("" + iterations);
            scaleTextField.setText("" + scale);
            fileNameTextField.setText("" + fileName);
            timeTextField.setText("" + timeBetweenFrames);
        }

        // boundary
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
        setFileName(fileNameTextField.getText());
        setTimeBetweenFrames(parseTextToInt(timeTextField.getText()));

        left = parseTextToInt(leftTextField.getText());
        top = parseTextToInt(topTextField.getText());
        right = parseTextToInt(rightTextField.getText());
        bottom = parseTextToInt(bottomTextField.getText());
    }

    /**
     * Parses text to int if possible. If not, 0 will be returned
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
     * @param originalGol The game object to be cloned
     * @param iterations Number of iterations to evolve, also number of frames to be added to gif
     * @throws IOException IO exception, thrown by GIFLib
     */
    public void startWriteGolSequenceToGIF(GameOfLife originalGol, int iterations) throws IOException {

        GameOfLife clonedGol = originalGol.clone();

        GIFWriter gifWriter = new GIFWriter(getGifWidth(), getGifHeight(), fileName, timeBetweenFrames);

        writeGoLSequenceToGIF(gifWriter, clonedGol, iterations);
    }

    /**
     * Recursive GIF creation. Every instance will add a frame for the current generation,
     * then recursively proceed to the next frame and generation.
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

            // these are 0 and 0 the first instance (reset in clone() method),
            // then they will increase according to the movement of the pattern
            int offsetX = game.getOffsetX();
            int offsetY = game.getOffsetY();

            // draw current generation of the game board to current image in writer
            for (int gameX = left + offsetX; gameX <= right + offsetX; gameX++)
                for (int gameY = top + offsetY; gameY <= bottom + offsetY; gameY++){

                    if(game.isCellAlive(gameX, gameY)){

                        int gifX = (gameX - left - offsetX) * scale;
                        int gifY = (gameY - top - offsetY) * scale;

                        int gifMaxX = gifX + scale-1;
                        int gifMaxY = gifY + scale-1;

                        try {
                            writer.fillRect(gifX, gifMaxX, gifY, gifMaxY, Color.BLACK);
                        } catch (ArrayIndexOutOfBoundsException e){
                            continue;
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
}