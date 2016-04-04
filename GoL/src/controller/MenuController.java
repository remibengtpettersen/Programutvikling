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
 * Controller for the menu bars on top of the stage
 * */
public class MenuController {

    private MasterController masterController;

    @FXML
    private MenuItem openBtn;

    /**
     * Stores the reference to the masterController
     * @param masterController reference to the masterController
     */
    public void initialize(MasterController masterController) {

        this.masterController = masterController;
    }

    /**
     * Opens the fileChooser so the user can choose a pattern to import
     */
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

    /**
     * opens a dialog so the user can choose a custom rule
     * @param actionEvent
     */
    public void setCustomRule(ActionEvent actionEvent) {

        TextInputDialog dialog = new TextInputDialog(masterController.canvasController.gol.getRule().toString());

        dialog.setTitle("Custom rule");
        dialog.setHeaderText("Enter custom rule code");
        dialog.setContentText("B: Neighbours needed for birth\nS: Neighbours needed for survival\n" +
                "Example: Conway's rule would be B3/S23");

        Optional<String> result = dialog.showAndWait();

        // Traditional way to get the response value.
        if (result.isPresent()){
            System.out.println("Custom rule set: " + result.get());

            masterController.canvasController.setRule(result.get());
        }
    }

    public void setLWDRule(ActionEvent actionEvent) {
        masterController.canvasController.setRule("B3/S012345678");
    }

    public void setSeedsRule(ActionEvent actionEvent) {
        masterController.canvasController.setRule("B2/S");
    }

    public void setDiamoebaRule(ActionEvent actionEvent) {
        masterController.canvasController.setRule("B35678/S5678");
    }

    public void setReplicatorRule(ActionEvent actionEvent) {
        masterController.canvasController.setRule("B1357/S1357");
    }

    public void setDNNRule(ActionEvent actionEvent) {
        masterController.canvasController.setRule("B3678/S34678");
    }

    public void clearGrid(ActionEvent actionEvent) { masterController.canvasController.clearGrid(); }
}
