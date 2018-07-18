package cn.j1angvei.adbfx.adb;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "fullPath")
public class FileInfo {
    public static final FileInfo SDCARD = new FileInfo(null, "sdcard", true);

    private final ObjectProperty<FileInfo> parent;
    private final StringProperty name;
    private final BooleanProperty isDirectory;
    private final ListProperty<FileInfo> subDir;
    private final ListProperty<FileInfo> subFiles;
    private final StringProperty fullPath;
    private final StringProperty time;

    public FileInfo(FileInfo parent, String name, boolean isDirectory) {
        this.parent = new SimpleObjectProperty<>(parent);
        this.name = new SimpleStringProperty(name);
        this.isDirectory = new SimpleBooleanProperty(isDirectory);

        String parentPath = parent == null ? "/" : parent.getFullPath();
        String selfPath = parentPath + name + (isDirectory ? "/" : "");
        this.fullPath = new SimpleStringProperty(selfPath);

        this.subDir = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.subFiles = new SimpleListProperty<>(FXCollections.observableArrayList());

        this.time = new SimpleStringProperty();
    }

    public final void updateSubDirs(FileInfo fileInfo) {
        if (!fileInfo.isIsDirectory()) {
            throw new IllegalArgumentException("Try to insert file to subDir list");
        }
        subDir.remove(fileInfo);
        subDir.add(fileInfo);
    }

    public final void updateSubFiles(FileInfo fileInfo) {
        if (fileInfo.isIsDirectory()) {
            throw new IllegalArgumentException("Try to insert directory to subFiles list");
        }
        subFiles.remove(fileInfo);
        subFiles.add(fileInfo);
    }


    public FileInfo getParent() {
        return parent.get();
    }

    public void setParent(FileInfo parent) {
        this.parent.set(parent);
    }

    public ObjectProperty<FileInfo> parentProperty() {
        return parent;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public boolean isIsDirectory() {
        return isDirectory.get();
    }

    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory.set(isDirectory);
    }

    public BooleanProperty isDirectoryProperty() {
        return isDirectory;
    }

    public ObservableList<FileInfo> getSubDir() {
        return subDir.get();
    }

    public void setSubDir(ObservableList<FileInfo> subDir) {
        this.subDir.set(subDir);
    }

    public ListProperty<FileInfo> subDirProperty() {
        return subDir;
    }

    public ObservableList<FileInfo> getSubFiles() {
        return subFiles.get();
    }

    public void setSubFiles(ObservableList<FileInfo> subFiles) {
        this.subFiles.set(subFiles);
    }

    public ListProperty<FileInfo> subFilesProperty() {
        return subFiles;
    }

    public String getFullPath() {
        return fullPath.get();
    }

    public void setFullPath(String fullPath) {
        this.fullPath.set(fullPath);
    }

    public StringProperty fullPathProperty() {
        return fullPath;
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public StringProperty timeProperty() {
        return time;
    }

    @Override
    public String toString() {
        return getFullPath();
    }
}
