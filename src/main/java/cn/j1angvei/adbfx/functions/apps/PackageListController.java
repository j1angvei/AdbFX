package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.adb.PackageDetailService;
import cn.j1angvei.adbfx.adb.PackageListService;
import cn.j1angvei.adbfx.adb.PackageOperationService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
public class PackageListController extends BaseController<PackageListModel> {

    public TableView<PackageInfo> tablePackageList;
    public Text textListHint;
    public Button btnRefreshList;

    //package list arguments
    public ToggleGroup toggleAppStatus;
    public ToggleGroup toggleAppType;

    //package operation

    public MenuButton menuBtnPermissions;
    public ListView<String> listPermissions;


    public MenuButton menuBtnHide;
    public MenuItem menuHide;
    public MenuItem menuUnhide;

    public Button btnApkPath;

    public Button btnClearData;

    //operation result
    public TitledPane titledResult;
    public TextArea areaResult;


    private PackageListService mPackageListService;
    private PackageDetailService mPackageDetailService;
    private PackageOperationService mPackageOperationService;


    @Override
    protected PackageListModel initModel() {
        return new PackageListModel();
    }

    @Override
    protected void initArguments() {
        mPackageListService = new PackageListService();
        mPackageDetailService = new PackageDetailService(getModel().getPackageInfoList());
        mPackageOperationService = new PackageOperationService();
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
        btnRefreshList.setOnAction(event -> {
            getModel().getPackageInfoList().clear();
            mPackageListService.restart(getModel().getStatusArg().get(), getModel().getTypeArg().get());
        });

        //change placeholder text
        textListHint.textProperty().bind(Bindings.createStringBinding(() ->
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
        Stream.of(menuBtnPermissions, btnApkPath, menuBtnHide, btnClearData)
                .forEach(buttonBase -> buttonBase.disableProperty().bind(
                        Bindings.isNull(tablePackageList.getSelectionModel().selectedItemProperty())));

        ReadOnlyObjectProperty<PackageInfo> selectedItem = tablePackageList.getSelectionModel().selectedItemProperty();
        selectedItem.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                listPermissions.getItems().setAll(newValue.getPermissions());
            }
        });
        btnApkPath.setOnAction(event -> {
            titledResult.setExpanded(true);
            mPackageOperationService.restart(PackageListController.this.getChosenDevice(),
                    selectedItem.get().getPackageName(), PackageOperationService.Operation.APK_PATH);
        });
        menuHide.setOnAction(event -> {
            titledResult.setExpanded(true);
            mPackageOperationService.restart(getChosenDevice(),
                    selectedItem.get().getPackageName(), PackageOperationService.Operation.HIDE);
        });
        menuUnhide.setOnAction(event -> {
            titledResult.setExpanded(true);
            mPackageOperationService.restart(getChosenDevice(),
                    selectedItem.get().getPackageName(), PackageOperationService.Operation.UNHIDE);
        });
        btnClearData.setOnAction(event -> {
            titledResult.setExpanded(true);
            mPackageOperationService.restart(getChosenDevice(),
                    selectedItem.get().getPackageName(), PackageOperationService.Operation.CLEAR_DATA);
        });
        areaResult.textProperty().bind(mPackageOperationService.valueProperty());

    }

    @Override
    protected void initData() {

    }
}
