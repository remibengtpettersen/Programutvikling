package s305080.Statistics.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.DynamicGameOfLife;
import model.GameOfLife;
import tools.MessageBox;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Truls on 19/04/16.
 */
public class StatsController {
    @FXML
    TextField input;
    @FXML
    LineChart<Integer,Integer> lineChart;
    @FXML
    NumberAxis xAxis, yAxis;
    private XYChart.Series<Integer, Integer> cellCount;
    private XYChart.Series<Integer, Integer> difference;
    private XYChart.Series<Integer, Integer> similarity;
    private GameOfLife statsGol;
    private GameOfLife gol;
    private int lastCellCount;
    private int iterations;
    private double alfa = 0.5;
    private double beta = 3.0;
    private double gamma = 0.25;
    private final double maxIntervalLength = 15;

    public void setGameOfLife(GameOfLife gameOfLife) {
        gol = gameOfLife;

    }
    public void setUp() {
        input.setOnKeyPressed(this::keyPressedInTextBox);
        cellCount = new XYChart.Series();
        cellCount.setName("Cellcount");
        difference = new XYChart.Series();
        difference.setName("Difference");
        similarity = new XYChart.Series();
        similarity.setName("Similarity");
        lineChart.getData().addAll(cellCount, difference, similarity);
        addLineChartWidthListener();
        System.out.println("Initialize");
        statsGol = gol.clone();
    }

    private void addLineChartWidthListener() {
        lineChart.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(lineChart.getWidth() / iterations < maxIntervalLength)
                    lineChart.setCreateSymbols(false);
                else
                    lineChart.setCreateSymbols(true);
            }
        });
    }

    private void keyPressedInTextBox(KeyEvent keyEvent) {
        if(keyEvent.getCode().toString().equals("ENTER"))
            try {
                iterations = Integer.parseInt(input.getText());
                if(iterations > 1000)
                    MessageBox.alert("To many iterations");
                else
                    updateStats();
            }
            catch (NumberFormatException e){
                input.setText(input.getText().replaceAll("[\\D]*", ""));
            }

    }

    private void updateStats() {

        statsGol = gol.clone();
        setCorrectRule();

        int [][] stats = getStats(statsGol, iterations);
        clearGraph();
        if(lineChart.getWidth() / iterations < maxIntervalLength)
            lineChart.setCreateSymbols(false);
        else
            lineChart.setCreateSymbols(true);
        displayStats(stats);
    }

    private int[][] getStats(GameOfLife gol, int iterations) {
        int [][] data = new int[3][iterations];
        double[] phies = new double[iterations];
        for (int i = 0; i < iterations; i++) {


            if(i > 0){
                data[1][i] = gol.getCellCount() - data[0][i - 1];
                phies[i]  = phi(gol, data[1][i]);
            }
            else{
                phies[i] = phi(gol, 0);
            }
            data[0][i] = gol.getCellCount();
            lastCellCount = data[0][i];
            gol.nextGeneration();
        }
        for (int i = 0; i < iterations; i++) {

            for (int j = 0; j < iterations; j++) {

                if(i != j) {
                    int currentComparing = (int) (100 * Math.min(phies[i], phies[j]) / Math.max(phies[i], phies[j]));

                    if (data[2][i] < currentComparing) {
                        data[2][i] = currentComparing;
                    }
                }
            }
        }

        return data;
    }

    private void clearGraph() {
        lineChart.setAnimated(false);
        cellCount.getData().clear();
        difference.getData().clear();
        similarity.getData().clear();
        lineChart.setAnimated(true);
    }

    private void displayStats(int[][] data) {
        for (int i = 0; i < iterations; i++) {
            cellCount.getData().add(new XYChart.Data<>(i, data[0][i]));
            difference.getData().add(new XYChart.Data<>(i, data[1][i]));
            similarity.getData().add(new XYChart.Data<>(i, data[2][i]));
        }
    }

    private void setCorrectRule() {
        if(!statsGol.getRule().toString().equals(gol.getRule().toString())) {
            statsGol.setRule(gol.getRule().toString());
        }
        System.out.println("\n");
    }

    private double phi(GameOfLife gol, int difference) {
        return alfa * gol.getCellCount()
                + beta * difference
                + gamma * g(gol);
    }

    public int g(GameOfLife gol){
        int count = 0;
        int[] boundingBox = gol.getBoundingBox();
        for (int x = boundingBox[0]; x <= boundingBox[1]; x++) {
            for (int y = boundingBox[2]; y <= boundingBox[3]; y++) {
                if(gol.isCellAlive(x, y))
                    count += (x + y);
            }
        }
        return count;
    }


}
