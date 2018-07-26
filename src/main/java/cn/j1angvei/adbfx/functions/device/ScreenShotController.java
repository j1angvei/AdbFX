package cn.j1angvei.adbfx.functions.device;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.FileManager;
import cn.j1angvei.adbfx.adb.ScreenShotService;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

public class ScreenShotController extends BaseController<ScreenShotModel> {
    public CheckBox checkLandscape;
    public TextField fieldSaveDir;
    public Button btnAlterDir;

    public Pagination paginationImages;
    public Button btnTakeScreenShot;

    public Slider sliderScale;
    public VBox boxScale;

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
              screenShot params
         ********************************************************** */
        fieldSaveDir.textProperty().bind(Bindings.createStringBinding(() -> getModel().getSaveDir().get().getAbsolutePath(), getModel().getSaveDir()));
        btnAlterDir.setOnAction(event -> {
            File chosenDir = FileManager.getInstance().chooseDirectory("Choose directory to save screenShot", getModel().getSaveDir().get());
            if (chosenDir != null) {
                getModel().getSaveDir().set(chosenDir);
            }
        });


        /* **********************************************************
              Take screenShot
         ********************************************************** */
        btnTakeScreenShot.setOnAction(event -> mScreenShotService.restart(
                getChosenDevice(), fieldSaveDir.getText(), checkLandscape.isSelected()));
        //new screenShot arrived
        mScreenShotService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !getModel().getSavedImages().contains(newValue)) {
                getModel().getSavedImages().add(newValue);
                paginationImages.setCurrentPageIndex(getModel().getSavedImages().size() - 1);
            }
        });

        /* **********************************************************
               Show taken screenShot
         ********************************************************** */
        paginationImages.setPageFactory(param -> {
            if (getModel().getSavedImages().isEmpty()) {
                Image image = new Image("/img/ph_screen_shot.png");
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(128);
                imageView.setFitWidth(128);
                imageView.setPreserveRatio(true);
                return imageView;
            } else {
                File file = getModel().getSavedImages().get(param);
                ImageHolder imageHolder = new ImageHolder(file) {
                    @Override
                    public void onDelete() {
                        getModel().getSavedImages().remove(file);
                    }
                };
                imageHolder.scaleProperty().bind(sliderScale.valueProperty());
                return imageHolder;
            }
        });
        paginationImages.pageCountProperty().bind(Bindings.createIntegerBinding(() ->
                        getModel().getSavedImages().size(),
                getModel().getSavedImages()));

        boxScale.visibleProperty().bind(Bindings.not(getModel().getSavedImages().emptyProperty()));
    }

    @Override
    protected void initData() {

    }

}
