package cn.j1angvei.adbfx.functions.device;

import cn.j1angvei.adbfx.FileManager;
import cn.j1angvei.adbfx.NodeManager;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author j1angvei
 * @since 18/7/17
 */
@Slf4j
public class ImageHolder extends ScrollPane {
    @FXML
    private ImageView imageView;
    @FXML
    private MenuItem menuOpenFile;
    @FXML
    private MenuItem menuOpenDir;
    //    @FXML
//    private MenuItem menuRename;
//    @FXML
//    private TextField fieldName;
    @FXML
    private MenuItem menuDelete;

    public ImageHolder() {
        NodeManager.loadCustomNode(this, "/ImageHolder.fxml");
    }

    public ImageHolder(File file, DoubleProperty scaleRatio, ListProperty<File> allImages) {
        this();

        Image image = new Image(file.toURI().toString());
        double realWidth = image.getWidth();

        imageView.setImage(image);
        imageView.fitWidthProperty().bind(scaleRatio.multiply(realWidth));

        menuOpenFile.setOnAction(event -> FileManager.openFile(file));
        menuOpenDir.setOnAction(event -> FileManager.openFile(file.getParentFile()));
        menuDelete.setOnAction(event -> {
            allImages.remove(file);
            FileManager.deleteFile(file);
        });

    }

    public void setImage(File file) {


//        scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
//            @Override
//            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds bounds) {
//                if (!mSliderInit) {
//                    mSliderInit = true;
//
//                    double viewPortWidth = bounds.getWidth();
//                    double viewPortHeight = bounds.getHeight();
//
//                    double widthRatio = viewPortWidth / mRealWidth;
//                    double heightRatio = viewPortHeight / mRealHeight;
//                    double initRatio = Math.min(widthRatio, heightRatio);
//
//                    log.debug("real:{},{};viewPort:{},{};initRatio:{}",
//                            mRealWidth, mRealHeight, viewPortWidth, viewPortHeight, initRatio);
//                    slider.setValue(initRatio);
//
//
//                }
//            }
//        });
    }
}
