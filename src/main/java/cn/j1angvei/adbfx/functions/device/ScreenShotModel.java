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
public class ScreenShotModel {
    private final ListProperty<File> savedImages;

    private final ObjectProperty<File> saveDir;

    public ScreenShotModel() {
        savedImages = new SimpleListProperty<>(FXCollections.observableArrayList());

        saveDir = new SimpleObjectProperty<>(SystemUtils.getUserHome());
    }
}
