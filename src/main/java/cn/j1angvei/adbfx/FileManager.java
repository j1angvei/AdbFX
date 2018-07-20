package cn.j1angvei.adbfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
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

    public static Node loadFxml(@NonNull String path) {
        try {
            return FXMLLoader.load(AdbFxApp.class.getResource(path), ResourceBundle.getBundle("strings"));
        } catch (IOException e) {
            log.error("Error when load fxml {},{}", path, e);
        }
        return null;
    }

    public static String getStrings(String key) {
        return ResourceBundle.getBundle("strings").getString(key);
    }

    public static void openFile(@NonNull File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            log.error("Error when open file {},{}", file, e);
        }
    }

    public static void deleteFile(@NonNull File file) {
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            log.error("Error when delete file{}, {}", file, e);
        }
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

//    public static void simpleUnzip(String zipFilePath, String destDir) {
//        try {
//            ZipFile zipFile = new ZipFile(zipFilePath);
//            zipFile.extractAll(destDir);
//        } catch (ZipException e) {
//            log.error("Error when unzip file {} to {}", zipFilePath, destDir, e);
//        }
//    }
//
//    public static void unzip(String zipFilePath, String destDir) {
//        File targetDir = new File(destDir);
//
//        try (ZipArchiveInputStream i = new ZipArchiveInputStream(new FileInputStream(zipFilePath))) {
//            ZipArchiveEntry entry = null;
//            while ((entry = i.getNextZipEntry()) != null) {
//                if (!i.canReadEntryData(entry)) {
//                    // log something?
//                    continue;
//                }
//                String name = targetDir.getAbsolutePath() + File.separator + entry.getName();
//                File f = new File(name);
//                if (entry.isDirectory()) {
//                    if (!f.isDirectory() && !f.mkdirs()) {
//                        throw new IOException("failed to create directory " + f);
//                    }
//                } else {
//                    File parent = f.getParentFile();
//                    entry.setUnixMode();
//                    if (!parent.isDirectory() && !parent.mkdirs()) {
//                        throw new IOException("failed to create directory " + parent);
//                    }
//                    try (OutputStream o = Files.newOutputStream(f.toPath())) {
//                        IOUtils.copy(i, o);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public enum Extension {
        APK("*.apk"),
        ALL("*.*");

        public final String wildcard;

        Extension(String wildcard) {
            this.wildcard = wildcard;
        }
    }
}
