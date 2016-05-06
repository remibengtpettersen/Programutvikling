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

    private  List <String> fileContent;
    private FileChooser saveFileChooser;
    private Stage stage;
    private String ruleText;
    private Format format;
    private GameOfLife gol;


    enum Format {
        RLE, PlainText
    }

    public void writeToFile(GameOfLife gol, Stage stage){

        this.gol = gol;

        if(gol.getCellCount() == 0){

            MessageBox.alert("No pattern to save");
            System.out.println("YOU SHALL NOT SAVE");
                return; //throw exception

        }

        fileContent = new ArrayList<>();

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

        if(format == Format.RLE)
            writeRLE(file);

        if(format == Format.PlainText)
            writePlainText(file);


    }

    private  void writeRLE(File file) {

        fileContent.addAll(getRleFormat(gol));

        if(!file.toString().endsWith(".rle")){
            file = new File(file.toString() + ".rle");
        }

        try {
            Files.write(file.toPath(), fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List <String> getRleFormat(GameOfLife gol) {

        List<String> list = new ArrayList<>();

        StringBuilder currentLine = new StringBuilder();

        int [] boundingBox = gol.getBoundingBox();

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
                else if(lastBit == gol.isCellAlive(x, y)){
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
                lastBit = gol.isCellAlive(x, y);

            }

            firstInLine = true;
            if (lastBit){
                currentLine.append((counter == 1) ? "" : counter).append("o");
            }

            if(y != boundingBox[3])
                currentLine.append("$");

            counter = 0;
        }

        currentLine.append("!");
        list.add(currentLine.toString());

        return list;

    }

    private void writePlainText(File file) {

        fileContent.addAll(getPlainTextFormat(gol));


        if(!file.toString().endsWith(".cells")){
            file = new File(file.toString() + ".cells");
        }

        try {
            Files.write(file.toPath(), fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List <String> getPlainTextFormat(GameOfLife gol) {
        StringBuilder currentLine = new StringBuilder();

        List <String> list = new ArrayList<>();

        int [] boundingBox = gol.getBoundingBox();


        for(int y = boundingBox[2]; y <= boundingBox[3]; y++){

            for(int x = boundingBox[0]; x <= boundingBox[1]; x++){

                if(gol.isCellAlive(x, y)){
                    currentLine.append('O');
                }
                else
                    currentLine.append('.');
            }
            list.add(currentLine.toString());
            currentLine = new StringBuilder();
        }
        return list;
    }

    private void collectMetaData(Stage primaryStage) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader(ToFile.class.getResource("MetaData.fxml"));
        root = loader.load();
        MetaDataController mController = loader.getController();
        mController.setList(fileContent);
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

    public void setFormat(Format format) {
        this.format = format;
    }

    void closeStage() {
        stage.close();
    }

    void setInitialFileName(String initialFileName) {
        saveFileChooser.setInitialFileName(initialFileName);
    }

    void setRuleText(String ruleText) {
        this.ruleText = ruleText;
    }
}
