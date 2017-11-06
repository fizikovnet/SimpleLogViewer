package logViewer.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import logViewer.Containers.ThreadContainer;
import logViewer.Model.Entry;
import logViewer.Services.ParserFileService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainController {

    @FXML
    public TableView<Entry> mainTable;
    @FXML
    public TableColumn<Entry, String> clmTime;
    @FXML
    public TableColumn<Entry, String> clmLevel;
    @FXML
    public TableColumn<Entry, String> clmClass;
    @FXML
    public TableColumn<Entry, String> clmMsg;
    @FXML
    public MenuBar mainMenu;
    @FXML
    public TableColumn<Entry, String> clmThread;
    @FXML
    public TextArea fullMessage;
    @FXML
    public MenuItem menuOpenFile;

    public CheckBox fltrLevelERR;
    public CheckBox fltrLevelWARN;
    public CheckBox fltrLevelDEBUG;
    public CheckBox fltrLevelINFO;
    public CheckBox fltrLevelALL;
    public CheckBox fltrLevelTRACE;
    public ChoiceBox<String> fltrChoiceThread;

    private ObservableList<Entry> allEntries = FXCollections.observableArrayList();

    private HashMap<String, Boolean> levelStatusFilter = new HashMap<>();
    final FileChooser fileChooser = new FileChooser();

    public static AtomicInteger counter = new AtomicInteger(0);
    public static final ThreadContainer threadContainer = new ThreadContainer();
    private FilteredList<Entry> filteredData;

    ParserFileService parserFileService = new ParserFileService();

    {
        levelStatusFilter.put("ERROR", false);
        levelStatusFilter.put("WARN", false);
        levelStatusFilter.put("INFO", false);
        levelStatusFilter.put("DEBUG", false);
        levelStatusFilter.put("TRACE", false);
    }

    @FXML
    private void initialize() {
        clmTime.setCellValueFactory(cell -> cell.getValue().timeProperty());
        clmLevel.setCellValueFactory(cell -> cell.getValue().levelProperty());
        clmClass.setCellValueFactory(cell -> cell.getValue().clazzProperty());
        clmThread.setCellValueFactory(cell -> cell.getValue().threadProperty());
        clmMsg.setCellValueFactory(cell -> cell.getValue().messageProperty());

        fltrLevelALL.setSelected(true);

        mainTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        fullMessage.setText((newValue != null) ? newValue.toString() + newValue.getFullMessage() : "")
        );
        fltrChoiceThread.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTableByThread(newValue)
        );

        addListenersForLevelsCheckboxes();
        setSettingsForTable();
    }

    public void openFile(ActionEvent actionEvent) {
        Window stage = mainMenu.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            parserFileService.performFile(file, allEntries);
        }
        filteredData = new FilteredList<>(allEntries);
        mainTable.setItems(filteredData);
        fltrChoiceThread.setItems(FXCollections.observableArrayList(threadContainer.getThreads()));
    }

    private void filterTable(Boolean checked, String level) {
        levelStatusFilter.put(level, checked);
        filteredData.setPredicate((row) -> {

            for (Map.Entry<String, Boolean> statusLevel : levelStatusFilter.entrySet()) {
                if (row.getLevel() != null ) {
                    if (statusLevel.getKey().equals(row.getLevel()) && statusLevel.getValue()) {
                        return true;
                    }
                    continue;
                }
                return false;
            }
            return false;
        });
        mainTable.setItems(filteredData);
    }

    private void filterTableByThread(String thread) {
        if (thread.equals("ALL")) {
            mainTable.setItems(filteredData);
        } else {
            filteredData.setPredicate((row) -> {
                if (row.getThread().equals(thread)) {
                    return true;
                }
                return false;
            });
            mainTable.setItems(filteredData);
        }
    }

    private void resetLevelFilter() {
        fltrLevelERR.setSelected(false);
        fltrLevelDEBUG.setSelected(false);
        fltrLevelINFO.setSelected(false);
        fltrLevelWARN.setSelected(false);
        mainTable.setItems(allEntries);
    }

    private void addListenersForLevelsCheckboxes() {
        fltrLevelALL.selectedProperty().addListener((observable, oldValue, newValue) -> resetLevelFilter());

        fltrLevelERR.selectedProperty().addListener((observable, oldValue, newValue) -> {
            fltrLevelALL.setSelected(false);
            filterTable(newValue, "ERROR");
        });
        fltrLevelDEBUG.selectedProperty().addListener((observable, oldValue, newValue) -> {
            fltrLevelALL.setSelected(false);
            filterTable(newValue, "DEBUG");
        });
        fltrLevelINFO.selectedProperty().addListener((observable, oldValue, newValue) -> {
            fltrLevelALL.setSelected(false);
            filterTable(newValue, "INFO");
        });
        fltrLevelWARN.selectedProperty().addListener((observable, oldValue, newValue) -> {
            fltrLevelALL.setSelected(false);
            filterTable(newValue, "WARN");
        });
        fltrLevelTRACE.selectedProperty().addListener((observable, oldValue, newValue) -> {
            fltrLevelALL.setSelected(false);
            filterTable(newValue, "TRACE");
        });
    }

    private void setSettingsForTable() {
        mainTable.setRowFactory(new Callback<TableView<Entry>, TableRow<Entry>>() {
            @Override
            public TableRow<Entry> call(TableView<Entry> tableView) {
                final TableRow<Entry> row = new TableRow<Entry>() {
                    @Override
                    protected void updateItem(Entry item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && item.getLevel() != null) {
                            switch (item.getLevel()) {
                                case "ERROR":
                                    setStyle("-fx-background-color: #ff69b4;");
                                    break;
                                case "DEBUG":
                                    setStyle("-fx-background-color: #f0ffff;");
                                    break;
                                case "INFO":
                                    setStyle("-fx-background-color: #faebd7;");
                                    break;
                                case "WARN":
                                    setStyle("-fx-background-color: #ffb6c1;");
                                    break;
                                case "TRACE":
                                    setStyle("-fx-background-color: #ffffe0;");
                                    break;
                            }
                        }
                    }
                };
                return row;
            }
        });
    }
}
