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
public class PullFileService extends Service<String> {
    private IDevice mDevice;
    private List<FileInfo> mFileInfoList;
    private String mLocalDir;

    public void restart(
            @NonNull IDevice device,
            @NonNull List<FileInfo> fileInfoList,
            @NonNull String localDir) {
        mDevice = device;
        mFileInfoList = fileInfoList;
        mLocalDir = localDir;
        this.restart();
    }


    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                StringBuilder result = new StringBuilder();

                int fileSize = mFileInfoList.size();

                for (int i = 0; i < fileSize; i++) {

                    updateProgress(i, fileSize);

                    FileInfo fileInfo = mFileInfoList.get(i);
                    String localPath = mLocalDir + File.separator + fileInfo.getName();
                    String remotePath = fileInfo.getFullPath();

                    log.debug("Pull files: local path,{};remote path,{}", localPath, remotePath);

                    result.append(String.format("%d. Pull %s from %s\n", i + 1, remotePath, localPath));

                    try {
                        mDevice.pullFile(remotePath, localPath);
                        result.append("Success\n");
                    } catch (AdbCommandRejectedException | SyncException | IOException | TimeoutException e) {
                        log.error("Error when pull files from remote device", e);
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
