package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by Andreas on 09.03.2016.
 */
public class MenuController {

    private MasterController masterController;


    @FXML
    private MenuItem openBtn;

    public void initialize(MasterController masterController) {
        this.masterController = masterController;

    }


    public void openFileChooser(){
        masterController.choosePattern();
    }
    public void onAbout(ActionEvent actionEvent) {
        System.out.println("About pressed");
    }
}
