package s305061;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

   // private int currentIteration = 0;
    //private int lastCellCount = 0;

    private int minIteration = 0;
    private int maxIteration = 100;

    private final float ALPHA = 0.5f;
    private final float BETA = 3.0f;
    private final float GAMMA = 0.25f;

    private GameOfLife2D gol;
    private GameOfLife2D clonedGol;
    //private ArrayList<StatDataElement> dataElements = new ArrayList<>(100);

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

        System.out.println("fadfdsfdf");
    }

    public void clearStats(){

        livingSeries.getData().clear();
        growthSeries.getData().clear();
        //currentIteration = 0;//game.getIterationCount();
    }

    public void setGol(GameOfLife gol){
        this.gol = (GameOfLife2D)gol;
    }

    /*public void updateStats(GameOfLife game){

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
    }*/

    private ArrayList<StatDataElement> getStatistics(int iterations){
        return getStatistics(gol, iterations);
    }

    private ArrayList<StatDataElement> getStatistics(GameOfLife gol, int iterations){

        //dataElements.clear();
        ArrayList<StatDataElement> dataElements = new ArrayList<>(100);

        clonedGol = ((GameOfLife2D)gol).clone();
        //clonedGol.aggregateNeighbours();            // calculates the cell count, but is fucked 

        int previousLiving = 0;

        for(int iteration = 0; iteration < iterations; iteration++){

            int currentLiving = clonedGol.getCellCount();
            System.out.println(currentLiving);
            int growth = 0;

            if(iteration > 0)
                growth = currentLiving - previousLiving;

            StatDataElement newDataElement = new StatDataElement(iteration, currentLiving, growth);

            dataElements.add(newDataElement);

            clonedGol.nextGeneration();

            previousLiving = currentLiving;
        }


        return dataElements;
    }

    private void displayStatistics(ArrayList<StatDataElement> dataElements){

        clearStats();

       /* for(StatDataElement dataElement : dataElements){

            livingSeries.getData().add(new XYChart.Data<>((double)dataElement.getIteration(), (double)dataElement.getLiving()));
            growthSeries.getData().add(new XYChart.Data<>((double)dataElement.getIteration(), (double)dataElement.getGrowth()));
        }*/

        for(int i = 0; i < dataElements.size(); i++){

            livingSeries.getData().add(new XYChart.Data<>((double)i, (double)dataElements.get(i).getLiving()));
            growthSeries.getData().add(new XYChart.Data<>((double)i, (double)dataElements.get(i).getGrowth()));
        }
    }

    public void test(ActionEvent actionEvent) {
        displayStatistics(getStatistics(10));
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
