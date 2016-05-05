package model.Parser;

import model.PatternFormatException;

import java.util.regex.Pattern;

/**
 * Created by Truls on 12/04/16.
 */
public class Life05Parser extends PatternParser {

    private static int startPosX;
    private static int startPosY;
    private static int offSetX;
    private static int offSetY;
    private static int startOfSubPattern;

    /**
     * reads the string content from a Life 1.05 file
     * @return the boolean array produced from the list
     */
    static boolean[][] parseLife05() throws PatternFormatException {

        patternWidth = 0;
        patternHeight = 0;
        startPosX = 0;
        startPosY = 0;
        offSetX = 0;
        offSetY = 0;
        startOfSubPattern = 0;
        patternParameters = Pattern.compile("#P (.+) (.+)");

        extractRulesFromMetaData();
        findUpperLeftCellCoordinates();
        getGridProperties();
        buildPatternArray();

        return patternArray;
    }



    private static void extractRulesFromMetaData() throws PatternFormatException {

        while (!fileContentList.get(FIRST_LINE).startsWith("#P")){
            if(fileContentList.get(FIRST_LINE).startsWith("#R")){
                extractRules();
            }

            else if(fileContentList.get(FIRST_LINE).startsWith("#N")) {
                if (lastImportedRule != null)
                    throw new PatternFormatException("Multiple rules present");
                lastImportedRule = "23/3"; // Conway's default rule
            }

            fileContentList.remove(FIRST_LINE);
        }
    }

    private static void extractRules() throws PatternFormatException {

        if(lastImportedRule != null) {
            throw new PatternFormatException("Multiple rules present");
        }
        lastImportedRule = fileContentList.get(FIRST_LINE).replaceAll("[^1-9/1-9]", "");
    }

    private static void findUpperLeftCellCoordinates() {

        for (String currentLine : fileContentList) {

            if (currentLine.startsWith("#P")) {
                patternMatcher = patternParameters.matcher(currentLine);

                if (patternMatcher.matches()) {

                    if (Integer.parseInt(patternMatcher.group(1)) < startPosX) {
                        startPosX = Integer.parseInt(patternMatcher.group(1));
                    }

                    if (Integer.parseInt(patternMatcher.group(2)) < startPosY) {
                        startPosY = Integer.parseInt(patternMatcher.group(2));
                    }
                }
            }
        }
    }

    private static void getGridProperties() {

        for(int i = 0; i < fileContentList.size(); i++){

            if(fileContentList.get(i).startsWith("#P")){
                patternMatcher = patternParameters.matcher(fileContentList.get(i));

                if(patternMatcher.matches()){
                    offSetX = Integer.parseInt(patternMatcher.group(1));
                    offSetY = Integer.parseInt(patternMatcher.group(2));
                }

                startOfSubPattern = i;
            }

            if(fileContentList.get(i).length() - startPosX + offSetX > patternWidth){
                patternWidth = fileContentList.get(i).length() - startPosX + offSetX;
            }

            if((i- startOfSubPattern) - startPosY + offSetY > patternHeight){
                patternHeight = i - startOfSubPattern - startPosY + offSetY;
            }
        }
    }

    private static void buildPatternArray() throws PatternFormatException {

        int x = 0;
        int y = 0;
        patternArray = new boolean[patternWidth][patternHeight];

        for (String currentLine : fileContentList) {
            if (currentLine.startsWith("#P")) {
                patternMatcher = patternParameters.matcher(currentLine);
                if (patternMatcher.matches()) {
                    offSetX = Integer.parseInt(patternMatcher.group(1));
                    offSetY = Integer.parseInt(patternMatcher.group(2));
                    x = offSetX - startPosX;
                    y = offSetY - startPosY;
                }
            } else {
                for (int j = 0; j < currentLine.length(); j++) {
                    currentCharacter = currentLine.charAt(j);

                    if (currentCharacter == '.') {
                        patternArray[x][y] = false;
                        x++;
                    } else if (currentCharacter == '*') {
                        patternArray[x][y] = true;
                        x++;
                    }
                }
                y++;
                x = offSetX - startPosX;
            }
        }
    }
}
