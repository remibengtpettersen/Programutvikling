package s305080.PatternSaver;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.GameOfLife;
import tools.MessageBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Truls on 29/03/16.
 */
public class ToFile {

    private  List <String> list;
    private  int[] boundingBox;
    private  boolean[][] grid;
    FileChooser saveFileChooser;
    private Stage stage;
    private String ruleText;
    LagringsFormat format;
    GameOfLife gol;


    public enum LagringsFormat {
        RLE, PlainText
    }

    public void writeToFile(GameOfLife gol, Stage stage){

        this.gol = gol;
        int [] boundingBox = gol.getBoundingBox();

        if(boundingBox[0] > boundingBox[1]){
            MessageBox.alert("No pattern to save");
            System.out.println("YOU SHALL NOT SAVE");
            return; //throw exception
        }
        this.grid = gol.getGrid();
        this.boundingBox = boundingBox;

        list = new ArrayList<String>();

        saveFileChooser = new FileChooser();

        try {
            collectMetaData(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(format == null)
            return;

        File file = saveFileChooser.showSaveDialog(stage);
        if(file == null){
            return; //throw exception
        }

        if(format == LagringsFormat.RLE)
            writeRLE(file);

        if(format == LagringsFormat.PlainText)
            writePlainText(file);


    }

    private  void writeRLE(File file) {

        StringBuilder currentLine = new StringBuilder();

        currentLine.append("x = "+(boundingBox[1]-boundingBox[0]+1)+", y = "+(boundingBox[3]-boundingBox[2]+1) + ((ruleText == null)?"":", rule = " + ruleText));

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

    private void writePlainText(File file) {

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

    private void collectMetaData(Stage primaryStage) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader(ToFile.class.getResource("MetaData.fxml"));
        root = loader.load();
        MetaDataController mController = loader.getController();
        mController.setList(list);
        Scene scene = new Scene(root);

        mController.setComunicationLink(this);
        mController.insertRule(gol.getRule().toString());

        stage = new Stage();
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        root.requestFocus();
        stage.showAndWait();

    }

    public void setFormat(LagringsFormat format) {
        this.format = format;
    }

    public void closeStage() {
        stage.close();
    }

    public void setInitialFileName(String initialFileName) {
        saveFileChooser.setInitialFileName(initialFileName);
    }

    public void setRuleText(String ruleText) {
        this.ruleText = ruleText;
    }
}
