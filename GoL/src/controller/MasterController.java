package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileParser;
import model.PatternFormatException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class MasterController {

    private Stage stage;
    private Scene scene;

    private short stageWidth = 800;
    private short stageHeight = 600;

    @FXML public CanvasController canvasController;         //these should probably be private and with getters
    @FXML public MenuController menuController;
    @FXML public ToolController toolController;

    FileChooser patternChooser = new FileChooser();

    public void initialize(Stage stage, BorderPane root) {

        this.stage = stage;
        scene = new Scene(root, stageWidth, stageHeight);

        canvasController.initialize(this);
        menuController.initialize(this);
        toolController.initialize(this);

        stage.setTitle("Game of life - GoL");
        stage.setScene(scene);
        stage.show();

        patternChooser.setTitle("Choose pattern file");
        patternChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GoL pattern files", "*.rle", "*.lif", "*.life", "*.cells"));

        setEvents();
    }

    private void setEvents(){
        canvasController.getCanvas().widthProperty().bind(
                scene.widthProperty());
        canvasController.getCanvas().heightProperty().bind(
                scene.heightProperty().subtract(70));
    }

    public void choosePattern(){
        File file = patternChooser.showOpenDialog(stage);
        if(file != null) {
            try {
                canvasController.setImportPattern(FileParser.read(file));
            }
            catch (PatternFormatException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

