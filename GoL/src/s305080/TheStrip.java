package s305080;

import controller.MasterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Truls on 06/04/16.
 */
public class TheStrip {

    Parent root;
    TheStripController theStripController;

    public void display(boolean[][] grid, MasterController masterController){
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../s305080/TheStrip.fxml"));
        try {
            root = loader.load();

            theStripController = loader.getController();
            theStripController.setGrid(grid);
            theStripController.setMaster(masterController);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();
        stage.setMaxHeight(440);
        bindCanvasToStage(stage);

        stage.setX(masterController.stage.getX()+masterController.stage.getWidth());
        stage.setY(masterController.stage.getY());
      //  masterController.canvasController.getCanvas().setOnMouseClicked(event -> theStripController.updateStrip());
        masterController.canvasController.getCanvas().setOnMouseClicked(event -> theStripController.updateStrip());
    }

    private void bindCanvasToStage(Stage stage) {
        theStripController.canvas.heightProperty().bind(stage.heightProperty().subtract(40));
        theStripController.canvas.widthProperty().bind(theStripController.canvas.heightProperty().multiply(20));
    }
}
