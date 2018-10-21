package hk.assistant.ui.listhk;

import hk.assistant.database.DatabaseHandler;
import hk.assistant.ui.listguest.GuestListController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class HKListController implements Initializable {

    @FXML
    private TableView<HKListController. HK> tableView;
    @FXML
    private TableColumn<HKListController.HK, String> nameCol;
    @FXML
    private TableColumn<HKListController.HK, String> idCol;
    @FXML
    private TableColumn<HKListController.HK, String> dateOfBirthCol;
    @FXML
    private TableColumn<HKListController.HK, String> phoneNumCol;
    @FXML
    private TableColumn<HKListController.HK, String> roomNumCol;
    @FXML
    private TableColumn<HKListController.HK, String> startDateCol;
    @FXML
    private TableColumn<HKListController.HK, String> endDateCol;

    private ObservableList<HKListController.HK> list = FXCollections.observableArrayList();
    private DatabaseHandler handler = DatabaseHandler.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initCol();
        loadData();
    }


    private void initCol() {

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateOfBirthCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        roomNumCol.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }


    private void loadData() {

        ResultSet rs = handler.execQuery("SELECT * FROM HK");
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                String id = rs.getString("id");
                String dateOfBirth = rs.getString("dateOfBirth");
                String phoneNum = rs.getString("phoneNum");
                String roomNum = rs.getString("roomNum");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");

                list.add(new HK(name, id, dateOfBirth, phoneNum, roomNum, startDate, endDate));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tableView.getItems().setAll(list);
    }


    @FXML
    private void HKDeleteOption (ActionEvent event) {

        HKListController.HK selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a housekeeper.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure you want to delete the selected HK ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            Boolean result = DatabaseHandler.getInstance().deleteHK(selectedForDeletion);
            if (result) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Housekeeper was deleted.");
                alert1.showAndWait();

                list.remove(selectedForDeletion);
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Housekeeper deletion failed.");
                alert1.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Housekeeper deletion cancelled.");
            alert1.showAndWait();
        }
    }


    public static class HK {

        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty dateOfBirth;
        private final SimpleStringProperty phoneNum;
        private final SimpleStringProperty roomNum;
        private final SimpleStringProperty startDate;
        private final SimpleStringProperty endDate;

        HK(String name, String id, String dateOfBirth, String phoneNum,
              String roomNum, String startDate, String endDate) {
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
            this.dateOfBirth = new SimpleStringProperty(dateOfBirth);
            this.phoneNum = new SimpleStringProperty(phoneNum);
            this.roomNum = new SimpleStringProperty(roomNum);
            this.startDate = new SimpleStringProperty(startDate);
            this.endDate = new SimpleStringProperty(endDate);
        }

        public String getName() {
            return name.get();
        }
        public String getId() {
            return id.get();
        }
        public String getDateOfBirth() {
            return dateOfBirth.get();
        }
        public String getPhoneNum() {
            return phoneNum.get();
        }
        public String getRoomNum() {
            return roomNum.get();
        }
        public String getStartDate() {
            return startDate.get();
        }
        public String getEndDate() {
            return endDate.get();
        }
    }
}