package cn.j1angvei.adbfx.functions.apps;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.InstallReceiver;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * @author j1angvei
 * @since 18/7/14
 */
@Slf4j
public class InstallApkService extends Service<String> {

    private InstallApkModel mInstallApkModel;

    public InstallApkService(InstallApkModel installApkModel) {
        mInstallApkModel = installApkModel;
    }

    @Override
    protected Task<String> createTask() {
        return new InstallApkTask();
    }

    private class InstallApkTask extends Task<String> {
        @Override
        protected String call() throws Exception {
            IDevice device = mInstallApkModel.getChosenDevice().get();
            if (device == null) {
                log.error("No device is chosen to install apk", new NullPointerException("device is null"));
                return null;
            }

            List<String> apks = mInstallApkModel.getApksToInstall();
            int count = apks.size();

            Set<String> args = mInstallApkModel.getInstallArgs();
            String[] argsArray = args.toArray(new String[0]);

            InstallReceiver receiver = new InstallReceiver();
            StringBuilder builder = new StringBuilder();

            log.debug("device,{};apks,{};args:{}", device, apks, args);
            for (int i = 0; i < apks.size(); i++) {
                String path = apks.get(i);
                updateProgress(i, count);
                log.debug("Start:{},path,{}", i, path);
                device.installPackage(path, false, receiver, argsArray);
                builder.append(i)
                        .append(". ")
                        .append(path)
                        .append(":\n");
                if (receiver.isSuccessfullyCompleted()) {
                    builder.append("Success");
                } else {
                    builder.append(receiver.getErrorMessage());
                }
                builder.append("\n");
            }
            updateProgress(count, count);
            Thread.sleep(500);
            return builder.toString();
        }
    }
}
