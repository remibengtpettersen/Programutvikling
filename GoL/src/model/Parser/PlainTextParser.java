package model.Parser;

import java.io.IOException;

/**
 * Created by Truls on 12/04/16.
 */
public class PlainTextParser extends PatternParser {
    /**
     * Reads a .cells / plain text file
     * @return the boolean array produced from the file
     */
    static boolean[][] readPlainText() throws IOException {

        while(lineList.get(FIRST_LINE).startsWith("!")){
            lineList.remove(FIRST_LINE);
        }

        patternHeight = lineList.size();
        patternWidth = 0;

        for(int x = 0; x < lineList.size(); x++){
            if(lineList.get(x).length() > patternWidth){
                patternWidth = lineList.get(x).length();
            }
        }
        patternArray = new boolean[patternWidth][patternHeight];

        for(int y = 0; y < patternHeight; y++){
            for(int x = 0; x < lineList.get(y).length(); x++){

                if(lineList.get(y).charAt(x) == 'O'){
                    patternArray[x][y] = true;
                }
                else{
                    patternArray[x][y] = false;
                }
            }
        }
        return patternArray;
    }
}
