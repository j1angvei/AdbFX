package cn.j1angvei.adbfx;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {
    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        initArguments();
        initView();
        initData();
    }

    protected abstract void initArguments();


    protected abstract void initView();

    protected abstract void initData();


    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
