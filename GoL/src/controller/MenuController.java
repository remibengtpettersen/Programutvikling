package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.rules.RuleParser;
import s305073.controller.EditorController;
import tools.MessageBox;

import java.io.IOException;
import java.util.Optional;

/**
 * GifPropertiesController for the menu bars on top of the stage
 * */
public class MenuController {

    public RadioMenuItem staticButton, dynamicButton;
    private MasterController masterController;
    
    @FXML private RadioMenuItem theStripS305080, statsS305080, markupS305080;

    /**
     * Stores the reference to the masterController
     * @param masterController reference to the masterController
     */
    public void initialize(MasterController masterController) {

        this.masterController = masterController;
    }

    /**
     * Launch fileChooser for importing pattern files.
     */
    public void openFileChooser(){

        masterController.choosePattern();
    }

    /**
     * Save to pattern file.
     */
    public void saveFile(){

        masterController.getCanvasController().saveToFile();
    }

    /**
     * Launched about window for "Game of Life".
     */
    public void onAbout() {
        MessageBox.alert("This is game of life");
    }

    /**
     * Set Conway Rule - Born 3 / Survive 2 and 3.
     */
    public void setConwayRule() {

        masterController.getCanvasController().setRule(RuleParser.CLASSIC_RULESTRING);
    }

    /**
     * Set High life rule - Born 3 and 6 / Survive 2 and 3.
     */
    public void setHighLifeRule() {

        masterController.getCanvasController().setRule(RuleParser.HIGHLIFE_RULESTRING);
    }

    /**
     *  Dialog for setting custom rule.
     */
    public void setCustomRule() {

        // set active rule text in input dialog
        TextInputDialog dialog = new TextInputDialog(masterController.getCanvasController().gol.getRule().toString());

        // set title, header and content text
        dialog.setTitle("Custom rule");
        dialog.setHeaderText("Enter custom rule code");
        dialog.setContentText("B: Neighbours needed for birth\nS: Neighbours needed for survival\n" +
                "Example: Conway's rule would be B3/S23");

        // launch dialog
        Optional<String> result = dialog.showAndWait();

        // check if result is entered
        if (result.isPresent()){
            System.out.println("Custom rule set: " + result.get());

            // set custom rule
            masterController.getCanvasController().setRule(result.get());
        }
    }

    /**
     *  Set LWD-rule - Born 3 / Survive 1, 2, 3, 4, 5, 6, 7 and 8.
     */
    public void setLWDRule() { masterController.getCanvasController().setRule(RuleParser.LWD_RULESTRING); }

    /**
     *  Set Seeds-rule - Born 2 / Survive none
     */
    public void setSeedsRule() { masterController.getCanvasController().setRule(RuleParser.SEEDS_RULESTRING); }

    /**
     *  Set Diamoeba-rule - Borne 3, 5, 6, 7 and 8 / Survive 5, 6, 7 and 8.
     */
    public void setDiamoebaRule() {
        masterController.getCanvasController().setRule(RuleParser.DIAMOEBA_RULESTRING);
    }

    /**
     *  Set Replicator-rule - Born 1, 3, 5 and 7 /Survive 1, 3, 5 and 7.
     */
    public void setReplicatorRule() { masterController.getCanvasController().setRule(RuleParser.REPLICATOR_RULESTRING); }

    /**
     *  Set DNN-rule - Born 3, 6, 7 and 8 / Survive 3, 4, 6, 7 and 8.
     */
    public void setDNNRule() { masterController.getCanvasController().setRule(RuleParser.DNN_RULESTRING); }

    /**
     *  Clear grid - empty´s all arrays, reset cell counter and redraws canvas
     */
    public void clearGrid() { masterController.getCanvasController().clearGrid(); }


