package cn.j1angvei.adbfx.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HorzLabeledView extends HBox {
    private StringProperty labelText;

    public HorzLabeledView() {
        labelText = new SimpleStringProperty();
        Label label = new Label();
        label.textProperty().bind(labelText);
        getChildren().add(label);
    }

    public HorzLabeledView(double spacing) {
        super(spacing);
    }

    public HorzLabeledView(Node... children) {
        super(children);
    }

    public HorzLabeledView(double spacing, Node... children) {
        super(spacing, children);
    }

    public String getLabelText() {
        return labelText.get();
    }

    public void setLabelText(String labelText) {
        this.labelText.set(labelText);
    }

    public StringProperty labelTextProperty() {
        return labelText;
    }
}
