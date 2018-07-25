package cn.j1angvei.adbfx.home;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.functions.Function;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;
import java.util.stream.Stream;


public class HomeController extends BaseController<HomeModel> {
    @FXML
    private BorderPane borderMain;
    @FXML
    private TabPane tabOpennedFunctions;
    @FXML
    private TilePane tileFunctionList;
    @FXML
    private VBox boxNoDevice;


    @Override
    protected HomeModel initModel() {
        return HomeModel.getInstance();
    }

    @Override
    protected void initArguments() {
        boxNoDevice.visibleProperty().bind(Bindings.isNull(getModel().getSelectedDevice()));
        tileFunctionList.visibleProperty().bind(getModel().getOpenedFunctions().emptyProperty());

        Stream.of(Function.values()).forEach(new Consumer<Function>() {
            @Override
            public void accept(Function function) {
                FunctionTile tile = new FunctionTile(function);
                tile.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        getModel().getOpenedFunctions().add(function);
                    }
                });
                tileFunctionList.getChildren().add(tile);
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
