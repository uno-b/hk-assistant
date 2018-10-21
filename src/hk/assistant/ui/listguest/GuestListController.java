package hk.assistant.ui.listguest;

import hk.assistant.database.DatabaseHandler;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class GuestListController implements Initializable {

    ObservableList<Guest> list = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Guest> tableView;
    @FXML
    private TableColumn<Guest, String> nameCol;
    @FXML
    private TableColumn<Guest, String> roomCol;
    @FXML
    private TableColumn<Guest, String> inCol;
    @FXML
    private TableColumn<Guest, String> outCol;
    @FXML
    private TableColumn<Guest, String> adultsCol;
    @FXML
    private TableColumn<Guest, String> teensCol;
    @FXML
    private TableColumn<Guest, String> childrenCol;
    @FXML
    private TableColumn<Guest, Boolean> isVipCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initCol();

        loadData();
    }


    private void initCol() {

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        inCol.setCellValueFactory(new PropertyValueFactory<>("in"));
        outCol.setCellValueFactory(new PropertyValueFactory<>("out"));
        adultsCol.setCellValueFactory(new PropertyValueFactory<>("adults"));
        teensCol.setCellValueFactory(new PropertyValueFactory<>("teens"));
        childrenCol.setCellValueFactory(new PropertyValueFactory<>("children"));
        isVipCol.setCellValueFactory(new PropertyValueFactory<>("isVip"));
    }


    private void loadData() {

        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM GUESTS";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String name = rs.getString("guestName");
                String room = rs.getString("roomNum");
                String in = rs.getString("checkInDate");
                String out = rs.getString("checkOutDate");
                String adults = rs.getString("numOfAdults");
                String teens = rs.getString("numOfYoung");
                String children = rs.getString("numOfChild");
                Boolean isVip = rs.getBoolean("isVip");

                list.add(new Guest(name, room, in, out, adults, teens, children, isVip));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tableView.setItems(list);
    }


    @FXML
    private void guestDeleteOption(ActionEvent event) {

        Guest selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a guest.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Are you sure you want to delete the selected guest in room #" + selectedForDeletion.getRoom() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            Boolean result = DatabaseHandler.getInstance().deleteGuest(selectedForDeletion);
            if (result) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Guest was deleted.");
                alert1.showAndWait();

                list.remove(selectedForDeletion);
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Guest deletion failed.");
                alert1.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Guest deletion cancelled.");
            alert1.showAndWait();
        }
    }


    public static class Guest {

        private final SimpleStringProperty name;
        private final SimpleStringProperty room;
        private final SimpleStringProperty in;
        private final SimpleStringProperty out;
        private final SimpleStringProperty adults;
        private final SimpleStringProperty teens;
        private final SimpleStringProperty children;
        private final SimpleBooleanProperty isVip;

        Guest(String name, String room, String in, String out,
              String adults, String teens, String children, Boolean isVip) {
            this.name = new SimpleStringProperty(name);
            this.room = new SimpleStringProperty(room);
            this.in = new SimpleStringProperty(in);
            this.out = new SimpleStringProperty(out);
            this.adults = new SimpleStringProperty(adults);
            this.teens = new SimpleStringProperty(teens);
            this.children = new SimpleStringProperty(children);
            this.isVip = new SimpleBooleanProperty(isVip);
        }

        public String getName() {
            return name.get();
        }
        public String getRoom() {
            return room.get();
        }
        public String getIn() {
            return in.get();
        }
        public String getOut() {
            return out.get();
        }
        public String getAdults() {
            return adults.get();
        }
        public String getTeens() {
            return teens.get();
        }
        public String getChildren() {
            return children.get();
        }
        public Boolean getIsVip() {
            return isVip.get();
        }
    }
}