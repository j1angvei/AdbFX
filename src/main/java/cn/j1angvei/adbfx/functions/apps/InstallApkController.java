package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.FileManager;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.*;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class InstallApkController extends BaseController<InstallApkModel> {
    public Button btnLocal;
    public Button btnHistory;
    public Button btnClear;
    public Button btnStart;
    public ListView<File> listApksToInstall;

    public CheckBox checkLock;
    public CheckBox checkReplace;
    public CheckBox checkTest;
    public CheckBox checkSdcard;
    public CheckBox checkDebug;
    public CheckBox checkPermission;
    public ProgressBar progressInstall;
    public TextArea textResult;

    private CheckBox[] checkBoxes;

    private InstallApkService mInstallApkService;

    @Override
    protected InstallApkModel initModel() {
        return new InstallApkModel();
    }

    @Override
    protected void initArguments() {
        checkBoxes = new CheckBox[]{checkLock, checkReplace, checkTest, checkSdcard, checkDebug, checkPermission};
        mInstallApkService = new InstallApkService(getmModel());
    }

    @Override
    protected void initView() {
        //listen for arg checkbox change
        Stream.of(checkBoxes)
                .forEach(checkBox -> checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String arg = (String) checkBox.getUserData();
                    if (newValue) {
                        getmModel().getInstallArgs().add(arg);
                    } else {
                        getmModel().getInstallArgs().remove(arg);
                    }
                }));
        //init args
        Stream.of(checkBoxes)
                .forEach(checkBox -> {
                    if (checkBox.isSelected()) {
                        String arg = (String) checkBox.getUserData();
                        getmModel().getInstallArgs().add(arg);
                    }
                });

        //apk paths to install
        listApksToInstall.itemsProperty().bindBidirectional(getmModel().getApksToInstall());
        FileManager.loadByDragDrop(listApksToInstall, getmModel().getApksToInstall(), FileManager.Extension.APK);

        btnLocal.setOnAction(event -> {
            List<File> addedFiles = FileManager.getInstance().loadFilesByExplorer("Choose *.apk files to install",
                    FileManager.Extension.APK);
            getmModel().addApkList(addedFiles);
        });
        btnClear.setOnAction(event -> getmModel().getApksToInstall().clear());

        BooleanBinding disableInstall = Bindings.createBooleanBinding(() ->
                        mInstallApkService.isRunning() || getmModel().getApksToInstall().isEmpty(),
                mInstallApkService.runningProperty(), getmModel().getApksToInstall().emptyProperty());
        btnStart.disableProperty().bind(disableInstall);
        btnStart.setOnAction(event -> mInstallApkService.restart());

        //run install service
        btnStart.disableProperty().bind(mInstallApkService.runningProperty());
        progressInstall.visibleProperty().bind(mInstallApkService.runningProperty());
        progressInstall.progressProperty().bind(mInstallApkService.progressProperty());
        //result
        textResult.textProperty().bind(mInstallApkService.valueProperty());

    }

    @Override
    protected void initData() {

    }
}
