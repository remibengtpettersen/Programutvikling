package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
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
    public MasterController(Stage primaryStage){
        theStage = primaryStage;
        theStage.setTitle("Game of Life");
        openFrontPage();
    }

    /**
     * Loads and opens the front page
     */
    void openFrontPage(){

        loadResource("../view/FrontPage.fxml");
        frontPageController = loader.getController();
        frontPageController.setMaster(this);
    }

    /**
     * Loads and open the game page
     */
    void launchGame(){

        loadResource("../view/GamePage.fxml");
        gameController = loader.getController();
        gameController.setMaster(this);
    }

    /**
     * Loads a FXML-file onto the primaryStage
     * @param path relative path to FXML-file
     */
    private void loadResource(String path) {

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
    }
}
