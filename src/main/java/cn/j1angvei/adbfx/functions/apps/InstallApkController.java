package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.FileManager;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
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
    //    public ComboBox<String> comboApkHistory;
//    public Button btnClearHistory;
    public ListView<String> listInstallHistory;
    public MenuItem menuClearHistory;
    public TitledPane titledResult;

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
        /* ********************************************************
            install arguments
         ********************************************************* */
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
        listInstallHistory.itemsProperty().bind(getModel().getHistoryApks());
        listInstallHistory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getModel().addInputApk(newValue);
            }
        });
        menuClearHistory.setOnAction(event -> getModel().getHistoryApks().clear());

        /* ********************************************************
            Apk input
         ********************************************************* */
        listApksToInstall.itemsProperty().bindBidirectional(getModel().getApksToInstall());
        FileManager.loadPathsByDragDrop(listApksToInstall, getModel().getApksToInstall(), FileManager.Extension.APK);

        btnAddApk.setOnAction(event -> {
            List<File> addedFiles = FileManager.getInstance().loadFilesByExplorer("Choose *.apk files to install",
                    FileManager.Extension.APK);
            addedFiles.forEach(file -> getModel().addInputApk(file.getAbsolutePath()));
        });
        btnClearInput.setOnAction(event -> getModel().getApksToInstall().clear());

        btnStartInstall.disableProperty().bind(Bindings.createBooleanBinding(() -> mInstallApkService.isRunning() ||
                        getModel().getApksToInstall().isEmpty(),
                mInstallApkService.runningProperty(), getModel().getApksToInstall().emptyProperty()));
        btnStartInstall.setOnAction(event -> mInstallApkService.restart());

        //execute install service
        progressInstall.visibleProperty().bind(mInstallApkService.runningProperty());
        getModel().getApksToInstall().sizeProperty().addListener((observable, oldValue, newValue) -> {
            int size = newValue.intValue();
            if (size == 1) {
                //show indeterminate progress
                progressInstall.progressProperty().unbind();
                progressInstall.setProgress(-1.0);
            } else {
                //show detail progress
                progressInstall.progressProperty().bind(mInstallApkService.progressProperty());
            }
        });

        //result
        textResult.textProperty().bind(mInstallApkService.valueProperty());
        titledResult.visibleProperty().bind(Bindings.not(mInstallApkService.runningProperty()));
    }

    @Override
    protected void initData() {
        log.debug("progress bar init progress:{}", progressInstall.getProgress());
    }
}
