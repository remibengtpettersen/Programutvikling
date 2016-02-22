package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import model.GameOfLife;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class GameController implements Initializable{

    MasterController master;
    GameOfLife gol;

    @FXML
    Canvas canvas;
    @FXML
    Slider speedSlider;
    @FXML
    Slider zoomSlider;
    @FXML
    ColorPicker cellColor;
    @FXML
    ColorPicker backgroundColor;
    @FXML
    ChoiceBox ruleChoiceBox;
    Button saveBtn;
    @FXML
    Button loadBtn;


    /**
     * To pass reference between controllers
     * @param master
     */
    public void setMaster(MasterController master) {
        this.master = master;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gol = new GameOfLife(123);
        initializeListeners();
    }

    private void initializeListeners() {

    }
}
