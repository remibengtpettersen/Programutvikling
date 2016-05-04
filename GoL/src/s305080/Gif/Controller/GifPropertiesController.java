package s305080.Gif.Controller;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import s305080.Gif.GifSaver;

/**
 * Created by Truls on 28/04/16.
 */
public class GifPropertiesController {
    public TextField iterations, gPerIteration, framerate;
    public Label info1, info2, info3;
    private GifSaver parent;

    public void textField1Typed(KeyEvent key) {

        if(check(iterations, info1)) {

            String code = key.getCode().toString();

            if (code.equals("ENTER")) {
                info1.setText("");

                gPerIteration.requestFocus();
            }
        }
    }
    public void textField2Typed(KeyEvent key) {


        if(check(gPerIteration, info2)) {

            String code = key.getCode().toString();

            if (code.equals("ENTER")) {
                info2.setText("");
                framerate.requestFocus();
            }
        }
    }
    public void textField3Typed(KeyEvent key) {

        if(check(framerate, info3)) {

            String code = key.getCode().toString();

            if (code.equals("ENTER")) {
                if(check(framerate, info3) && check(gPerIteration, info2) && check(iterations, info1)){
                    closeAndProceed();
                }
            }
        }
    }

    private boolean check(TextField textField, Label label) {
        try{
            System.out.println(Integer.parseInt(textField.getText()));
            if(textField.isFocused()) {
                label.setText("Press ENTER to proceed");
            }
            else {
                label.setText("");
            }
            return true;
        }
        catch (NumberFormatException ignored){
            label.setText("<-- must be numbers");
            return false;
        }
    }

    public void checkForTab(KeyEvent key) {

        String code = key.getCode().toString();

        if (code.equals("TAB")) {
            if (iterations.isFocused()) {
                if(check(iterations, info1)){
                    info1.setText("");
                }
            }
            else if (gPerIteration.isFocused()) {
                if(check(gPerIteration, info2)){
                    info2.setText("");
                }
            }
            else if (framerate.isFocused()){
                if(check(framerate, info3) && check(gPerIteration, info2) && check(iterations, info1)){
                    closeAndProceed();
                }
            }
        }

    }

    private void closeAndProceed() {
        int iterations = Integer.parseInt(this.iterations.getText());
        int gPerIteration = Integer.parseInt(this.gPerIteration.getText());
        int framerate = Integer.parseInt(this.framerate.getText());

        parent.setUserRequest(iterations, gPerIteration, framerate);
        parent.closeWindow();
    }

    public void checkAllAbove() {
        System.out.println(iterations.isFocused());
        if(gPerIteration.isFocused()){
            check(iterations, info1);
        }
        else if(framerate.isFocused()){
            check(gPerIteration, info2);
            check(iterations, info1);
        }

    }

    public void setParent(GifSaver parent) {
        this.parent = parent;
    }
}

