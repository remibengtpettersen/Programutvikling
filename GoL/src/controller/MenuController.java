package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.rules.ClassicRule;

import java.io.File;
import java.util.Optional;

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
        System.out.println("About clicked");
    }

    public void setConwayRule(ActionEvent actionEvent) {
        masterController.canvasController.setRule("classic");
    }

    public void setHighLifeRule(ActionEvent actionEvent) {
        masterController.canvasController.setRule("highlife");
    }

    public void setCustomRule(ActionEvent actionEvent) {

        TextInputDialog dialog = new TextInputDialog("B3/S23");
        dialog.setTitle("Custom rule");
        dialog.setHeaderText("Enter custom rule code");
        //dialog.setHeaderText(null);
        dialog.setContentText("B: Neighbours needed for birth\nS: Neighbours needed for survival\n" +
                "Example: Conway's rule would be B3/S23");

        Optional<String> result = dialog.showAndWait();

        // Traditional way to get the response value.
        if (result.isPresent()){
            System.out.println("Custom rule set: " + result.get());

            masterController.canvasController.setRule(result.get());
        }

        //Lambda
        //result.ifPresent(ruleCode -> System.out.println("Your choice: " + ruleCode));
    }
}
