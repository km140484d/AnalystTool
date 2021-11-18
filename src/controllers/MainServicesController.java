package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Math.*;

public class MainServicesController extends Controller implements Initializable {

    private final String pageURL = "/pages/";

    private final String servicesPageURL = "/pages/services/";

    private static List<String> serviceList = new ArrayList<>(
            Arrays.asList("concatinateFiles", "splitFileEqually", "partialFileLoad")
    );

    @FXML
    public GridPane serviceTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int serviceCount = serviceList.size();
        int serviceSquareCount = (int) ceil(sqrt(serviceList.size()));
        int totalButtonCount = (int) pow(serviceSquareCount, 2);
        try {
            for (int i = 0; i < totalButtonCount; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pageURL + "service_item.fxml"));
                VBox serviceParent = fxmlLoader.load();
                Button serviceButton = (Button) serviceParent.lookup("#serviceButton");
                if (i < serviceCount) {
                    String serviceName = serviceList.get(i);
                    serviceButton.setText(serviceName);
                    setServiceAction(serviceName, serviceButton);
                }else
                    serviceButton.setDisable(true);
                serviceTable.add(serviceButton, i / serviceSquareCount, i % serviceSquareCount);
                serviceTable.setMargin(serviceButton, new Insets(5, 10, 5, 10));
            }
        }catch (Exception e) {
            System.out.print(e.getLocalizedMessage());
        }
    }

    private void setServiceAction(String serviceName, Button serviceButton) {
        serviceButton.setOnAction(e -> {
            try {
                switch (serviceName) {
                    case "concatinateFiles":
                        textService("concatenator.fxml", "Concatinator");
                        break;
                    case "splitFileEqually":
                        textService("equal_splitter.fxml", "EqualSplitter");
                        break;
                    case "partialFileLoad":
                        textService("partial_loader.fxml", "PartialLoader");
                        break;
                    default:
                        throw new Exception("Such service doesn't exist!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void textService(String servicePageName, String serviceControllerClass) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(servicesPageURL + servicePageName));
        scene.setRoot(root);
        stage.setTitle(serviceControllerClass);
    }
}
