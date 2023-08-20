package net.bc100dev.algo.creator;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.bc100dev.algo.creator.window.MainController;

import java.util.Objects;

public class MainClass extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("windows/main.fxml")));
        primaryStage.setTitle("Algorithm Creator");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.setOnShown(windowEvent -> MainController.COMM_STR = "shown");
        primaryStage.setOnCloseRequest(windowEvent -> MainController.COMM_STR = "closing");
        primaryStage.show();
    }
}
