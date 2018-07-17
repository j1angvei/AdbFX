package cn.j1angvei.adbfx.adb;

import cn.j1angvei.adbfx.functions.apps.PackageInfo;
import com.android.ddmlib.*;
import javafx.beans.property.ListProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Slf4j
public class PackageDetailService extends Service<Void> {

    private static final String PREFIX_VERSION_NAME = "versionName=";
    private static final String PREFIX_VERSION_CODE = "versionCode=";
    private static final String PREFIX_TARGET_SDK = "targetSdk=";
    private static final String PREFIX_FIRST_INSTALL_TIME = "firstInstallTime=";
    private static final String PREFIX_LAST_UPDATE_TIME = "lastUpdateTime=";//
    private static final String PERMISSION_BLOCK_START = "grantedPermissions:";
    private static final String PREFIX_PERMISSION = "android.permission.";


    private final ListProperty<PackageInfo> mPackageInfoContainer;
    private List<String> mPackageList;

    public PackageDetailService(ListProperty<PackageInfo> container) {
        mPackageInfoContainer = container;
    }

    public void restart(List<String> pkgList) {
        mPackageList = pkgList;
        restart();
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                if (mPackageList == null) {
                    NullPointerException exception = new NullPointerException("Package list is NULL");
                    log.error("Error when get package detail", exception);
                }

                mPackageInfoContainer.clear();

                IDevice device = PackageManager.getChosenDevice();

                mPackageList.forEach(packageName -> {

                    PackageInfo cached = PackageManager.getInstance().loadFromCache(packageName, device.getSerialNumber());
                    if (cached != null) {
                        mPackageInfoContainer.add(cached);
                        return;
                    }

                    PackageInfo packageInfo = new PackageInfo(packageName, device.getSerialNumber());

                    String cmd = String.format("pm dump %s", packageName);

                    log.debug("Get package detail:{}", cmd);

                    try {
                        device.executeShellCommand(cmd, new MultiLineReceiver() {
                            @Override
                            public void processNewLines(String[] lines) {
                                AtomicBoolean inPermissionBlock = new AtomicBoolean(false);
                                Stream.of(lines).forEach(s -> {
                                    if (s.startsWith(PREFIX_VERSION_NAME)) {
                                        packageInfo.setVersionName(s.substring(PREFIX_VERSION_NAME.length()));
                                    } else if (s.startsWith(PREFIX_VERSION_CODE)) {
                                        String[] parts = s.split("\\s+");
                                        int partSize = parts.length;
                                        packageInfo.setVersionCode(parts[0].substring(PREFIX_VERSION_CODE.length()));
                                        packageInfo.setTargetSdk(parts[partSize - 1].substring(PREFIX_TARGET_SDK.length()));
                                    } else if (s.startsWith(PREFIX_FIRST_INSTALL_TIME)) {
                                        packageInfo.setFirstInstall(s.substring(PREFIX_FIRST_INSTALL_TIME.length()));
                                    } else if (s.startsWith(PREFIX_LAST_UPDATE_TIME)) {
                                        packageInfo.setLastUpdate(s.substring(PREFIX_LAST_UPDATE_TIME.length()));
                                    } else if (s.startsWith(PERMISSION_BLOCK_START)) {
                                        inPermissionBlock.set(true);
                                    } else if (inPermissionBlock.get()) {
                                        if (s.startsWith(PREFIX_PERMISSION)) {
                                            packageInfo.getPermissions().add(s);
                                        } else {
                                            inPermissionBlock.set(false);
                                        }
                                    }
                                });
                            }

                            @Override
                            public boolean isCancelled() {
                                return false;
                            }
                        });

                    } catch (AdbCommandRejectedException | ShellCommandUnresponsiveException | TimeoutException | IOException e) {
                        log.error("Error when get package detail for {},", packageInfo.getPackageName(), e);
                    }
                    mPackageInfoContainer.add(packageInfo);
                    PackageManager.getInstance().storeToCache(packageInfo);
                });

                return null;
            }
        };
    }
}
