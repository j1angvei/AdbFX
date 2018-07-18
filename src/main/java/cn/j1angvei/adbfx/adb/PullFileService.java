package cn.j1angvei.adbfx.adb;

import com.android.ddmlib.IDevice;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PullFileService extends Service<String> {
    private IDevice mDevice;

    public void restart(IDevice device, String remote) {
    }


    @Override
    protected Task<String> createTask() {
        return null;
    }
}
