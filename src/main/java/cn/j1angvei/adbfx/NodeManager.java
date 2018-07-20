package cn.j1angvei.adbfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
public class NodeManager {
    private static final NodeManager INSTANCE = new NodeManager();

    private Map<String, Node> mNodeMap;

    private NodeManager() {
        mNodeMap = new HashMap<>();
    }

    public static NodeManager getInstance() {
        return INSTANCE;
    }

    public Node loadFxml(@NonNull String path) {

        if (mNodeMap.containsKey(path)) {
            return mNodeMap.get(path);
        }
        try {
            Node node = FXMLLoader.load(AdbFxApp.class.getResource(path), ResourceBundle.getBundle("strings"));
            mNodeMap.put(path, node);
            return node;
        } catch (IOException e) {
            log.error("Error when load fxml {},{}", path, e);
        }
        return null;
    }
}
