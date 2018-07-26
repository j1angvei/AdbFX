package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.home.HomeModel;
import com.android.ddmlib.IDevice;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.Getter;

@Getter
public class UninstallAppModel {
    private final ObjectProperty<IDevice> chosenDevice;
    private final StringProperty packageName;
    private final BooleanProperty keepData;
    private final ListProperty<String> uninstallHistory;

    public UninstallAppModel() {
        chosenDevice = new SimpleObjectProperty<>();
        chosenDevice.bind(HomeModel.getInstance().getChosenDevice());

        packageName = new SimpleStringProperty();
        keepData = new SimpleBooleanProperty();

        uninstallHistory = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public final void syncUninstallHistory() {
        String name = packageName.get();
        if (name != null && !uninstallHistory.contains(name)) {
            uninstallHistory.add(name);
        }

    }
}
