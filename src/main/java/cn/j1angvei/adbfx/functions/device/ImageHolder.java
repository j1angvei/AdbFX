package cn.j1angvei.adbfx.functions.device;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * @author j1angvei
 * @since 18/7/17
 */
@Slf4j
public class ImageHolder extends ScrollPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Group group;
    @FXML
    private ImageView imageView;
    @FXML
    private Slider slider;

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

    public ImageHolder(File file) {
        this();
        setImage(file);
    }

    private boolean mSliderInit;
    private double mRealWidth;
    private double mRealHeight;

    public void setImage(File file) {
        Image image = new Image(file.toURI().toString());
        mRealWidth = image.getWidth();
        mRealHeight = image.getHeight();

        imageView.setImage(image);

        imageView.fitWidthProperty().bind(slider.valueProperty().multiply(mRealWidth));

        scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds bounds) {
                if (!mSliderInit) {
                    mSliderInit = true;

                    double viewPortWidth = bounds.getWidth();
                    double viewPortHeight = bounds.getHeight();

                    double widthRatio = viewPortWidth / mRealWidth;
                    double heightRatio = viewPortHeight / mRealHeight;
                    double initRatio = Math.min(widthRatio, heightRatio);

                    log.debug("real:{},{};viewPort:{},{};initRatio:{}",
                            mRealWidth, mRealHeight, viewPortWidth, viewPortHeight, initRatio);
                    slider.setValue(initRatio);


                }
            }
        });
    }
}
