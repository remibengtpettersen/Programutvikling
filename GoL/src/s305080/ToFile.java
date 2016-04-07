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
import java.util.TreeMap;

/**
 * Created by Truls on 29/03/16.
 */
public class ToFile {

    private static List <String> list;
    private static int[] boundingBox;
    private static boolean[][] grid;

    public static void writeToFile(boolean[][] grid, int[] boundingBox, Stage stage){

        if(boundingBox[0] > boundingBox[1]){
            System.out.println("YOU SHALL NOT SAVE");
            return; //throw exception
        }
        ToFile.grid = grid;
        ToFile.boundingBox = boundingBox;

        list = new ArrayList<String>();

        try {
            collectMetaData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChooser f = new FileChooser();

        File file = f.showSaveDialog(stage);
        if(file == null){
            return; //throw exception
        }
      writeRLE(file);
//    writePlainText(file);
    }

    private static void writeRLE(File file) {

        StringBuilder currentLine = new StringBuilder();

        currentLine.append("x = "+(boundingBox[1]-boundingBox[0]+1)+", y = "+(boundingBox[3]-boundingBox[2]+1)+ ", rule = s32/b3");

        list.add(currentLine.toString());

        currentLine = new StringBuilder();
        int counter = 0;
        boolean lastBit = false;
        boolean firstInLine = true;
        for (int y = boundingBox[2]; y <= boundingBox[3]; y++) {

            for (int x = boundingBox[0]; x <= boundingBox[1]; x++) {

                if(firstInLine){
                    counter++;
                    firstInLine = false;
                }
                else if(lastBit == grid[x][y]){
                    counter ++;
                    if (currentLine.length() > 40){
                        list.add(currentLine.toString());
                        currentLine = new StringBuilder();
                    }
                }
                else{
                    if (lastBit){
                        currentLine.append((counter == 1) ? "" : counter).append("o");
                    }
                    else {
                        currentLine.append((counter == 1) ? "" : counter).append("b");
                    }
                    counter = 1;
                }
                lastBit = grid[x][y];

            }

            firstInLine = true;
            if (lastBit){
                currentLine.append((counter == 1) ? "" : counter).append("o");
            }


            currentLine.append("$");

            counter = 0;
        }

        currentLine.append("!");
        list.add(currentLine.toString());

        if(!file.toString().endsWith(".rle")){
            file = new File(file.toString() + ".rle");
        }

        try {
            Files.write(file.toPath(), list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writePlainText(File file) {

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
