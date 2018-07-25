package cn.j1angvei.adbfx.home;

import cn.j1angvei.adbfx.NodeManager;
import cn.j1angvei.adbfx.functions.Function;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;

public class FunctionTile extends Button {

    @FXML
    private ImageView imgIcon;

    public FunctionTile(Function function) {
        FXMLLoader fxmlLoader = NodeManager.loadCustomNode(this, "/FunctionTile.fxml");
        ResourceBundle resourceBundle = fxmlLoader.getResources();
        setText(resourceBundle.getString(function.title));
        Image image = new Image(function.icon);
        imgIcon.setImage(image);
    }
}
