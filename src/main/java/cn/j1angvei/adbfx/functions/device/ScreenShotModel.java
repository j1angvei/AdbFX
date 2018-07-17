package cn.j1angvei.adbfx.functions.device;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import lombok.Getter;

import java.io.File;

@Getter
public class ScreenShotModel {
    private final ListProperty<File> savedImages;
    private final IntegerProperty currentIndex;

    public ScreenShotModel() {
        savedImages = new SimpleListProperty<>(FXCollections.observableArrayList());
        currentIndex = new SimpleIntegerProperty(0);
    }
}
