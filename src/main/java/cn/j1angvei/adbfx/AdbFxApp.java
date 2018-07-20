package cn.j1angvei.adbfx;

import cn.j1angvei.adbfx.adb.PackageManager;
import com.android.ddmlib.AndroidDebugBridge;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class AdbFxApp extends Application {
    private static final String HOME_FXML = "/Home.fxml";

    public static void main(String[] args) {
        AdbFxApp.launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        //init adb service as needed
        AndroidDebugBridge.initIfNeeded(false);
        PackageManager.getInstance().registerListener();
    }

    @Override
    public void start(Stage primaryStage) {
        FileManager.getInstance().init(primaryStage);

        BorderPane borderPane = (BorderPane) NodeManager.getInstance().loadFxml(HOME_FXML);
        if (borderPane != null) {
            Scene scene = new Scene(borderPane);
            primaryStage.setScene(scene);
            primaryStage.setWidth(900);
            primaryStage.setMinWidth(750);
            primaryStage.setHeight(600);
            primaryStage.setMinHeight(450);
            String title = FileManager.getStrings("app_title");
            primaryStage.setTitle(title);
            primaryStage.show();
        }
    }

    @Override
    public void stop() throws Exception {
        AndroidDebugBridge.terminate();
        PackageManager.getInstance().unregisterListener();
        super.stop();
    }
}
