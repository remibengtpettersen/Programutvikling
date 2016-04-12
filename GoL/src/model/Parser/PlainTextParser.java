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
    static boolean[][] parsePlainText() throws IOException {

        while(fileContent.get(FIRST_LINE).startsWith("!")){
            fileContent.remove(FIRST_LINE);
        }

        patternHeight = fileContent.size();
        patternWidth = 0;

        for(int x = 0; x < fileContent.size(); x++){
            if(fileContent.get(x).length() > patternWidth){
                patternWidth = fileContent.get(x).length();
            }
        }
        patternArray = new boolean[patternWidth][patternHeight];

        for(int y = 0; y < patternHeight; y++){
            for(int x = 0; x < fileContent.get(y).length(); x++){

                if(fileContent.get(y).charAt(x) == 'O'){
                    patternArray[x][y] = true;
                }
                else{
                    patternArray[x][y] = false;
                }
            }
        }
        lastImportedRule = null;
        return patternArray;
    }
}
