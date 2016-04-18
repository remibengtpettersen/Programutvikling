package s305061;

import lieng.GIFWriter;
import lieng.GifSequenceWriter;
import model.EvolveException;
import model.GameOfLife;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Andreas on 15.04.2016.
 */
public class ToGif {

    private static String path = "testgif.gif";
    private static int width = 20;
    private static int height = 20;
    private static int timePerMilliSecond = 1000/60;
    private static int iterations = 50;

    public static void test() throws IOException {
        // create the GIFWriter object
        lieng.GIFWriter gwriter = new lieng.GIFWriter(width,height,path,timePerMilliSecond);

        // fill the upper half of the image with blue
        gwriter.fillRect(0, width-1, 0, height/2, Color.BLUE);

        // insert the painted image to the animation sequence
        // and proceed to the next image
        gwriter.insertAndProceed();

        // fill the lower half of the image with blue
        gwriter.fillRect(0, width-1, height/2, height-1, Color.BLUE);

        // insert the painted image into the animation sequence
        gwriter.insertCurrentImage();

        // close the GIF stream.
        gwriter.close();
    }

    public static void startWriteGolSequenceToGIF(GameOfLife game) throws IOException {

        GameOfLife clonedGol = game.clone();
        GIFWriter gifWriter = new GIFWriter(width, height, path, timePerMilliSecond);

        writeGoLSequenceToGIF(gifWriter, clonedGol, 0);
        //writeGoLSequenceToGIFTest(gifWriter, clonedGol);
    }

    public static void writeGoLSequenceToGIFTest(lieng.GIFWriter writer, GameOfLife game) throws IOException {
        writer.createNextImage();

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                if(game.getGrid()[x][y]) {
                    writer.fillRect(x, x, y, y, Color.BLUE);
                    //System.out.println("Added pixel at " + x + " " + y);
                }
            }

        writer.insertCurrentImage();

        //writer.insertAndProceed();

        tools.Utilities.print2DArray(game.getGrid());
        game.nextGeneration();

        game.getGrid()[0][0] = false;
        game.getGrid()[7][7] = true;

        tools.Utilities.print2DArray(game.getGrid());

        writer.createNextImage();

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                if(game.getGrid()[x][y]) {
                    writer.fillRect(x, x, y, y, Color.RED);
                    //System.out.println("Added pixel at " + x + " " + y);
                }
            }


        writer.insertCurrentImage();

        game.nextGeneration();

        writer.close();
    }

    private static void writeGoLSequenceToGIF(lieng.GIFWriter writer, GameOfLife game, int counter) throws IOException {
        //<condition to end recursion>
        if(counter >= 50){
            writer.close();
        } else {

            writer.createNextImage();
            //<draw game board to current image in writer>
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++) {
                    if(game.getGrid()[x][y]) {
                        writer.fillRect(x, x, y, y, Color.BLUE);
                        //System.out.println("Added pixel at " + x + " " + y);
                    }
                }

            //<insert image to GIF sequence and proceed to next image, via writer>
            //writer.insertAndProceed();
            writer.insertCurrentImage();

            //<nextGeneration call to game>
            game.nextGeneration();

            System.out.println(counter);
            //<recursive call to writeGoLSequenceToGIF>
            writeGoLSequenceToGIF(writer, game, ++counter);
        }
    }
}
