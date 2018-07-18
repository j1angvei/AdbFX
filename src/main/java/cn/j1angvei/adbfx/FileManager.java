package cn.j1angvei.adbfx;

import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FileManager {
    private static FileManager ourInstance = new FileManager();
    private Stage mStage;
    private FileChooser mFileChooser = new FileChooser();
    private File mLastDir;

    private FileManager() {
    }

    public static FileManager getInstance() {
        return ourInstance;
    }

    public static void loadPathsByDragDrop(Node node, List<String> observableList, Extension extension) {
        startDrag(node);

        node.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();

            dragboard.getFiles().forEach(file -> {
                String path = file.getAbsolutePath();
                if (FilenameUtils.wildcardMatch(file.getName(), extension.wildcard) && !observableList.contains(path)) {
                    observableList.add(path);
                }
            });

            event.setDropCompleted(dragboard.hasFiles());
            event.consume();

        });

    }

    public static void loadFilesByDragDrop(Node node, List<File> list, Extension extension) {
        startDrag(node);

        node.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();

            dragboard.getFiles().forEach(file -> {
                if (FilenameUtils.wildcardMatch(file.getName(), extension.wildcard) && !list.contains(file)) {
                    list.add(file);
                }
            });

            event.setDropCompleted(dragboard.hasFiles());
            event.consume();

        });
    }

    private static void startDrag(Node node) {
        node.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.LINK);
            } else {
                event.consume();
            }
        });
    }

    void init(Stage stage) {
        if (stage == null) {
            throw new NullPointerException("Can't set stage to null");
        }
        this.mStage = stage;
    }

    public List<File> loadFilesByExplorer(String description, Extension extension) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(description, extension.wildcard);
        mFileChooser.getExtensionFilters().add(filter);
        if (mLastDir != null) {
            mFileChooser.setInitialDirectory(mLastDir);
        }
        List<File> addedFiles = mFileChooser.showOpenMultipleDialog(mStage);
        if (addedFiles != null) {
            mLastDir = addedFiles.get(0).getParentFile();
            return addedFiles;
        }
        return Collections.emptyList();
    }

    public File chooseDirectory(String title, File initDir) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(initDir);
        return chooser.showDialog(mStage);

    }

    public enum Extension {
        APK("*.apk"),
        ALL("*.*");

        public final String wildcard;

        Extension(String wildcard) {
            this.wildcard = wildcard;
        }
    }
}
