package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/**
 * Controller for the menu bars on top of the stage
 * */
public class MenuController {

    private MasterController masterController;

    @FXML
    private MenuItem openBtn;
    @FXML
    RadioMenuItem theStripS305080;

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

    public void saveFile(){
        masterController.getCanvasController().saveToFile();
    }
    public void onAbout(ActionEvent actionEvent) {

        System.out.println("About clicked");
    }

    public void setConwayRule(ActionEvent actionEvent) {

        masterController.getCanvasController().setRule("classic");
    }

    public void setHighLifeRule(ActionEvent actionEvent) {

        masterController.getCanvasController().setRule("highlife");
    }

    /**
     * opens a dialog so the user can choose a custom rule
     * @param actionEvent
     */
    public void setCustomRule(ActionEvent actionEvent) {

        TextInputDialog dialog = new TextInputDialog(masterController.getCanvasController().gol.getRule().toString());

        dialog.setTitle("Custom rule");
        dialog.setHeaderText("Enter custom rule code");
        dialog.setContentText("B: Neighbours needed for birth\nS: Neighbours needed for survival\n" +
                "Example: Conway's rule would be B3/S23");

        Optional<String> result = dialog.showAndWait();

        // Traditional way to get the response value.
        if (result.isPresent()){
            System.out.println("Custom rule set: " + result.get());

            masterController.getCanvasController().setRule(result.get());
        }
    }

    public void setLWDRule(ActionEvent actionEvent) {
        masterController.getCanvasController().setRule("B3/S012345678");
    }

    public void setSeedsRule(ActionEvent actionEvent) {
        masterController.getCanvasController().setRule("B2/S");
    }

    public void setDiamoebaRule(ActionEvent actionEvent) {
        masterController.getCanvasController().setRule("B35678/S5678");
    }

    public void setReplicatorRule(ActionEvent actionEvent) {
        masterController.getCanvasController().setRule("B1357/S1357");
    }

    public void setDNNRule(ActionEvent actionEvent) {
        masterController.getCanvasController().setRule("B3678/S34678");
    }

    public void clearGrid(ActionEvent actionEvent) { masterController.getCanvasController().clearGrid(); }

    public void showTheStrip(){

        if(!theStripS305080.isSelected())
            masterController.closeTheStrip();
        else
            masterController.showTheStrip();
    }

    //region s305080

    /**
     * Changes the selected status of the radioMenuItem
     * @param theStripIsShowing True if it should be selected, false if not
     */
    public void setTheStripIsShowing(boolean theStripIsShowing) {
        theStripS305080.setSelected(theStripIsShowing);
    }
    //endregion
}
