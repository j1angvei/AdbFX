package cn.j1angvei.adbfx.adb;

import com.android.ddmlib.*;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
public class ScreenRecordService extends Service<Void> {

    private IDevice mDevice;
    private ScreenRecorderOptions mRecorderOptions;
    private String mRemoteDir;
    private String mLocalDir;

    private StringProperty mOutput;

    public void restart(
            @NonNull IDevice device,
            @NonNull ScreenRecorderOptions screenRecorderOptions,
            @NonNull String remoteDir,
            @NonNull String localDir,
            @NonNull StringProperty output) {
        mDevice = device;
        mRecorderOptions = screenRecorderOptions;

        mRemoteDir = remoteDir;
        mLocalDir = localDir;
        mOutput = output;
        this.restart();
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                String videoName = String.format("ScreenRecord_AdbFX_%d.mp4", System.currentTimeMillis());
                String remotePath = mRemoteDir + videoName;
                String localPath = mLocalDir + File.separator + videoName;

                appendOutput(String.format("File saved at remote path:%s;local path:{}", remotePath));
                log.debug("Start recording...");
                try {
                    mDevice.startScreenRecorder(remotePath, mRecorderOptions, new OutputReceiver());
                    appendOutput("Recording complete, Pull from device to local ");
                    mDevice.pullFile(remotePath, localPath);
                    appendOutput("Success\n\n");
                } catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException | SyncException e) {
                    log.error("Error when take screen recording", e);
                    appendOutput(e.getMessage() + "\n\n");
                }
                return null;
            }
        };
    }

    private void appendOutput(String... lines) {

        Stream.of(lines).forEach(s -> mOutput.setValue(mOutput.getValueSafe() + s + "\n"));
    }

    private class OutputReceiver extends MultiLineReceiver {

        @Override
        public void processNewLines(String[] lines) {
            appendOutput(lines);
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }
}
