package cn.j1angvei.adbfx.functions.apps;

import cn.j1angvei.adbfx.DeviceModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import lombok.Getter;

@Getter
public class PackageListModel extends DeviceModel {
    private final ListProperty<PackageInfo> packageList;
    private final ListProperty<String> arguments;

    private final SetProperty<PackageInfo> detailedPackageInfo;

    public PackageListModel() {
        packageList = new SimpleListProperty<>(FXCollections.observableArrayList());
        arguments = new SimpleListProperty<>(FXCollections.observableArrayList());

        detailedPackageInfo = new SimpleSetProperty<>(FXCollections.observableSet());
    }
}
