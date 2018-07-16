package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.BaseController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class UninstallAppController extends BaseController<UninstallAppModel> {
    public CheckBox checkKeepData;
    public TextField fieldPackageName;
    public ListView<String> listUninstallHistory;
    public Button btnClearHistory;
    public ListView<String> listInstalledPackages;
    public Text textNoPackages;
    public TextArea areaResult;
    public Button btnStartUninstall;
    public Button btnRefreshPackages;

    private UninstallAppService mUninstallAppService;
    private GetPackagesService mGetPackagesService;

    @Override
    protected UninstallAppModel initModel() {
        return new UninstallAppModel();
    }

    @Override
    protected void initArguments() {
        mUninstallAppService = new UninstallAppService(getModel());
        mGetPackagesService = new GetPackagesService(getModel().getChosenDevice());
    }

    @Override
    protected void initView() {
        // uninstall args
        getModel().getKeepData().bind(checkKeepData.selectedProperty());

        // input package name
        getModel().getPackageName().bind(fieldPackageName.textProperty());

        // start uninstall
        btnStartUninstall.setOnAction(event -> {
            getModel().syncUninstallHistory();
            mUninstallAppService.restart();
        });

        // when uninstalling app, disable button and change its text
        btnStartUninstall.textProperty().bind(Bindings.createStringBinding(() ->
                        mUninstallAppService.isRunning() ? "Uninstalling..." : "Uninstall",
                mUninstallAppService.runningProperty()));
        btnStartUninstall.disableProperty().bind(mUninstallAppService.runningProperty());

        /* **************************************************
            show install history
         ************************************************** */
        listUninstallHistory.itemsProperty().bind(getModel().getUninstallHistory());
        listUninstallHistory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fieldPackageName.setText(newValue));
        btnClearHistory.setOnAction(event -> getModel().getUninstallHistory().clear());
        btnClearHistory.disableProperty().bind(Bindings.isEmpty(getModel().getUninstallHistory()));

        /* **************************************************
            show installed packages
         ************************************************** */
        listInstalledPackages.setItems(FXCollections.observableArrayList());
        listInstalledPackages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fieldPackageName.setText(newValue));
        mGetPackagesService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                listInstalledPackages.getItems().setAll(newValue);
            } else {
                listInstalledPackages.getItems().clear();
            }
        });
        btnRefreshPackages.setOnAction(event -> mGetPackagesService.restart());
        btnRefreshPackages.disableProperty().bind(mGetPackagesService.runningProperty());
        textNoPackages.textProperty().bind(Bindings.createStringBinding(() ->
                        mGetPackagesService.isRunning() ? "Loading packages from device..." : "No packages available",
                mGetPackagesService.runningProperty()));
        /* **************************************************
            show install result, success or failure and why it failed
         ************************************************** */
        areaResult.textProperty().bind(mUninstallAppService.valueProperty());


    }

    @Override
    protected void initData() {

    }
}
