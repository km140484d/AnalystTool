import controllers.MainServicesController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resources.Constants;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        try {
            primaryStage.setTitle("Analyst Text Services");
            MainServicesController.setPrimaryStage(primaryStage);
            Parent serviceTable = FXMLLoader.load(getClass().getResource("/pages/service_table.fxml"));
            Scene primaryScene = new Scene(serviceTable, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
            primaryScene.getStylesheets().addAll(getClass().getResource("resources/css/menu").toExternalForm(),
                    getClass().getResource("resources/css/base").toExternalForm());
            primaryStage.setScene(primaryScene);
            MainServicesController.setPrimaryScene(primaryScene);
            primaryStage.show();
            primaryStage.setMinHeight(Constants.SCENE_HEIGHT);
            primaryStage.setMinWidth(Constants.SCENE_WIDTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}