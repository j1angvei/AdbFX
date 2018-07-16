package cn.j1angvei.adbfx.adb.pm;

import cn.j1angvei.adbfx.actionbar.ActionBarModel;
import com.android.ddmlib.IDevice;
import javafx.concurrent.Task;

public abstract class PackageManager<V> extends Task<V> {

    protected IDevice getChosenDevice() {

        return ActionBarModel.getInstance().getChosenDevice().get();
    }

}
