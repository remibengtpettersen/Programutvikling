package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Configuration;
import model.GameOfLife;
import model.PatternFormatException;
import model.PatternParser;

import java.io.File;
import java.io.IOException;

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
    @FXML public StatController statController;

    private FileChooser patternChooser = new FileChooser();

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

        canvasController.initialize(this);
        menuController.initialize(this);
        toolController.initialize(this);

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
     * opens the file chooser so the user can choose a pattern file to import
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

    public void openStatWindow() {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../view/StatView.fxml"));

        try {
            Stage statStage = new Stage();
            stage.setTitle("Statistics");
            statStage.setScene(loader.load());
            statController = loader.getController();
            statController.updateStatistics(canvasController.gol);
            statStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML documents");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("");
        }
    }

    public void updateStatWindow(GameOfLife gol) {

        if(statController != null && gol != null)
            statController.updateStatistics(gol);
    }
}

