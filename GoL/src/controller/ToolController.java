package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.Cell;
import model.rules.Rule;

import java.util.Objects;

/**
 * The controller for the tool bar on the bottom of the stage
 */
public class ToolController {

    private static final byte SPEED_FACTOR = 15; //The empiric factor
    public Label ruleLabel;

    private MasterController masterController;
    private Image imgPause;
    private Image imgPlay;

    @FXML private ColorPicker liveCellColorPicker;
    @FXML private ColorPicker deadCellColorPicker;
    @FXML private Label cellCountLabel;
    @FXML private Slider speedSlider;
    @FXML private Slider zoomSlider;
    @FXML private Button btnPlay;
    @FXML private ImageView imgViewBtnPlay;

    /**
     * Initializes the tool bar.
     * Sets reference to master controller and loads resources.
     * @param masterController Reference to the master controller
     */
    public void initialize(MasterController masterController) {

        // sets reference to the master controller
        this.masterController = masterController;

        // file paths for finding images for the play/pause button
        String pathImages = "/icons/";
        String pauseImageName = "bars.png";
        String playImageName = "arrows.png";

        // images to be displayed over the play/pause button
        imgPause = new Image(getClass().getResourceAsStream(pathImages + pauseImageName),
                imgViewBtnPlay.getFitWidth(), imgViewBtnPlay.getFitHeight(), true, true);
        imgPlay = new Image(getClass().getResourceAsStream(pathImages + playImageName),
                imgViewBtnPlay.getFitWidth(), imgViewBtnPlay.getFitHeight(), true, true);

        // sets minimum and maximum values for the zoom slider
        zoomSlider.setMin(Math.log(Cell.MIN_SIZE));
        zoomSlider.setMax(Math.log(Cell.MAX_SIZE));

        // sets default liveCellColor to be black, in case configuration is unreadable
        liveCellColorPicker.setValue(Color.BLACK);

        // tries to get colors from the configuration, and sets to the color pickers
        try {
            Color liveCellColor = (Color) Color.class.getField(masterController.configuration.getCellColor()).get(null);
            Color deadCellColor = (Color) Color.class.getField(masterController.configuration.getBackgroundColor()).get(null);

            liveCellColorPicker.setValue(liveCellColor);
            deadCellColorPicker.setValue(deadCellColor);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the color used to render the live cells. Will re-render.
     */
    public void changeCellColor() {

        masterController.getCanvasController().setLiveColor(liveCellColorPicker.getValue());
        masterController.getCanvasController().renderCanvas();
    }

    /**
     * Changes the color used to render the dead cells, in other words, the background. Will re-render (
     */
    public void changeDeadColor() {

        masterController.getCanvasController().setDeadColor(deadCellColorPicker.getValue());
        masterController.getCanvasController().renderCanvas();
    }

    /**
     * Changes the speed of the simulation when the speedSlider is dragged
     */
    public void speedSliderDragged(){

        int speed = (int)Math.exp(-speedSlider.getValue() / SPEED_FACTOR);

        masterController.getCanvasController().setFrameDelay(speed);
    }

    /**
     * Changes the cellSize in the simulation when the zoomSlider is dragged
     */
    public void zoomSliderDragged(){

        masterController.getCanvasController().setCellSize(Math.exp(zoomSlider.getValue()));
    }

    /**
     * Updates the cellCount label in the toolView
     * @param cellCount the numbers of live cells in the grid
     */
    public void giveCellCount(int cellCount) {

        cellCountLabel.setText("Cellcount: "+cellCount);
    }

    /**
     * Toggles between pause and play game states.
     */
    public void togglePause() {

        if (Objects.equals(btnPlay.getText(), "Play")) {

            changeButtonIconToPause();
            masterController.getCanvasController().startAnimation();
        }
        else {

            changeButtonIconToPlay();
            masterController.getCanvasController().stopAnimation();
        }
    }

    /**
     * Changes the btnPlay image to a play button image.
     */
    public void changeButtonIconToPlay() {

        btnPlay.setText("Play");
        btnPlay.setGraphic(new ImageView(imgPlay));
    }

    /**
     * Changes the btnPlay image to a pause button image.
     */
    public void changeButtonIconToPause() {

        btnPlay.setText("Pause");
        btnPlay.setGraphic(new ImageView(imgPause));
    }

    /**
     * Sets the value of the zoomSlider to match the zoom value
     * @param zoom Zoom value to match
     */
    public void setZoom(double zoom) {

        zoomSlider.setValue(Math.log(zoom));
    }

    /**
     * Sets the value of speedSlider to match the speed value
     * @param speed Game speed value to match
     */
    public void setSpeed(double speed) {

        // prevents the speedSlider showing a speed below 0
        if(speed < 0) speed = 0;

        speedSlider.setValue(-Math.log(speed) * SPEED_FACTOR);
    }

    /**
     * Adds an amount to the speedSlider. Is called when scrolling.
     * @param deltaX Amount to add to speedSlider
     */
    public void addSpeedValue(double deltaX) {

        speedSlider.setValue(speedSlider.getValue()+deltaX);
        speedSliderDragged();
    }

    public void setRuleLabel(Rule rule){
        ruleLabel.setText(rule.toString());
    }

}
