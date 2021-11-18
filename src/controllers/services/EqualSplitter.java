package controllers.services;

import components.Window;
import controllers.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import resources.Constants;

import java.io.*;

public class EqualSplitter extends Controller {

    private final String pageURL = "/pages/";

    private File inputFile;
    private String outputDirectoryPath;
    private String outputFileName;
    private String outputFileExtension;
    private int outputFileNumber;

    @FXML
    public TextField inputFileText;
    @FXML
    public TextField outputDirectoryText;
    @FXML
    public TextField outputFileNumberText;
    @FXML
    public Label executionStatusLabel;

    public void openInputFileChooser(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter textExtensionFilter =
                new FileChooser.ExtensionFilter("TXT files", "*.txt", "*.csv");
        fileChooser.getExtensionFilters().add(textExtensionFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            inputFile = selectedFile;
            String absoluteInputPath = inputFile.getAbsolutePath();
            inputFileText.setText(absoluteInputPath);
            outputFileName = absoluteInputPath.substring(absoluteInputPath.lastIndexOf("\\"), absoluteInputPath.lastIndexOf("."));
            outputFileExtension = absoluteInputPath.substring(absoluteInputPath.lastIndexOf("."));
        }
    }

    public void openOutputDirectoryChooser(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            outputDirectoryPath = selectedDirectory.getAbsolutePath();
            outputDirectoryText.setText(outputDirectoryPath);
        }
    }

    public void executeFileSplitting(ActionEvent actionEvent) throws IOException {
        if (inputFile == null || outputDirectoryPath == null || outputFileName == null || outputFileExtension == null)
            new Window(true, "Message", "Text fields must not be empty!").display();
        else if (!inputFile.isFile())
            new Window(true, "Message", "Input file must be a text file and not directory!").display();
        else if (!numericInputValidation(outputFileNumberText.getText()))
            new Window(true, "Message", "File number must be a positive number!").display();
        else {
            String outputFilePath = outputDirectoryPath + "/" + outputFileName + "_";
            executionStatusLabel.setText(Constants.IN_PROGRESS_STATUS);
            fileSplitter(inputFile, outputFilePath, outputFileExtension, outputFileNumber);
            executionStatusLabel.setText(Constants.FINISHED_STATUS);
        }
    }

    public static void fileSplitter(File inputFile, String outputFilePath, String outputFileExtension, int outputFileNumber) {
        String line = "";

        BufferedWriter[] bufferedWriters = new BufferedWriter[outputFileNumber];
        try (FileReader fr = new FileReader(inputFile);
             BufferedReader br = new BufferedReader(fr)) {
            line = br.readLine(); //header
            for (int i = 0; i < outputFileNumber; i++) {
                bufferedWriters[i] = new BufferedWriter(new FileWriter(new File(outputFilePath + i + outputFileExtension)));
                bufferedWriters[i].write(line + "\n");
            }
            line = br.readLine(); //first line
            for (int j = 0; line != null; j++, line = br.readLine())
                bufferedWriters[j % outputFileNumber].write(line + "\n");
            for (int i = 0; i < outputFileNumber; i++)
                bufferedWriters[i].close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBackHome(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(pageURL + "service_table.fxml"));
        scene.setRoot(root);
        stage.setTitle("Home");
    }

    public boolean numericInputValidation(String textValue) {
        if (!textValue.matches("(\\d{0,7}([\\.]\\d{0,4})?)*"))
            outputFileNumberText.setText("");
        else {
            int rowNumber = Integer.parseInt(textValue);
            if (rowNumber < 1)
                outputFileNumberText.setText("");
            else
                outputFileNumber = rowNumber;
            return true;
        }
        return false;
    }
}
