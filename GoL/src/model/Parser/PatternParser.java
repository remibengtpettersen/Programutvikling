package model.Parser;

/**
 * Created by Truls on 18/01/16.
 */

import model.PatternFormatException;
import tools.MessageBox;

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

    static final byte FIRST_LINE = 0;
    static Pattern patternParameters;
    static Matcher patternMatcher;
    static int patternHeight;
    static int patternWidth;
    static boolean[][] patternArray;
    static List<String> fileContentList;
    static List<String> metaData;
    static char currentCharacter;
    static String lastImportedRule;
    private static URL url;

    /**
     * Reads a Game of Life pattern file and returns an array of the pattern
     * @param patternFile the file to read from
     * @return the boolean array produced from the file
     * @throws IOException if the file could not be red
     */
    static public boolean[][] read(File patternFile) throws IOException {

        lastImportedRule = null;

        fileContentList = readLinesFromFile(patternFile);

        if(patternFile.toString().endsWith(".cells")){
            return PlainTextParser.parsePlainText();
        }
        else if(patternFile.toString().endsWith(".rle")){
            return RleParser.parseRle();
        }
        else if(patternFile.toString().endsWith(".lif") || patternFile.toString().endsWith(".life")){
            return checkLifeFormat();
        }

        MessageBox.alert("File is not in supported format");
        return null;
    }

    /**
     * Reads a Game of Life pattern file from the web and returns an array of the pattern
     * @param pattern The web file to read from
     * @return The boolean array produced from the file
     * @throws IOException if the url could not be red
     */
    static public boolean[][] readUrl(String pattern) throws IOException {

        lastImportedRule = null;

        fileContentList = new ArrayList<>();
        try {
            url = new URL(pattern);
        }
        catch (MalformedURLException ignored){
            MessageBox.alert("Invalid URL");
            return null;
        }

        Scanner s = new Scanner(url.openStream());

        while (s.hasNext()){
            fileContentList.add(s.nextLine());
        }

        if(pattern.endsWith(".cells")){
            return PlainTextParser.parsePlainText();
        }
        else if(pattern.endsWith(".rle")){
            return RleParser.parseRle();
        }
        else if(pattern.endsWith(".lif") || pattern.endsWith(".life")){
            return checkLifeFormat();
        }

        MessageBox.alert("Could not find supported format");
        return null;
    }

    /**
     * Checks if a file with .lif or .life file types, is either Life 1.05 or Life 1.06,
     * then parses the file with the appropriate parser
     * @return The boolean array produced from the file
     * @throws PatternFormatException if the format is violated
     */
    private static boolean[][] checkLifeFormat() throws PatternFormatException {

        if(fileContentList.get(FIRST_LINE).contains("Life 1.05")){
            return Life05Parser.parseLife05();
        }
        else if(fileContentList.get(FIRST_LINE).contains("Life 1.06")) {
            return Life06Parser.parseLife06();
        }
        return null;
    }

    /**
     * Reads all lines from a file
     * @param patternFile A text file
     * @return A list of strings, with each string containing a line from a file
     * @throws IOException Is thrown if patternFile is unreadable
     */
    private static List<String> readLinesFromFile(File patternFile) throws IOException {
        return Files.readAllLines(patternFile.toPath());
    }

    /**
     * Gets the last imported rule
     * @return The last imported rule
     */
    public static String getLastImportedRule(){
        return lastImportedRule;
    }



}
