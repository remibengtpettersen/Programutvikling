package s305061.gif;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import lieng.GIFWriter;
import model.GameOfLife;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andreas on 20.04.2016.
 */
public class GifController {

    @FXML public Canvas canvas;

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
    private String fileName = "test.gif";
    private int timeBetweenFrames = 16;

    private int left = 0;
    private int top = 0;
    private int right = 10;
    private int bottom = 10;

    private int getPatternWidth(){ return right-left; }
    private int getPatternHeight(){ return bottom-top; }

    private int getGifWidth(){ return getPatternWidth()*scale; }
    private int getGifHeight(){ return getPatternHeight()*scale; }

    private void setTimeBetweenFrames(int timeBetweenFrames){
        this.timeBetweenFrames = timeBetweenFrames;
    }

    private void setFileName(String fileName){

        if(fileName.endsWith(".gif")){
            this.fileName = fileName;
        } else {
            this.fileName = fileName + ".gif";
        }
    }

    public void initialize(GameOfLife gol) {

        this.gol = gol;

        autoCrop();
    }

    public void autoCrop() {

        int[] rectangle = gol.getBoundingBox();

        left = rectangle[0];
        right = rectangle[1];
        top = rectangle[2];
        bottom = rectangle[3];

        pushVariablesToTextFields();
    }

    public void createGif() {

        pullVariablesFromTextFields();

        try {
            startWriteGolSequenceToGIF(gol, iterations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pushVariablesToTextFields(){

        leftTextField.setText(""+left);
        topTextField.setText(""+top);
        rightTextField.setText(""+right);
        bottomTextField.setText(""+bottom);
    }

    @FXML
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

    private int parseTextToInt(String string){

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(string);

        if(m.matches()) {
            return Integer.parseInt(m.group());
        }
        else {
            return 0;
        }
    }

    public void startWriteGolSequenceToGIF(GameOfLife game, int iterations) throws IOException {

        GameOfLife clonedGol = game.clone();

        GIFWriter gifWriter = new GIFWriter(getGifWidth(), getGifHeight(), fileName, timeBetweenFrames);

        writeGoLSequenceToGIF(gifWriter, clonedGol, iterations);
    }

    private void writeGoLSequenceToGIF(lieng.GIFWriter writer, GameOfLife game, int counter) throws IOException {
        //<condition to end recursion>
        if(counter <= 0){
            writer.close();
        } else {

            writer.createNextImage();

            //<draw game board to current image in writer>


            for (int gameX = left; gameX < right; gameX++)
                for (int gameY = top; gameY < bottom; gameY++){

                    int gifX = (gameX - left) * scale;
                    int gifY = (gameY - top) * scale;

                    int gifMaxX = gifX + scale - 1;
                    int gifMaxY = gifY + scale - 1;

                    if(game.getGrid()[gameX][gameY]){
                        writer.fillRect(gifX, gifMaxX, gifY, gifMaxY, Color.BLUE);
                    }
                }

            //<insert image to GIF sequence and proceed to next image, via writer>
            //writer.insertAndProceed();
            writer.insertCurrentImage();

            //<nextGeneration call to game>
            game.nextGeneration();

            //<recursive call to writeGoLSequenceToGIF>
            writeGoLSequenceToGIF(writer, game, --counter);
        }
    }
}
