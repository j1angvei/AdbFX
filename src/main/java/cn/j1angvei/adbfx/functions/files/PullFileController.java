package cn.j1angvei.adbfx.functions.files;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.FileManager;
import cn.j1angvei.adbfx.adb.FileInfo;
import cn.j1angvei.adbfx.adb.FileInfoService;
import cn.j1angvei.adbfx.adb.PullFileService;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class PullFileController extends BaseController<PullFileModel> {

    public TextField fieldLocalPath;
    public Button btnChooseLocalPath;

    public MenuButton menuCurrentRemotePath;
    public Button btnUpperPath;
    public Button btnRefresh;

    public ListView<FileInfo> listSubPaths;

    public ListView<FileInfo> listSubFiles;
    public Button btnSelectAll;
    public Button btnClearSelection;
    public Button btnPullFiles;

    public ProgressIndicator progressIndicator;

    public TextArea areaOutput;


    private FileInfoService mFileInfoService;
    private PullFileService mPullFileService;

    @Override
    protected PullFileModel initModel() {
        return new PullFileModel();
    }

    @Override
    protected void initArguments() {

        mFileInfoService = new FileInfoService();
        mPullFileService = new PullFileService();
    }

    @Override
    protected void initView() {
        /* ************************************************************************
            Configure local path to save files
         ************************************************************************ */
        //show local path text
        fieldLocalPath.textProperty().bind(Bindings.createStringBinding(() ->
                getModel().getLocalPath().getAbsolutePath(), getModel().localPathProperty()));
        //choose new local path
        btnChooseLocalPath.setOnAction(event -> {
            File chosenDir = FileManager.getInstance().chooseDirectory("Choose directory to save pulled files", getModel().getLocalPath());
            if (chosenDir != null) {
                getModel().setLocalPath(chosenDir);
            }
        });

        /* ************************************************************************
           Select android device folder path
         ************************************************************************ */
        //show current chosen file path
        log.debug("init :{}", getModel().getChosenFileInfo());
        menuCurrentRemotePath.textProperty().bind(Bindings.createStringBinding(() -> {
            FileInfo fileInfo = getModel().getChosenFileInfo();
            log.debug("Chosen file:{}", fileInfo);
            return fileInfo == null ? "Choose Directory" : fileInfo.getFullPath();
        }, getModel().chosenFileInfoProperty()));

        btnUpperPath.setOnAction(event -> {
            FileInfo parent = getModel().getChosenFileInfo().getParent();
            if (parent != null) {
                getModel().setChosenFileInfo(parent);
            }
        });
        btnUpperPath.disableProperty().bind(Bindings.equal(FileInfo.SDCARD, getModel().chosenFileInfoProperty()));
        btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mFileInfoService.restart(getChosenDevice(), getModel().getChosenFileInfo());
            }
        });
        //when no path is chosen, add sdcard path to sub dir list
        getModel().chosenFileInfoProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                listSubPaths.getItems().add(FileInfo.SDCARD);
            }
        });
        //sub dirs
        listSubPaths.itemsProperty().bind(getModel().getChosenFileInfo().subDirProperty());
        listSubPaths.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FileInfo>() {
            @Override
            public void changed(ObservableValue<? extends FileInfo> observable, FileInfo oldValue, FileInfo newValue) {
                if (newValue != null) {
                    getModel().setChosenFileInfo(newValue);
                }
            }
        });


//        /* ************************************************************************
//            sub files under chosen path
//         ************************************************************************ */
//        //sub files
//        listSubFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        listSubFiles.itemsProperty().bind(getModel().getChosenFileInfo().subFilesProperty());
//        btnSelectAll.setOnAction(event -> listSubFiles.getSelectionModel().selectAll());
//        btnClearSelection.setOnAction(event -> listSubFiles.getSelectionModel().clearSelection());
//
//        /* ************************************************************************
//            sub files under chosen path
//         ************************************************************************ */
//        btnPullFiles.setOnAction(event -> mPullFileService.restart(getChosenDevice(), null));
//
//        /* ************************************************************************
//           pull file result
//         ************************************************************************ */

    }

    @Override
    protected void initData() {


    }
}
