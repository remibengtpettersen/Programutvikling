package tools;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;

import java.awt.*;

/**
 * Created by Truls on 18/04/16.
 */
public class MessageBox {

    private static Alert alert;

    /**
     * Opens a dialog to alert the user about something
     * @param message to be displayed to the user
     */
    public static void alert(String message){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.show();
        alert.setOnCloseRequest(event -> close());
    }


    /**
     * Closes the alert box if it is open
     */
    public static void close() {
        if (alert != null){
            alert.close();
            alert = null;
        }
        System.out.println("testingtesting");
    }


}
