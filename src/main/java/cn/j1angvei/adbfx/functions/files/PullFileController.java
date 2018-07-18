package cn.j1angvei.adbfx.functions.files;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.FileManager;
import cn.j1angvei.adbfx.adb.FileInfo;
import cn.j1angvei.adbfx.adb.FileInfoService;
import cn.j1angvei.adbfx.adb.PullFileService;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.Callable;

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

    private static void setFileInfoCellFactory(ListView<FileInfo> list, boolean isDir) {
        list.setCellFactory(param -> {
            ListCell<FileInfo> cell = new ListCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                FileInfo fileInfo = cell.getItem();
                if (fileInfo != null) {
                    return isDir ? fileInfo.getName() :
                            String.format("%s\t[%s]", fileInfo.getName(), fileInfo.getTime());
                } else {
                    return null;
                }
            }, cell.itemProperty()));
            return cell;
        });
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
            File chosenDir = FileManager.getInstance().chooseDirectory(
                    "Choose directory to save pulled files",
                    getModel().getLocalPath());
            if (chosenDir != null) {
                getModel().setLocalPath(chosenDir);
            }
        });

        /* ************************************************************************
           Select android device folder path
         ************************************************************************ */
        menuCurrentRemotePath.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() {
                return getModel().getChosenFileInfo().getFullPath();
            }
        }, getModel().chosenFileInfoProperty()));
        btnUpperPath.setOnAction(event -> {
            FileInfo parent = getModel().getChosenFileInfo().getParent();
            if (parent != null) {
                getModel().setChosenFileInfo(parent);
            }
        });
        btnUpperPath.disableProperty().bind(Bindings.equal(FileInfo.SDCARD, getModel().chosenFileInfoProperty()));
        btnRefresh.setOnAction(event -> mFileInfoService.restart(getChosenDevice(), getModel().getChosenFileInfo()));
        //chosen dir changes
        setFileInfoCellFactory(listSubPaths, true);
        setFileInfoCellFactory(listSubFiles, false);
        mFileInfoService.runningProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                listSubPaths.getItems().setAll(getModel().getChosenFileInfo().getSubDir());
                listSubFiles.getItems().setAll(getModel().getChosenFileInfo().getSubFiles());
            }
        });
        // change chosen device path
        listSubPaths.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                getModel().setChosenFileInfo(newValue);
            }
        });

        getModel().chosenFileInfoProperty().addListener(new ChangeListener<FileInfo>() {
            @Override
            public void changed(ObservableValue<? extends FileInfo> observable, FileInfo oldValue, FileInfo newValue) {
                mFileInfoService.restart(getChosenDevice(), newValue);
            }
        });
        /* ************************************************************************
            sub files under chosen path
         ************************************************************************ */
        //sub files
        listSubFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        btnSelectAll.setOnAction(event -> listSubFiles.getSelectionModel().selectAll());
        btnClearSelection.setOnAction(event -> listSubFiles.getSelectionModel().clearSelection());

        /* ************************************************************************
            sub files under chosen path
         ************************************************************************ */
        btnPullFiles.setOnAction(event -> mPullFileService.restart(getChosenDevice(), null));

        /* ************************************************************************
           pull file result
         ************************************************************************ */

    }

    @Override
    protected void initData() {


    }
}
