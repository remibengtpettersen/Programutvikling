package s305073.model;

import model.DynamicGameOfLife;
import model.GameOfLife;

/**
 * Created by remibengtpettersen on 27.04.2016.
 */
public class PatternConverter {

    public static String ConvertPatternToRle(GameOfLife gol) {

        // fit grid to pattern
        ((DynamicGameOfLife)gol).fitBoardToPattern();

        // get pattern bounding box
        int[] patternBounds = gol.getBoundingBox();

        // get min and max column
        int minColumn = patternBounds[2];
        int maxColumn = patternBounds[3] + 1;

        // get min and max row
        int minRow = patternBounds[0];
        int maxRow = patternBounds[1] + 1;

        // get length and width
        int height = maxRow - minRow;
        int width = maxColumn - minColumn;

        // initialize string builder for containing string - main string
        StringBuilder lineBuilder = new StringBuilder();

        // will contain alive positions - temporary container
        StringBuilder alive = new StringBuilder();

        // will contain dead positions - temporary container
        StringBuilder dead = new StringBuilder();

        // iterates column by row in pattern
        for (int i = minRow; i < maxRow; i++) {

            // initialize counters for alive and dead positions
            int aliveCounter = 0;
            int deadCounter = 0;

            // iterates row by row in pattern
            for (int j = minColumn; j < maxColumn; j++) {

                // check if position is true or false
                if (gol.isCellAlive(i, j)) {

                    // increment count for alive positions
                    aliveCounter++;

                    // reset dead count
                    deadCounter = 0;

                    // empty string containing dead elements
                    dead.delete(0, dead.length());

                    // appending 'o' for occupied
                    alive.append("o");

                    // check if number of alive positions detected, in sequence, is greater then 1
                    if (alive.length() > 1) {

                        // check if alive count is greater then two
                        if (aliveCounter > 2) {

                            // remove the two last characters from string
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);

                            // replace content in alive with new number of alive positions in sequence
                            alive.replace(0, alive.length(), aliveCounter + "o");
                        }
                        else {
                            // remove last character string
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);

                            // replace content in alive with number of alive positions in sequence
                            alive.replace(0, alive.length(), aliveCounter + "o");
                        }
                    }

                    // appends alive string to main string
                    lineBuilder.append(alive);

                } else {
                    // increment dead count
                    deadCounter++;

                    // reset alive count
                    aliveCounter = 0;

                    // empty string containing alive elements
                    alive.delete(0, dead.length());

                    // check that pattern row position is not end of row
                    if (j != width - 1) {
                        // append 'b' for empty
                        dead.append("b");
                    }

                    // check if dead positions detected, in sequence, is greater then 1
                    if (dead.length() > 1) {

                        // check if alive count is greater then two
                        if (deadCounter > 2) {

                            // remove the two last characters from string
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);

                            // replace content in dead with new number of alive positions in sequence
                            dead.replace(0, dead.length(), deadCounter + "b");
                        }
                        else {
                            // remove last character string
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);

                            // replace content in alive with number of alive positions in sequence
                            dead.replace(0, dead.length(), deadCounter + "b");
                        }
                    }

                    // check if position is end of line
                    if (j != width - 1) {
                        // append dead string to main string
                        lineBuilder.append(dead);
                    }

                    // check if row contains only empty positions
                    if (deadCounter == width) {
                        // add line of empty positions
                        lineBuilder.append(dead);
                    }
                }
            }

            // delete alive and dead string - prepare for new row
            alive.delete(0, alive.length());
            dead.delete(0, dead.length());


            // indicates new pattern row - according to RLE-format
            lineBuilder.append("$");

        }

        lineBuilder.deleteCharAt(lineBuilder.length() - 1);
        lineBuilder.append("!");

        // return string of pattern in RLE-format
        return lineBuilder.toString();
    }

    public static int[] getPatternSize(GameOfLife gol) {
        int[] size = new int[2];

        // get pattern bounding box
        int[] patternBounds = gol.getBoundingBox();

        // get min and max column
        int minColumn = patternBounds[2];
        int maxColumn = patternBounds[3] + 1;

        // get min and max row
        int minRow = patternBounds[0];
        int maxRow = patternBounds[1] + 1;

        // get length and width
        size[0] = maxRow - minRow;
        size[1] = maxColumn - minColumn;

        return size;
    }
}
