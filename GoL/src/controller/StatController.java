package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import model.GameOfLife;

/**
 * Created by And on 06.04.2016.
 */
public class StatController {
    @FXML
    private LineChart<Double, Double> graph;

    ObservableList<XYChart.Series<Double, Double>> lineChartData;
    LineChart.Series<Double, Double> livingSeries;
    LineChart.Series<Double, Double> growthSeries;

    //private ArrayList<Integer> livingStats = new ArrayList<>(100);
    //private ArrayList<Integer> growthStats = new ArrayList<>(100);

    private int iteration = 0;
    private int lastCellCount = 0;

    @FXML
    protected void initialize() {

        lineChartData = FXCollections.observableArrayList();

        livingSeries = new LineChart.Series<Double, Double>();
        livingSeries.setName("Antall levende celler");
        lineChartData.add(livingSeries);

        growthSeries = new LineChart.Series<Double, Double>();
        growthSeries.setName("Endring i levende celler");
        lineChartData.add(growthSeries);


        graph.setCreateSymbols(false);
        graph.setAnimated(false);

        graph.setData(lineChartData);
    }

    public void updateStatistics(GameOfLife game){

        int cellCount = game.getCellCount();

        int growth = 0;

        if(iteration > 0)
            growth = cellCount - lastCellCount;

        //livingStats.add(cellCount);
        //growthStats.add(growth);

        livingSeries.getData().add(new XYChart.Data<Double, Double>((double)iteration, (double)cellCount));
        growthSeries.getData().add(new XYChart.Data<Double, Double>((double)iteration, (double)growth));

        lastCellCount = cellCount;
        iteration++;

        //updateChart();
    }

    /*private void updateChart() {

        int last = livingStats.size() - 1;

        livingSeries.getData().add(new XYChart.Data<Double, Double>((double)last, (double) livingStats.get(last)));
        growthSeries.getData().add(new XYChart.Data<Double, Double>((double)last, (double) growthStats.get(last)));
    }*/
}
