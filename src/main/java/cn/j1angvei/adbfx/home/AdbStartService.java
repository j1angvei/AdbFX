package cn.j1angvei.adbfx.home;

import com.android.ddmlib.AndroidDebugBridge;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

/**
 * @author j1angvei
 * @since 18/7/14
 */
@Slf4j
public class AdbStartService extends Service<AndroidDebugBridge> {
    public static final String ADB_PATH = SystemUtils.getUserHome().getAbsolutePath() + File.separator +
            ".adbfx" + File.separator +
            "platform-tools" + File.separator +
            "adb";

    @Override
    protected Task<AndroidDebugBridge> createTask() {
        return new AdbStartTask();
    }

    private class AdbStartTask extends Task<AndroidDebugBridge> {
        @Override
        protected AndroidDebugBridge call() {
            log.debug("Restart adb service");
            AndroidDebugBridge.disconnectBridge();
            return AndroidDebugBridge.createBridge(ADB_PATH, true);
        }
    }
}
