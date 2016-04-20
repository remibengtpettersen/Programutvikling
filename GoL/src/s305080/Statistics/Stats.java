package s305080.Statistics;

import controller.MasterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        sController.initializing();

        stage.setOnCloseRequest(event -> masterController.getMenuController().setStatsShowing(false));
        stage.setX(0);
        stage.setWidth(masterController.stage.getX());
        /*
        *

        Stage stage = new Stage();

        gol = gol.clone();

        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Iterations");
        yAxis.setLabel("Number of cells");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Statistics");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Number of cells");
        //populating the series with data
        for (int i = 0; i < 100; i++) {
            series.getData().add(new XYChart.Data(i,gol.getCellCount()));
            gol.nextGeneration();
        }
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();

        *
        * */

    }

    public void close() {
        stage.close();
    }
}
