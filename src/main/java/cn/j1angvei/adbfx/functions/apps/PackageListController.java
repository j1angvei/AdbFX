package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.adb.PackageManager;
import javafx.beans.binding.Bindings;
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
    private PackageManager.GetListService mGetListService;

    private PackageManager.GetDetailService mGetDetailService;

    @Override
    protected PackageListModel initModel() {
        return new PackageListModel();
    }

    @Override
    protected void initArguments() {
        mGetListService = PackageManager.getList(getModel().getArguments());
        mGetDetailService = PackageManager.getDetail();
    }

    @Override
    protected void initView() {
        /* ===============================================================
             package list
         =================================================================*/
        //show newly added packages and remove old ones
        mGetListService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            newValue.forEach(packageInfo -> tablePackageList.getItems().add(packageInfo));
            tablePackageList.sort();
        });
        //refresh package list
        try {
            menuRefreshList.setOnAction(event -> {
                tablePackageList.getItems().clear();
                mGetListService.restart();
            });
        } catch (Exception e) {
            log.error("Error when click menu refresh", e);
        }
        //change placeholder text
        textRefreshList.textProperty().bind(Bindings.createStringBinding(() ->
                        mGetListService.isRunning() ? "Loading all packages... " :
                                "Found no packages, right click to refresh list",
                mGetListService.runningProperty()));
        barPackagesActions.disableProperty().bind(
                Bindings.isNull(tablePackageList.getSelectionModel().selectedItemProperty()));
        /* ===============================================================
             package detail
         =================================================================*/

    }

    @Override
    protected void initData() {

    }
}
