package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class MasterController {

    private Stage stage;
    private Scene scene;

    private short stageWidth = 800;
    private short stageHeight = 600;

    @FXML public CanvasController canvasController;      //these should probably be private and with getters
    @FXML public MenuController menuController;
    @FXML public ToolController toolController;

    private static BorderPane root = new BorderPane(); //the root node. Starts out empty, no FXML

    public static BorderPane getRoot() { return root; }

    /**
     *
     * @param primaryStage
     */
     public MasterController(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuView.fxml"));
        //MenuBar bar = loader.load();
        //root.setTop(bar);
        root.setTop(loader.load());                 //loads the menu document, then attaches it to the empty root
        menuController = loader.getController();
        menuController.initialize(this);

        loader = new FXMLLoader(getClass().getResource("../view/CanvasView.fxml"));
        root.setCenter(loader.load());
        canvasController = loader.getController();
        canvasController.initialize(this);

        loader = new FXMLLoader(getClass().getResource("../view/ToolView.fxml"));
        root.setBottom(loader.load());
        toolController = loader.getController();
        toolController.initialize(this);

        stage = primaryStage;
        scene = new Scene(root, stageWidth, stageHeight);
        stage.setTitle("Game of life - GoL");
        stage.setScene(scene);
        stage.show();

        setEvents();
    }

    private void setEvents(){
        canvasController.getCanvas().widthProperty().bind(
                scene.widthProperty());
        canvasController.getCanvas().heightProperty().bind(
                scene.heightProperty().subtract(100));      
    }


    /**
     * Loads a FXML-file onto the primaryStage
     * @param path relative path to FXML-file
     */
    /*private void loadResource(String path) {

        loader = new FXMLLoader(getClass().getResource(path));

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("En IO feil");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("En annen feil");
        }
        theScene = new Scene(root, stageWidth, stageHeight);
        theStage.setScene(theScene);
        theStage.show();
    }*/
}
