package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.adb.PackageDetailService;
import cn.j1angvei.adbfx.adb.PackageListService;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PackageListController extends BaseController<PackageListModel> {

    public TableView<PackageInfo> tablePackageList;
    public MenuItem menuRefreshList;
    public Text textRefreshList;
    public ButtonBar barPackagesActions;

    public SplitMenuButton menuGetApk;

    private PackageListService mPackageListService;
    private PackageDetailService mPackageDetailService;


    @Override
    protected PackageListModel initModel() {
        return new PackageListModel();
    }

    @Override
    protected void initArguments() {
        mPackageListService = new PackageListService(getModel().getArguments());
        mPackageDetailService = new PackageDetailService(getModel().getPackageList());
    }

    @Override
    protected void initView() {
        /* ===============================================================
             package list
         =================================================================*/
        //show newly added packages and remove old ones
        tablePackageList.itemsProperty().bind(getModel().getPackageList());
        tablePackageList.itemsProperty().addListener((observable, oldValue, newValue) -> tablePackageList.sort());
        mPackageListService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            getModel().getPackageList().setAll(newValue);
        });
        //refresh package list
        menuRefreshList.setOnAction(event -> {
            getModel().getPackageList().clear();
            mPackageListService.restart();
        });

        //change placeholder text
        textRefreshList.textProperty().bind(Bindings.createStringBinding(() ->
                        mPackageListService.isRunning() ? "Loading all packages... " :
                                "Found no packages, right click to refresh list",
                mPackageListService.runningProperty()));

        /* ===============================================================
             package detail
         =================================================================*/
        //when get package list is done, load package detail.
        getModel().getPackageList().addListener(new ListChangeListener<PackageInfo>() {
            @Override
            public void onChanged(Change<? extends PackageInfo> c) {
                if (c.next() && c.wasAdded()) {
                    mPackageDetailService.restart();
                }
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
