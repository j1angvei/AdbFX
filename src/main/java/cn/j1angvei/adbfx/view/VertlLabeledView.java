package cn.j1angvei.adbfx.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VertlLabeledView extends VBox {

    public VertlLabeledView(String text, Node... children) {
        setSpacing(4.0f);
        Label label = new Label(text);

        setAlignment(Pos.TOP_LEFT);
        getChildren().add(label);
        getChildren().addAll(children);
    }
}
