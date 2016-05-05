package s305061.statistics;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import model.GameOfLife;

/**
 * @author Andreas s305061
 *
 * Controller for statistics window.
 * Handles statistics gathering in addition to GUI control
 */
public class StatController {

    // constants for calculation of similarity measure
    private static final double ALPHA = 0.5f;
    private static final double BETA = 3.0f;
    private static final double GAMMA = 0.25f;

    // gui components
    @FXML private LineChart<Double, Double> graph;
    @FXML private TextField textField;
    @FXML private ProgressBar progressBar;

    // statistics data
    private LineChart.Series<Double, Double> livingSeries;
    private LineChart.Series<Double, Double> growthSeries;
    private LineChart.Series<Double, Double> similaritySeries;

    // checks if thread is working on getting statistics
    private boolean busy = false;

    // reference to game object to evolve and gather statistics from
    private GameOfLife gol;

    /**
     * Initialization method. Called when statistics window opens
     */
    @FXML
    private void initialize() {

        ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();

        livingSeries = new LineChart.Series<>();
        livingSeries.setName("Live cells");
        lineChartData.add(livingSeries);

        growthSeries = new LineChart.Series<>();
        growthSeries.setName("Cell growth");
        lineChartData.add(growthSeries);

        similaritySeries = new LineChart.Series<>();
        similaritySeries.setName("Similarity measure");
        lineChartData.add(similaritySeries);

        graph.setData(lineChartData);
    }

    /**
     * Clears the statistics data elements from the line chart.
     */
    private void clearStats(){

        livingSeries.getData().clear();
        growthSeries.getData().clear();
        similaritySeries.getData().clear();
    }

    /**
     * Collects statistics for a specified number of iterations. Number of live cells, cell growth and similarity measure will be collected
     * @param totalIterations Number of iterations to evolve and collect statistics from
     * @return  An array of integer values representing live cells, cell growth and similarity measure for each iteration
     */
    private int[][] getStatistics(int totalIterations){

        int[][] stats = new int[3][totalIterations];
        double[] representations = new double[totalIterations];

        GameOfLife clonedGol = gol.clone();

        int previousLiving = 0;
        double previousGeometricFactor = 0;

        setProgress(0);

        // for each iteration, add live cell count and cell growth to the array stats,
        // then add the iteration's reduced representation to the array representations
        for(int currentIteration = 0; currentIteration < totalIterations; currentIteration++){

            int previousIteration = currentIteration - 1;
            int lastIteration = totalIterations - 1;

            int currentLiving = clonedGol.getCellCount();
            int previousGrowth = currentLiving - previousLiving;
            double currentGeometricFactor = getGeometricFactor(clonedGol);

            stats[0][currentIteration] = currentLiving;

            // if current is not first iteration, set previous iteration data
            if(currentIteration > 0) {

                // set growth data for previous iteration
                stats[1][previousIteration] = previousGrowth;

                // set a reduced representation for previous iteration
                representations[previousIteration] = getReducedRepresentation(
                        previousLiving, previousGrowth, previousGeometricFactor);
            }

            // next generation
            clonedGol.nextGeneration();

            // if current is the last iteration, set current iteration data.
            // Needs to be after a nextGeneration() to get the current (not for the game, but for the loop) growth
            if(currentIteration == lastIteration) {

                int currentGrowth = clonedGol.getCellCount() - currentLiving;

                // set growth data for this last iteration
                stats[1][lastIteration] = currentGrowth;

                // set a reduced representation for this last iteration
                representations[lastIteration] = getReducedRepresentation(
                        currentLiving, currentGrowth, currentGeometricFactor);
            }

            previousLiving = currentLiving;
            previousGeometricFactor = currentGeometricFactor;

            // update progress bar
            setProgress(0.9*((double)currentIteration/(double)totalIterations));
        }

        // compare all the reduced representations with each other, return the best match for each iteration
        for(int repA = 0; repA < totalIterations; repA++) {

            int maxSimilarity = 0;

            for(int repB = 0; repB < totalIterations; repB++) {

                // don't compare a representation with itself
                if(repA == repB)
                    continue;

                // get similarity between two representations in percent
                int similarity = compareRepresentations(representations[repA], representations[repB]);

                // this will get the true maximum similarity after sufficient iterations
                if (similarity > maxSimilarity){
                    maxSimilarity = similarity;
                }
            }

            // set the best match as the similarity measure for this iteration
            stats[2][repA] = maxSimilarity;

            // update progress bar
            setProgress(0.9 + 0.1*((double)repA/(double)(totalIterations)));
        }

        // update progress bar to 100%
        setProgress(1);

        // return
        return stats;
    }

