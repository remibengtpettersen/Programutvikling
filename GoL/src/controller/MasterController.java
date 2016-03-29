package controller;

import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Configuration;
import model.PatternParser;
import model.PatternFormatException;

import java.io.File;
import java.io.IOException;
import java.io.SyncFailedException;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class MasterController {

    public Configuration configuration;
    public Stage stage;
    public Scene scene;

    @FXML public CanvasController canvasController;
    @FXML public MenuController menuController;
    @FXML public ToolController toolController;

    private FileChooser patternChooser = new FileChooser();

    public void initialize(Stage stage, BorderPane root) throws IOException {

        configuration = new Configuration();
        configuration.getConfigurationFromFile();
        configuration.setConfiguration();

        this.stage = stage;
        scene = new Scene(root, configuration.getWidth(), configuration.getHeight());

        canvasController.initialize(this);
        menuController.initialize(this);
        toolController.initialize(this);

        stage.setTitle("Game of life - GoL");
        stage.setScene(scene);
        stage.show();

        patternChooser.setTitle("Choose pattern file");
        patternChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GoL pattern files", "*.rle", "*.lif", "*.life", "*.cells"));

        String patternDir = "../../Patterns";
       // patternChooser.setInitialDirectory(new File(patternDir)); virket ikke for meg

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
}

