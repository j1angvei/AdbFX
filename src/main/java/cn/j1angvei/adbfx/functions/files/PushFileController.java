package cn.j1angvei.adbfx.functions.files;

import cn.j1angvei.adbfx.BaseController;
import cn.j1angvei.adbfx.FileManager;
import cn.j1angvei.adbfx.adb.PushFileService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.util.List;

public class PushFileController extends BaseController<PushFileModel> {

    @FXML
    private ListView<File> listFiles;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnPush;
    @FXML
    private TextField fieldDestination;
    @FXML
    private ListView<String> listSubDir;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private TextArea areaOutput;

    private PushFileService mPushFileService;


    @Override
    protected PushFileModel initModel() {
        return new PushFileModel();
    }

    @Override
    protected void initArguments() {
        mPushFileService = new PushFileService();
    }

    @Override
    protected void initView() {

        /* *******************************************
            Manage file list
         ******************************************* */
        //add contextMenu to list
//        listFiles.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
//            @Override
//            public ListCell<File> call(ListView<File> param) {
//                ListCell<File> listCell = new ListCell<>();
//                listCell.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
//                    @Override
//                    public String call() throws Exception {
//                        if (listCell.getItem() == null) {
//                            return null;
//                        } else {
//                            return listCell.getItem().getAbsolutePath();
//                        }
//                    }
//                }, listCell.itemProperty()));
//
//                ContextMenu contextMenu = new ContextMenu();
//                MenuItem menuItem = new MenuItem("Remove");
//                menuItem.setOnAction(event -> param.getItems().remove(listCell.getItem()));
//                contextMenu.getItems().add(menuItem);
//
//                listCell.setContextMenu(contextMenu);
//
//                return listCell;
//            }
//        });
        //add from explorer
        btnAdd.setOnAction(event -> {
            List<File> addedFiles = FileManager.getInstance().loadFilesByExplorer("Choose  files to push",
                    FileManager.Extension.ALL);
            if (addedFiles != null && !addedFiles.isEmpty()) {
                listFiles.getItems().addAll(addedFiles);
            }
        });
        //drag to add files
        FileManager.loadFilesByDragDrop(listFiles, listFiles.getItems(), FileManager.Extension.ALL);
        //clear list
        btnClear.setOnAction(event -> listFiles.getItems().clear());
        //start push
        btnPush.setOnAction(event -> mPushFileService.restart(getChosenDevice(), listFiles.getItems(), fieldDestination.getText()));
        /* *******************************************
           set push destination
         ******************************************* */


        /* *******************************************
           Push file result
         ******************************************* */
        areaOutput.textProperty().bind(mPushFileService.valueProperty());
        btnPush.disableProperty().bind(mPushFileService.runningProperty());
        progressIndicator.visibleProperty().bind(mPushFileService.runningProperty());
        progressIndicator.progressProperty().bind(mPushFileService.progressProperty());

    }

    @Override
    protected void initData() {

    }
}
