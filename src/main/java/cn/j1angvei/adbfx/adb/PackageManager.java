package cn.j1angvei.adbfx.adb;

import cn.j1angvei.adbfx.actionbar.ActionBarModel;
import cn.j1angvei.adbfx.functions.apps.PackageInfo;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class PackageManager {
    private static final PackageManager INSTANCE = new PackageManager();

    private final Map<String, PackageInfo> mDetailedPackages;

    private AndroidDebugBridge.IClientChangeListener mClientChangeListener;

    private PackageManager() {
        mDetailedPackages = new HashMap<>();
        mClientChangeListener = new AndroidDebugBridge.IClientChangeListener() {
            @Override
            public void clientChanged(Client client, int changeMask) {
                if (Client.CHANGE_INFO == changeMask) {
                    log.debug("Client:{};device:{}", client.getClientData().getPackageName(), client.getDevice().getSerialNumber());
                }
            }
        };
    }

    public static PackageManager getInstance() {
        return INSTANCE;
    }

    public static IDevice getChosenDevice() {
        return ActionBarModel.getInstance().getChosenDevice().get();
    }

    public PackageInfo loadFromCache(String packageName, String deviceSn) {
        return mDetailedPackages.get(packageName + deviceSn);
    }

    public void storeToCache(PackageInfo info) {
        mDetailedPackages.put(info.getPackageName() + info.getDeviceSn(), info);
    }


    public void registerListener() {
        AndroidDebugBridge.addClientChangeListener(mClientChangeListener);
    }

    public void unregisterListener() {
        AndroidDebugBridge.removeClientChangeListener(mClientChangeListener);
    }

}
