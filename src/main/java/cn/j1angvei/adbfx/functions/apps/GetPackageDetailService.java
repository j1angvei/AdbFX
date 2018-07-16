package cn.j1angvei.adbfx.functions.apps;


import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GetPackageDetailService extends Service<PackageInfo> {
    @Override
    protected Task<PackageInfo> createTask() {
        return null;
    }

    private class GetPackageDetailTask extends Task<PackageInfo> {
        @Override
        protected PackageInfo call() {
            return null;
        }
    }
}
