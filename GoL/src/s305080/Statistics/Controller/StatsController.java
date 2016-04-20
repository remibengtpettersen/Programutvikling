package s305080.Statistics.Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import model.GameOfLife;
import tools.MessageBox;
import tools.Utilities;


/**
 * Created by Truls on 19/04/16.
 */
public class StatsController {
    @FXML
    TextField input;
    @FXML
    LineChart<Number,Number> lineChart;
    @FXML
    NumberAxis xAxis, yAxis;
    XYChart.Series cellCount;
    XYChart.Series difference;
    XYChart.Series something;
    private GameOfLife statsGol;
    private GameOfLife gol;
    private int lastCellCount;
    private int iterations;


    public StatsController(){

    }

    private void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().toString().equals("ENTER"))
            try {
                iterations = Integer.parseInt(input.getText());
                if(iterations > 1000)
                    MessageBox.alert("To many iterations");
                else
                    updateStats();
            }
            catch (Exception e){
                input.setText(input.getText().replaceAll("[\\D]*", ""));
            }

    }

    private void updateStats() {

        lineChart.setAnimated(false);
        cellCount.getData().clear();
        difference.getData().clear();
        lineChart.setAnimated(true);

        System.out.println(statsGol.getRule().toString().equals(gol.getRule().toString()));

        statsGol.deepCopyOnSet(gol.getGrid());

        if(!statsGol.getRule().toString().equals(gol.getRule().toString())) {
            statsGol.setRule(gol.getRule().toString());
        }else {
            statsGol.updateRuleGrid();
        }

        for (int i = 0; i < iterations; i++) {
            statsGol.nextGeneration();
            cellCount.getData().add(new XYChart.Data(i, statsGol.getCellCount()));
            if(i > 0)
                difference.getData().add(new XYChart.Data(i, statsGol.getCellCount() - lastCellCount));
            lastCellCount = statsGol.getCellCount();
        }


        System.out.println(2);
    }

    public void initializing() {
        input.setOnKeyPressed(this::keyPressed);
        cellCount = new XYChart.Series();
        cellCount.setName("Cellcount");
        difference = new XYChart.Series();
        difference.setName("Difference");
        lineChart.getData().addAll(cellCount, difference);
        System.out.println("Initialize");
        statsGol = gol.clone();

    }

    private void textChanged(InputMethodEvent inputMethodEvent) {
        System.out.println(2);
    }

    public void setGameOfLife(GameOfLife gameOfLife) {
        gol = gameOfLife;

    }
}
