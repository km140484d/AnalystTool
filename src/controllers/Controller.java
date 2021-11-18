package controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

    protected static Stage stage;
    protected static Scene scene;

    public static void setPrimaryStage(Stage stagePrim){
        stage = stagePrim;
    }

    public static void setPrimaryScene(Scene scenePrim){
        scene = scenePrim;
    }

}