package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.adb.PackageDetailService;
import cn.j1angvei.adbfx.adb.PackageListService;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PackageListController extends BaseController<PackageListModel> {

    public TableView<PackageInfo> tablePackageList;
    public MenuItem menuRefreshList;
    public Text textRefreshList;
    public ButtonBar barPackagesActions;

    public SplitMenuButton menuGetApk;

    public ToggleGroup toggleAppStatus;
    public ToggleGroup toggleAppType;

    private PackageListService mPackageListService;
    private PackageDetailService mPackageDetailService;


    @Override
    protected PackageListModel initModel() {
        return new PackageListModel();
    }

    @Override
    protected void initArguments() {
        mPackageListService = new PackageListService();
        mPackageDetailService = new PackageDetailService(getModel().getPackageInfoList());
    }

    @Override
    protected void initView() {
        /* ===============================================================
             package list arguments
         =================================================================*/
        // list status, enabled, disabled or all
        getModel().getStatusArg().bind(Bindings.createStringBinding(() ->
                        (String) toggleAppStatus.getSelectedToggle().getUserData(),
                toggleAppStatus.selectedToggleProperty()));
        //list type, system app, third party app or all
        getModel().getTypeArg().bind(Bindings.createStringBinding(() ->
                        (String) toggleAppType.getSelectedToggle().getUserData(),
                toggleAppType.selectedToggleProperty()));

        /* ===============================================================
             package list
         =================================================================*/
        //show newly added packages and remove old ones
        tablePackageList.itemsProperty().bind(getModel().getPackageInfoList());
        tablePackageList.itemsProperty().addListener((observable, oldValue, newValue) -> tablePackageList.sort());


        //refresh package list
        menuRefreshList.setOnAction(event -> {
            getModel().getPackageInfoList().clear();
            mPackageListService.restart(getModel().getStatusArg().get(), getModel().getTypeArg().get());
        });

        //change placeholder text
        textRefreshList.textProperty().bind(Bindings.createStringBinding(() ->
                        mPackageDetailService.isRunning() ? "Loading all packages... " :
                                "Found no packages, right click to refresh list",
                mPackageDetailService.runningProperty()));

        /* ===============================================================
             package detail
         =================================================================*/
        //when get package list is done, load package detail.
        mPackageListService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                mPackageDetailService.restart(newValue);
            }
        });

         /* ===============================================================
             package item operation
         =================================================================*/
        barPackagesActions.disableProperty().bind(
                Bindings.isNull(tablePackageList.getSelectionModel().selectedItemProperty()));
    }

    @Override
    protected void initData() {

    }
}
