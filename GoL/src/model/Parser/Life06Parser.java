package model.Parser;

import model.PatternFormatException;

import java.util.regex.Pattern;

/**
 * Created by Truls on 12/04/16.
 */
public class Life06Parser extends PatternParser {
    /**
     * reads the string content from a Life 1.06 file
     * @return the boolean array produced from the list
     */
    static boolean[][] parseLife06() throws PatternFormatException {

        while(fileContentList.get(FIRST_LINE).startsWith("#")){
            fileContentList.remove(FIRST_LINE);
        }
        patternParameters = Pattern.compile("(.+) (.+)");

        patternMatcher = patternParameters.matcher(fileContentList.get(FIRST_LINE));

        if(!patternMatcher.matches()){
            throw new PatternFormatException("Couldn't parse Life 1.06 file");
        }

        int startPosX = Integer.parseInt(patternMatcher.group(1));
        int startPosY = Integer.parseInt(patternMatcher.group(2));

        int possibleHeight;
        int possibleWidth;

        patternWidth = startPosX;
        possibleHeight = startPosY;

        for(int i = 0; i < fileContentList.size(); i++){
            patternMatcher = patternParameters.matcher(fileContentList.get(i));

            if(!patternMatcher.matches()){
                return null;
            }
            possibleWidth = Integer.parseInt(patternMatcher.group(1));
            possibleHeight = Integer.parseInt(patternMatcher.group(2));

            if(possibleHeight > patternHeight){
                patternHeight = possibleHeight;
            }

            if(possibleWidth > patternWidth){
                patternWidth = possibleWidth;
            }
            else if(possibleWidth < startPosX){
                startPosX = possibleWidth;
            }
        }

        patternWidth -= startPosX;
        possibleHeight-=startPosY;

        patternArray = new boolean[patternWidth + 1][possibleHeight + 1];

        for(int i = 0; i < fileContentList.size(); i++){
            patternMatcher = patternParameters.matcher(fileContentList.get(i));

            if(!patternMatcher.matches()){
                throw new PatternFormatException("Couldn't parse Life 1.06 file");
            }
            patternArray[Integer.parseInt(patternMatcher.group(1)) - startPosX][Integer.parseInt(patternMatcher.group(2)) - startPosY] = true;
        }
        lastImportedRule = null;
        return patternArray;
    }
}
