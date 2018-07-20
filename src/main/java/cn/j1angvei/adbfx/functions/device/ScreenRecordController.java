package cn.j1angvei.adbfx.functions.device;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.FileManager;
import cn.j1angvei.adbfx.adb.ScreenRecordService;
import com.android.ddmlib.ScreenRecorderOptions;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ScreenRecordController extends BaseController<ScreenRecordModel> {
    private static final String REMOTE_DIR = "/sdcard/adbfx";
    private static final Integer[] BIT_RATE_VALUES = {1, 2, 3, 4};
    private static final int TIME_LIMIT_MAX = 180;
    private static final List<Integer> TIME_LIMIT_VALUES = new ArrayList<>();

    static {
        int start = 5;
        while (start <= TIME_LIMIT_MAX) {
            TIME_LIMIT_VALUES.add(start);
            start += 5;
        }
    }

    //config
    @FXML
    private CheckBox checkDefaultSize;
    @FXML
    private TextField fieldWidth, fieldHeight;
    @FXML
    private ComboBox<Integer> comboTimeLimit;
    @FXML
    private ChoiceBox<Integer> choiceBitRate;
    @FXML
    private CheckBox checkTouch;
    @FXML
    private TextField fieldLocalPath;
    @FXML
    private Button btnAlterLocal;

    //content
    @FXML
    private ListView<File> listVideos;

    //action
    @FXML
    private Button btnStartRecording, btnOpenFolder, btnOpenFile;
    //result
    @FXML
    private Label labelCountDown;
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
        // set video width and height
        Stream.of(fieldWidth, fieldHeight).forEach(textField ->
                textField.disableProperty().bind(checkDefaultSize.selectedProperty()));
        // set time limit
        comboTimeLimit.getItems().addAll(TIME_LIMIT_VALUES);
        comboTimeLimit.getSelectionModel().selectLast();
        // set bit rate
        choiceBitRate.getItems().addAll(BIT_RATE_VALUES);
        choiceBitRate.getSelectionModel().selectLast();
        // set local dir to save video
        fieldLocalPath.textProperty().bind(Bindings.createStringBinding(() ->
                        getModel().getLocalPath().getAbsolutePath(),
                getModel().localPathProperty()));
        // choose new local dir
        btnAlterLocal.setOnAction(event -> {
            File chosenDir = FileManager.getInstance().chooseDirectory(
                    "Choose directory to save video files",
                    getModel().getLocalPath());
            if (chosenDir != null) {
                getModel().setLocalPath(chosenDir);
            }
        });
        /* **************************************************
        Start screen recording
         ************************************************** */
        btnStartRecording.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mScreenRecordService.restart(getChosenDevice(),
                        buildRecorderOptions(), REMOTE_DIR,
                        fieldLocalPath.getText(), areaOutput.textProperty());
            }
        });

        /* **************************************************
           Action dealing with video files
         ************************************************** */
        btnOpenFolder.setOnAction(event ->
                FileManager.openFile(getModel().getLocalPath()));
        btnOpenFile.setOnAction(event ->
                FileManager.openFile(listVideos.getSelectionModel().getSelectedItem()));
        btnOpenFile.disableProperty().bind(Bindings.isNull(listVideos.getSelectionModel().selectedItemProperty()));
        /* **************************************************
        Complete screen recording
         ************************************************** */
        mScreenRecordService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !listVideos.getItems().contains(newValue)) {
                listVideos.getItems().add(newValue);
            }
        });
        labelCountDown.visibleProperty().bind(mScreenRecordService.runningProperty());
        btnStartRecording.disableProperty().bind(mScreenRecordService.runningProperty());
        btnStartRecording.textProperty().bind(Bindings.createStringBinding(() ->
                        mScreenRecordService.isRunning() ? "Recording" : "Start",
                mScreenRecordService.runningProperty()));
    }

    private ScreenRecorderOptions buildRecorderOptions() {

        ScreenRecorderOptions.Builder builder = new ScreenRecorderOptions.Builder()
                .setBitRate(choiceBitRate.getValue())
                .setShowTouches(checkTouch.isSelected())
                .setTimeLimit(comboTimeLimit.getValue(), TimeUnit.SECONDS);
        if (!checkDefaultSize.isSelected() &&
                fieldHeight.getText().matches("\\d+") &&
                fieldWidth.getText().matches("\\d+")) {
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