    /**
     * Compares two reduced representations and calculate the similarity in percent
     * @param repA A reduced representation of an iteration
     * @param repB A reduced representation of another iteration
     * @return Similarity of the two representations in percent
     */
    private int compareRepresentations(double repA, double repB) {

        // the smallest of the representations
        double min = Math.min(repA, repB);

        // the biggest of the representations
        double max = Math.max(repA, repB);

        // similarity in percent
        return (int)(100*min/max);
    }

    /**
     * Sums the x and y coordinates of all the live cells on the grid.
     * Used in calculation of the similarity measure, enables the position of patterns to be a factor
     * @param gol Reference to the DynamicGameOfLife object to gather information from
     * @return The geometric factor, the sum of x and y coordinates of live cells
     */
    private double getGeometricFactor(GameOfLife gol) {

        double geoFactor = 0;

        for(int x = 0; x < gol.getGridWidth(); x++)
            for (int y = 0; y < gol.getGridHeight(); y++)
                if(gol.isCellAlive(x,y))
                    geoFactor += x - gol.getOffsetX() + y - gol.getOffsetY();

        return geoFactor;
    }

    /**
     * Calculates the reduced mathematical representation of a game board one iteration
     * @param living Number of live cells this iteration
     * @param growth Live cell growth this iteration
     * @param geoFactor Geometric factor for this iteration
     * @return Reduced representation
     */
    private double getReducedRepresentation(int living, int growth, double geoFactor){

        return ALPHA * living + BETA * growth + GAMMA * geoFactor;
    }

    /**
     * Displays statistics at the line chart
     * Calls clearStats() to empty the line chart, then loops through getStatistics(iterations) and adds the statistics to line chart
     * @param stats An array of statistic data elements
     */
    private void displayStatistics(int[][] stats){

        // clear all statistics data from line chart
        clearStats();

        int iterations = stats[0].length;

        // add new statistics data to line chart
        for(int iteration = 0; iteration < iterations; iteration++){

            livingSeries.getData().add(new XYChart.Data<>(
                    (double)iteration, (double)stats[0][iteration]));

            growthSeries.getData().add(new XYChart.Data<>(
                    (double)iteration, (double)stats[1][iteration]));

            similaritySeries.getData().add(new XYChart.Data<>(
                    (double)iteration, (double)stats[2][iteration]));
        }
    }

    /**
     *  Called when GUI button "Show statistics" is clicked,
     *  or if the enter key is pressed while in the iteration text field.
     *  If the text in the text field is a number,
     *  displayStatistics(iterations) will show statistics for that number of iterations
     */
    @FXML
    public void onInputEntered() {

        String string = textField.getText();
        int iterations;

        try{
            iterations = Integer.parseInt(string);
        }
        catch (NumberFormatException e){
            textField.setText("");
            textField.setPromptText("Could not parse number");
            return;
        }

        getAndDisplayConcurrently(iterations);
    }

    /**
     * Will get and display statistics concurrently, without freezing the game or GUI.
     * @param iterations Number of iterations to evolve and collect statistics from
     */
    private void getAndDisplayConcurrently(int iterations){

        // checks if already working, return if it is
        if(busy)
            return;

        Task task = new Task() {
            @Override
            protected Void call() throws Exception  {

                busy = true;
                int[][] stats = getStatistics(iterations);

                Platform.runLater(() -> {

                    displayStatistics(stats);
                    busy = false;
                });

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * Will get and display statistics without the use of concurrency.
     * @param iterations Number of iterations to evolve and collect statistics from
     */
    @Deprecated
    private void getAndDisplaySequentially(int iterations){

        int[][] stats = getStatistics(iterations);
        displayStatistics(stats);
    }

    /**
     * Sets a reference to the DynamicGameOfLife object to be cloned for statistics gathering
     * @param gol Original DynamicGameOfLife object
     */
    public void setGol(GameOfLife gol){
        this.gol = gol;
    }

    /**
     * Sets the progress of the progress bar
     * @param progress Progress from 0.0 to 1.0
     */
    private void setProgress(double progress){ progressBar.setProgress(progress); }
}