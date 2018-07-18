package cn.j1angvei.adbfx.functions.files;

import cn.j1angvei.adbfx.adb.FileInfo;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

public class PullFileModel {
    private final ObjectProperty<File> localPath;
    private final ObjectProperty<FileInfo> chosenFileInfo;
    private final ListProperty<FileInfo> selectedFiles;

    public PullFileModel() {
        localPath = new SimpleObjectProperty<>(SystemUtils.getUserHome());
        chosenFileInfo = new SimpleObjectProperty<>(FileInfo.SDCARD);
        selectedFiles = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public File getLocalPath() {
        return localPath.get();
    }

    public void setLocalPath(File localPath) {
        this.localPath.set(localPath);
    }

    public ObjectProperty<File> localPathProperty() {
        return localPath;
    }

    public FileInfo getChosenFileInfo() {
        return chosenFileInfo.get();
    }

    public void setChosenFileInfo(FileInfo chosenFileInfo) {
        this.chosenFileInfo.set(chosenFileInfo);
    }

    public ObjectProperty<FileInfo> chosenFileInfoProperty() {
        return chosenFileInfo;
    }

    public ObservableList<FileInfo> getSelectedFiles() {
        return selectedFiles.get();
    }

    public void setSelectedFiles(ObservableList<FileInfo> selectedFiles) {
        this.selectedFiles.set(selectedFiles);
    }

    public ListProperty<FileInfo> selectedFilesProperty() {
        return selectedFiles;
    }
}
