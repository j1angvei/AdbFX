package cn.j1angvei.adbfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class AdbFxApp extends Application {

    public static void main(String[] args) {
        AdbFxApp.launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("AdbFX");
        primaryStage.show();
    }
}
