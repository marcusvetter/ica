package de.marcusvetter.ica;

import de.marcusvetter.ica.ui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/layout.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        controller.init();

        Scene scene = new Scene(root, 700, 500);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        primaryStage.setTitle("Image Color Analyzer");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.getRoot().requestFocus();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
