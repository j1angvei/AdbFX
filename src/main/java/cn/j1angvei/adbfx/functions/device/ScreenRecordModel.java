package cn.j1angvei.adbfx.functions.device;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

public class ScreenRecordModel {
    private final ListProperty<File> recordedVideos;
    private final ObjectProperty<File> localPath;

    public ScreenRecordModel() {
        recordedVideos = new SimpleListProperty<>(FXCollections.observableArrayList());
        localPath = new SimpleObjectProperty<>(SystemUtils.getUserHome());
    }

    public ObservableList<File> getRecordedVideos() {
        return recordedVideos.get();
    }

    public void setRecordedVideos(ObservableList<File> recordedVideos) {
        this.recordedVideos.set(recordedVideos);
    }

    public ListProperty<File> recordedVideosProperty() {
        return recordedVideos;
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

}