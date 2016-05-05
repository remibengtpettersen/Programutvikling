package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Configuration;
import model.Parser.PatternParser;
import model.PatternFormatException;
import s305061.gif.GifController;
import s305061.statistics.StatController;
import s305080.Statistics.Stats;
import s305080.theStrip.TheStrip;
import tools.MessageBox;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class MasterController {

    public Configuration configuration;
    public Stage stage;
    public Scene scene;

    private FileChooser patternChooser = new FileChooser();

    @FXML private CanvasController canvasController;
    @FXML private MenuController menuController;
    @FXML private ToolController toolController;

    //region s305061
    @FXML private StatController statController;
    @FXML private GifController gifController;
    //endregion

    //region s305080
    TheStrip theStrip;
    Stats stats;
    //endregion

    /**
     *
     * Called to initialize master controller after its root element has been completely processed
     *
     * @param stage The primary stage
     * @param root The border pane with the mainView
     * @throws IOException
     */
    public void initialize(Stage stage, BorderPane root) throws IOException {

        // read and loads game of life configurations from file
        configuration = new Configuration("../GoL/resources/config.properties");

        // set stage to field
        this.stage = stage;

        // instantiate scene with root element and size
        scene = new Scene(root, configuration.getWidth(), configuration.getHeight());

        // set title and scene to stage
        stage.setTitle("Game of life - GoL");
        stage.setScene(scene);

        // open GUI
        stage.show();

        // pass reference ,master controller, to toolController, canvasController and menuController
        toolController.initialize(this);
        canvasController.initialize(this);
        menuController.initialize(this);

        // set title for pattern chooser and configure allowed extensions
        patternChooser.setTitle("Choose pattern file");
        patternChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GoL pattern files", "*.rle", "*.lif", "*.life", "*.cells"));

        String patternDir = "../../Patterns";

        // bind canvas to scene size
        bindCanvas();
    }

    /**
     * Binds the canvas size to the scene size
     */
    private void bindCanvas(){
        // binds canvas to scene width
        canvasController.getCanvas().widthProperty().bind(scene.widthProperty());
        // binds canvas to scene height and subtracts an area for menu- and toolbar
        canvasController.getCanvas().heightProperty().bind(scene.heightProperty().subtract(70));
    }

    //region s305061
    /**
     * Opens the s305061 statistics window
     */
    public void openStatWindow() {
        // load fxml view for statistics
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../s305061/statistics/StatView.fxml"));

        // try to load fxml to reference
        try {
            Parent root = loader.load();
            // set control
            statController = loader.getController();

            // create stage and set scene
            Stage statStage = new Stage();
            statStage.setScene(new Scene(root));


            statController.setGol(canvasController.gol);
            statStage.setTitle("Statistics");

            statStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the statistics (s305061) FXML document, IO exception");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the statistics (s305061) FXML document");
        }
    }

    public void openGifWindow() {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../s305061/gif/GifView.fxml"));

        try {
            Parent root = loader.load();
            gifController = loader.getController();

            Stage gifStage = new Stage();
            gifStage.setScene(new Scene(root));

            gifController.initialize(gifStage, canvasController.gol);
            gifStage.setTitle("Create GIF");

            gifStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the gif (s305061) FXML document, IO exception");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the gif (s305061) FXML document");
        }
    }
    //endregion

    /**
     * Opens the file chooser so the user can choose a pattern file to import
     */
    public void choosePattern(){

        // lock canvas control
        canvasController.interaction = true;
        // open pattern chooser
        File file = patternChooser.showOpenDialog(stage);

        // release canvas control
        canvasController.interaction = false;

        // check if file is NOT null
        if(file != null) {

            try {
                // set pattern to canvas
                canvasController.setClipBoardPattern(PatternParser.read(file));
            }
            catch (PatternFormatException e) {
                MessageBox.alert(e.getMessage());
            } catch (IOException e) {
                MessageBox.alert("Could not read file");
            }
        }
    }

    /**
     *
     * @return
     */
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
        theStrip.display(this);
        if(stats == null)
            stage.setOnCloseRequest(event -> closeTheStrip());
        else{
            stage.setOnCloseRequest(event -> {
                closeTheStrip();
                closeStats();
            });
        }
    }
    public void showStats() {
        stats = new Stats();
        stats.display(canvasController.gol, this);
        if(theStrip == null)
            stage.setOnCloseRequest(event -> closeStats());
        else{
            stage.setOnCloseRequest(event -> {
                closeTheStrip();
                closeStats();
            });
        }
    }


    /**
     * Closes the Strip
     */
    void closeTheStrip(){
        theStrip.close();
        theStrip = null;
    }


    public void closeStats() {
        stats.close();
        stats = null;
    }

    public Configuration getConfig() {
        return configuration;
    }

    public void importFromUrl() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Import URL");
        dialog.setHeaderText(null);
        dialog.setContentText("Paste url to import from:");

        canvasController.interaction = true;

        Optional<String> result = dialog.showAndWait();

// The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(name -> {
            try {
                canvasController.setClipBoardPattern(PatternParser.readUrl(result.get()));
            }
            catch (PatternFormatException e){
                MessageBox.alert(e.getMessage());
            }
            catch (IOException e) {
                MessageBox.alert("Could not read from URL");
            }
        });
        canvasController.interaction = false;
    }


    //endregion
}

