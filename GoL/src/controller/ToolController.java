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

import java.util.Objects;

/**
 * Created by Andreas on 09.03.2016.
 */
public class ToolController {

    private MasterController masterController;
    private Image imgPause;
    private Image imgPlay;
    private String pathImages = "/icons/";
    private String playImageName = "arrows.png";
    private String pauseImageName = "bars.png";

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


    public void initialize(MasterController masterController) {
        this.masterController = masterController;
        cellColorPicker.setValue(Color.BLACK);

        imgPause = new Image(getClass().getResourceAsStream(pathImages + pauseImageName), imgViewBtnPlay.getFitWidth(), imgViewBtnPlay.getFitHeight(), true, true);
        imgPlay = new Image(getClass().getResourceAsStream(pathImages + playImageName), imgViewBtnPlay.getFitWidth(), imgViewBtnPlay.getFitHeight(), true, true);

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

    public void speedSliderDragged(){

        masterController.canvasController.setFrameDelay(-(int)speedSlider.getValue());
    }


    public void zoomSliderDragged(){
        masterController.canvasController.setCellSize(Math.exp(zoomSlider.getValue()/28));
    }

    public void giveCellCount(int cellCount) {
        cellCountLabel.setText("Cellcount: "+cellCount);
    }

    public void playGame(ActionEvent actionEvent) {

        if (Objects.equals(btnPlay.getText(), "Play")) {
            changeIconToPause();
            masterController.canvasController.startAnimation();
        }
        else {
            changeIconToPlay();
            masterController.canvasController.stopAnimation();
        }
    }

    public void changeIconToPlay() {
        btnPlay.setText("Play");
        btnPlay.setGraphic(new ImageView(imgPlay));
    }

    public void changeIconToPause() {
        btnPlay.setText("Pause");
        btnPlay.setGraphic(new ImageView(imgPause));
    }

    public void setZoom(double zoom) {
        zoomSlider.setValue(Math.log(zoom)*28);
    }

    public void setMinZoom(double minZoom) {
         zoomSlider.setMin(Math.log(minZoom)*28);
    }
}
