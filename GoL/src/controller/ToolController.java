package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;

/**
 * Created by Andreas on 09.03.2016.
 */
public class ToolController {

    private MasterController masterController;

    @FXML
    private ColorPicker frontColorPicker;

    public void initialize(MasterController masterController) {
        this.masterController = masterController;
    }

    public void changeFrontColor(ActionEvent actionEvent) {
        masterController.canvasController.cellColor = frontColorPicker.getValue();     //ugly, but temporary
        masterController.canvasController.renderLife();
    }
}
