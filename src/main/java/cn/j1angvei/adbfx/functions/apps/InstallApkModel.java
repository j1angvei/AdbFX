package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.home.HomeModel;
import com.android.ddmlib.IDevice;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import lombok.Getter;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author j1angvei
 * @since 18/7/14
 */
@Getter
public class InstallApkModel {
    private final ObjectProperty<IDevice> chosenDevice;
    private final ListProperty<String> apksToInstall;
    private final SetProperty<String> installArgs;
    private final ListProperty<String> historyApks;

    public InstallApkModel() {
        chosenDevice = new SimpleObjectProperty<>();
        chosenDevice.bind(HomeModel.getInstance().getChosenDevice());

        apksToInstall = new SimpleListProperty<>(FXCollections.observableArrayList());
        installArgs = new SimpleSetProperty<>(FXCollections.observableSet());

        historyApks = new SimpleListProperty<>(FXCollections.observableArrayList());
        apksToInstall.addListener((ListChangeListener<String>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach((Consumer<String>) path -> {
                        if (!historyApks.contains(path)) {
                            historyApks.add(path);
                        }
                    });
                }
            }
        });
    }

    public void addInputApk(String apkPath) {
        if (apkPath != null && !apksToInstall.contains(apkPath)) {
            apksToInstall.add(apkPath);
        }
    }

    public void addInputApkList(List<String> apkPaths) {
        if (apkPaths != null && apkPaths.isEmpty()) {
            apkPaths.forEach(this::addInputApk);
        }
    }
}
