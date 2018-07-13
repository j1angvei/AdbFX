package cn.j1angvei.adbfx.functions;

public enum Function {
    /**
     * 安装APK
     */
    INSTALL_APK(
            "install_apk",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.APPS),

    /**
     * 卸载应用
     */
    UNINSTALL_PKG(
            "uninstall_app",
            "/img/test.png",
            "/function/UninstallApp.fxml",
            Category.APPS),
    /**
     * 解析APK
     */
    PARSE_APK(
            "parse_apk",
            "/img/test.png",
            "/function/ParseApk.fxml",
            Category.APPS),

    /**
     * 包列表
     */
    PKG_LIST(
            "pkg_list",
            "/img/test.png",
            "/function/AllApps.fxml",
            Category.APPS),


    /**
     * 提取文件
     */
    PULL_FILES(
            "pkg_list",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.FILES),

    /**
     * 推送文件
     */
    PUSH_FILES(
            "pkg_list",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.FILES),

    /**
     * 文件管理
     */
    FILE_MGR(
            "pkg_list",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.FILES),

    /**
     * 屏幕截图
     */
    SCREEN_SHOT(
            "screen_shot",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEVICE),
    /**
     * 屏幕录制
     */
    SCREEN_RECORDING(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEVICE),

    /**
     * 设备信息
     */
    DEVICE_INFO(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEVICE),

    /**
     * 设备信息
     */
    LOGCAT(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEBUG),

    /**
     * 设备信息
     */
    EMULATE_TOUCH(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEBUG),
    /**
     * 启动组件（activity,intent,service,broadcast）
     */
    START_COMPONENT(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.DEBUG),
    /**
     * 刷机相关（activity,intent,service,broadcast）
     */
    FLASH_DEVICE(
            "screen_recording",
            "/img/test.png",
            "/function/InstallApk.fxml",
            Category.ADVANCED),
    /**
     * 运行脚本（activity,intent,service,broadcast）
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
