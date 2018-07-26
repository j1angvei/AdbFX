package cn.j1angvei.adbfx.functions.device;

import cn.j1angvei.adbfx.FileManager;
import cn.j1angvei.adbfx.NodeManager;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.ResourceBundle;

/**
 * @author j1angvei
 * @since 18/7/17
 */
public abstract class ImageHolder extends ScrollPane {
    private ObjectProperty<File> imgFile;
    private DoubleProperty scale;

    public ImageHolder() {
        imgFile = new SimpleObjectProperty<>();
        scale = new SimpleDoubleProperty();

        setFitToHeight(true);
        setFitToWidth(true);
        initContextMenu();

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);
        Group group = new Group(imageView);
        StackPane stackPane = new StackPane(group);
        setContent(stackPane);

        imgFile.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Image image = new Image(newValue.toURI().toString());
                double realWidth = image.getWidth();
                imageView.setImage(image);
                imageView.fitWidthProperty().bind(scale.multiply(realWidth));
            }
        });
    }

    public ImageHolder(File file) {
        this();
        imgFile.set(file);
    }

    private void initContextMenu() {
        ResourceBundle resourceBundle = NodeManager.defaultResources();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem openFile = new MenuItem(resourceBundle.getString("open_file"));
        openFile.setOnAction(event -> FileManager.openFile(getImgFile()));

        MenuItem openDir = new MenuItem(resourceBundle.getString("open_folder"));
        openDir.setOnAction(event -> FileManager.openFile(getImgFile().getParentFile()));

        MenuItem delete = new MenuItem(resourceBundle.getString("delete"));
        delete.setOnAction(event -> {
            FileManager.deleteFile(imgFile.get());
            onDelete();
        });

        contextMenu.getItems().addAll(openFile, openDir, delete);

        setContextMenu(contextMenu);
    }

    public abstract void onDelete();

    public File getImgFile() {
        return imgFile.get();
    }

    public void setImgFile(File imgFile) {
        this.imgFile.set(imgFile);
    }

    public ObjectProperty<File> imgFileProperty() {
        return imgFile;
    }

    public double getScale() {
        return scale.get();
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }

    public DoubleProperty scaleProperty() {
        return scale;
    }
}
