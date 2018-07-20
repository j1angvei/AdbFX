package cn.j1angvei.adbfx;

import cn.j1angvei.adbfx.adb.PackageManager;
import com.android.ddmlib.AndroidDebugBridge;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdbFxApp extends Application {
    private static final String HOME_FXML = "/Home.fxml";

    private static final String ADB_MAC = "platform-tools_r28.0.0-darwin.zip";
    private static final String ADB_LINUX = "platform-tools_r28.0.0-linux.zip";
    private static final String ADB_WINDOWS = "platform-tools_r28.0.0-windows.zip";

    @Override
    public void init() throws Exception {
        super.init();
        //init adb service as needed
        AndroidDebugBridge.initIfNeeded(false);
        PackageManager.getInstance().registerListener();
    }

    public static void main(String[] args) {
//        initEnvironment();
        AdbFxApp.launch(args);
    }

//    private static void initEnvironment() {
//        boolean adbExisted = new File(AdbStartService.ADB_PATH).exists();
//        if (!adbExisted) {
//            String adbFilePath;
//            if (SystemUtils.IS_OS_MAC) {
//                adbFilePath = ADB_MAC;
//            } else if (SystemUtils.IS_OS_LINUX) {
//                adbFilePath = ADB_LINUX;
//            } else if (SystemUtils.IS_OS_WINDOWS) {
//                adbFilePath = ADB_WINDOWS;
//            } else {
//                throw new IllegalArgumentException("Unsupported system OS!");
//            }
//            adbFilePath = SystemUtils.getUserDir().getAbsolutePath() + File.separator + adbFilePath;
//            log.debug("adb file paths:{}", adbFilePath);
//            String destDir = SystemUtils.getUserHome().getAbsolutePath() + File.separator + ".adbfx" + File.separator;
//
//            FileManager.unzip(adbFilePath, destDir);
//
//
//        }
//    }

    @Override
    public void stop() throws Exception {
        AndroidDebugBridge.terminate();
        PackageManager.getInstance().unregisterListener();
        super.stop();
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
}
