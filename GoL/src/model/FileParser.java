package model;

/**
 * Created by Truls on 18/01/16.
 */

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileParser {

    private static final byte FIRSTLINE = 0;
    private static Pattern patternParameters;
    private static Matcher patternMatcher;
    private static int patternHeight;
    private static int patternWidth;
    private static boolean[][] patternArray;
    private static List<String> lineList;

    /**
     * Reads a Game of Life pattern file and returns an array of the pattern
     * @param patternFile the file to read frome
     * @return the boolean array produced from the file
     */
    static public boolean[][] read(File patternFile) throws IOException {

        if(patternFile.toString().endsWith(".cells")){
            return readPlainText(patternFile);
        }
        else if(patternFile.toString().endsWith(".rle")){
            return readRLE(patternFile);
        }
        else if(patternFile.toString().endsWith(".lif") || patternFile.toString().endsWith(".life")){
            return readLife(patternFile);
        }
        return null;
    }

    /**
     * Reads
     * @param patternFile a .lif or .life file
     * @return the boolean array produced from the file
     */
    private static boolean[][] readLife(File patternFile) throws IOException {

        lineList = Files.readAllLines(patternFile.toPath());

        if(lineList.get(FIRSTLINE).contains("Life 1.05")){
            return life05(lineList);
        }
        else if(lineList.get(FIRSTLINE).contains("Life 1.06")) {
            return life06(lineList);
        }
        return null;
    }

    /**
     * reads the string content from a Life 1.05 file
     * @param list a list of strings red from a .lif file
     * @return the boolean array produced from the list
     */
    private static boolean[][] life05(List<String> list) {

        patternWidth = 0;
        patternHeight = 0;
        patternParameters = Pattern.compile("#P (.+) (.+)");

        while (!list.get(FIRSTLINE).startsWith("#P")){
            list.remove(FIRSTLINE);
        }

        int startPosX = 0;
        int startPosY = 0;

        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).startsWith("#P")) {
                patternMatcher = patternParameters.matcher(list.get(i));
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

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).startsWith("#P")){
                patternMatcher = patternParameters.matcher(list.get(i));
                if(patternMatcher.matches()){
                    offSetX = Integer.parseInt(patternMatcher.group(1));
                    offSetY = Integer.parseInt(patternMatcher.group(2));
                }

                unknownInt = i;
            }
            if(list.get(i).length() - startPosX + offSetX > patternWidth){
                patternWidth = list.get(i).length() - startPosX + offSetX;
            }
            if((i-unknownInt) - startPosY + offSetY > patternHeight){
                patternHeight = i - unknownInt - startPosY + offSetY;
            }
        }

        boolean[][] patternArray = new boolean[patternWidth][patternHeight];

        char currentCharacter;

        int x = 0;
        int y = 0;

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).startsWith("#P")){
                patternMatcher = patternParameters.matcher(list.get(i));
                if(patternMatcher.matches()){
                    System.out.println("Did match");
                    offSetX = Integer.parseInt(patternMatcher.group(1));
                    offSetY = Integer.parseInt(patternMatcher.group(2));
                    x = offSetX - startPosX;
                    y = offSetY - startPosY;
                }
            }
            else{
                for(int j = 0; j < list.get(i).length(); j++){
                    currentCharacter = list.get(i).charAt(j);

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


    /**
     * reads the string content from a Life 1.06 file
     * @param list a list of strings red from a .lif file
     * @return the boolean array produced from the list
     */
    private static boolean[][] life06(List<String> list) {

        while(list.get(FIRSTLINE).startsWith("#")){
            list.remove(FIRSTLINE);
        }
        patternParameters = Pattern.compile("(.+) (.+)");
        //Matcher m;

        patternMatcher = patternParameters.matcher(list.get(FIRSTLINE));

        if(!patternMatcher.matches()){

            return null;
        }
        int startPosX = Integer.parseInt(patternMatcher.group(1));
        int startPosY = Integer.parseInt(patternMatcher.group(2));


        int possibleHeight;
        int possibleWidth;

        int width = startPosX;
        int height = startPosY;

        for(int i =0; i < list.size(); i++){
            patternMatcher = patternParameters.matcher(list.get(i));

            if(!patternMatcher.matches()){
                return null;
            }
            possibleWidth = Integer.parseInt(patternMatcher.group(1));
            possibleHeight = Integer.parseInt(patternMatcher.group(2));

            if(possibleHeight > height){
                height = possibleHeight;
            }

            if(possibleWidth > width){
                width = possibleWidth;
            }
            else if(possibleWidth < startPosX){
                startPosX = possibleWidth;
            }

        }

        width-=startPosX;
        height-=startPosY;

        boolean[][] imp = new boolean[width+1][height+1];

        for(int i = 0; i < list.size(); i++){
            patternMatcher = patternParameters.matcher(list.get(i));

            if(!patternMatcher.matches()){
                return null;
            }
            imp[Integer.parseInt(patternMatcher.group(1))-startPosX][Integer.parseInt(patternMatcher.group(2))-startPosY] = true;
        }


        return imp;
    }

    /**
     * Reads a RLE file
     * @param patternFile the RLE file
     * @return the boolean array produced from the file
     */
    private static boolean[][] readRLE(File patternFile) throws IOException {

        lineList = readLinesFromFile(patternFile);

        while (lineList.get(FIRSTLINE).startsWith("#")){
            lineList.remove(FIRSTLINE);
        }

        patternParameters = Pattern.compile("^x = ([0-9]+), y = ([0-9]+), rule = (.+)$");
        patternMatcher = patternParameters.matcher(lineList.get(FIRSTLINE));

        if(!patternMatcher.matches()) {
            throw new PatternFormatException();
        }

        patternHeight = Integer.parseInt(patternMatcher.group(1));
        patternWidth = Integer.parseInt(patternMatcher.group(2));

        patternArray = new boolean[patternHeight][patternWidth];

        lineList.remove(FIRSTLINE);

        char currentCharacter;
        int pattern = 0; //ToBeNamed...
        int x = 0;
        int y = 0;

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
                    }
                    else {
                        for(int k = 0; k < pattern; k++){
                            patternArray[x][y] = true;
                            x++;
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
                    return patternArray;
                }
            }
        }
        return null;
    }

    private static List<String> readLinesFromFile(File patternFile) throws IOException {
        return Files.readAllLines(patternFile.toPath());
    }

    /**
     * Reads a .cells / plain text file
     * @param patternFile The .cells file
     * @return the boolean array produced from the file
     */
    private static boolean[][] readPlainText(File patternFile) throws IOException {

        lineList = Files.readAllLines(patternFile.toPath());

        while(lineList.get(FIRSTLINE).startsWith("!")){
            lineList.remove(FIRSTLINE);
        }

        patternHeight = lineList.size();
        patternWidth = 0;

        for(int x = 0; x < lineList.size(); x++){
            if(lineList.get(x).length() > patternHeight){
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
