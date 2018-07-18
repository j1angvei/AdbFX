package cn.j1angvei.adbfx.adb;

import com.android.ddmlib.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class FileOperateService extends Service<Void> {

    private IDevice mDevice;
    private String mCurrentDir;
    private Operation mOperation;

    public void list(IDevice device, String currentDir, List<String> subDirs, List<String> subFiles) {
        mOperation = Operation.LS;
        mDevice = device;
        mCurrentDir = currentDir;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                String cmd = String.format("ls -l %s", mCurrentDir);

                try {
                    mDevice.executeShellCommand(cmd, new MultiLineReceiver() {
                        @Override
                        public void processNewLines(String[] lines) {

                        }

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }
                    });
                } catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
                    log.error("Error when operate remote file", e);
                }
                return null;
            }
        };
    }

    public enum Operation {
        LS,
        MV,
        CP,
        REDIR,
        CHOWN,
        CHMOD
    }

}
