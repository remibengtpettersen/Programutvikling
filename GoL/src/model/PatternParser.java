package model;

/**
 * Created by Truls on 18/01/16.
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternParser {

    private static final byte FIRST_LINE = 0;
    private static Pattern patternParameters;
    private static Matcher patternMatcher;
    private static int patternHeight;
    private static int patternWidth;
    private static boolean[][] patternArray;
    private static List<String> lineList;
    private static char currentCharacter;

    /**
     * Reads a Game of Life pattern file and returns an array of the pattern
     * @param patternFile the file to read frome
     * @return the boolean array produced from the file
     */
    static public boolean[][] read(File patternFile) throws IOException {

        lineList = readLinesFromFile(patternFile);

        if(patternFile.toString().endsWith(".cells")){
            return readPlainText();
        }
        else if(patternFile.toString().endsWith(".rle")){
            return readRLE();
        }
        else if(patternFile.toString().endsWith(".lif") || patternFile.toString().endsWith(".life")){
            return readLife();
        }
        return null;
    }

    static public boolean[][] read(String pattern) throws IOException {
        lineList = new ArrayList<String>();
        URL url = new URL(pattern);
        Scanner s = new Scanner(url.openStream());
        while (s.hasNext()){
            lineList.add(s.nextLine());
        }

        if(pattern.endsWith(".cells")){
            return readPlainText();
        }
        else if(pattern.endsWith(".rle")){
            return readRLE();
        }
        else if(pattern.endsWith(".lif") || pattern.endsWith(".life")){
            return readLife();
        }
        return null;
    }



    /**
     * Reads
     * @param patternFile a .lif or .life file
     * @return the boolean array produced from the file
     */
    private static boolean[][] readLife() throws IOException {



        if(lineList.get(FIRST_LINE).contains("Life 1.05")){
            return life05();
        }
        else if(lineList.get(FIRST_LINE).contains("Life 1.06")) {
            return life06();
        }
        return null;
    }

    /**
     * reads the string content from a Life 1.05 file
     * @param lineList a list of strings red from a .lif file
     * @return the boolean array produced from the list
     */
    private static boolean[][] life05() {
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

    /**
     * reads the string content from a Life 1.06 file
     * @param list a list of strings red from a .lif file
     * @return the boolean array produced from the list
     */
    private static boolean[][] life06() throws PatternFormatException {

        while(lineList.get(FIRST_LINE).startsWith("#")){
            lineList.remove(FIRST_LINE);
        }
        patternParameters = Pattern.compile("(.+) (.+)");

        patternMatcher = patternParameters.matcher(lineList.get(FIRST_LINE));

        if(!patternMatcher.matches()){
            throw new PatternFormatException();
        }

        int startPosX = Integer.parseInt(patternMatcher.group(1));
        int startPosY = Integer.parseInt(patternMatcher.group(2));

        int possibleHeight;
        int possibleWidth;

        patternWidth = startPosX;
        possibleHeight = startPosY;

        for(int i =0; i < lineList.size(); i++){
            patternMatcher = patternParameters.matcher(lineList.get(i));

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

        for(int i = 0; i < lineList.size(); i++){
            patternMatcher = patternParameters.matcher(lineList.get(i));

            if(!patternMatcher.matches()){
                throw new PatternFormatException();
            }
            patternArray[Integer.parseInt(patternMatcher.group(1)) - startPosX][Integer.parseInt(patternMatcher.group(2)) - startPosY] = true;
        }
        return patternArray;
    }

    /**
     * Reads a RLE file
     * @param patternFile the RLE file
     * @return the boolean array produced from the file
     */
    private static boolean[][] readRLE() throws IOException {

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
    private static boolean[][] readPlainText() throws IOException {

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
