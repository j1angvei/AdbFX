package cn.j1angvei.adbfx.adb;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class PushFileService extends Service<String> {

    private IDevice mDevice;
    private List<File> mLocalFiles;
    private String mRemoteDir;

    public void restart(
            @NonNull IDevice device,
            @NonNull List<File> localFilePaths,
            @NonNull String remoteDir) {
        mDevice = device;
        mLocalFiles = localFilePaths;
        mRemoteDir = remoteDir;
        restart();
    }

    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                StringBuilder result = new StringBuilder();

                int fileSize = mLocalFiles.size();

                for (int i = 0; i < fileSize; i++) {

                    updateProgress(i, fileSize);

                    File file = mLocalFiles.get(i);
                    String localPath = file.getAbsolutePath();
                    String remotePath = String.format("%s/%s", mRemoteDir, file.getName());

                    result.append(String.format("%d. Push %s to %s\n", i + 1, localPath, remotePath));

                    try {
                        mDevice.pushFile(localPath, remotePath);
                        result.append("Success\n");
                    } catch (AdbCommandRejectedException | SyncException | IOException | TimeoutException e) {
                        log.error("Error when push files to remote device", e);
                        result.append("Error:\t");
                        result.append(e.getMessage()).append("\n");
                    }
                }
                updateProgress(fileSize, fileSize);
                Thread.sleep(1000);

                return result.toString();
            }
        };
    }
}
