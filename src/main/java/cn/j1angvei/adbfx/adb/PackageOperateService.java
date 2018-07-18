package cn.j1angvei.adbfx.adb;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
public class PackageOperateService extends Service<String> {


    private IDevice mDevice;
    private Operation mOperation;
    private String mPackageName;

    public void restart(IDevice device, String packageName, Operation operation) {
        mDevice = device;
        mOperation = operation;
        mPackageName = packageName;
        restart();
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                if (mDevice == null || mOperation == null || mPackageName == null) {
                    log.error("Error when do package operation",
                            new NullPointerException(String.format("device is %s, operation is %s,package name is %s",
                                    mDevice, mOperation, mPackageName)));
                    return null;
                }

                StringBuilder result = new StringBuilder(StringUtils.capitalize(mOperation.word))
                        .append(" for package [")
                        .append(mPackageName)
                        .append("]:\n");

                String cmd = String.format("pm %s %s", mOperation.word, mPackageName);
                log.debug("do package operation,{}", cmd);
                try {
                    mDevice.executeShellCommand(cmd, new MultiLineReceiver() {
                        @Override
                        public void processNewLines(String[] lines) {
                            Stream.of(lines).forEach(s -> result.append(s).append("\n"));
                        }

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }
                    });
                    result.append("\n");

                } catch (IOException e) {
                    log.error("Error when do package operation", e);
                }

                return result.toString();
            }

        };
    }


    public enum Operation {
        //        ENABLE("enable"),
//        DISABLE("disable"),
        HIDE("hide"),
        UNHIDE("unhide"),

        //        GRANT_PERMISSION("grant"),
//        REVOKE_PERMISSION("revoke"),
        APK_PATH("fullPath"),
        CLEAR_DATA("clear");

        public final String word;

        Operation(String word) {
            this.word = word;
        }
    }
}
