package cn.j1angvei.adbfx.functions.apps;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.InstallReceiver;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class UninstallAppService extends Service<String> {
    private UninstallAppModel mUninstallAppModel;

    public UninstallAppService(UninstallAppModel uninstallAppModel) {
        mUninstallAppModel = uninstallAppModel;
    }

    @Override
    protected Task<String> createTask() {
        return new UninstallAppTask();
    }

    private class UninstallAppTask extends Task<String> {
        @Override
        protected String call() throws Exception {
            IDevice device = mUninstallAppModel.getChosenDevice().get();
            String packageName = mUninstallAppModel.getPackageName().get();
            boolean keepData = mUninstallAppModel.getKeepData().get();


            StringBuilder builder = new StringBuilder();
            builder.append(String.format("Ready to uninstall %s from %s\n", packageName, device.getSerialNumber()));

            try {
                String command = String.format("pm uninstall %s %s", keepData ? "-k" : "", packageName);
                log.debug("Uninstall shell command,{}", command);

                InstallReceiver receiver = new InstallReceiver();
                device.executeShellCommand(command, receiver);

                if (receiver.getErrorMessage() == null) {
                    builder.append("Success");
                } else {
                    builder.append(String.format("Failure [%s]", receiver.getErrorMessage()));
                }
            } catch (IOException e) {
                builder.append(String.format("Failure - [%s]", e.getMessage()));
                log.error("Error when uninstall {}", packageName, e);
            }
            builder.append("\n");
            return builder.toString();
        }
    }
}
