package cn.j1angvei.adbfx.functions.apps;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import lombok.Getter;

@Getter
public class PackageListModel {
    private final ListProperty<PackageInfo> packageInfoList;
    private final SetProperty<String> listArguments;
    private final StringProperty statusArg;
    private final StringProperty typeArg;


    public PackageListModel() {
        packageInfoList = new SimpleListProperty<>(FXCollections.observableArrayList());
        listArguments = new SimpleSetProperty<>(FXCollections.observableSet());
        statusArg = new SimpleStringProperty();
        typeArg = new SimpleStringProperty();

    }
}
