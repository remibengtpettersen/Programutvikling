package model.Parser;

import model.PatternFormatException;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by Truls on 12/04/16.
 */
class RleParser extends PatternParser {
    /**
     * Reads a RLE file
     * @return the boolean array produced from the file
     */
    static boolean[][] readRLE() throws IOException {

        while (lineList.get(FIRST_LINE).startsWith("#")){
            lineList.remove(FIRST_LINE);
        }

        patternParameters = Pattern.compile("^x = ([0-9]+), y = ([0-9]+), rule = (.+)$");
        patternMatcher = patternParameters.matcher(lineList.get(FIRST_LINE));

        if(!patternMatcher.matches()) {
            throw new PatternFormatException();
        }

        patternHeight = Integer.parseInt(patternMatcher.group(1));
        patternWidth = Integer.parseInt(patternMatcher.group(2));


        patternArray = new boolean[patternHeight][patternWidth];

        lineList.remove(FIRST_LINE);

        int pattern = 0; //ToBeNamed...
        int x = 0;
        int y = 0;

        int cellsAdded = 0;

        for(int i = 0; i < lineList.size(); i++){
            for(int j = 0; j < lineList.get(i).length(); j++){

                currentCharacter = lineList.get(i).charAt(j);

                if(Character.isDigit(currentCharacter)){
                    pattern = pattern * 10 + (currentCharacter - '0');
                }
                else if(currentCharacter == 'b'){
                    if(pattern == 0){
                        patternArray[x][y] = false;
                        x++;
                    }
                    else {
                        for(int k = 0; k < pattern; k++){
                            patternArray[x][y] = false;
                            x++;
                        }
                        pattern = 0;
                    }
                }
                else if(currentCharacter == 'o'){

                    if(pattern == 0){
                        patternArray[x][y] = true;
                        x++;
                        cellsAdded++;
                    }
                    else {
                        for(int k = 0; k < pattern; k++){
                            patternArray[x][y] = true;
                            x++;
                            cellsAdded++;
                        }
                        pattern = 0;
                    }
                }
                else if(currentCharacter == '$'){

                    if(pattern == 0){
                        y++;
                    }
                    else{
                        y+= pattern;
                    }
                    pattern = 0;

                    x=0;
                }
                else if(currentCharacter == '!'){
                    System.out.println(cellsAdded);
                    return patternArray;
                }
            }
        }
        System.out.println("Couldn't read RLE");
        return null;
    }
}
