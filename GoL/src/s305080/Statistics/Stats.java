package s305080.Statistics;

import controller.MasterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DynamicGameOfLife;
import model.GameOfLife;
import s305080.Statistics.Controller.StatsController;

import java.io.IOException;

/**
 * Created by Truls on 19/04/16.
 */
public class Stats {

    Parent root;
    private Stage stage;
    private MasterController masterController;

    public void display(GameOfLife gol, MasterController masterController){

        this.masterController = masterController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/View.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        Scene scene = new Scene(root);
        StatsController sController = loader.getController();

        stage.setScene(scene);
        stage.show();

        sController.setGameOfLife(gol);
        sController.setUp();

        stage.setOnCloseRequest(event -> masterController.getMenuController().setStatsShowing(false));
        stage.setX(0);
        stage.setWidth(masterController.getStage().getX());

    }

    public void close() {
        stage.close();
    }
}
