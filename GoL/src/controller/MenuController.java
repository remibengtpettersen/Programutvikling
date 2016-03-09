package controller;

import javafx.event.ActionEvent;

/**
 * Created by Andreas on 09.03.2016.
 */
public class MenuController {

    private MasterController masterController;

    public void initialize(MasterController masterController) {
        this.masterController = masterController;
    }

    public void onAbout(ActionEvent actionEvent) {
        System.out.println("About pressed");
    }
}
