package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    /**
     * Loads the manView fxml document and starts the application
     * @param stage the primary stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception{

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../view/MainView.fxml"));

        try {
            BorderPane root = loader.load();
            MasterController rootController = loader.getController();

            rootController.initialize(stage, root);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML documents");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("");
        }
    }

    /**
     * launches the application
     * @param args
     */
    public static void main(String[] args) {
       launch(args);
    }
}
