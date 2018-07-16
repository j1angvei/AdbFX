package cn.j1angvei.adbfx.adb;

import cn.j1angvei.adbfx.actionbar.ActionBarModel;
import cn.j1angvei.adbfx.functions.apps.PackageInfo;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import javafx.beans.property.SetProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Slf4j
public class PackageManager {

    private PackageManager() {
    }

    /**
     * get all packages installed in chosen device
     *
     * @param args get package command's arguments
     * @return package list
     */
    public static GetListService getList(SetProperty<String> args) {
        return new GetListService(args);
    }

    /**
     * get package detail
     *
     * @return package detail
     */
    public static GetDetailService getDetail() {
        return new GetDetailService();
    }

    private static IDevice getChosenDevice() {
        return ActionBarModel.getInstance().getChosenDevice().get();
    }


    public static class GetListService extends Service<List<PackageInfo>> {

        private static final String PREFIX = "package:";

        private final SetProperty<String> arguments;

        GetListService(SetProperty<String> arguments) {
            this.arguments = arguments;
        }

        @Override
        protected Task<List<PackageInfo>> createTask() {
            return new Task<List<PackageInfo>>() {
                @Override
                protected List<PackageInfo> call() throws Exception {
                    List<PackageInfo> packageList = new ArrayList<>();

                    StringBuilder cmdBuilder = new StringBuilder("pm list packages");
                    arguments.forEach(s -> cmdBuilder.append(" ").append(s));
                    log.debug("get list packages cmd:{}", cmdBuilder.toString());
                    IDevice device = getChosenDevice();
                    try {
                        device.executeShellCommand(cmdBuilder.toString(), new MultiLineReceiver() {
                            @Override
                            public void processNewLines(String[] lines) {
                                Stream.of(lines)
                                        .filter(s -> s != null && s.startsWith(PREFIX))
                                        .forEach(e -> {
                                            String pkg = e.substring(PREFIX.length());
                                            PackageInfo packageInfo = new PackageInfo(pkg, device.getSerialNumber());
                                            packageList.add(packageInfo);
                                        });
                            }

                            @Override
                            public boolean isCancelled() {
                                return false;
                            }
                        });
                    } catch (IOException e) {
                        log.error("Error when get package list ", e);
                    }
                    return packageList;
                }
            };

        }
    }

    public static class GetDetailService extends Service<PackageInfo> {
        private static final String PREFIX_VERSION_NAME = "versionName=";
        private static final String PREFIX_VERSION_CODE = "versionCode=";
        private static final String PREFIX_TARGET_SDK = "targetSdk=";
        private static final String PREFIX_FIRST_INSTALL_TIME = "firstInstallTime=";
        private static final String PREFIX_LAST_UPDATE_TIME = "lastUpdateTime=";//
        private static final String PERMISSION_BLOCK_START = "grantedPermissions:";
        private static final String PREFIX_PERMISSION = "android.permission.";

        private PackageInfo packageInfo;

        public void restart(PackageInfo packageInfo) {
            this.packageInfo = packageInfo;
            this.restart();
        }

        @Override
        protected Task<PackageInfo> createTask() {
            if (packageInfo == null) {
                throw new NullPointerException("param packageInfo is NULL ");
            }

            return new Task<PackageInfo>() {
                @Override
                protected PackageInfo call() throws Exception {

                    String cmd = String.format("pm dump %s", packageInfo.getPackageName());
                    try {
                        getChosenDevice().executeShellCommand(cmd, new MultiLineReceiver() {
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
                    } catch (IOException e) {
                        log.error("Error when get package detail for {},", packageInfo.getPackageName(), e);
                    }

                    PackageInfo result = packageInfo;
                    packageInfo = null;
                    return result;
                }
            };
        }
    }

}
