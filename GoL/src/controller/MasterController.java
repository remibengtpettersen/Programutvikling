package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Configuration;
import model.Parser.PatternParser;
import model.PatternFormatException;
import s305080.theStrip.TheStrip;
import model.*;
import s305061.StatController;

import java.io.File;
import java.io.IOException;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class MasterController {

    public Configuration configuration;
    public Stage stage;
    public Scene scene;

    @FXML private CanvasController canvasController;
    @FXML private MenuController menuController;
    @FXML private ToolController toolController;
    @FXML private StatController statController;

    //region s305080
    TheStrip theStrip;
    //endregion
    private FileChooser patternChooser = new FileChooser();
    private Configuration config;

    /**
     *
     * @param stage the primary stage
     * @param root the border pane with the mainView
     * @throws IOException
     */
    public void initialize(Stage stage, BorderPane root) throws IOException {

        configuration = new Configuration("./GoL/resources/config.properties");

        this.stage = stage;
        scene = new Scene(root, configuration.getWidth(), configuration.getHeight());

        stage.setTitle("Game of life - GoL");
        stage.setScene(scene);
        stage.show();

        toolController.initialize(this);
        canvasController.initialize(this);
        menuController.initialize(this);


        patternChooser.setTitle("Choose pattern file");
        patternChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GoL pattern files", "*.rle", "*.lif", "*.life", "*.cells"));

        String patternDir = "../../Patterns";

        bindCanvas();
    }

    /**
     * Binds the canvas size to the scene size
     */
    private void bindCanvas(){

        canvasController.getCanvas().widthProperty().bind(scene.widthProperty());
        canvasController.getCanvas().heightProperty().bind(scene.heightProperty().subtract(70));
    }

    /**
     * Opens the file chooser so the user can choose a pattern file to import
     */
    public void choosePattern(){

        canvasController.busy = true;
        File file = patternChooser.showOpenDialog(stage);
        canvasController.busy = false;
        if(file != null) {

            try {
                canvasController.setImportPattern(PatternParser.read(file));
            }
            catch (PatternFormatException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //region s305061
    /**
     * Opens the statistics window
     */
    public void openStatWindow() {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../s305061/StatView.fxml"));

        try {
            BorderPane root = loader.load();
            statController = loader.getController();

            Stage statStage = new Stage();
            statStage.setScene(new Scene(root));

            //statController.updateStats(canvasController.gol);
            statController.setGol(canvasController.gol);
            statStage.setTitle("Statistics");

            statStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the statistics (s305061) FXML document, IO exception");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the statistics (s305061) FXML document, unknown exception");
        }
    }

    public void updateGameInfo(GameOfLife gol) {

        toolController.giveCellCount(gol.getCellCount());

        //if(statController != null)
            //statController.updateStats(gol);
    }

    public void clearGrid() {

        canvasController.clearGrid();

        if(statController != null)
            statController.clearStats();
    }
    //endregion

    public CanvasController getCanvasController(){
        return canvasController;
    }
    public ToolController getToolController(){
        return toolController;
    }
    public MenuController getMenuController(){
        return menuController;
    }

    //region s305080

    /**
     * Displays theStrip
     */
    void showTheStrip() {
        theStrip = new TheStrip();
        theStrip.display(canvasController.gol.getGrid(), this);
    }

    /**
     * Closes the Strip
     */
    void closeTheStrip(){
        theStrip.close();
        theStrip = null;
    }

    public Configuration getConfig() {
        return configuration;
    }
    //endregion
}

