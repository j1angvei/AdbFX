package cn.j1angvei.adbfx.home;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.NodeManager;
import cn.j1angvei.adbfx.functions.Function;
import cn.j1angvei.adbfx.functions.FunctionCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;


public class HomeController extends BaseController<HomeModel> {
    public ListView<Function> listFunctions;
    public BorderPane borderMain;

    @Override
    protected HomeModel initModel() {
        return new HomeModel();
    }

    @Override
    protected void initArguments() {

        listFunctions.getItems().addAll(Function.values());
        listFunctions.setCellFactory(new Callback<ListView<Function>, ListCell<Function>>() {
            @Override
            public ListCell<Function> call(ListView<Function> param) {
                return new FunctionCell(getResourceBundle());
            }
        });
        listFunctions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Function>() {
            @Override
            public void changed(ObservableValue<? extends Function> observable, Function oldValue, Function newValue) {
                if (newValue != null) {

                }
            }
        });
        listFunctions.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Node node = NodeManager.getInstance().loadFxml(Function.values()[newValue.intValue()].ui);
                borderMain.setCenter(node);

            }
        });

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
