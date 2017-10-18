package de.marcusvetter.ica.ui;

import de.marcusvetter.ica.analyzer.Analyzer;
import de.marcusvetter.ica.analyzer.AnalyzerResult;
import de.marcusvetter.ica.license.Serial;
import de.marcusvetter.ica.util.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private Stage primaryStage;

    private Analyzer.Color selectedColor;
    private Analyzer.FilterType selectedFilterType;
    private List<BufferedImage> selectedImages;

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
        List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);

        if (files != null) {
            selectedImages = new ArrayList();

            files.forEach((file) -> {
                if (file != null) {
                    try {
                        selectedImages.add(ImageIO.read(file));
                        imagePreview.setImage(new Image(file.toURI().toString()));
                    } catch (IOException e) {
                        Logger.error(e.getMessage());
                    }
                }
            });
        }

    }

    private Integer getFilterValueAsInt() {
        try {
            return Integer.parseInt(textFieldFilterValue.getText());
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isConfigurationComplete() {
        return selectedColor != null && getFilterValueAsInt() != null && selectedImages != null && selectedImages.size() > 0;
    }

    public void startAnalyzing() {
        if (isConfigurationComplete()) {
            Logger.log("Start analyzing " + selectedImages.size() + " images with filter value: " + getFilterValueAsInt());
            textAreaResult.clear();
            int idx = 0;
            for (BufferedImage image : selectedImages) {
                AnalyzerResult result = new Analyzer().countPixel(image, selectedColor, selectedFilterType, getFilterValueAsInt());
                printResult(result);
                Logger.log(String.format("Image %d (%dx%d): %d of %d (%.2f%%)", idx++, image.getWidth(),
                        image.getHeight(), result.getFilteredPixels(), result.getAllPixels(),
                        result.getFilteredPixelInPercent()));
            }

            copyResultToClipboard();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void init() {
        Serial.checkSerialNumber();
//      Serial.clearSerialNumber();
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

        choiceBoxColor.getSelectionModel().select(Analyzer.Color.GREEN);
    }

    private void initChoiceBoxFilterType() {
        choiceBoxFilterType.setItems(FXCollections.observableArrayList(Analyzer.FilterType.LESS_THAN, Analyzer.FilterType.EQUALS, Analyzer.FilterType.GREATER_THAN));

        choiceBoxFilterType.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
                    selectedFilterType = choiceBoxFilterType.getItems().get((Integer) number2);
                });

        choiceBoxFilterType.getSelectionModel().select(Analyzer.FilterType.GREATER_THAN);
    }

    private void printResult(AnalyzerResult result) {
        textAreaResult.appendText(String.format("%s\t%s\n", result.getFilteredPixels(), result.getAllPixels()));
    }

    private void copyResultToClipboard() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        clipboard.clear();
        content.putString(textAreaResult.getText());
        clipboard.setContent(content);
    }

}
