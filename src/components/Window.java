package components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Window extends Stage {

    public Window(boolean modal, String title, String message) {
        // Set modality
        /* APPLICATION_MODAL - Defines a modal window that blocks events from being
         * delivered to any other application window.*/
        /* WINDOW_MODAL - Defines a modal window that block events from being delivered
         * to its entire owner window hierarchy.*/
        /* NONE - Defines a top-level window that is not modal and does not block any
         * other window.*/
        if (modal) {
            this.initModality(Modality.APPLICATION_MODAL);
        } else {
            this.initModality(Modality.NONE);
        }

        // Set title
        this.setTitle(title);

        // Set content
        Label label = new Label(message);
        VBox layout = new VBox();
        layout.setPadding(new Insets(30,30,30,30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label);
        this.setScene(new Scene(layout));
    }

    public void display() {
        // Display window
        /* For non modal windows use show() in order to return to its owner.
         * For modal windows use showAndWait() along with modality in order to wait
         * for it to be closed before returning to its owner.*/
        if (this.getModality().equals(Modality.APPLICATION_MODAL)) {
            this.showAndWait();
        } else {
            this.show();
        }
    }

}