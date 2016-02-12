package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class MasterController {

    Stage theStage;
    Scene theScene;
    Parent root;
    FXMLLoader loader;
    FrontPageController frontPageController;
    GameController gameController;
    private short stageWidth = 600;
    private short stageHeight = 400;

    /**
     *
     * @param primaryStage
     */
    public MasterController(Stage primaryStage) throws IOException {
        theStage = primaryStage;
        theStage.setTitle("Game of Life");
        openFrontPage();
    }

    void openFrontPage() throws IOException {

        loadResource("../view/FrontPage.fxml");
        frontPageController = loader.getController();
        frontPageController.setMaster(this);

    }

    void launchGame() throws IOException {

        loadResource("../view/GamePage.fxml");
        gameController = loader.getController();
        gameController.setMaster(this);

    }

    /**
     * Loads a FXML-file onto the primaryStage
     * @param path relative path to FXML-file
     * @throws IOException
     */
    private void loadResource(String path) throws IOException {
        loader = new FXMLLoader(getClass().getResource(path));
        root = loader.load();
        theScene = new Scene(root, stageWidth, stageHeight);
        theStage.setScene(theScene);
        theStage.show();
    }
}
