package hk.assistant.ui.unclean;

import com.jfoenix.controls.JFXDatePicker;
import hk.assistant.database.DatabaseHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;


public class uncleanRoomController implements Initializable {

    @FXML
    private TableView<uncleanRoomController.uncleanRoom> tableView;
    @FXML
    private TableColumn<uncleanRoomController.uncleanRoom, String> roomNumCol;
    @FXML
    private TableColumn<uncleanRoomController.uncleanRoom, String> checkOutCol;
    @FXML
    private TableColumn<uncleanRoomController.uncleanRoom, String> noteCol;
    @FXML
    private JFXDatePicker uncleanDate;
    private DatabaseHandler handler = DatabaseHandler.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        loadData();
    }


    private void initCol() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date input = sdf.parse("2018-09-21");
            LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            this.uncleanDate.setValue(date);
            roomNumCol.setCellValueFactory(new PropertyValueFactory<>("room"));
            checkOutCol.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
            noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void loadData() {
        ObservableList<uncleanRoomController.uncleanRoom> list = FXCollections.observableArrayList();
        ArrayList<String> asRoomsList = new ArrayList<>();
        ArrayList<String> asCleanDate = new ArrayList<>();

        String uncleanDateStr = this.uncleanDate.getValue().toString();

        try {
            ResultSet rs = handler.execQuery("SELECT * FROM ASSIGNMENT");
            while (rs.next()) {
                String room = rs.getString("gRoomNum");
                String date = rs.getString("date");
                asRoomsList.add(room);
                asCleanDate.add(date);
            }

            rs = handler.execQuery("SELECT * FROM GUESTS");
            while (rs.next()) {
                boolean flag = false;   // uncleanDate is valid
                boolean isAssigned = false;
                String room = rs.getString("roomNum");
                String checkInDateStr = rs.getString("checkInDate");
                String checkOutDateStr = rs.getString("checkOutDate");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date checkinDate = sdf.parse(checkInDateStr);
                    Date checkOutDate = sdf.parse(checkOutDateStr);
                    Date uncleanDate = sdf.parse(uncleanDateStr);
                    if (uncleanDate.equals(checkinDate) || (uncleanDate.after(checkinDate) && uncleanDate.before(checkOutDate))) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (flag == true) {
                    String checkOut = rs.getString("checkOutDate");
                    ResultSet rs1 = handler.execQuery("SELECT * FROM ASSIGNMENT WHERE gRoomNum = '" + room + "'");
                    while (rs1.next()) {
                        isAssigned = true;
                        String cleanDate = rs1.getString("date");
                        if (!cleanDate.equals(uncleanDate)) {
                            list.add(new uncleanRoom(room, checkOut, ""));
                        }
                    }
                    if (!isAssigned) {
                        list.add(new uncleanRoom(room, checkOut, ""));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            ResultSet rs = handler.execQuery("SELECT * FROM UNCLEANROOMS");
            while (rs.next()) {
                String roomNum = rs.getString("roomNum");
                String checkOutDate = rs.getString("checkOutDate");
                String note = rs.getString("note");
                list.add(new uncleanRoom(roomNum, checkOutDate, note));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.getItems().setAll(list);
    }


    public static class uncleanRoom {
        private final SimpleStringProperty room;
        private final SimpleStringProperty checkOut;
        private final SimpleStringProperty note;

        uncleanRoom(String room, String checkOut, String note) {
            this.room = new SimpleStringProperty(room);
            this.checkOut = new SimpleStringProperty(checkOut);
            this.note = new SimpleStringProperty(note);
        }

        public String getRoom() {
            return room.get();
        }

        public String getCheckOut() {
            return checkOut.get();
        }

        public String getNote() {
            return note.get();
        }
    }


    @FXML
    private void refresh (ActionEvent event) {
        loadData();
    }
}