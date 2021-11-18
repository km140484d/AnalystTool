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

public class PartialLoader extends Controller {

    private final String pageURL = "/pages/";

    private File inputFile;
    private String outputDirectoryPath;

    private String outputFileName;
    private String outputFileExtension;
    private int outputFileRowNumber;

    @FXML
    public TextField inputFileText;
    @FXML
    public TextField outputDirectoryText;
    @FXML
    public TextField outputRowNumberText;
    @FXML
    public Label executionStatusLabel;

    public boolean numericInputValidation(String textValue) {
        if (!textValue.matches("(\\d{0,7}([\\.]\\d{0,4})?)*"))
            outputRowNumberText.setText("");
        else {
            int rowNumber = Integer.parseInt(textValue);

            if (rowNumber < 1)
                outputRowNumberText.setText("");
            else
                outputFileRowNumber = rowNumber;
            return true;
        }
        return false;
    }

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

    public void executePartialLoad(ActionEvent actionEvent) {
        if (inputFile == null || outputDirectoryPath == null || outputFileName == null || outputFileExtension == null)
            new Window(true, "Message", "Text fields must not be empty!").display();
        else if (!inputFile.isFile())
            new Window(true, "Message", "Input file must be a text file and not directory!").display();
        else if (!numericInputValidation(outputRowNumberText.getText()))
            new Window(true, "Message", "Row number must be a positive number!").display();
        else {
            String outputFilePath = outputDirectoryPath + "/" + outputFileName + "_part" + outputFileExtension;
            File outputFile = new File(outputFilePath);
            executionStatusLabel.setText(Constants.IN_PROGRESS_STATUS);
            partialLoadCsv(inputFile, outputFile, outputFileRowNumber);
            executionStatusLabel.setText(Constants.FINISHED_STATUS);
        }
    }

    public static void partialLoadCsv(File inputFile, File outputFile, int rowCount) {
        String line = "";

        try (FileWriter fw = new FileWriter(outputFile);
             BufferedWriter bw = new BufferedWriter(fw);
             FileReader fr = new FileReader(inputFile);
             BufferedReader br = new BufferedReader(fr)) {
            line = br.readLine(); //header
            bw.write(line + "\n");
            line = br.readLine(); //first line
            for (int j = 0; j < rowCount && line != null; j++, line = br.readLine())
                bw.write(line + "\n");
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
