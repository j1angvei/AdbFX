package cn.j1angvei.adbfx.functions.device;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public void setImage(File file) {
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }
}
