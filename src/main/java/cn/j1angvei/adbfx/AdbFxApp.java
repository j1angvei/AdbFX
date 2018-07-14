package cn.j1angvei.adbfx;

import com.android.ddmlib.AndroidDebugBridge;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class AdbFxApp extends Application {

    public static void main(String[] args) {
        AdbFxApp.launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        //init adb service as needed
        AndroidDebugBridge.initIfNeeded(false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FileManager.getInstance().init(primaryStage);

        AnchorPane borderPane = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);
        primaryStage.setTitle("AdbFX");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        AndroidDebugBridge.terminate();
        super.stop();
    }
}
