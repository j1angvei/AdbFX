package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.adb.pm.PackageDetailTask;
import cn.j1angvei.adbfx.adb.pm.PackageListTask;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableView;

public class PackageListController extends BaseController<PackageListModel> {
    public TableView<PackageInfo> tablePackageList;
    public MenuItem menuRefreshList;

    public SplitMenuButton menuGetApk;

    private PackageListTask mPackageListTask;

    private PackageDetailTask mPackageDetailTaskTask;

    @Override
    protected PackageListModel initModel() {
        return new PackageListModel();
    }

    @Override
    protected void initArguments() {
        mPackageListTask = new PackageListTask(getModel().getArguments());

        mPackageDetailTaskTask = new PackageDetailTask();
    }

    @Override
    protected void initView() {
        /* ==========================================================================================================
            render package list
         ============================================================================================================ */

        mPackageListTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            getModel().getPackageList().clear();
            newValue.forEach(s -> getModel().getPackageList().add(new PackageInfo(s)));
        });

        menuRefreshList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new Thread(mPackageListTask).start();
            }
        });

        tablePackageList.itemsProperty().bind(getModel().getPackageList());

        menuGetApk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mPackageDetailTaskTask.setPackageToGetDetail("cn.j1angvei.aiocrdemo");
                new Thread(mPackageDetailTaskTask).start();

            }
        });

    }

    @Override
    protected void initData() {

    }
}
