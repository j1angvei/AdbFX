package cn.j1angvei.adbfx.adb;

import cn.j1angvei.adbfx.actionbar.ActionBarModel;
import cn.j1angvei.adbfx.functions.apps.PackageInfo;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class PackageManager {
    private static final PackageManager INSTANCE = new PackageManager();

    private final Set<PackageInfo> mDetailedPackages;

    private AndroidDebugBridge.IClientChangeListener mClientChangeListener;

    private PackageManager() {
        mDetailedPackages = new HashSet<>();
        mClientChangeListener = (client, changeMask) -> log.debug("Client:{};device:{}", client.getClientData().getPackageName(), client.getDevice().getSerialNumber());
    }

    public static PackageManager getInstance() {
        return INSTANCE;
    }

    public static IDevice getChosenDevice() {
        return ActionBarModel.getInstance().getChosenDevice().get();
    }

    public boolean searchDetailedPackage(PackageInfo info) {
        for (PackageInfo packageInfo : mDetailedPackages) {
            if (packageInfo.equals(info)) {
                info = packageInfo;
                return true;
            }
        }
        return false;
    }

    public void addDetailedPackage(PackageInfo info) {
        mDetailedPackages.remove(info);
        mDetailedPackages.add(info);
    }


    public void registerListener() {
        AndroidDebugBridge.addClientChangeListener(mClientChangeListener);
    }

    public void unregisterListener() {
        AndroidDebugBridge.removeClientChangeListener(mClientChangeListener);
    }


}
