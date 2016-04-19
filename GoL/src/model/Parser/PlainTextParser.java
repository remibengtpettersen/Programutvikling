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

        while(fileContentList.get(FIRST_LINE).startsWith("!")){
            fileContentList.remove(FIRST_LINE);
        }

        patternHeight = fileContentList.size();
        patternWidth = 0;

        for(int x = 0; x < fileContentList.size(); x++){
            if(fileContentList.get(x).length() > patternWidth){
                patternWidth = fileContentList.get(x).length();
            }
        }
        patternArray = new boolean[patternWidth][patternHeight];

        for(int y = 0; y < patternHeight; y++){
            for(int x = 0; x < fileContentList.get(y).length(); x++){

                if(fileContentList.get(y).charAt(x) == 'O'){
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
