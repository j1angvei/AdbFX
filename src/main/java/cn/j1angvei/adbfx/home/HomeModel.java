package cn.j1angvei.adbfx.home;

import cn.j1angvei.adbfx.actionbar.ActionBarModel;
import cn.j1angvei.adbfx.functions.Function;
import com.android.ddmlib.IDevice;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import lombok.Getter;

/**
 * @author j1angvei
 * @since 18/7/14
 */
@Getter
public class HomeModel {

    private static HomeModel sInstance;
    private final ObjectProperty<IDevice> selectedDevice;
    private final SetProperty<Function> openedFunctions;

    private HomeModel() {
        selectedDevice = new SimpleObjectProperty<>();
        selectedDevice.bind(ActionBarModel.getInstance().getChosenDevice());
        openedFunctions = new SimpleSetProperty<>(FXCollections.observableSet());
    }

    public static HomeModel getInstance() {
        if (sInstance == null) {
            sInstance = new HomeModel();
        }
        return sInstance;
    }
}
