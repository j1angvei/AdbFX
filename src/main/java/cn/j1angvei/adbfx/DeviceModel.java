package cn.j1angvei.adbfx;

import cn.j1angvei.adbfx.actionbar.ActionBarModel;
import com.android.ddmlib.IDevice;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

@Getter
public abstract class DeviceModel extends BaseModel {
    private final ObjectProperty<IDevice> chosenDevice;

    public DeviceModel() {
        chosenDevice = new SimpleObjectProperty<>();
        chosenDevice.bind(ActionBarModel.getInstance().getChosenDevice());
    }
}
