package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import model.Cell;

import java.util.Objects;

/**
 * The controller for the tools on the bottom of the stage
 */
public class ToolController {

    private MasterController masterController;
    private Image imgPause;
    private Image imgPlay;

    private byte zoomFactor;
    private byte speedFactor = 15; //The empiric factor

    @FXML
    private ColorPicker cellColorPicker;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private Label cellCountLabel;
    @FXML
    private Slider speedSlider;
    @FXML
    private Slider zoomSlider;
    @FXML
    private Button btnPlay;
    @FXML
    private ImageView imgViewBtnPlay;

    /**
     * Does the necessary preparations for this controller to be used.
     * @param masterController the reference to the masterController
     */
    public void initialize(MasterController masterController) {

        this.masterController = masterController;
        cellColorPicker.setValue(Color.BLACK);

        String pathImages = "/icons/";
        String pauseImageName = "bars.png";
        String playImageName = "arrows.png";
        imgPause = new Image(getClass().getResourceAsStream(pathImages + pauseImageName), imgViewBtnPlay.getFitWidth(), imgViewBtnPlay.getFitHeight(), true, true);
        imgPlay = new Image(getClass().getResourceAsStream(pathImages + playImageName), imgViewBtnPlay.getFitWidth(), imgViewBtnPlay.getFitHeight(), true, true);
        zoomFactor = (byte)(zoomSlider.getMax() / Math.log(Cell.MAX_SIZE));
        try {
            cellColorPicker.setValue((Color) Color.class.getField(masterController.configuration.getCellColor()).get(null));
            backgroundColorPicker.setValue((Color) Color.class.getField(masterController.configuration.getBackgroundColor()).get(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the color used to render the cells
     * @param actionEvent
     */
    public void changeCellColor(ActionEvent actionEvent) {

        masterController.getCanvasController().getCell().setColor(cellColorPicker.getValue());    //ugly, but temporary
        masterController.getCanvasController().renderCanvas();
    }

    /**
     * Changes the color used to render the background
     * @param actionEvent
     */
    public void changeBackgroundColor(ActionEvent actionEvent) {

        masterController.getCanvasController().setBackgroundColor(backgroundColorPicker.getValue());    //ugly, but temporary
        masterController.getCanvasController().renderCanvas();
    }

    /**
     * Changes the speed of the simulation when the speedSlider is dragged
     */
    public void speedSliderDragged(){

        masterController.getCanvasController().setFrameDelay((int)Math.exp(-speedSlider.getValue()/speedFactor));
    }

    /**
     * Changes the cellSize in the simulation when the zoomSlider is dragged
     */
    public void zoomSliderDragged(){

        masterController.getCanvasController().setCellSize(Math.exp(zoomSlider.getValue()/zoomFactor));
    }

    /**
     * Updates the cellCount label in the toolView
     * @param cellCount the numbers of live cells in the grid
     */
    public void giveCellCount(int cellCount) {

        cellCountLabel.setText("Cellcount: "+cellCount);
    }

    /**
     * Toggles between pause and play game
     * @param actionEvent
     */
    public void togglePause(ActionEvent actionEvent) {

        if (Objects.equals(btnPlay.getText(), "Play")) {
            changeIconToPause();
            masterController.getCanvasController().startAnimation();
        }
        else {
            changeIconToPlay();
            masterController.getCanvasController().stopAnimation();
        }
    }

    /**
     * changes the btnPlay image to a play button image
     */
    public void changeIconToPlay() {

        btnPlay.setText("Play");
        btnPlay.setGraphic(new ImageView(imgPlay));
    }

    /**
     * changes the btnPlay image to a pause button image
     */
    public void changeIconToPause() {

        btnPlay.setText("Pause");
        btnPlay.setGraphic(new ImageView(imgPause));
    }

    /**
     * sets the value of the zoomSLider to match the zoom value
     * @param zoom the zoom value to match
     */
    public void setZoom(double zoom) {

        zoomSlider.setValue(Math.log(zoom) * zoomFactor);
    }


    public void setMinZoom(double minZoom) {
        zoomSlider.setMin(Math.log(minZoom) * zoomFactor);
    }

    public void setSpeed(double speed) {

        if(speed < 0) speed = 0;
        speedSlider.setValue(-Math.log(speed) * speedFactor);
    }


    public void addSpeedValue(double deltaX) {

        speedSlider.setValue(speedSlider.getValue()+deltaX);
        speedSliderDragged();
    }
}
