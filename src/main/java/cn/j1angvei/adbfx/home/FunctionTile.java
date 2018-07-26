package cn.j1angvei.adbfx.home;

import cn.j1angvei.adbfx.NodeManager;
import cn.j1angvei.adbfx.functions.Function;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.util.ResourceBundle;

@Slf4j
public class FunctionTile extends Button {


    public FunctionTile(Function function) {
        setStyle("-fx-background-color: transparent");
        ResourceBundle resourceBundle = NodeManager.defaultResources();
        setText(resourceBundle.getString(function.title));
        Image image = new Image(function.icon);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(72);
        imageView.setFitHeight(72);
        imageView.setPreserveRatio(true);
        setGraphic(imageView);
        setContentDisplay(ContentDisplay.TOP);
        setPrefSize(128, 128);
    }

    public FunctionTile() {
    }

    public FunctionTile(String text) {
        super(text);
    }

    public FunctionTile(String text, Node graphic) {
        super(text, graphic);
    }

}
