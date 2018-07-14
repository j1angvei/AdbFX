package cn.j1angvei.adbfx.functions;

public enum Function {
    /**
     * install apk
     */
    INSTALL_APK(
            "install_apk",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.APPS),

    /**
     * uninstall apk
     */
    UNINSTALL_PKG(
            "uninstall_app",
            "/img/test.png",
            "/function/UninstallApp.fxml",
            Category.APPS),
    /**
     * parse apk
     */
    PARSE_APK(
            "parse_apk",
            "/img/test.png",
            "/function/ParseApk.fxml",
            Category.APPS),

    /**
     * package list
     */
    PKG_LIST(
            "pkg_list",
            "/img/test.png",
            "/function/AllApps.fxml",
            Category.APPS),


    /**
     * pull file
     */
    PULL_FILES(
            "pkg_list",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.FILES),

    /**
     * push file
     */
    PUSH_FILES(
            "pkg_list",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.FILES),

    /**
     * file manager
     */
    FILE_MGR(
            "pkg_list",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.FILES),

    /**
     * screen shot
     */
    SCREEN_SHOT(
            "screen_shot",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEVICE),
    /**
     * screen recording
     */
    SCREEN_RECORDING(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEVICE),

    /**
     * device info
     */
    DEVICE_INFO(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEVICE),

    /**
     * log cat
     */
    LOGCAT(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEBUG),

    /**
     * device information
     */
    EMULATE_TOUCH(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEBUG),
    /**
     * start component(activity,intent,service,broadcast)
     */
    START_COMPONENT(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEBUG),
    /**
     * flash device(activity, intent, service, broadcast)
     */
    FLASH_DEVICE(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.ADVANCED),
    /**
     * run script(activity, intent,service,broadcast)
     */
    RUN_SCRIPT(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.ADVANCED);


    public final String title;
    public final String icon;
    public final String ui;
    public final Category category;


    Function(String title, String icon, String ui, Category category) {
        this.title = title;
        this.icon = icon;
        this.ui = ui;
        this.category = category;
    }

    public enum Category {
        APPS,
        FILES,
        DEVICE,
        DEBUG,
        ADVANCED
    }
}
