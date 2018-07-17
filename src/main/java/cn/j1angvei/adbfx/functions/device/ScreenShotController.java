package cn.j1angvei.adbfx.functions.device;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.adb.ScreenShotService;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.apache.commons.lang3.SystemUtils;

public class ScreenShotController extends BaseController<ScreenShotModel> {
    public CheckBox checkLandscape;
    public Button btnTakeScreenShot;
    public Pagination paginationImages;
    public TextField fieldSaveDir;
    public Button btnChooseDir;
    public Button btnOpenImageDir;

    private ScreenShotService mScreenShotService;

//    private double fitWidth, fitHeight;

    @Override
    protected ScreenShotModel initModel() {
        return new ScreenShotModel();
    }

    @Override
    protected void initArguments() {
        mScreenShotService = new ScreenShotService();
        fieldSaveDir.setText(SystemUtils.getUserHome().getAbsolutePath());

    }

    @Override
    protected void initView() {
        /* **********************************************************
              Take screenShot
         ********************************************************** */
        btnTakeScreenShot.setOnAction(event -> mScreenShotService.restart(
                getChosenDevice(), fieldSaveDir.getText(), checkLandscape.isSelected()));
        //new screenShot arrived
        mScreenShotService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !getModel().getSavedImages().contains(newValue)) {
                getModel().getSavedImages().add(newValue);
            }
        });

        /* **********************************************************
               Show taken screenShot
         ********************************************************** */
        paginationImages.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                if (getModel().getSavedImages().isEmpty()) {
                    Image image = new Image("/img/ph_screen_shot.png");
                    return new ImageView(image);
                } else {
                    return new ImageHolder(getModel().getSavedImages().get(param));
                }
            }
        });
        paginationImages.pageCountProperty().bind(Bindings.createIntegerBinding(() ->
                        getModel().getSavedImages().size(),
                getModel().getSavedImages()));

        paginationImages.currentPageIndexProperty().bindBidirectional(getModel().getCurrentIndex());
    }

    @Override
    protected void initData() {

    }

}
