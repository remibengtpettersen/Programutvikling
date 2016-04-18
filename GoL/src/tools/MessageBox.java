package tools;

import javafx.scene.control.Alert;

import java.awt.*;

/**
 * Created by Truls on 18/04/16.
 */
public class MessageBox {
    public static void alert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.show();
    }
}
