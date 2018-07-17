package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.DeviceModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.Getter;

@Getter
public class PackageListModel extends DeviceModel {
    private final ListProperty<PackageInfo> packageList;
    private final SetProperty<String> listArguments;
    private final StringProperty statusArg;
    private final StringProperty typeArg;


    public PackageListModel() {
        packageList = new SimpleListProperty<>(FXCollections.observableArrayList());
        listArguments = new SimpleSetProperty<>(FXCollections.observableSet());
        statusArg = new SimpleStringProperty();
        typeArg = new SimpleStringProperty();

    }
}
