package cn.j1angvei.adbfx.adb;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FileOperateService extends Service<String> {
    @Override
    protected Task<String> createTask() {
        return new Task<String>() {
            @Override
            protected String call() {
                return null;
            }
        };
    }

    public enum Operation {
        LS,
        MV,
        CP,
        REDIR,
        CHOWN,
        CHMOD
    }

}
