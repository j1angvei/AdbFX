package cn.j1angvei.adbfx.functions.device;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import lombok.Getter;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

@Getter
public class ScreenRecordModel {
    private final ListProperty<File> recordedVideos;
    private final ObjectProperty<File> localPath;

    public ScreenRecordModel() {
        recordedVideos = new SimpleListProperty<>(FXCollections.observableArrayList());
        localPath = new SimpleObjectProperty<>(SystemUtils.getUserHome());
    }
}
