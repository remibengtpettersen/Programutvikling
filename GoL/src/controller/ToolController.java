package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

/**
 * Created by Andreas on 09.03.2016.
 */
public class ToolController {

    private MasterController masterController;

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

    public void initialize(MasterController masterController) {
        this.masterController = masterController;
        cellColorPicker.setValue(Color.BLACK);
    }

    /**
     * Changes the color used to render the cells
     * @param actionEvent
     */
    public void changeCellColor(ActionEvent actionEvent) {
        masterController.canvasController.setCellColor(cellColorPicker.getValue());    //ugly, but temporary
        masterController.canvasController.renderLife();
    }

    /**
     * Changes the color used to render the background
     * @param actionEvent
     */
    public void changeBackgroundColor(ActionEvent actionEvent) {
        masterController.canvasController.setBackgroundColor(backgroundColorPicker.getValue());    //ugly, but temporary
        masterController.canvasController.renderLife();
    }

    public void zoomSliderDragged(){

    }

    public void giveCellCount(int cellCount) {
        cellCountLabel.setText("Cellcount: "+cellCount);
    }

}
