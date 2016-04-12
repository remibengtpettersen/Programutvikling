package model.Parser;

import java.util.regex.Pattern;

/**
 * Created by Truls on 12/04/16.
 */
public class Life05Parser extends PatternParser {
    /**
     * reads the string content from a Life 1.05 file
     * @return the boolean array produced from the list
     */
    static boolean[][] life05() {

        patternWidth = 0;
        patternHeight = 0;
        patternParameters = Pattern.compile("#P (.+) (.+)");

        while (!lineList.get(FIRST_LINE).startsWith("#P")){
            lineList.remove(FIRST_LINE);
        }

        int startPosX = 0;
        int startPosY = 0;

        for(int i = 0; i < lineList.size(); i++) {
            if (lineList.get(i).startsWith("#P")) {
                patternMatcher = patternParameters.matcher(lineList.get(i));
                if (patternMatcher.matches()) {
                    if(Integer.parseInt(patternMatcher.group(1)) < startPosX){
                        startPosX = Integer.parseInt(patternMatcher.group(1));
                    }
                    if(Integer.parseInt(patternMatcher.group(2)) < startPosY){
                        startPosY= Integer.parseInt(patternMatcher.group(2));
                    }
                }
            }
        }

        int offSetX = 0;
        int offSetY = 0;

        int unknownInt = 0;

        for(int i = 0; i < lineList.size(); i++){
            if(lineList.get(i).startsWith("#P")){
                patternMatcher = patternParameters.matcher(lineList.get(i));
                if(patternMatcher.matches()){
                    offSetX = Integer.parseInt(patternMatcher.group(1));
                    offSetY = Integer.parseInt(patternMatcher.group(2));
                }

                unknownInt = i;
            }
            if(lineList.get(i).length() - startPosX + offSetX > patternWidth){
                patternWidth = lineList.get(i).length() - startPosX + offSetX;
            }
            if((i-unknownInt) - startPosY + offSetY > patternHeight){
                patternHeight = i - unknownInt - startPosY + offSetY;
            }
        }

        patternArray = new boolean[patternWidth][patternHeight];

        int x = 0;
        int y = 0;

        for(int i = 0; i < lineList.size(); i++){
            if(lineList.get(i).startsWith("#P")){
                patternMatcher = patternParameters.matcher(lineList.get(i));
                if(patternMatcher.matches()){
                    offSetX = Integer.parseInt(patternMatcher.group(1));
                    offSetY = Integer.parseInt(patternMatcher.group(2));
                    x = offSetX - startPosX;
                    y = offSetY - startPosY;
                }
            }
            else{
                for(int j = 0; j < lineList.get(i).length(); j++){
                    currentCharacter = lineList.get(i).charAt(j);

                    if(currentCharacter == '.'){
                        patternArray[x][y] = false;
                        x++;
                    }
                    else if(currentCharacter == '*'){
                        patternArray[x][y] = true;
                        x++;
                    }
                }
                y++;
                x = offSetX - startPosX;
            }
        }
        return patternArray;
    }
}
