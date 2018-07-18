package cn.j1angvei.adbfx.functions.device;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

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
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ImageHolder.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public ImageHolder(File file, DoubleProperty scaleRatio, ListProperty<File> allImages) {
        this();

        Image image = new Image(file.toURI().toString());
        double realWidth = image.getWidth();

        imageView.setImage(image);
        imageView.fitWidthProperty().bind(scaleRatio.multiply(realWidth));

        menuOpenFile.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuOpenDir.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(file.getParentFile().toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuDelete.setOnAction(event -> {
            allImages.remove(file);
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