    //region s305080
    public void showTheStrip(){

        if(!theStripS305080.isSelected())
            masterController.closeTheStrip();
        else
            masterController.showTheStrip();
    }
    /**
     * Changes the selected status of the radioMenuItem
     * @param theStripIsShowing True if it should be selected, false if not
     */
    public void setTheStripIsShowing(boolean theStripIsShowing) {
        theStripS305080.setSelected(theStripIsShowing);
    }

    public void showS305080Stats(){
        if(!statsS305080.isSelected())
            masterController.closeStats();
        else
            masterController.showStats();
    }
    /**
     * Changes the selected status of the radioMenuItem
     * @param theStripIsShowing True if it should be selected, false if not
     */
    public void setStatsShowing(boolean theStripIsShowing) {
        statsS305080.setSelected(theStripIsShowing);
    }
    // endregion

    /**
     *  Student: s305073
     *  Launch editor for build new or manipulating existing pattern.
     */
    public void launchEditor() {
        // instantiate stage for editor
        Stage editor = new Stage();

        // load editor fxml view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../s305073/view/EditorView.fxml"));

        // set modality to window and locks primary stage for access
        editor.initModality(Modality.WINDOW_MODAL);
        editor.initOwner(masterController.stage);

        // initialize root
        GridPane root = null;

        // tries to load fxml root object to grid pane reference
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // instantiate new scene, set fxml root and scene width and height
        Scene scene = new Scene(root, 1000, 710);
        // set scene for editor
        editor.setScene(scene);

        // set control to editorController
        EditorController editorController = loader.getController();

        // deep copy and assign to new reference variable
        editorController.getDeepCopyGol(masterController.getCanvasController().gol);

        editorController.initialize(masterController);

        // set pattern in editor window
        editorController.setPattern();

        // stop game of life animation
        masterController.getCanvasController().stopAnimation();

        // change icon on control button
        masterController.getToolController().changeButtonIconToPlay();

        // set editor title
        editor.setTitle("Pattern Editor");

        // set location on screen - (x, y)
        editor.setX(550);
        editor.setY(250);

        // launch editor
        editor.showAndWait();
    }


    //region s305061
    public void openStatWindow() {

        masterController.openStatWindow();
    }

    public void openGifWindow() {

        masterController.openGifWindow();
    }
    //endregion

    /**
     * Enables selection of area in grid through canvas position.
     */
    public void activateMarkup() {
        if (markupS305080.isSelected()){
            masterController.getCanvasController().activateMarkup();
        }
        else {
            masterController.getCanvasController().deactivateMarkup();
        }
    }

    /**
     * Cut pattern from game board.
     */
    public void cut() {
        masterController.getCanvasController().cutMarkedArea();
    }

    /**
     * Copy pattern from game board.
     */
    public void copy() {
        masterController.getCanvasController().copyMarkedArea();
    }

    /**
     * Paste pattern to game board.
     */
    public void paste() {
        masterController.getCanvasController().pasteClipBoard();
    }

    public void saveToGif() {
        masterController.getCanvasController().saveToGif();
    }

    public void openFromUrl() {
        masterController.importFromUrl();
    }

    public void changeToStaticBoard() {
        if (!staticButton.isSelected()){
            staticButton.setSelected(true);
        }
        else{
            dynamicButton.setSelected(false);
            masterController.getCanvasController().changeToStatic();
        }
    }

    public void changeToDynamicBoard() {
        if (!dynamicButton.isSelected()){
            dynamicButton.setSelected(true);
        }
        else{
            staticButton.setSelected(false);
            masterController.getCanvasController().changeToDynamic();
        }
    }

    public void openTips() {
        MessageBox.alert("Move around wit right-click + drag\n" +
                "Draw cells with left-click\n" +
                "Zoom with scroll or slider\n" +
                "control speed with vertical scroll or slider\n" +
                "Pause game with right-click or pause button.\n" +
                "\n" +
                "Rotate import-pattern with 'z' and 'x'\n" +
                "\n" +
                "Last imported pattern is in clipboard, insert with 'ctrl + V'");
    }

}
