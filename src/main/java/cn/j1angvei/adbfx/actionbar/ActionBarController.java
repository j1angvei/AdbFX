package cn.j1angvei.adbfx.actionbar;

import cn.j1angvei.adbfx.BaseController;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
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
                }

                String name = String.format("%s %s (Android %s, API %s) [%s]",
                        object.isEmulator() ? object.getSerialNumber() : object.getProperty(IDevice.PROP_DEVICE_MANUFACTURER),
                        object.isEmulator() ? object.getAvdName() : object.getProperty(IDevice.PROP_DEVICE_MODEL),
                        object.getProperty(IDevice.PROP_BUILD_VERSION),
                        object.getProperty(IDevice.PROP_BUILD_API_LEVEL),
                        (object.getState() != null ? object.getState().name().toUpperCase() : "ERROR")

                );


                return StringUtils.capitalize(name);
            }


            @Override
            public IDevice fromString(String string) {
                return null;
            }
        });
        comboAllDevices.itemsProperty().bind(getModel().getConnectedDevices());
        getModel().getConnectedDevices().emptyProperty().addListener((observable, oldValue, newValue) -> {
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
                Platform.runLater(() -> getModel().getConnectedDevices().add(device));
            }

            @Override
            public void deviceDisconnected(IDevice device) {
                Platform.runLater(() -> getModel().getConnectedDevices().remove(device));
            }

            @Override
            public void deviceChanged(IDevice device, int changeMask) {
                if (changeMask == IDevice.CHANGE_STATE) {
                    Platform.runLater(() -> getModel().getUpdatedDevice().setValue(device));
                }
            }
        });
        //bind chosen device
        getModel().getChosenDevice().bind(comboAllDevices.valueProperty());

        //restart adb daemon
        btnRestartAdb.setOnAction(event -> {
            comboAllDevices.getItems().clear();
            comboAllDevices.getSelectionModel().clearSelection();
            mAdbStartService.restart();
        });
        mAdbStartService.runningProperty().addListener((observable, oldValue, newValue) -> {
            btnRestartAdb.setDisable(newValue);
            String text = getResourceBundle().getString(newValue ? "starting" : "restart_adb");
            btnRestartAdb.setText(text);
        });
    }

    @Override
    protected void initData() {
        mAdbStartService.restart();
    }
}
