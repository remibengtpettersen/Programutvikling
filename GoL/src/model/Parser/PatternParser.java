package model.Parser;

/**
 * Created by Truls on 18/01/16.
 */

import model.PatternFormatException;
import tools.Utilities;

import java.io.*;
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
    static List<String> lineList;
    static char currentCharacter;

    /**
     * Reads a Game of Life pattern file and returns an array of the pattern
     * @param patternFile the file to read frome
     * @return the boolean array produced from the file
     */
    static public boolean[][] read(File patternFile) throws IOException {

        lineList = readLinesFromFile(patternFile);

        if(patternFile.toString().endsWith(".cells")){
            return PlainTextParser.readPlainText();
        }
        else if(patternFile.toString().endsWith(".rle")){
            return RleParser.readRLE();
        }
        else if(patternFile.toString().endsWith(".lif") || patternFile.toString().endsWith(".life")){
            return readLife();
        }
        return null;
    }

    static public boolean[][] readUrl(String pattern) throws IOException {

        lineList = new ArrayList<String>();
        URL url = new URL(pattern);
        Scanner s = new Scanner(url.openStream());
        while (s.hasNext()){
            lineList.add(s.nextLine());
        }

        if(pattern.endsWith(".cells")){
            return PlainTextParser.readPlainText();
        }
        else if(pattern.endsWith(".rle")){
            return RleParser.readRLE();
        }
        else if(pattern.endsWith(".lif") || pattern.endsWith(".life")){
            return readLife();
        }
        return null;
    }

    /**
     * Reads
     * @return the boolean array produced from the file
     */
    private static boolean[][] readLife() throws IOException {

        if(lineList.get(FIRST_LINE).contains("Life 1.05")){
            return Life05Parser.life05();
        }
        else if(lineList.get(FIRST_LINE).contains("Life 1.06")) {
            return Life06Parser.life06();
        }
        return null;
    }

    private static List<String> readLinesFromFile(File patternFile) throws IOException {
        return Files.readAllLines(patternFile.toPath());
    }


}
