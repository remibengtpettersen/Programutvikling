package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    private Properties properties;

    private String windowWidth = "";
    private String windowHeight = "";
    private String gameSpeed = "";
    private String cellSize = "";
    private String cellColor = "";
    private String cellType = "";
    private String canvasColor = "";

    private String propertiesFileName = "./config.properties";
    //endregion

    public Configuration() {
        initialize();
    }

    private void initialize() {

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
        return this.cellColor;
    }

    public String getCellSize() {
        return this.cellSize;
    }

    public String getGameSpeed() {
        return this.gameSpeed;
    }

    public String getCanvasColor() {
        return this.canvasColor;
    }

    public void getConfigurationFromFile() throws IOException{

        properties = new Properties();
        inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

        try {
            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            }
            else {
                throw new FileNotFoundException();
            }
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
        this.canvasColor = this.properties.getProperty("canvas.color");
    }
    //endregion
}