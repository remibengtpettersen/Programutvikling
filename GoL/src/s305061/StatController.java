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

    ObservableList<XYChart.Series<Double, Double>> lineChartData;
    LineChart.Series<Double, Double> livingSeries;
    LineChart.Series<Double, Double> growthSeries;

    //private int iteration = 0;
    private int lastCellCount = 0;
    private ArrayList<Double> representations = new ArrayList<>(100);

    private final double ALPHA = 0.5;
    private final double BETA = 3.0;
    private final double GAMMA = 0.25;

    @FXML
    protected void initialize() {

        lineChartData = FXCollections.observableArrayList();

        livingSeries = new LineChart.Series<>();
        livingSeries.setName("Antall levende celler");
        lineChartData.add(livingSeries);

        growthSeries = new LineChart.Series<>();
        growthSeries.setName("Endring i levende celler");
        lineChartData.add(growthSeries);

        graph.setCreateSymbols(false);
        graph.setAnimated(false);

        graph.setData(lineChartData);
    }

    public void updateStats(GameOfLife gol){

        int cellCount = gol.getCellCount();
        int iteration = gol.getIterationCount();

        int growth = 0;

        if(iteration > 0)
            growth = cellCount - lastCellCount;

        int geometricFactor = calculateGeometricFactor(gol);
        double currentRepresentation = calculateRepresentation(cellCount, growth, geometricFactor);

        representations.add(currentRepresentation);

        System.out.println(currentRepresentation);

        livingSeries.getData().add(new XYChart.Data<>((double)iteration, (double)cellCount));
        growthSeries.getData().add(new XYChart.Data<>((double)iteration, (double)growth));

        lastCellCount = cellCount;
        //iteration++;
    }

    public void clearStats(){

        livingSeries.getData().clear();
        growthSeries.getData().clear();
        lastCellCount = 0;
        representations.clear();
    }

    private double calulateSimilarityMeasure(int currentRepresentation){

        for (int i = 0; i < representations.size()-1; i++) {    //size() - 1 because the last element is the currentRepresentation
            //if()
        }

        return 0;
    }

    private double calculateRepresentation(int cellCount, int growth, int geometricFactor){

        return ALPHA*cellCount + BETA*growth + GAMMA*geometricFactor;
    }

    private int calculateGeometricFactor(GameOfLife gol){

        boolean[][] grid = ((GameOfLife2D)gol).getGrid();

        int coordinateSum = 0;

        for(int x = 0; x < grid.length; x++)
            for(int y = 0; y < grid[0].length; y++){

                if(grid[x][y])
                    coordinateSum += x+y;
            }

        return coordinateSum;
    }

}
