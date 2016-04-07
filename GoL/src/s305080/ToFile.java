package s305080;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Truls on 29/03/16.
 */
public class ToFile {

    static List <String> list;

    public static void writeToFile(boolean[][] grid, int[] boundingBox, Stage stage){



        list = new ArrayList<String>();

        try {
            collectMetaData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder currentLine = new StringBuilder();

        for(int y = boundingBox[2]; y <= boundingBox[3]; y++){

            for(int x = boundingBox[0]; x <= boundingBox[1]; x++){

                if(grid[x][y]){
                    currentLine.append('O');
                }
                else
                    currentLine.append('.');
            }
            list.add(currentLine.toString());
            currentLine = new StringBuilder();
        }

        FileChooser f = new FileChooser();

        File file = f.showSaveDialog(stage);
        if(file == null){
            return; //throw exception
        }
        if(!file.toString().endsWith(".cells")){
            file = new File(file.toString() + ".cells");
        }

        try {
            Files.write(file.toPath(), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void collectMetaData() throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader(ToFile.class.getResource("../s305080/MetaData.fxml"));
            root = loader.load();



        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.showAndWait();
    }
}
