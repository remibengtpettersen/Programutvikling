package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.GameOfLife;
import s305061.gif.ToGif;

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
        //
        GameOfLife gol = new GameOfLife(20, 20);

        //gol.getGrid()[4][3] = true;
        //gol.getGrid()[4][4] = true;
        gol.getGrid()[4][3] = true;
        gol.getGrid()[5][3] = true;
        gol.getGrid()[6][3] = true;
        gol.getGrid()[5][1] = true;
        gol.getGrid()[6][2] = true;
        //gol.getGrid()[99][99] = true;

        /*for (int x = 0; x < 20; x++)
            for (int y = 0; y < 20; y++) {
                //if(x%2==0 && y%2==0 || y < 10)

                if(Math.random()>0.5)
                    gol.getGrid()[x][y] = true;

            }*/


        try {
            ToGif.startWriteGolSequenceToGIF(gol, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
