package model.Parser;

import model.PatternFormatException;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Truls on 12/04/16.
 */
class RleParser extends PatternParser {

    /**
     * Reads a RLE file
     * @return the boolean array produced from the file
     */
    static boolean[][] parseRle() throws PatternFormatException {

        extractMetaData();
        extractGridProperties();

        patternArray = new boolean[patternHeight][patternWidth];

            if (buildPatternArray())
                return patternArray;
            else
                throw new PatternFormatException("Missing exclamation mark");
    }

    /**
     * Extracts rules from metadata tag #r
     */
    private static void extractXlifeRuleFormat() {

        for(String currentLine : metaData)
            if (currentLine.startsWith("#r"))
                lastImportedRule = currentLine.replaceAll("[^1-9/1-9]", "");
    }

    /**
     * Extracts the meta data from the header of the file, and puts it in the metaData list.
     */
    private static void extractMetaData() {

        metaData = new ArrayList<>();

        while (fileContentList.get(FIRST_LINE).startsWith("#")){
            metaData.add(fileContentList.get(FIRST_LINE));
            fileContentList.remove(FIRST_LINE);
        }
    }

    /**
     * Extracts the grid properties from the rle String list.
     * @throws PatternFormatException Is thrown if rle format is violated.
     */
    private static void extractGridProperties() throws PatternFormatException {

        patternParameters = Pattern.compile("^x[ ]*=[ ]*([0-9]+)[ ]*,[ ]*y[ ]*=[ ]*([0-9]+)[ ]*,[ ]*.*[ ]*=[ ]*(.+)$");
        Pattern patternParametersXLife = Pattern.compile("^x[ ]*=[ ]*([0-9]+)[ ]*,[ ]*y[ ]*=[ ]*([0-9]+)$");

        patternMatcher = patternParametersXLife.matcher(fileContentList.get(FIRST_LINE));

        if(patternMatcher.matches()) {
            extractXlifeRuleFormat();
        }
        else{
            patternMatcher = patternParameters.matcher(fileContentList.get(FIRST_LINE));
            if(!patternMatcher.matches())
                throw new PatternFormatException("Couldn't extract grid properties from RLE file");

            if (lastImportedRule != null)
                throw new PatternFormatException("Multiple rules present");

            lastImportedRule = patternMatcher.group(3);
        }

        patternHeight = Integer.parseInt(patternMatcher.group(1));
        patternWidth = Integer.parseInt(patternMatcher.group(2));

        fileContentList.remove(FIRST_LINE);
    }

    /**
     * Runs through the String list and builds the Pattern array.
     * @return True if build is successful, false if not.
     */
    private static boolean buildPatternArray() throws PatternFormatException {

        int tagOccurrence = 0;
        int x = 0;
        int y = 0;

        int cellsAdded = 0;

        for (String currentLine : fileContentList) {
            for (int currentCharIndex = 0; currentCharIndex < currentLine.length(); currentCharIndex++) {

                currentCharacter = currentLine.charAt(currentCharIndex);

                if (Character.isDigit(currentCharacter)) {
                    tagOccurrence = tagOccurrence * 10 + (currentCharacter - '0');
                } else if (currentCharacter == 'b') {
                    if (tagOccurrence == 0) {
                        setPatternArray(x, y, false);
                        x++;
                    } else {
                        for (int i = 0; i < tagOccurrence; i++) {
                            setPatternArray(x, y, false);
                            x++;
                        }
                        tagOccurrence = 0;
                    }
                } else if (currentCharacter == 'o') {

                    if (tagOccurrence == 0) {
                        setPatternArray(x, y, true);
                        x++;
                        cellsAdded++;
                    } else {
                        for (int i = 0; i < tagOccurrence; i++) {
                            setPatternArray(x, y, true);
                            x++;
                            cellsAdded++;
                        }
                        tagOccurrence = 0;
                    }
                } else if (currentCharacter == '$') {

                    if (tagOccurrence == 0) {
                        y++;
                    } else {
                        y += tagOccurrence;
                    }
                    tagOccurrence = 0;

                    x = 0;
                } else if (currentCharacter == '!') {
                    System.out.println("Cells imported: " + cellsAdded);
                    return true;
                }
            }
        }
        return false;
    }

    private static void setPatternArray(int x, int y, boolean state) throws PatternFormatException {
        if(x >= patternArray.length || y >= patternArray[x].length){
            throw new PatternFormatException("Pattern properties too small");
        }
        patternArray[x][y] = state;
    }
}