package cn.j1angvei.adbfx;

import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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

    public static void loadByDragDrop(Node node, List<File> observableList, Extension extension) {
        node.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.LINK);
            } else {
                event.consume();
            }
        });

        node.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();

            dragboard.getFiles().forEach(file -> {
                if (FilenameUtils.wildcardMatch(file.getName(), extension.wildcard) && !observableList.contains(file)) {
                    observableList.add(file);
                }
            });

            event.setDropCompleted(dragboard.hasFiles());
            event.consume();

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

    public enum Extension {
        APK("*.apk"),
        ALL("*.*");

        public final String wildcard;

        Extension(String wildcard) {
            this.wildcard = wildcard;
        }
    }
}
