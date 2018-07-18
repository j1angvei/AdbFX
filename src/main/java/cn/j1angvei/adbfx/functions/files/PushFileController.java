package cn.j1angvei.adbfx.functions.files;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.FileManager;
import cn.j1angvei.adbfx.adb.PushFileService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.util.List;

public class PushFileController extends BaseController<PushFileModel> {

    @FXML
    private ListView<File> listFiles;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnPush;
    @FXML
    private TextField fieldDestination;
    @FXML
    private ListView<String> listSubDir;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private TextArea areaOutput;

    private PushFileService mPushFileService;


    @Override
    protected PushFileModel initModel() {
        return new PushFileModel();
    }

    @Override
    protected void initArguments() {
        mPushFileService = new PushFileService();
    }

    @Override
    protected void initView() {

        /* *******************************************
            Manage file list
         ******************************************* */
        listFiles.itemsProperty().bind(getModel().getFilesToPush());
        //add from explorer
        btnAdd.setOnAction(event -> {
            List<File> addedFiles = FileManager.getInstance().loadFilesByExplorer("Choose  files to push",
                    FileManager.Extension.ALL);
            if (addedFiles != null && !addedFiles.isEmpty()) {
                getModel().getFilesToPush().addAll(addedFiles);
            }
        });
        //drag to add files
        FileManager.loadFilesByDragDrop(listFiles, getModel().getFilesToPush(), FileManager.Extension.ALL);
        //clear list
        btnClear.setOnAction(event -> getModel().getFilesToPush().clear());
        btnClear.disableProperty().bind(getModel().getFilesToPush().emptyProperty());
        //start push
        btnPush.setOnAction(event -> mPushFileService.restart(getChosenDevice(), getModel().getFilesToPush(), fieldDestination.getText()));
        btnPush.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        mPushFileService.isRunning() || getModel().getFilesToPush().isEmpty(),
                mPushFileService.runningProperty(), getModel().getFilesToPush().emptyProperty()));
        /* *******************************************
           set push destination
         ******************************************* */


        /* *******************************************
           Push file result
         ******************************************* */
        areaOutput.textProperty().bind(mPushFileService.valueProperty());
        progressIndicator.visibleProperty().bind(mPushFileService.runningProperty());
        progressIndicator.progressProperty().bind(mPushFileService.progressProperty());

    }

    @Override
    protected void initData() {

    }
}
