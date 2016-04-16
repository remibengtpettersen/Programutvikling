package s305061;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import model.GameOfLife;
import model.GameOfLife2D;

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
    LineChart.Series<Double, Double> similaritySeries;

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

    public void setGol(GameOfLife gol){
        this.gol = (GameOfLife2D)gol;
    }

    @FXML
    protected void initialize() {

        lineChartData = FXCollections.observableArrayList();

        livingSeries = new LineChart.Series<>();
        livingSeries.setName("Live cells");
        lineChartData.add(livingSeries);

        growthSeries = new LineChart.Series<>();
        growthSeries.setName("Cell growth");
        lineChartData.add(growthSeries);

        similaritySeries = new LineChart.Series<>();
        similaritySeries.setName("Similarity measure");
        lineChartData.add(similaritySeries);

        graph.setCreateSymbols(false);
        graph.setAnimated(false);

        graph.setData(lineChartData);
    }

    public void clearStats(){

        livingSeries.getData().clear();
        growthSeries.getData().clear();
        similaritySeries.getData().clear();
        //currentIteration = 0;//game.getIterationCount();
    }

    public int[][] getStatistics(GameOfLife game, int iterations){

        int[][] stats = new int[3][iterations];
        float[] representations = new float[iterations];

        clonedGol = ((GameOfLife2D)gol).clone();

        int previousLiving = 0;

        for(int iteration = 0; iteration < iterations; iteration++){

            int currentLiving = clonedGol.getCellCount();
            int growth = 0;

            if(iteration > 0)
                growth = currentLiving - previousLiving;

            stats[0][iteration] = currentLiving;
            stats[1][iteration] = growth;
            //stats[2][iteration] = (int)(Math.random()*100);

            representations[iteration] = ALPHA*currentLiving + BETA*growth + GAMMA*getGeometricFactor(game);

            clonedGol.nextGeneration();
            previousLiving = currentLiving;
        }

        stats[2] = new int[iterations];

        return stats;
    }

    private float getGeometricFactor(GameOfLife game) {

        //boolean[][] grid = game.getGrid;

        return 0;
    }

    private void displayStatistics(int[][] stats){

        for(int iteration = 0; iteration < stats[0].length; iteration++){

            livingSeries.getData().add(new XYChart.Data<>(
                    (double)iteration, (double)stats[0][iteration]));

            growthSeries.getData().add(new XYChart.Data<>(
                    (double)iteration, (double)stats[1][iteration]));

            similaritySeries.getData().add(new XYChart.Data<>(
                    (double)iteration, (double)stats[2][iteration]));
        }

        //clearStats();

       /* for(StatDataElement dataElement : dataElements){

            livingSeries.getData().add(new XYChart.Data<>((double)dataElement.getIteration(), (double)dataElement.getLiving()));
            growthSeries.getData().add(new XYChart.Data<>((double)dataElement.getIteration(), (double)dataElement.getGrowth()));
        }*/

        /*for(int i = 0; i < dataElements.size(); i++){

            livingSeries.getData().add(new XYChart.Data<>((double)i, (double)dataElements.get(i).getLiving()));
            growthSeries.getData().add(new XYChart.Data<>((double)i, (double)dataElements.get(i).getGrowth()));
        }*/
    }



    public void test(ActionEvent actionEvent) {

        clearStats();

        displayStatistics(getStatistics(gol, 100));
    }

    //region old stuff
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

    //private ArrayList<StatDataElement> getStatistics(int iterations){
        //return getStatistics(gol, iterations);
    //}

    /*private ArrayList<StatDataElement> getStatistics(GameOfLife gol, int iterations){

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
    }*/

        /*public void test(ActionEvent actionEvent) {
        if(!showLive.isSelected()) {
            livingSeries.getData().clear();
        }

        if(!showGrowth.isSelected()) {
            growthSeries.getData().clear();
        }
    }*/
    //endregion




}
