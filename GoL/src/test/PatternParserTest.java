package test;

import model.PatternParser;
import org.junit.*;
import tools.Utilities;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * This is the test class for the PatterParser that will test if the different GoL patterns are imported correctly.
 */
public class PatternParserTest {

    private static boolean [][] plainText;
    private static boolean [][] rle;
    private static boolean [][] life05;
    private static boolean [][] life06;

    private static boolean [][] life06URL;

    private static String gliderUrl = "http://www.conwaylife.com/patterns/glider_106.lif";

    private static boolean [][] threeEnginecordershiprake05Pattern;
    private static boolean [][] threeEnginecordershiprake06Pattern;

    private static boolean [][] glider;


    /**
     * Sets up the blueprint for the glider in an array. Then loads the RLE, PlainText, Life 1.05 and Life 1.06 from files.
     * @throws IOException Incompatible file format or missing file.
     */
    @BeforeClass
    public static void setUp() throws IOException {

        // This the blueprint of a glider
        glider = new boolean[][]{
                new boolean[]{false, false, true},
                new boolean[]{true, false, true},
                new boolean[]{false, true, true}};

        // This is simple glider pattern
        String gliderPlainText = "src/test/Test pattern/glider.cells";
        plainText = PatternParser.read(new File(gliderPlainText));

        String gliderRLE = "src/test/Test pattern/glider.cells";
        rle = PatternParser.read(new File(gliderRLE));

        String gliderLife05 = "src/test/Test pattern/glider.cells";
        life05 = PatternParser.read(new File(gliderLife05));

        String gliderLife06 = "src/test/Test pattern/glider.cells";
        life06 = PatternParser.read(new File(gliderLife06));

        life06URL = PatternParser.readUrl(gliderUrl);

        // This is a big more complex pattern called a three Engine cordership rake
        String threeEnginecordershiprake05 = "src/test/Test pattern/3enginecordershiprake_105.lif";
        threeEnginecordershiprake05Pattern = PatternParser.read(new File(threeEnginecordershiprake05));

        String threeEnginecordershiprake06 = "src/test/Test pattern/3enginecordershiprake_106.lif";
        threeEnginecordershiprake06Pattern = PatternParser.read(new File(threeEnginecordershiprake06));

    }

    /**
     * Reads a glider in plain text format and compares it to the blueprint array.
     */
    @Test
    public void testGliderPlainText(){
        Utilities.print2DArray(plainText);
        Assert.assertArrayEquals(glider, plainText);
    }
    /**
     * Reads a glider in RLE format and compares it to the blueprint array.
     */
    @Test
    public void testGliderRLE(){
        Utilities.print2DArray(rle);
        Assert.assertArrayEquals(glider, rle);
    }
    /**
     * Reads a glider in Life 1.05 format and compares it to the blueprint array.
     */
    @Test
    public void testGliderLife05(){
        Utilities.print2DArray(life05);
        Assert.assertArrayEquals(glider, life05);
    }
    /**
     * Reads a glider in Life 1.06 format and compares it to the blueprint array.
     */
    @Test
    public void testGliderLife06(){
        Utilities.print2DArray(life06);
        Assert.assertArrayEquals(glider, life06);
    }

    /**
     * Reads a glider in Life 1.06 format from a URL and compares it to the blueprint array.
     */
    @Test
    public void testGliderLife06ThroughUrl(){
        Utilities.print2DArray(life06URL);
        Assert.assertArrayEquals(glider, life06URL);
    }



    /**
     * Reads a threeEnginecordershiprake in Life 1.05 and Life 1.06 an compares them to each other.
     */
    @Test
    public void compareUsingthreeEnginecordershiprake(){
        Assert.assertArrayEquals(threeEnginecordershiprake06Pattern, threeEnginecordershiprake05Pattern);
    }
}