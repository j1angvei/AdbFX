package cn.j1angvei.adbfx.functions.apps;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class GetPackagesService extends Service<List<String>> {

    private static final String PREFIX = "package:";
    private final ObjectProperty<IDevice> chosenDevice;

    public GetPackagesService(ObjectProperty<IDevice> chosenDevice) {
        this.chosenDevice = chosenDevice;
    }

    @Override
    protected Task<List<String>> createTask() {
        return new GetPackageTask();
    }

    private class GetPackageTask extends Task<List<String>> {

        @Override
        protected List<String> call() throws Exception {
            IDevice device = chosenDevice.get();
            List<String> result = new ArrayList<>();
            try {
                device.executeShellCommand("pm list packages -3", new MultiLineReceiver() {
                    @Override
                    public void processNewLines(String[] lines) {
                        Stream.of(lines).filter(s -> s != null && s.startsWith(PREFIX)).forEach(s -> result.add(s.substring(PREFIX.length())));
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }
                });
            } catch (IOException e) {
                log.error("Error when get package list from device:{}", device.getSerialNumber(), e);
            }
            return result;
        }
    }
}
