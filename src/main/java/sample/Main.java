package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.usb4java.Device;

import javax.usb.*;
import java.awt.*;
import java.util.List;

public class Main extends Application {

    public static final DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    public void start(Stage primaryStage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getResource("/Views/main.fxml"));
        primaryStage.setTitle("Super Market Άγγελος POS System");

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());
        scene.getStylesheets().add(getClass().getResource("/Css/stylesheet.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
