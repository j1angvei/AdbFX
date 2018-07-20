package cn.j1angvei.adbfx.functions;

public enum Function {
    /**
     * install apk
     */
    INSTALL_APK(
            "function_install_apk",
            "/img/install_apk.png",
            "/function/InstallApk.fxml",
            Category.APPS),

    /**
     * uninstall apk
     */
    UNINSTALL_PKG(
            "function_uninstall_app",
            "/img/uninstall_app.png",
            "/function/UninstallApp.fxml",
            Category.APPS),
//    /**
//     * parse apk
//     */
//    PARSE_APK(
//            "parse_apk",
//            "/img/test.png",
//            "/function/ParseApk.fxml",
//            Category.APPS),

    /**
     * package list
     */
    PKG_LIST(
            "function_pkg_list",
            "/img/package_list.png",
            "/function/PackageList.fxml",
            Category.APPS),


    /**
     * pull file
     */
    PULL_FILES(
            "function_pull_file",
            "/img/pull_from_device.png",
            "/function/PullFile.fxml",
            Category.FILES),

    /**
     * push file
     */
    PUSH_FILES(
            "function_push_file",
            "/img/push_to_device.png",
            "/function/PushFile.fxml",
            Category.FILES),

//    /**
//     * file manager
//     */
//    FILE_MGR(
//            "pkg_list",
//            "/img/test.png",
//            "/function/InstallApk.fxml",
//            Category.FILES),

    /**
     * screen shot
     */
    SCREEN_SHOT(
            "function_screen_shot",
            "/img/screen_shot.png",
            "/function/ScreenShot.fxml",
            Category.DEVICE),
    /**
     * screen recording
     */
    SCREEN_RECORDING(
            "function_screen_record",
            "/img/screen_record.png",
            "/function/ScreenRecord.fxml",
            Category.DEVICE),

//    /**
//     * device info
//     */
//    DEVICE_INFO(
//            "screen_recording",
//            "/img/test.png",
//            "/function/InstallApk.fxml",
//            Category.DEVICE),
//
//    /**
//     * log cat
//     */
//    LOGCAT(
//            "screen_recording",
//            "/img/test.png",
//            "/function/InstallApk.fxml",
//            Category.DEBUG),
//
//    /**
//     * device information
//     */
//    EMULATE_TOUCH(
//            "screen_recording",
//            "/img/test.png",
//            "/function/InstallApk.fxml",
//            Category.DEBUG),
//    /**
//     * start component(activity,intent,service,broadcast)
//     */
//    START_COMPONENT(
//            "screen_recording",
//            "/img/test.png",
//            "/function/InstallApk.fxml",
//            Category.DEBUG),
//    /**
//     * flash device(activity, intent, service, broadcast)
//     */
//    FLASH_DEVICE(
//            "screen_recording",
//            "/img/test.png",
//            "/function/InstallApk.fxml",
//            Category.ADVANCED),
//    /**
//     * execute script(activity, intent,service,broadcast)
//     */
//    RUN_SCRIPT(
//            "screen_recording",
//            "/img/test.png",
//            "/function/InstallApk.fxml",
//            Category.ADVANCED)

    ;


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
