package model;

import java.io.*;
import java.util.Objects;
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
    private String propertiesFileName = "./GoL/resources/config.properties";
    private Properties properties = new Properties();
    private File file = new File(propertiesFileName);
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

    public Configuration() {
        initialize();
    }

    private void initialize() {
        convertFileToInputStream();

        if (!file.exists()) {
            createFile();
            generateConfigurationFileContent();
            writeConfigurationToFile();
            setInputStream();
        }
    }

    private void convertFileToInputStream() {
        inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
    }

    private void setInputStream() {
        try {
            inputStream = new FileInputStream("./GoL/resources/config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //region getters
    public int getWidth() {
        return Integer.parseInt(this.windowWidth);
    }

    public int getHeight() {
        return Integer.parseInt(this.windowHeight);
    }

    public String getCellType() {
        return this.cellType;
    }

    public String getCellColor() {
        return this.cellColor.toUpperCase();
    }

    public String getBackgroundColor() {
        return this.backgroundColor.toUpperCase();
    }

    public double getCellSize() {
        return Double.parseDouble(this.cellSize);
    }

    public short getGameHeight() {return Short.parseShort(gameHeight);}

    public short getGameWidth() {return Short.parseShort(gameWidth);}

    public int getGameSpeed() {
        return Integer.parseInt(this.gameSpeed);
    }

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
                                    "cell.color = green\n" +
                                    "cell.size = 10\n" +
                                    "# Set canvas properties\n" +
                                    "canvas.background.color = white\n" +
                                    "canvas.grid = false\n";
    }

    private void createFile() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public boolean isCanvasGrid() {
        canvasGrid.toLowerCase();

        if (Objects.equals(canvasGrid, "true")) {
            return true;
        }
        else {
            return false;
        }
    }
}