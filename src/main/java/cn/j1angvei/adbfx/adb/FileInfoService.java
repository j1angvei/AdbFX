package cn.j1angvei.adbfx.adb;

import com.android.ddmlib.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class FileInfoService extends Service<Void> {
    private static final String SOFT_LINK = "\\s+->$";
    private static final Pattern DATE = Pattern.compile("\\s+\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}\\s+");

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

                            Stream.of(lines).filter(s -> s != null && !s.isEmpty())
                                    .map(s -> s.replaceAll(SOFT_LINK, ""))
                                    .forEach(s -> {
                                        Matcher matcher = DATE.matcher(s);
                                        if (matcher.find()) {
                                            String prefix = s.substring(0, matcher.start());
                                            String time = matcher.group().trim();
                                            String suffix = s.substring(matcher.end());

                                            boolean isDir = prefix.startsWith("d");

                                            FileInfo fileInfo = new FileInfo(mFileInfo, suffix, isDir);
                                            fileInfo.setTime(time);
                                            log.debug("newly found sub file/dir,{}", fileInfo);
                                            if (isDir) {
                                                mFileInfo.updateSubDirs(fileInfo);
                                            } else {
                                                mFileInfo.updateSubFiles(fileInfo);
                                            }
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
