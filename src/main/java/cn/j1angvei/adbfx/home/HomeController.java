package cn.j1angvei.adbfx.home;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.NodeManager;
import cn.j1angvei.adbfx.functions.Function;
import cn.j1angvei.adbfx.functions.FunctionCell;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;


public class HomeController extends BaseController<HomeModel> {

    // root view
    @FXML
    private BorderPane borderMain;

    // action bar
    @FXML
    private MenuItem menuRestartAdb;
    @FXML
    private ComboBox<IDevice> comboAllDevices;

    // function tiles
    @FXML
    private ScrollPane scrollFunctionTileContainer;
    @FXML
    private TabPane tabOpenedFunctions;
    @FXML
    private TilePane tileFunctions;

    // function list
    @FXML
    private ToggleButton toggleBtnFunctions;

    @FXML
    private ListView<Function> listFunctions;

    // no device hint
    @FXML
    private VBox boxNoDevice;

    @Override
    protected HomeModel initModel() {
        return HomeModel.getInstance();
    }

    @Override
    protected void initArguments() {


    }

    @Override
    protected void initView() {

        setupActionBar();

        setupContentArea();

        setupFunctionList();

    }

    private void setupActionBar() {
        AdbStartService adbStartService = new AdbStartService();

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
                Platform.runLater(() -> getModel().getUpdatedDevice().setValue(device));
            }
        });
        //bind chosen device
        getModel().getChosenDevice().bind(comboAllDevices.valueProperty());

        //restart adb daemon
        menuRestartAdb.setOnAction(event -> {
            comboAllDevices.getItems().clear();
            comboAllDevices.getSelectionModel().clearSelection();
            adbStartService.restart();
        });
        adbStartService.runningProperty().addListener((observable, oldValue, newValue) -> {
            menuRestartAdb.setDisable(newValue);
            String text = getResourceBundle().getString(newValue ? "starting" : "restart_adb");
            menuRestartAdb.setText(text);
        });

        adbStartService.restart();
    }

    private void setupContentArea() {

        boxNoDevice.visibleProperty().bind(Bindings.isNull(getModel().getChosenDevice()));

        scrollFunctionTileContainer.visibleProperty().bind(getModel().getOpenedFunctions().emptyProperty());

        Stream.of(Function.values()).forEach(function -> {
            FunctionTile tile = new FunctionTile(function);
            tile.setOnAction(event -> getModel().getOpenedFunctions().add(function));
            tileFunctions.getChildren().add(tile);
        });

        getModel().getOpenedFunctions().addListener((SetChangeListener<Function>) change -> {
            if (change.wasAdded()) {
                Function function = change.getElementAdded();
                Tab tab = getModel().getInitializedTabs().get(function);
                if (tab == null) {
                    tab = new Tab(getResourceBundle().getString(function.title), NodeManager.loadFxml(function.ui));
                    tab.setOnClosed(event -> getModel().getOpenedFunctions().remove(function));
                    getModel().getInitializedTabs().put(function, tab);
                }
                tabOpenedFunctions.getTabs().add(tab);
            }
        });

    }


    private void setupFunctionList() {

        KeyValue left = new KeyValue(listFunctions.prefWidthProperty(), 0);
        Duration start = Duration.ZERO;
        KeyValue right = new KeyValue(listFunctions.prefWidthProperty(), 160);
        Duration end = new Duration(350);


        Timeline showTimeline = new Timeline(new KeyFrame(start, left), new KeyFrame(end, right));
        Timeline hideTimeline = new Timeline(new KeyFrame(start, right), new KeyFrame(end, left));
        hideTimeline.setOnFinished(event -> borderMain.setLeft(null));

        toggleBtnFunctions.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    borderMain.setLeft(listFunctions);
                    showTimeline.playFromStart();
                } else {
                    hideTimeline.playFromStart();
                }
            }
        });
        listFunctions.getItems().setAll(Function.values());
        listFunctions.setCellFactory(new Callback<ListView<Function>, ListCell<Function>>() {
            @Override
            public ListCell<Function> call(ListView<Function> param) {
                return new FunctionCell(getResourceBundle());
            }
        });
        listFunctions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Function>() {
            @Override
            public void changed(ObservableValue<? extends Function> observable, Function oldValue, Function newValue) {
                if (newValue != null) {
                    getModel().getOpenedFunctions().add(newValue);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}
