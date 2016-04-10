package s305061;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import model.GameOfLife;
import model.GameOfLife2D;

import java.util.ArrayList;

/**
 * Created by And on 06.04.2016.
 */
public class StatController {
    @FXML
    private LineChart<Double, Double> graph;

    //@FXML private CheckMenuItem showLive;
    //@FXML private CheckMenuItem showGrowth;

    ObservableList<XYChart.Series<Double, Double>> lineChartData;
    LineChart.Series<Double, Double> livingSeries;
    LineChart.Series<Double, Double> growthSeries;

    private int currentIteration = 0;
    private int lastCellCount = 0;

    private int minIteration = 0;
    private int maxIteration = 100;

    private final float ALPHA = 0.5f;
    private final float BETA = 3.0f;
    private final float GAMMA = 0.25f;

    private GameOfLife2D clonedGol;

    @FXML
    protected void initialize() {

        lineChartData = FXCollections.observableArrayList();

        livingSeries = new LineChart.Series<>();
        livingSeries.setName("Live cells");
        lineChartData.add(livingSeries);

        growthSeries = new LineChart.Series<>();
        growthSeries.setName("Cell growth");
        lineChartData.add(growthSeries);


        graph.setCreateSymbols(false);
        graph.setAnimated(false);

        graph.setData(lineChartData);
    }

    public void clearStats(){

        livingSeries.getData().clear();
        growthSeries.getData().clear();
        currentIteration = 0;//game.getIterationCount();
    }

    public void updateStats(GameOfLife game){

        int cellCount = game.getCellCount();
        int growth = 0;
        if(currentIteration > 0)
            growth = cellCount - lastCellCount;

        //if(showLive.isSelected())
        livingSeries.getData().add(new XYChart.Data<>((double) currentIteration, (double)cellCount));

        //if(showGrowth.isSelected())
        growthSeries.getData().add(new XYChart.Data<>((double) currentIteration, (double)growth));

        //float geometricPosition = 0;

        lastCellCount = cellCount;
        currentIteration++;
    }

    private ArrayList<StatDataElement> getStatistics(GameOfLife gol, int iterations){


        return null;
    }


    /*public void test(ActionEvent actionEvent) {
        if(!showLive.isSelected()) {
            livingSeries.getData().clear();
        }

        if(!showGrowth.isSelected()) {
            growthSeries.getData().clear();
        }
    }*/
}
