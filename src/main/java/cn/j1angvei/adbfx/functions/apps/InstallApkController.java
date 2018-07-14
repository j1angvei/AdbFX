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
    public Button btnAddApk;
    public Button btnClearInput;
    public Button btnStartInstall;
    public ListView<String> listApksToInstall;

    public CheckBox checkLock;
    public CheckBox checkReplace;
    public CheckBox checkTest;
    public CheckBox checkSdcard;
    public CheckBox checkDebug;
    public CheckBox checkPermission;
    public ProgressBar progressInstall;
    public TextArea textResult;
    public ComboBox<String> comboApkHistory;
    public Button btnClearHistory;

    private CheckBox[] checkBoxes;

    private InstallApkService mInstallApkService;

    @Override
    protected InstallApkModel initModel() {
        return new InstallApkModel();
    }

    @Override
    protected void initArguments() {
        checkBoxes = new CheckBox[]{checkLock, checkReplace, checkTest, checkSdcard, checkDebug, checkPermission};
        mInstallApkService = new InstallApkService(getModel());
    }

    @Override
    protected void initView() {
        //listen for arg checkbox change
        Stream.of(checkBoxes)
                .forEach(checkBox -> checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    String arg = (String) checkBox.getUserData();
                    if (newValue) {
                        getModel().getInstallArgs().add(arg);
                    } else {
                        getModel().getInstallArgs().remove(arg);
                    }
                }));
        //init args
        Stream.of(checkBoxes)
                .forEach(checkBox -> {
                    if (checkBox.isSelected()) {
                        String arg = (String) checkBox.getUserData();
                        getModel().getInstallArgs().add(arg);
                    }
                });

        /* ********************************************************
            Apk history
         ********************************************************* */
        comboApkHistory.itemsProperty().bind(getModel().getHistoryApks());
        btnClearHistory.setOnAction(event -> getModel().getHistoryApks().clear());
        comboApkHistory.setOnAction(event -> {
            String apkPath = comboApkHistory.getValue();
            if (apkPath != null) {
                getModel().addInputApk(apkPath);
            }
        });
        /* ********************************************************
            Apk input
         ********************************************************* */
        //apk paths to install
        listApksToInstall.itemsProperty().bindBidirectional(getModel().getApksToInstall());
        FileManager.loadByDragDrop(listApksToInstall, getModel().getApksToInstall(), FileManager.Extension.APK);

        btnAddApk.setOnAction(event -> {
            List<File> addedFiles = FileManager.getInstance().loadFilesByExplorer("Choose *.apk files to install",
                    FileManager.Extension.APK);
            addedFiles.forEach(file -> getModel().addInputApk(file.getAbsolutePath()));
        });
        btnClearInput.setOnAction(event -> getModel().getApksToInstall().clear());

        BooleanBinding disableInstall = Bindings.createBooleanBinding(() ->
                        mInstallApkService.isRunning() || getModel().getApksToInstall().isEmpty(),
                mInstallApkService.runningProperty(), getModel().getApksToInstall().emptyProperty());
        btnStartInstall.disableProperty().bind(disableInstall);
        btnStartInstall.setOnAction(event -> mInstallApkService.restart());

        //run install service
        btnStartInstall.disableProperty().bind(mInstallApkService.runningProperty());
        progressInstall.visibleProperty().bind(mInstallApkService.runningProperty());
        progressInstall.progressProperty().bind(mInstallApkService.progressProperty());

        //result
        textResult.textProperty().bind(mInstallApkService.valueProperty());
    }

    @Override
    protected void initData() {

    }
}
