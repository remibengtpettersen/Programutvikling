package test;

import javafx.scene.paint.Color;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import model.Configuration;
import model.rules.ClassicRule;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 */
public class ConfigurationTest {

    private static Configuration config;
    static String configFile;


    @BeforeClass
    public static void setUp(){

        configFile = "./src/test/config.properties";

        config = new Configuration(configFile);
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {

        Files.delete(new File(configFile).toPath());
    }

    @Test
    public void getWidth() {
        Assert.assertEquals(1000, config.getWidth());
    }

    @Test
    public void getHeight(){
        Assert.assertEquals(800, config.getHeight());
    }

    @Test
    public void getCellColor() {
        Assert.assertEquals("BLACK", config.getCellColor());
    }

    @Test
    public void getBackgroundColor(){
        Assert.assertEquals("WHITE", config.getBackgroundColor());
    }

    @Test
    public void getCellSize() {
        Assert.assertEquals(10, config.getCellSize(), 0.0001);
    }

    @Test
    public void getGameHeight(){
        Assert.assertEquals(1000, config.getGameHeight());
    }

    @Test
    public void getGameWidth() {
        Assert.assertEquals(1000, config.getGameWidth());
    }

    @Test
    public void getGameSpeed(){
        Assert.assertEquals(20, config.getGameSpeed());
    }

    @Test
    public void isGridLinesOn(){
        Assert.assertEquals(false, config.isGridLinesOn());
    }
}