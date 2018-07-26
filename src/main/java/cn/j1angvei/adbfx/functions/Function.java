package cn.j1angvei.adbfx.functions;

public enum Function {
    /**
     * install apk
     */
    INSTALL_APK(
            "function_install_apk",
            "/img/install_apk.png",
            "/function/InstallApk.fxml"),

    /**
     * uninstall apk
     */
    UNINSTALL_PKG(
            "function_uninstall_app",
            "/img/uninstall_app.png",
            "/function/UninstallApp.fxml"),
    /**
     * parse apk
     */
    PARSE_APK(
            "function_parse_apk",
            "/img/test.png",
            "/function/ParseApk.fxml"),

    /**
     * package list
     */
    PKG_LIST(
            "function_pkg_list",
            "/img/package_list.png",
            "/function/PackageList.fxml"),


    /**
     * pull file
     */
    PULL_FILES(
            "function_pull_file",
            "/img/pull_from_device.png",
            "/function/PullFile.fxml"),

    /**
     * push file
     */
    PUSH_FILES(
            "function_push_file",
            "/img/push_to_device.png",
            "/function/PushFile.fxml"),

    /**
     * file manager
     */
    FILE_MGR(
            "function_file_mgr",
            "/img/test.png",
            "/function/InstallApk.fxml"),

    /**
     * screen shot
     */
    SCREEN_SHOT(
            "function_screen_shot",
            "/img/screen_shot.png",
            "/function/ScreenShot.fxml"),
    /**
     * screen recording
     */
    SCREEN_RECORDING(
            "function_screen_record",
            "/img/screen_record.png",
            "/function/ScreenRecord.fxml"),

    /**
     * device info
     */
    DEVICE_INFO(
            "function_device_info",
            "/img/test.png",
            "/function/InstallApk.fxml"),

    /**
     * log cat
     */
    LOGCAT(
            "function_logcat",
            "/img/test.png",
            "/function/InstallApk.fxml"),

    /**
     * device information
     */
    EMULATE_TOUCH(
            "function_emulate_touch",
            "/img/test.png",
            "/function/InstallApk.fxml"),
    /**
     * start component(activity,intent,service,broadcast)
     */
    START_COMPONENT(
            "function_start_component",
            "/img/test.png",
            "/function/InstallApk.fxml"),
    /**
     * flash device(activity, intent, service, broadcast)
     */
    FLASH_DEVICE(
            "function_flash_device",
            "/img/test.png",
            "/function/InstallApk.fxml"),
    /**
     * execute script(activity, intent,service,broadcast)
     */
    RUN_SCRIPT(
            "function_run_script",
            "/img/test.png",
            "/function/InstallApk.fxml");


    public final String title;
    public final String icon;
    public final String ui;


    Function(String title, String icon, String ui) {
        this.title = title;
        this.icon = icon;
        this.ui = ui;
    }

    public enum Category {
        APPS,
        FILES,
        DEVICE,
        DEBUG,
        ADVANCED
    }
}
