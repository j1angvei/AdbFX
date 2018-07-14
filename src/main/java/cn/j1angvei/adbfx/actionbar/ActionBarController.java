package cn.j1angvei.adbfx.actionbar;

import cn.j1angvei.adbfx.BaseController;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class ActionBarController extends BaseController<ActionBarModel> {
    public Button btnRestartAdb;
    public ComboBox<IDevice> comboAllDevices;


    private AdbStartService mAdbStartService;

    @Override
    protected ActionBarModel initModel() {
        return ActionBarModel.getInstance();
    }

    @Override
    protected void initArguments() {
        mAdbStartService = new AdbStartService();
    }

    @Override
    protected void initView() {

        //connected devices
        comboAllDevices.setConverter(new StringConverter<IDevice>() {
            @Override
            public String toString(IDevice object) {
                if (object == null) {
                    return null;
                } else
                    return String.format("%s %s (Android %s, API %s)",
                            object.getProperty(IDevice.PROP_DEVICE_MANUFACTURER),
                            object.getProperty(IDevice.PROP_DEVICE_MODEL),
                            object.getProperty(IDevice.PROP_BUILD_VERSION),
                            object.getProperty(IDevice.PROP_BUILD_API_LEVEL));
            }

            @Override
            public IDevice fromString(String string) {
                return null;
            }
        });
        comboAllDevices.itemsProperty().bind(getmModel().getConnectedDevices());
        getmModel().getConnectedDevices().emptyProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                comboAllDevices.getSelectionModel().clearSelection();
            } else if (comboAllDevices.getValue() == null) {
                comboAllDevices.getSelectionModel().selectFirst();
            }
        });

        // listen device change
        AndroidDebugBridge.addDeviceChangeListener(new AndroidDebugBridge.IDeviceChangeListener() {
            @Override
            public void deviceConnected(IDevice device) {
                Platform.runLater(() -> getmModel().getConnectedDevices().add(device));
            }

            @Override
            public void deviceDisconnected(IDevice device) {
                Platform.runLater(() -> getmModel().getConnectedDevices().remove(device));
            }

            @Override
            public void deviceChanged(IDevice device, int changeMask) {
                if (changeMask == IDevice.CHANGE_STATE) {
                    Platform.runLater(() -> getmModel().getUpdatedDevice().setValue(device));
                }
            }
        });
        //bind chosen device
        getmModel().getChosenDevice().bind(comboAllDevices.valueProperty());

        //restart adb daemon
        btnRestartAdb.setOnAction(event -> {
            comboAllDevices.getItems().clear();
            mAdbStartService.restart();
        });
        mAdbStartService.runningProperty().addListener((observable, oldValue, newValue) -> {
            btnRestartAdb.setDisable(newValue);
            btnRestartAdb.setText(newValue ? "Starting..." : "Restart ADB");
        });
    }

    @Override
    protected void initData() {
        mAdbStartService.restart();
    }
}
