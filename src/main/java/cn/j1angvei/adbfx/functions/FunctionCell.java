package cn.j1angvei.adbfx.functions;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;

/**
 * @author j1angvei
 * @since 18/7/19
 */
public class FunctionCell extends ListCell<Function> {

    private ResourceBundle mResourceBundle;

    public FunctionCell(ResourceBundle resourceBundle) {
        mResourceBundle = resourceBundle;
    }

    @Override
    protected void updateItem(Function item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setGraphic(null);
            setText("");
        } else {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(48);
            imageView.setFitHeight(48);
            Image image = new Image(item.icon);
            imageView.setImage(image);
            setGraphic(imageView);
            setText(item.title);
        }
    }
}
