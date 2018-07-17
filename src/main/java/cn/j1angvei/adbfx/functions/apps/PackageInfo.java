package cn.j1angvei.adbfx.functions.apps;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of = {"packageName", "deviceSn"})
public class PackageInfo {
    private final StringProperty packageName;
    private final StringProperty versionCode; //versionCode
    private final StringProperty targetSdk; //targetSdk
    private final StringProperty versionName; //versionName
    private final StringProperty firstInstall; //firstInstallTime
    private final StringProperty lastUpdate; //lastUpdateTime
    private final ListProperty<String> permissions; //grantedPermissions
    private final StringProperty deviceSn;

    public PackageInfo(String packageName, String serialNumber) {
        this.packageName = new SimpleStringProperty(packageName);
        versionCode = new SimpleStringProperty();
        targetSdk = new SimpleStringProperty();
        versionName = new SimpleStringProperty();
        firstInstall = new SimpleStringProperty();
        lastUpdate = new SimpleStringProperty();
        permissions = new SimpleListProperty<>(FXCollections.observableArrayList());
        deviceSn = new SimpleStringProperty(serialNumber);
    }

    public String getPackageName() {
        return packageName.get();
    }

    public void setPackageName(String packageName) {
        this.packageName.set(packageName);
    }

    public StringProperty packageNameProperty() {
        return packageName;
    }

    public String getVersionCode() {
        return versionCode.get();
    }

    public void setVersionCode(String versionCode) {
        this.versionCode.set(versionCode);
    }

    public StringProperty versionCodeProperty() {
        return versionCode;
    }

    public String getTargetSdk() {
        return targetSdk.get();
    }

    public void setTargetSdk(String targetSdk) {
        this.targetSdk.set(targetSdk);
    }

    public StringProperty targetSdkProperty() {
        return targetSdk;
    }

    public String getVersionName() {
        return versionName.get();
    }

    public void setVersionName(String versionName) {
        this.versionName.set(versionName);
    }

    public StringProperty versionNameProperty() {
        return versionName;
    }

    public String getFirstInstall() {
        return firstInstall.get();
    }

    public void setFirstInstall(String firstInstall) {
        this.firstInstall.set(firstInstall);
    }

    public StringProperty firstInstallProperty() {
        return firstInstall;
    }

    public String getLastUpdate() {
        return lastUpdate.get();
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate.set(lastUpdate);
    }

    public StringProperty lastUpdateProperty() {
        return lastUpdate;
    }

    public ObservableList<String> getPermissions() {
        return permissions.get();
    }

    public void setPermissions(ObservableList<String> permissions) {
        this.permissions.set(permissions);
    }

    public ListProperty<String> permissionsProperty() {
        return permissions;
    }

    public String getDeviceSn() {
        return deviceSn.get();
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn.set(deviceSn);
    }

    public StringProperty deviceSnProperty() {
        return deviceSn;
    }
}
