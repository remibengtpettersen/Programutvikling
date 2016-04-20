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
    @FXML private TextField iterationField;
    @FXML private TextField scaleField;
    @FXML private TextField originXField;
    @FXML private TextField originYField;
    @FXML private TextField widthField;
    @FXML private TextField heightField;

    private GameOfLife gol;

    private int iterations = 10;
    private int scale = 10;
    private int originX = 0;
    private int originY = 0;
    //private int width = 10;
    //private int height = 10;

    private int patternWidth = 10;
    private int patternHeight = 10;

    private int getGifWidth(){ return patternWidth*scale; }
    private int getGifHeight(){ return patternHeight*scale; }

    private String path = "test.gif";
    private int timePerMilliSecond = 500/60; //1000/60

    public void setGol(GameOfLife gol) {
        this.gol = gol;
    }

    public void crop() {

        int[] rectangle = gol.getBoundingBox();

        originX = rectangle[0];
        originY = rectangle[2];

        patternWidth = rectangle[1] - rectangle[0] + 1;
        patternHeight = rectangle[3] - rectangle[2] + 1;

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

        iterationField.setText(""+iterations);
        scaleField.setText(""+scale);
        originXField.setText(""+originX);
        originYField.setText(""+originY);
        widthField.setText(""+patternWidth);
        heightField.setText(""+patternHeight);
    }

    @FXML
    private void pullVariablesFromTextFields(){

        iterations = parseTextToInt(iterationField.getText());
        scale = parseTextToInt(scaleField.getText());
        originX = parseTextToInt(originXField.getText());
        originY = parseTextToInt(originYField.getText());
        patternWidth = parseTextToInt(widthField.getText());
        patternHeight = parseTextToInt(heightField.getText());
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

        GIFWriter gifWriter = new GIFWriter(getGifWidth(), getGifHeight(), path, timePerMilliSecond);

        writeGoLSequenceToGIF(gifWriter, clonedGol, iterations);
    }

    private void writeGoLSequenceToGIF(lieng.GIFWriter writer, GameOfLife game, int counter) throws IOException {
        //<condition to end recursion>
        if(counter <= 0){
            writer.close();
        } else {

            writer.createNextImage();

            //<draw game board to current image in writer>


            for (int gameX = originX; gameX < originX+patternWidth; gameX++)
                for (int gameY = originY; gameY < originY+patternHeight; gameY++){

                    int gifX = (gameX - originX) * scale;
                    int gifY = (gameY - originY) * scale;

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
