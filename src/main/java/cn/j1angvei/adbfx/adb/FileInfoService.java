package cn.j1angvei.adbfx.adb;

import com.android.ddmlib.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class FileInfoService extends Service<Void> {
    private static final Pattern BLANK = Pattern.compile("\\b+");
    private static final int LIMIT = 5;

    private IDevice mDevice;
    private FileInfo mFileInfo;

    public void restart(@NonNull IDevice device, @NonNull FileInfo fileInfo) {
        mDevice = device;
        mFileInfo = fileInfo;
        this.restart();
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {

                String cmd = String.format("ls %s -l", mFileInfo.getFullPath());
                log.debug("file info command:{}", cmd);
                try {
                    mDevice.executeShellCommand(cmd, new MultiLineReceiver() {
                        @Override
                        public void processNewLines(String[] lines) {
                            Stream.of(lines).forEach(new Consumer<String>() {
                                @Override
                                public void accept(String s) {
                                    log.debug("line:{}", s);
                                    //remove soft link
//                                    if (s.contains("->")) {
//                                        s = s.replaceAll("\\s+->.+$", "");
//                                    }
//                                    String[] segments;
//                                    if (s.startsWith("d")) {
//                                        segments = s.split("\\s+", 5);
//                                        log.debug("dir name:{}", segments[5]);
//                                    } else {
//                                        segments = s.split("\\s+", 6);
//                                        log.debug("file name:{}", segments[6]);
//                                    }
//                                    log.debug("Arrays:{}", Arrays.toString(segments));
                                }
                            });
                        }

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }
                    });
                } catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
                    log.error("Error when get file info in device", e);
                }
                return null;
            }
        };
    }
}
