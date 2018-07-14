package cn.j1angvei.adbfx;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController<M> implements Initializable {
    private M mModel;
    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        mModel = initModel();
        initArguments();
        initView();
        initData();
    }

    protected abstract M initModel();

    protected abstract void initArguments();


    protected abstract void initView();

    protected abstract void initData();


    public final ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public final M getmModel() {
        return mModel;
    }
}
