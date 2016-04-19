package s305080.theStrip;

import controller.MasterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Truls on 06/04/16.
 */
public class TheStrip {

    Parent root;
    TheStripController theStripController;
    Stage stage;
    MasterController masterController;

    public void display(boolean[][] grid, MasterController masterController){
        this.masterController = masterController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TheStrip.fxml"));
        try {
            root = loader.load();

            theStripController = loader.getController();
            theStripController.setGrid(grid);
            theStripController.setMaster(masterController);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        stage = new Stage();

        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();

        bindCanvasToStage(stage);

        stage.setX(masterController.stage.getX()+masterController.stage.getWidth());
        stage.setY(masterController.stage.getY());

        masterController.stage.setOnCloseRequest(event -> stage.close());

        masterController.getCanvasController().getCanvas().setOnMouseClicked(event -> theStripController.updateStrip());
        masterController.getCanvasController().getCanvas().setOnScrollFinished(event -> theStripController.updateStrip());
        stage.setOnCloseRequest(event -> {
            masterController.getCanvasController().getCanvas().setOnMouseClicked(null);
            masterController.getCanvasController().getCanvas().setOnScrollFinished(null);
            masterController.getMenuController().setTheStripIsShowing(false);
        });
    }

    private void bindCanvasToStage(Stage stage) {
        theStripController.canvas.heightProperty().bind(stage.heightProperty().subtract(40));
    }

    public void close() {
        masterController.getCanvasController().getCanvas().setOnMouseClicked(null);
        masterController.getCanvasController().getCanvas().setOnScrollFinished(null);
        stage.close();
    }
}
