package model;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 14.03.2016.
 * Pair programming.
 */
public class Configuration {

    //region properties
    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    private InputStream inputStream;
    private String propertiesFileName; //= "./resources/config.properties";
    private Properties properties = new Properties();
    private File file;
    private String windowWidth;
    private String windowHeight;
    private String gameSpeed;
    private String cellSize;
    private String gameSize;
    private String cellColor;
    private String cellType;
    private String backgroundColor;
    private String configurationsString;
    private String canvasGrid;
    private String gameHeight;
    private String gameWidth;
    //endregion

    /**
     * Configuration constructor.
     * @param propertiesFileName
     */
    public Configuration(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
        initialize();
        getConfigurationFromFile();
        setConfiguration();
    }

    /**
     * Creates a file if it is not there
     */
    private void initialize() {

        file = new File(propertiesFileName);

        if (!file.exists()) {
            createFile();
            generateConfigurationFileContent();
            writeConfigurationToFile();
            setInputStream();
        }
        else {
            setInputStream();
        }
    }

    /**
     * creates the inputStream from the config file
     */
    private void setInputStream() {
        try {
            inputStream = new FileInputStream(propertiesFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //region getters

    /**
     * Gets the width value from the config file
     * @return the with value from the config file
     */
    public int getWidth() {
        return Integer.parseInt(this.windowWidth);
    }

    /**
     * gets the height value from the config file
     * @return the height value from the config file
     */
    public int getHeight() {
        return Integer.parseInt(this.windowHeight);
    }

    public String getCellType() {
        return this.cellType;
    }

    /**
     * gets the cell color from the config file
     * @return the cell color from the config file
     */
    public String getCellColor() {
        return this.cellColor.toUpperCase();
    }

    /**
     * gets the background color from the config file
     * @return the background color from the config file
     */
    public String getBackgroundColor() {
        return this.backgroundColor.toUpperCase();
    }

    /**
     * gets the cell size from the config file
     * @return the cell size from the config file
     */
    public double getCellSize() {
        return Double.parseDouble(this.cellSize);
    }

    /**
     * gets the game height from the config file
     * @return the game height from the config file
     */
    public short getGameHeight() {return Short.parseShort(gameHeight);}

    /**
     * gets the game width from the config file
     * @return game width from the config file
     */
    public short getGameWidth() {return Short.parseShort(gameWidth);}

    /**
     * gets the game speed from the config file
     * @return the game speed from the config file
     */
    public int getGameSpeed() {
        return Integer.parseInt(this.gameSpeed);
    }

    /**
     * Returns true if the grid lines should be displayed
     * @return true if the grid lines should be displayed
     */
    public boolean isGridLinesOn() { return Boolean.parseBoolean(canvasGrid); }


    /**
     * Generates content for a new config file in the string configurationsString
     */
    private void generateConfigurationFileContent() {
        configurationsString =      "##########################################\n" +
                                    "# Configuration file for Game of Life    #\n" +
                                    "# ¡Enter text in lower case!             #\n" +
                                    "# Separate words with '.' e.g cell.color #\n" +
                                    "##########################################\n" +
                                    "# Set window size properties\n" +
                                    "window.width = 1000\n" +
                                    "window.height = 800\n" +
                                    "# Set game properties\n" +
                                    "game.speed = 20\n" +
                                    "game.width = 1000\n" +
                                    "game.height = 1000\n" +
                                    "# Set cell properties\n" +
                                    "cell.color = black\n" +
                                    "cell.size = 10\n" +
                                    "# Set canvas properties\n" +
                                    "canvas.background.color = white\n" +
                                    "canvas.grid = false\n";
    }

    /**
     * Creates a new config file
     */
    private void createFile() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads the input stream into the properties field
     */
    public void getConfigurationFromFile() {
        try {
            properties.load(inputStream);
            inputStream.close();
        }
        catch (FileNotFoundException exception) {
            LOGGER.log(Level.SEVERE, "property file '" + propertiesFileName + "' not found in the classpath");
        }
        catch (IOException exception) {
            LOGGER.log(Level.SEVERE, exception.toString());
        }
    }
    //endregion

    //region setters

    /**
     * sets the properties from "properties" to the local variables
     */
    public void setConfiguration() {
        this.windowWidth = this.properties.getProperty("window.width");
        this.windowHeight = this.properties.getProperty("window.height");
        this.gameSpeed = this.properties.getProperty("game.speed");
        this.cellColor = this.properties.getProperty("cell.color");
        this.cellSize = this.properties.getProperty("cell.size");
        this.backgroundColor = this.properties.getProperty("canvas.background.color");
        this.gameHeight = properties.getProperty("game.height");
        this.gameWidth = properties.getProperty("game.width");
        this.canvasGrid = properties.getProperty("canvas.grid");
    }
    //endregion

    /**
     * Writes the new config content to the file
     */
    private void writeConfigurationToFile() {
        try
        {
            FileWriter fileWriter = new FileWriter(propertiesFileName, true);
            fileWriter.write(configurationsString);
            fileWriter.close();
        }
        catch(IOException exception)
        {
            LOGGER.log(Level.SEVERE, "Error");
        }
    }

}