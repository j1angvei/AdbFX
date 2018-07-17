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

import java.io.File;

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
               Action to take screenShot
         ********************************************************** */
        btnTakeScreenShot.setOnAction(event -> mScreenShotService.restart(getChosenDevice(), fieldSaveDir.getText(), checkLandscape.isSelected()));
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
                Image image;
                if (getModel().getSavedImages().isEmpty()) {
                    image = new Image("/img/ph_screen_shot.png");
                } else {
                    File file = ScreenShotController.this.getModel().getSavedImages().get(param);
                    image = new Image(file.toURI().toString());
                }
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(400f);
                imageView.setFitHeight(300f);
                imageView.setPreserveRatio(true);
                return imageView;
            }
        });
        paginationImages.pageCountProperty().bind(Bindings.createIntegerBinding(() ->
                        getModel().getSavedImages().size(),
                getModel().getSavedImages()));


    }

    @Override
    protected void initData() {

    }

}
