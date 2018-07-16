package cn.j1angvei.adbfx.adb.pm;

import cn.j1angvei.adbfx.functions.apps.PackageInfo;
import com.android.ddmlib.MultiLineReceiver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * task to get package detail
 */
public class PackageDetailTask extends PackageManager<PackageInfo> {

    private static final String PREFIX_VERSION_NAME = "versionName=";
    private static final String PREFIX_VERSION_CODE = "versionCode=";
    private static final String PREFIX_TARGET_SDK = "targetSdk=";
    private static final String PREFIX_FIRST_INSTALL_TIME = "firstInstallTime=";
    private static final String PREFIX_LAST_UPDATE_TIME = "lastUpdateTime=";//
    private static final String PERMISSION_BLOCK_START = "grantedPermissions:";
    private static final String PREFIX_PERMISSION = "android.permission.";


    private String packageToGetDetail;

    public void setPackageToGetDetail(String packageToGetDetail) {
        this.packageToGetDetail = packageToGetDetail;
    }

    @Override
    protected PackageInfo call() throws Exception {
        if (packageToGetDetail == null) {
            throw new NullPointerException("Error when get package detail, for package name is NULL");
        }
        PackageInfo packageInfo = new PackageInfo(packageToGetDetail);

        String cmd = String.format("pm dump %s", packageToGetDetail);

        getChosenDevice().executeShellCommand(cmd, new MultiLineReceiver() {
            @Override
            public void processNewLines(String[] lines) {
                AtomicBoolean inPermissionBlock = new AtomicBoolean(false);
                Stream.of(lines).forEach(s -> {
                    if (s.startsWith(PREFIX_VERSION_NAME)) {
                        packageInfo.setVersionName(s.substring(PREFIX_VERSION_NAME.length()));
                    } else if (s.startsWith(PREFIX_VERSION_CODE)) {
                        String[] parts = s.split("\\s+");
                        packageInfo.setVersionCode(parts[0].substring(PREFIX_VERSION_CODE.length()));
                        packageInfo.setTargetSdk(parts[1].substring(PREFIX_TARGET_SDK.length()));
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

        return packageInfo;
    }
}
