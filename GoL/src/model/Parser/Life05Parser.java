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
    static boolean[][] parseLife05() {

        patternWidth = 0;
        patternHeight = 0;
        patternParameters = Pattern.compile("#P (.+) (.+)");

        while (!fileContent.get(FIRST_LINE).startsWith("#P")){
            fileContent.remove(FIRST_LINE);
        }

        int startPosX = 0;
        int startPosY = 0;

        for(int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).startsWith("#P")) {
                patternMatcher = patternParameters.matcher(fileContent.get(i));
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

        for(int i = 0; i < fileContent.size(); i++){
            if(fileContent.get(i).startsWith("#P")){
                patternMatcher = patternParameters.matcher(fileContent.get(i));
                if(patternMatcher.matches()){
                    offSetX = Integer.parseInt(patternMatcher.group(1));
                    offSetY = Integer.parseInt(patternMatcher.group(2));
                }

                unknownInt = i;
            }
            if(fileContent.get(i).length() - startPosX + offSetX > patternWidth){
                patternWidth = fileContent.get(i).length() - startPosX + offSetX;
            }
            if((i-unknownInt) - startPosY + offSetY > patternHeight){
                patternHeight = i - unknownInt - startPosY + offSetY;
            }
        }

        patternArray = new boolean[patternWidth][patternHeight];

        int x = 0;
        int y = 0;

        for(int i = 0; i < fileContent.size(); i++){
            if(fileContent.get(i).startsWith("#P")){
                patternMatcher = patternParameters.matcher(fileContent.get(i));
                if(patternMatcher.matches()){
                    offSetX = Integer.parseInt(patternMatcher.group(1));
                    offSetY = Integer.parseInt(patternMatcher.group(2));
                    x = offSetX - startPosX;
                    y = offSetY - startPosY;
                }
            }
            else{
                for(int j = 0; j < fileContent.get(i).length(); j++){
                    currentCharacter = fileContent.get(i).charAt(j);

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
        lastImportedRule = null;
        return patternArray;
    }
}
