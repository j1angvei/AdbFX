package cn.j1angvei.adbfx.functions;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

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
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            Image image = new Image(item.icon);
            imageView.setImage(image);
            setGraphic(imageView);
            setText(mResourceBundle.getString(item.title));
            Insets insets = new Insets(8, 4, 8, 4);
            setPadding(insets);
            setFont(new Font(16));
        }
    }
}
