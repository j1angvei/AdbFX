package cn.j1angvei.adbfx.adb;


import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class PackageListService extends Service<List<String>> {
    private static final String PREFIX = "package:";

    private String status;
    private String type;

    public void restart(String status, String type) {
        this.status = status;
        this.type = type;
        restart();
    }

    @Override
    protected Task<List<String>> createTask() {
        return new Task<List<String>>() {
            @Override
            protected List<String> call() throws Exception {
                if (status == null || type == null) {
                    log.error("Error when list packages, should set status(enable, disable, all) and type(system app, third party app or all)");
                    throw new NullPointerException("list package arguments is NULL, set status and type first");
                }
                IDevice device = PackageManager.getChosenDevice();

                List<String> packageList = new ArrayList<>();

                String cmd = String.format("pm list packages %s %s", status, type);
                log.debug("get list packages cmd:{}", cmd);

                try {
                    device.executeShellCommand(cmd, new MultiLineReceiver() {
                        @Override
                        public void processNewLines(String[] lines) {
                            Stream.of(lines)
                                    .filter(s -> s != null && s.startsWith(PREFIX) && s.contains("."))
                                    .forEach(e -> {
                                        String pkg = e.substring(PREFIX.length());
                                        packageList.add(pkg);
                                    });
                        }

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }
                    });
                } catch (IOException e) {
                    log.error("Error when get package list ", e);
                }
                log.debug("package list size:{}", packageList.size());
                return packageList;
            }
        };
    }
}
