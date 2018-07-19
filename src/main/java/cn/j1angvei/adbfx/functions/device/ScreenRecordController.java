package cn.j1angvei.adbfx.functions.device;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.adb.ScreenRecordService;
import com.android.ddmlib.ScreenRecorderOptions;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ScreenRecordController extends BaseController<ScreenRecordModel> {
    private static final String REMOTE_DIR = "/sdcard/adbfx";
    @FXML
    private CheckBox checkDefaultSize;
    @FXML
    private TextField fieldWidth;
    @FXML
    private TextField fieldHeight;
    @FXML
    private CheckBox checkTouch;
    @FXML
    private Slider sliderTime;
    @FXML
    private TextField fieldLocalPath;
    @FXML
    private Button btnChooseLocal;
    @FXML
    private ToggleGroup groupBitRate;
    @FXML
    private ListView<File> listVideos;
    @FXML
    private Button btnStartRecording;
    @FXML
    private Button btnOpenFolder;
    @FXML
    private Button btnPlayVideo;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TextArea areaOutput;
    private ScreenRecordService mScreenRecordService;

    @Override
    protected ScreenRecordModel initModel() {
        return new ScreenRecordModel();
    }

    @Override
    protected void initArguments() {
        mScreenRecordService = new ScreenRecordService();
    }

    @Override
    protected void initView() {
        /* **************************************************
        Screen recording config
         ************************************************** */
        fieldLocalPath.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() {
                return getModel().getLocalPath().get().getAbsolutePath();
            }
        }, getModel().getLocalPath()));

        /* **************************************************
        Start screen recording
         ************************************************** */
        btnStartRecording.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mScreenRecordService.restart(getChosenDevice(), buildRecorderOptions(), REMOTE_DIR, fieldLocalPath.getText(), areaOutput.textProperty());
            }
        });
        progressBar.visibleProperty().bind(mScreenRecordService.runningProperty());

        /* **************************************************
        Complete screen recording
         ************************************************** */


    }

    private ScreenRecorderOptions buildRecorderOptions() {

        String userData = (String) groupBitRate.getSelectedToggle().getUserData();

        ScreenRecorderOptions.Builder builder = new ScreenRecorderOptions.Builder()
                .setBitRate(Integer.parseInt(userData))
                .setShowTouches(checkTouch.isSelected())
                .setTimeLimit((int) sliderTime.getValue(), TimeUnit.SECONDS);
        if (!checkDefaultSize.isSelected() && fieldHeight.getText().matches("\\d+") && fieldWidth.getText().matches("\\d+")) {
            int width = Integer.parseInt(fieldWidth.getText());
            int height = Integer.parseInt(fieldHeight.getText());
            builder.setSize(width, height);
        }
        return builder.build();
    }

    @Override
    protected void initData() {

    }
}
