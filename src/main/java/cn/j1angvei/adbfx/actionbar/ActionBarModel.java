package cn.j1angvei.adbfx.actionbar;

import com.android.ddmlib.IDevice;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import lombok.Getter;

/**
 * @author j1angvei
 * @since 18/7/14
 */
@Getter
public class ActionBarModel {

    private static final ActionBarModel INSTANCE = new ActionBarModel();
    private final ObjectProperty<IDevice> updatedDevice;
    private final ListProperty<IDevice> connectedDevices;
    private final ObjectProperty<IDevice> chosenDevice;

    private ActionBarModel() {
        updatedDevice = new SimpleObjectProperty<>();
        connectedDevices = new SimpleListProperty<>(FXCollections.observableArrayList(param -> new Observable[]{updatedDevice}));
        chosenDevice = new SimpleObjectProperty<>();
    }

    public static ActionBarModel getInstance() {
        return INSTANCE;
    }
}
