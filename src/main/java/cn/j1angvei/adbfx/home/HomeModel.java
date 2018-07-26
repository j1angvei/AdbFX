package cn.j1angvei.adbfx.home;

import cn.j1angvei.adbfx.functions.Function;
import com.android.ddmlib.IDevice;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Tab;
import lombok.Getter;

/**
 * @author j1angvei
 * @since 18/7/14
 */
@Getter
public class HomeModel {

    private static final HomeModel INSTANCE = new HomeModel();

    // action bar
    private final ObjectProperty<IDevice> updatedDevice;
    private final ListProperty<IDevice> connectedDevices;
    private final ObjectProperty<IDevice> selectedDevice;

    // function tile
    private final SetProperty<Function> openedFunctions;
    private final MapProperty<Function, Tab> initializedTabs;


    private HomeModel() {
        updatedDevice = new SimpleObjectProperty<>();
        connectedDevices = new SimpleListProperty<>(FXCollections.observableArrayList(param -> new Observable[]{updatedDevice}));
        selectedDevice = new SimpleObjectProperty<>();

        openedFunctions = new SimpleSetProperty<>(FXCollections.observableSet());
        initializedTabs = new SimpleMapProperty<>(FXCollections.observableHashMap());

    }

    public static HomeModel getInstance() {
        return INSTANCE;
    }
}
