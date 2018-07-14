package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.actionbar.ActionBarModel;
import com.android.ddmlib.IDevice;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.Getter;

import java.io.File;
import java.util.List;

/**
 * @author j1angvei
 * @since 18/7/14
 */
@Getter
public class InstallApkModel {
    private final ObjectProperty<IDevice> chosenDevice;
    private final ListProperty<File> apksToInstall;
    private final SetProperty<String> installArgs;

    public InstallApkModel() {
        chosenDevice = new SimpleObjectProperty<>();
        chosenDevice.bind(ActionBarModel.getInstance().getChosenDevice());

        apksToInstall = new SimpleListProperty<>(FXCollections.observableArrayList());
        installArgs = new SimpleSetProperty<>(FXCollections.observableSet());
    }

    public void addApk(File file) {
        if (!apksToInstall.contains(file)) {
            apksToInstall.add(file);
        }
    }

    public void addApkList(List<File> apks) {
        apks.forEach(this::addApk);
    }
}
