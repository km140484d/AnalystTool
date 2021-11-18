package controllers.services;

import components.Window;
import controllers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import resources.Constants;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Concatenator extends Controller implements Initializable {

    private final String pageURL = "/pages/";

    private File inputDirectory;
    private File outputDirectory;

    @FXML
    public VBox concatinationBox;
    @FXML
    public TextField inputDirectoryText;
    @FXML
    public TextField outputDirectoryText;
    @FXML
    public TextField outputFileNameText;
    @FXML
    public Label executionStatusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public File directoryChooserSetup(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if(selectedDirectory != null) {
            return selectedDirectory;
        }
        return null;
    }

    public void openInputDirectoryChooser(ActionEvent actionEvent) {
        inputDirectory = directoryChooserSetup();
        if (inputDirectory != null)
            inputDirectoryText.setText(inputDirectory.getAbsolutePath());
    }

    public void openOutputDirectoryChooser(ActionEvent actionEvent) {
        outputDirectory = directoryChooserSetup();
        if (outputDirectory != null)
            outputDirectoryText.setText(outputDirectory.getAbsolutePath());
    }

    public void executeFileConcatenation(ActionEvent actionEvent) {
        String outputFileName = outputFileNameText.getText();
        if (inputDirectory == null || outputDirectory == null || outputFileName == null)
            new Window(true, "Message", "Text fields must not be empty!").display();
        else
            if (inputDirectory.isFile() || outputDirectory.isFile())
                new Window(true, "Message", "Directories must be chosen instead of files!").display();
            else {
                if (!outputFileName.contains("."))
                    outputFileName += ".csv";
                String fullOutputFileName = outputDirectory.getAbsolutePath() + "/" + outputFileName;
                File outputFile = new File(fullOutputFileName);
                executionStatusLabel.setText(Constants.IN_PROGRESS_STATUS);
                concatenateCsvFiles(inputDirectory, outputFile);
                executionStatusLabel.setText(Constants.FINISHED_STATUS);
            }
    }

    public static void concatenateCsvFiles(File inputDirectory, File outputFile){
        String line = "";

        File[] inputFiles = inputDirectory.listFiles();

        try (FileWriter fw = new FileWriter(outputFile);
             BufferedWriter bw = new BufferedWriter(fw)){
            for (int i=0; i < inputFiles.length; i++) {
                File inputFile = inputFiles[i];
                if (inputFile.isFile()) {
                    try (FileReader fr = new FileReader(inputFile);
                         BufferedReader br = new BufferedReader(fr);) {
                        String header = br.readLine();
                        if (i == 0) bw.write(header + "\n");
                        while ((line = br.readLine()) != null)
                            bw.write(line + "\n");
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBackHome(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(pageURL + "service_table.fxml"));
        scene.setRoot(root);
        stage.setTitle("Home");
    }
}
