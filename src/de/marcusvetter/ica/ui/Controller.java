package de.marcusvetter.ica.ui;

import de.marcusvetter.ica.analyzer.Analyzer;
import de.marcusvetter.ica.analyzer.AnalyzerResult;
import de.marcusvetter.ica.util.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller {

    private Stage primaryStage;

    private Analyzer.Color selectedColor;
    private Analyzer.FilterType selectedFilterType;
    private BufferedImage selectedImage;

    @FXML
    private ChoiceBox<Analyzer.Color> choiceBoxColor;

    @FXML
    private ChoiceBox<Analyzer.FilterType> choiceBoxFilterType;

    @FXML
    private ImageView imagePreview;

    @FXML
    private TextField textFieldFilterValue;

    @FXML
    private TextArea textAreaResult;

    public void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            Logger.log("Selected file: " + file.toURI().toString());

            try {
                selectedImage = ImageIO.read(file);
                imagePreview.setImage(new Image(file.toURI().toString()));
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }

    }

    private int getFilterValueAsInt() {
        String filterValue = textFieldFilterValue.getText();
        Logger.log("Filter value: " + filterValue);
        return filterValue.equals("") ? -1 : Integer.parseInt(filterValue);
    }

    private boolean isConfigurationComplete() {
        return selectedColor != null && selectedImage != null && getFilterValueAsInt() != -1;
    }

    public void startAnalyzing() {
        if (isConfigurationComplete()) {

            long startTime = System.nanoTime();

            AnalyzerResult result = new Analyzer().countPixel(selectedImage,selectedColor, selectedFilterType, getFilterValueAsInt());

            Logger.log(result.getFilteredPixels() + " of " + result.getAllPixels() + " (" + result.getFilteredPixelInPercent() + " %) ");

            long endTime = System.nanoTime();

            printResult(result, (endTime - startTime) / 1000 / 1000);
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void init() {
        initChoiceBoxColor();
        initChoiceBoxFilterType();
    }

    @FXML
    public void keyPressed(KeyEvent evt) {
        if (evt.getCode().equals(KeyCode.ENTER)) {
            startAnalyzing();
        }
    }

    private void initChoiceBoxColor() {
        choiceBoxColor.setItems(FXCollections.observableArrayList(Analyzer.Color.RED, Analyzer.Color.GREEN, Analyzer.Color.BLUE));

        choiceBoxColor.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
                    selectedColor = choiceBoxColor.getItems().get((Integer) number2);
                });

        choiceBoxColor.getSelectionModel().select(Analyzer.Color.RED);
    }

    private void initChoiceBoxFilterType() {
        choiceBoxFilterType.setItems(FXCollections.observableArrayList(Analyzer.FilterType.LESS_THAN, Analyzer.FilterType.EQUALS, Analyzer.FilterType.GREATER_THAN));

        choiceBoxFilterType.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
                    selectedFilterType = choiceBoxFilterType.getItems().get((Integer) number2);
                });

        choiceBoxFilterType.getSelectionModel().select(Analyzer.FilterType.LESS_THAN);
    }

    private void printResult(AnalyzerResult result, long duration) {
        textAreaResult.clear();
        textAreaResult.appendText(String.format("Processing took %s ms", duration));
        textAreaResult.appendText("\n");
        textAreaResult.appendText("------------------------");
        textAreaResult.appendText("\n");
        textAreaResult.appendText(String.format("%s of %s", result.getFilteredPixels(), result.getAllPixels()));
        textAreaResult.appendText("\n");
        textAreaResult.appendText(String.format("%s %%", result.getFilteredPixelInPercent()));
    }

}
