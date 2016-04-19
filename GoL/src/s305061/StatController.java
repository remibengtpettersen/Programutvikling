package s305061;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import model.GameOfLife;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by And on 06.04.2016.
 */
public class StatController {

    @FXML private LineChart<Double, Double> graph;
    @FXML private TextField textField;

    ObservableList<XYChart.Series<Double, Double>> lineChartData;
    LineChart.Series<Double, Double> livingSeries;
    LineChart.Series<Double, Double> growthSeries;
    LineChart.Series<Double, Double> similaritySeries;

    private final float ALPHA = 0.5f;
    private final float BETA = 3.0f;
    private final float GAMMA = 0.25f;

    private GameOfLife gol;

    public void setGol(GameOfLife gol){
        this.gol = gol;
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
    }

    public int[][] getStatistics(int iterations){

        int[][] stats = new int[3][iterations];
        double[] representations = new double[iterations];

        GameOfLife clonedGol = gol.clone();

        int previousLiving = 0;

        for(int iteration = 0; iteration < iterations; iteration++){

            int currentLiving = clonedGol.getCellCount();
            int growth = 0;

            if(iteration > 0)
                growth = currentLiving - previousLiving;

            stats[0][iteration] = currentLiving;
            stats[1][iteration] = growth;

            representations[iteration] = ALPHA*currentLiving + BETA*growth + GAMMA*getGeometricFactor(clonedGol);

            clonedGol.nextGeneration();
            previousLiving = currentLiving;
        }

        for(int repA = 0; repA < iterations; repA++) {

            int maxP = 0;

            for(int repB = 0; repB < iterations; repB++) {

                if(repA == repB)
                    continue;

                int p = compareRepresentations(representations[repA], representations[repB]);

                if (p > maxP){
                    maxP = p;
                }
            }

            stats[2][repA] = maxP;
        }

        return stats;
    }

    private int compareRepresentations(double repA, double repB) {

        double min = Math.min(repA, repB);
        double max = Math.max(repA, repB);

        int percent =(int)(100*min/max);

        return percent;
    }

    private float getGeometricFactor(GameOfLife game) {

        boolean[][] grid = game.getGrid();
        float geoFactor = 0;

        for(int x = 0; x < grid[0].length; x++)
            for (int y = 0; y < grid.length; y++)
                if(grid[x][y])
                    geoFactor += x + y;

        return geoFactor;
    }

    private void displayStatistics(int iterations){

        clearStats();

        int[][] stats = getStatistics(iterations);

        for(int iteration = 0; iteration < stats[0].length; iteration++){

            livingSeries.getData().add(new XYChart.Data<>(
                    (double)iteration, (double)stats[0][iteration]));

            growthSeries.getData().add(new XYChart.Data<>(
                    (double)iteration, (double)stats[1][iteration]));

            similaritySeries.getData().add(new XYChart.Data<>(
                    (double)iteration, (double)stats[2][iteration]));
        }
    }

    @FXML
    public void onButtonClicked(ActionEvent actionEvent) {

        String string = textField.getText();

        Pattern p = Pattern.compile("\\d+");//"\\d+");
        Matcher m = p.matcher(string);

        if(m.matches()) {
            int iterations = Integer.parseInt(m.group());
            displayStatistics(iterations);
        }
        else {
            textField.setText("Could not parse number");
        }
    }
}
