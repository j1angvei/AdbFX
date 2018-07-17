package cn.j1angvei.adbfx.adb;

import cn.j1angvei.adbfx.functions.apps.PackageInfo;
import com.android.ddmlib.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
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

    private final List<PackageInfo> mPackageInfoList;

    public PackageDetailService(List<PackageInfo> packageInfos) {
        mPackageInfoList = packageInfos;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                IDevice device = PackageManager.getChosenDevice();

                mPackageInfoList.stream().filter(new Predicate<PackageInfo>() {
                    @Override
                    public boolean test(PackageInfo packageInfo) {
                        log.debug("Before search:{}", packageInfo);
                        boolean result = !PackageManager.getInstance().searchDetailedPackage(packageInfo);
                        log.debug("After search:{}", packageInfo);

                        return result;
                    }
                }).forEach(packageInfo -> {
                    String cmd = String.format("pm dump %s", packageInfo.getPackageName());
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
                                        if (partSize > 1) {
                                            packageInfo.setTargetSdk(parts[partSize - 1].substring(PREFIX_TARGET_SDK.length()));
                                        }
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

                        PackageManager.getInstance().addDetailedPackage(packageInfo);
                    } catch (AdbCommandRejectedException | ShellCommandUnresponsiveException | TimeoutException | IOException e) {
                        log.error("Error when get package detail for {},", packageInfo.getPackageName(), e);
                    }

                    log.debug("Detail result{}", packageInfo);

                });
                return null;
            }
        };
    }
}
