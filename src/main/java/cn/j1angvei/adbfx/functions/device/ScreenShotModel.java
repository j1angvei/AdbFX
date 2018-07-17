package cn.j1angvei.adbfx.functions.device;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import lombok.Getter;

import java.io.File;

@Getter
public class ScreenShotModel {
    private final ListProperty<File> savedImages;

    public ScreenShotModel() {
        savedImages = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
}
