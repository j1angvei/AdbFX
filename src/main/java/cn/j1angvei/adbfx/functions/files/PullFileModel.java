package cn.j1angvei.adbfx.functions.files;

import cn.j1angvei.adbfx.adb.FileInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

public class PullFileModel {
    private final ObjectProperty<File> localPath;
    private final ObjectProperty<FileInfo> chosenFileInfo;

    public PullFileModel() {
        localPath = new SimpleObjectProperty<>(SystemUtils.getUserHome());
        chosenFileInfo = new SimpleObjectProperty<>(FileInfo.SDCARD);
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
}
