package cn.j1angvei.adbfx.functions.files;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import lombok.Getter;

import java.io.File;

@Getter
public class PushFileModel {
    private final ListProperty<File> filesToPush;

    public PushFileModel() {
        filesToPush = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
}
