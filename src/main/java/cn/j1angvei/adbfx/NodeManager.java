package cn.j1angvei.adbfx;

public class NodeManager {
    private static final NodeManager INSTANCE = new NodeManager();

    private NodeManager() {
    }

    public static NodeManager getInstance() {
        return INSTANCE;
    }
}
