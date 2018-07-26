package cn.j1angvei.adbfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class NodeManager {
    private static final NodeManager INSTANCE = new NodeManager();

//    private Map<String, Node> mNodeMap;

    private NodeManager() {
//        mNodeMap = new HashMap<>();
    }

    public static NodeManager getInstance() {
        return INSTANCE;
    }

    public static FXMLLoader loadCustomNode(@NonNull Object nodeSelf, @NonNull String path, @NonNull ResourceBundle resourceBundle) {

        FXMLLoader fxmlLoader = new FXMLLoader(getResource(path));
        fxmlLoader.setResources(resourceBundle);
        fxmlLoader.setRoot(nodeSelf);
        fxmlLoader.setController(nodeSelf);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            log.error("Error when load custom node {}", path, e);
        }
        return fxmlLoader;
    }

    public static FXMLLoader loadCustomNode(@NonNull Object nodeSelf, @NonNull String path) {
        return loadCustomNode(nodeSelf, path, defaultResources());
    }

    private static URL getResource(@NonNull String path) {
        return AdbFxApp.class.getResource(path);
    }

    public static ResourceBundle defaultResources() {
        return ResourceBundle.getBundle("strings", Locale.getDefault());
    }

    public static Node loadFxml(@NonNull String path) {

//        if (mNodeMap.containsKey(path)) {
//            return mNodeMap.get(path);
//        }
        try {
            //            mNodeMap.put(path, node);
            return FXMLLoader.load(getResource(path), defaultResources());
        } catch (IOException e) {
            log.error("Error when load fxml {},{}", path, e);
        }
        return null;
    }

}
