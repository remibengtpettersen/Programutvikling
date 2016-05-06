package s305080.PatternSaver;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Created by Truls on 13/04/16.
 */
public class MetaDataController {

    @FXML
    TextField name, author, description, rules;
    @FXML
    ComboBox format;

    private List<String> list;

    private ToFile toFile;

    public MetaDataController(){

    }

    public void insertMetaData(){
        if (format.getValue() == null){
            //Choose format
            System.out.println(0);
        }
        else {
            switch (format.getValue().toString()) {
                case "Plain text":
                    insertPlainTextMetaData();
                    break;
                case "RLE format":
                    insertRleMetaData();
                    break;
            }
            toFile.closeStage();
        }
    }

    private void insertPlainTextMetaData() {
        if(!name.getText().matches("[ ]*")){
            list.add("!Name: " + name.getText());
            toFile.setInitialFileName(name.getText());
        }
        if(!author.getText().matches("[ ]*")){
            list.add("!Author: " + author.getText());
        }
        if(!description.getText().matches("[ ]*")){
            list.add("!Description: " + description.getText());
        }
        if(!rules.getText().matches("[ ]*")){
            list.add("!Recomended rule : " + rules.getText());
        }
        toFile.setFormat(ToFile.Format.PlainText);

    }

    private void insertRleMetaData() {
        if(!name.getText().matches("[ ]*")){
            list.add("#N: " + name.getText());
            toFile.setInitialFileName(name.getText());
        }
        if(!author.getText().matches("[ ]*")){
            list.add("#O: " + author.getText());
        }
        if(!description.getText().matches("[ ]*")){
            list.add("#C: " + description.getText());
        }
        if(!rules.getText().matches("[ ]*")){
            toFile.setRuleText(rules.getText());
        }
        toFile.setFormat(ToFile.Format.RLE);
    }

    public void setList(List<String> list){
        this.list = list;
    }

    public void setComunicationLink(ToFile comunicationLink) {
        this.toFile = comunicationLink;
    }

    public void insertRule(String rule) {
        rules.setText(rule);

    }
}
