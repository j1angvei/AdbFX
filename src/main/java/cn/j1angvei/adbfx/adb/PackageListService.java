package cn.j1angvei.adbfx.adb;


import cn.j1angvei.adbfx.functions.apps.PackageInfo;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import javafx.beans.property.SetProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class PackageListService extends Service<List<PackageInfo>> {
    private static final String PREFIX = "package:";

    private final SetProperty<String> arguments;

    public PackageListService(SetProperty<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    protected Task<List<PackageInfo>> createTask() {
        return new Task<List<PackageInfo>>() {
            @Override
            protected List<PackageInfo> call() throws Exception {
                IDevice device = PackageManager.getChosenDevice();

                List<PackageInfo> packageList = new ArrayList<>();

                StringBuilder cmdBuilder = new StringBuilder("pm list packages");
                arguments.forEach(s -> cmdBuilder.append(" ").append(s));
                log.debug("get list packages cmd:{}", cmdBuilder.toString());

                try {
                    device.executeShellCommand(cmdBuilder.toString(), new MultiLineReceiver() {
                        @Override
                        public void processNewLines(String[] lines) {
                            Stream.of(lines)
                                    .filter(s -> s != null && s.startsWith(PREFIX) && s.contains("."))
                                    .forEach(e -> {
                                        String pkg = e.substring(PREFIX.length());
                                        packageList.add(new PackageInfo(pkg, device.getSerialNumber()));
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
                return packageList;
            }
        };
    }
}
