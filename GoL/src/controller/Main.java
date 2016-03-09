package controller;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
            new MasterController(primaryStage);
        } catch (IOException e){
            System.out.println("Failed to load FXML documents");
        } catch (Exception e) {
            System.out.println("Something happened!");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
