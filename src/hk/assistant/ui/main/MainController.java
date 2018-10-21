package hk.assistant.ui.main;

import com.jfoenix.controls.JFXTabPane;
import hk.assistant.utility.Functions;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hk.assistant.database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {

    /* objects in "Main" tab */

    @FXML
    private BorderPane rootPane;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private HBox guestInfo;
    @FXML
    private HBox HKInfo;
    @FXML
    private HBox btnHBox;
    @FXML
    private HBox dateHBox;
    @FXML
    private JFXTextField roomNumField;
    @FXML
    private JFXTextField HKIDField;
    @FXML
    private Label guestNameLbl;
    @FXML
    private Label checkInLbl;
    @FXML
    private Label checkOutLbl;
    @FXML
    private Label HKNameLbl;
    @FXML
    private Label phoneNumLbl;
    @FXML
    private Label lbl;
    @FXML
    private JFXDatePicker todayDateField;
    @FXML
    private JFXDatePicker dateField;
    @FXML
    private ListView dataList;

    /* objects in "Manage" tab */

    @FXML
    private JFXDatePicker mDateField;
    @FXML
    private TextField mHKNameField;
    @FXML
    private Button mAssignBtn;
    @FXML
    private Label selectedRoomLbl;

    private static MainController controller = null;
    private DatabaseHandler handler;
    private String selectedRoomNum = "none";
    private String HKID = "";
    private String gCheckOut = "";
    private String roomNum = "";
    private String HKName = "";
    private boolean guestFound = false;
    private boolean HKFound = false;
    private boolean hkNameFound = false;
    private boolean sideIconOn = false;
    private List<String> names = new ArrayList<>();


    public static MainController getInstance() {

        if (controller == null) {
            System.out.println("MainController not initialized...");
        }
        return controller;
    }


    @Override
    public void initialize (URL location, ResourceBundle resources) {

        controller = new MainController();
        handler = DatabaseHandler.getInstance();
        mAssignBtn.setDisable(true);

        ResultSet rs = handler.execQuery("SELECT name FROM HK");
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                names.add(name);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
        TextFields.bindAutoCompletion(mHKNameField, names);

        Functions.makeStageDragable(rootPane);
        Functions.makeStageDragable(tabPane);
    }


    private Stage loadWindow(String loc, String title, boolean isDecorated) {

        Stage stage = null;
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));

            if (isDecorated) {
                stage = new Stage(StageStyle.DECORATED);
            } else {
                stage = new Stage(StageStyle.UNDECORATED);
            }
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stage;
    }


    @FXML
    public void loadAddGuest () {

        Stage stage = loadWindow("/hk/assistant/ui/addguest/guest_add.fxml", "Add a Guest", false);
        Functions.makeStageDragable(stage);
    }


    @FXML
    public void loadAddHK () {

        Stage stage = loadWindow("/hk/assistant/ui/addhk/hk_add.fxml", "Add a Housekeeper", false);
        Functions.makeStageDragable(stage);
    }


    @FXML
    public void loadGuestTable () {

        Stage stage = loadWindow("/hk/assistant/ui/listguest/guest_list.fxml", "Guest Table", true);
        Functions.makeStageDragable(stage);
    }


    @FXML
    public void loadHKTable () {

        Stage stage = loadWindow("/hk/assistant/ui/listhk/hk_list.fxml", "Housekeeper Table", true);
        Functions.makeStageDragable(stage);
    }


    @FXML
    public void loadUncleanRoomTable () {

        Stage stage = loadWindow("/hk/assistant/ui/unclean/unclean_list.fxml", "Unclean Rooms Table", true);
        Functions.makeStageDragable(stage);
    }


    @FXML
    public void loadPrint () {

        Stage stage = loadWindow("/hk/assistant/report/report.fxml", "Print a Report", false);
        Functions.makeStageDragable(stage);
    }


    @FXML
    private void loadRoomInfo (ActionEvent event) {

        Boolean flag = false;   // data found
        roomNum = roomNumField.getText();
        ResultSet rs = handler.execQuery("SELECT * FROM GUESTS WHERE roomNum = '" + roomNum + "'");
        try {
            while (rs.next()) {
                String gName = rs.getString("guestName");
                String gCheckIn = rs.getString("checkInDate");
                gCheckOut = rs.getString("checkOutDate");
                roomNum = rs.getString("roomNum");

                guestNameLbl.setText("Guest Name : " + gName);
                checkInLbl.setText("Check-in Date : " + gCheckIn);
                checkOutLbl.setText("Check-out Date : " + gCheckOut);

                flag = true;
                guestFound = true;
            }
            if (!flag) {
                guestNameLbl.setText("");
                checkInLbl.setText("No guest in the given room was found");
                checkOutLbl.setText("");

                guestFound = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void loadHKInfo (ActionEvent event) {

        Boolean flag = false;   // data found
        HKID = HKIDField.getText();
        ResultSet rs = handler.execQuery("SELECT * FROM HK WHERE id = '" + HKID + "'");
        try {
            while (rs.next()) {
                HKName = rs.getString("name");
                String HKPhoneNum = rs.getString("phoneNum");

                HKNameLbl.setText("Housekeeper Name : " + HKName);
                phoneNumLbl.setText("Phone Number : " + HKPhoneNum);

                HKFound = true;
                flag = true;
            }
            if (!flag) {
                HKNameLbl.setText("No housekeeper with the given ID was found");
                phoneNumLbl.setText("");

                HKFound = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void assignHK(ActionEvent event) {

        if (guestFound == true && HKFound == true && !dateField.getValue().toString().equals("")) {
            String date = dateField.getValue().toString();
            String status = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date checkOutDate = sdf.parse(gCheckOut);
                Date cleanDate = sdf.parse(date);
                status = cleanDate.equals(checkOutDate) || cleanDate.after(checkOutDate) ? "Due Out" : "Stayover Service";
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Assignment Operation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to assign the housekeeper " + HKName
                                + " to room #" + roomNum + "?");

            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                String str = "INSERT INTO ASSIGNMENT(gRoomNum, HKID, date, status) VALUES ("
                        + "'" + roomNum + "',"
                        + "'" + HKID + "',"
                        + "'" + date + "',"
                        + "'" + status + "')";

                if (handler.execAction(str)) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Success.");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Housekeeper Assignment Successful.");
                    alert1.showAndWait();
                } else {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Failed.");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Housekeeper Assignment Failed.");
                    alert1.showAndWait();
                }
            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Cancelled.");
                alert1.setHeaderText(null);
                alert1.setContentText("Housekeeper Assignment Cancelled.");
                alert1.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please find the guest's room number and housekeeper's ID or choose a date first.");
            alert.showAndWait();
        }
    }


    @FXML
    private void assignHK2 (ActionEvent event) {

        String HKName = mHKNameField.getText();
        for (String str : names) {
            if (HKName.equals(str)) {
                hkNameFound = true;
                break;
            }
        }

        if (hkNameFound == true && !selectedRoomNum.equals("none") && !mDateField.getValue().toString().equals("")) {
            String name = mHKNameField.getText();
            String id = "";
            ResultSet rs = handler.execQuery("SELECT id FROM HK WHERE name = '" + name + "'");
            try {
                while (rs.next()) {
                    id = rs.getString("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String date = mDateField.getValue().toString();
            String status = "";
            rs = handler.execQuery("SELECT checkOutDate FROM GUESTS WHERE roomNum = '" + selectedRoomNum + "'");
            try {
                while (rs.next()) {
                    String str = rs.getString("checkOutDate");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date checkOutDate = sdf.parse(str);
                        Date cleanDate = sdf.parse(date);
                        status = cleanDate.equals(checkOutDate) || cleanDate.after(checkOutDate) ? "Due Out" : "Stayover Service";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Assignment Operation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to assign the housekeeper " + name
                    + " to room #" + selectedRoomNum + "?");

            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                String str = "INSERT INTO ASSIGNMENT(gRoomNum, HKID, date, status) VALUES ("
                        + "'" + selectedRoomNum + "',"
                        + "'" + id + "',"
                        + "'" + date + "',"
                        + "'" + status + "')";

                if (handler.execAction(str)) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Success.");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Housekeeper Assignment Successful.");
                    alert1.showAndWait();
                } else {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Failed.");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Housekeeper Assignment Failed.");
                    alert1.showAndWait();
                }
            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Cancelled.");
                alert1.setHeaderText(null);
                alert1.setContentText("Housekeeper Assignment Cancelled.");
                alert1.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter the required fields or enter valid HK name.");
            alert.showAndWait();
        }
    }


    @FXML
    private void openSidebar(ActionEvent event) throws IOException {

        BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        if (sideIconOn == false) {
            Parent sidebar = FXMLLoader.load(getClass().getResource("sidebar.fxml"));

            borderPane.setLeft(sidebar);
            sideIconOn = true;
        } else {
            borderPane.setLeft(null);
            sideIconOn = false;
        }
    }


    @FXML
    private void loadInfoOfSelectedRoom(ActionEvent event) throws SQLException {

        ObservableList<String> data = FXCollections.observableArrayList();

        mAssignBtn.setDisable(false);
        Button button = (Button) event.getSource();
        String dateAssigned = "";
        String roomNumber = button.getText();
        selectedRoomNum = button.getText();
        selectedRoomLbl.setText("Selected Room : " + selectedRoomNum);

        ResultSet rs = handler.execQuery("SELECT date FROM ASSIGNMENT WHERE gRoomNum = '" + roomNumber + "'");
        while (rs.next()) {
            dateAssigned = rs.getString("date");
        }
        String newRoomNum = roomNumber;
        ResultSet rs1 = handler.execQuery("SELECT * FROM ASSIGNMENT WHERE gRoomNum = '" + roomNumber + "' AND date = '" + dateAssigned + "'");
        while (rs1.next()) {
            String newHKID = rs1.getString("HKID");
            ResultSet subRs = handler.execQuery("SELECT * FROM HK WHERE id = '" + newHKID + "'");
            while (subRs.next()) {
                String newName = subRs.getString("name");
                String newDateOfBirth = subRs.getString("dateOfBirth");
                String newPhoneNum = subRs.getString("phoneNum");
                String newStartDate = subRs.getString("startDate");
                String newEndDate = subRs.getString("endDate");

                data.add("Assigned Housekeeper's Info");
                data.add("  Assigned Date : " + dateAssigned);
                data.add("  Name : " + newName);
                data.add("  ID : " + newHKID);
                data.add("  Date of Birth : " + newDateOfBirth);
                data.add("  Phone Number : " + newPhoneNum);
                data.add("  Work Start Date : " + newStartDate);
                data.add("  Work End Date : " + newEndDate);
                data.add("");
            }
        }

        ResultSet subRs1 = handler.execQuery("SELECT * FROM GUESTS WHERE roomNum = '" + newRoomNum + "'");
        while (subRs1.next()) {
            String newName = subRs1.getString("guestName");
            String newCheckInDate = subRs1.getString("checkInDate");
            String newCheckOutDate = subRs1.getString("checkOutDate");
            String newNumOfAdults = subRs1.getString("numOfAdults");
            String newNumOfYoung = subRs1.getString("numOfYoung");
            String newNumOfChild = subRs1.getString("numOfChild");
            String newIsVIP = subRs1.getBoolean("isVip") == true ? "VIP" : "Not VIP";

            data.add("Room's Info");
            data.add("  Guest's Name : " + newName);
            data.add("  Check-in Date : " + newCheckInDate);
            data.add("  Check-out Date : " + newCheckOutDate);
            data.add("  Number of Adults : " + newNumOfAdults);
            data.add("  Number of Youngsters : " + newNumOfYoung);
            data.add("  Number of Children : " + newNumOfChild);
            data.add("  Status : " + newIsVIP);
        }
        dataList.getItems().setAll(data);
    }


    @FXML
    private void unassignHK (ActionEvent event) {

        if (selectedRoomNum.equals("none")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a room.");
            alert.showAndWait();
            return;
        } else {
            if (handler.execAction("DELETE ASSIGNMENT WHERE gRoomNum = " + selectedRoomNum)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Assigned housekeeper was deleted from the selected room.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete the assigned housekeeper from the selected room.");
                alert.showAndWait();
            }
        }
    }


     /*
     Checks if the housekeepers or the guests are still there - deletes if either of their stay time doesn't match
     today's date. Example: If a housekeepers end date has passed, the said hk will be removed from the database
     */
     @FXML
    private void checkExistence(ActionEvent event) {

         if (!todayDateField.getValue().toString().equals("")) {
            guestInfo.setDisable(false);
            HKInfo.setDisable(false);
            btnHBox.setDisable(false);
            dateHBox.setDisable(false);
            lbl.setTextFill(Color.BLACK);
            String todayDate = todayDateField.getValue().toString();

            ResultSet rs4 = handler.execQuery("SELECT * FROM ASSIGNMENT");
            try {
                while (rs4.next()) {
                    String dateString = rs4.getString("date");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date currentDate = sdf.parse(todayDate);
                        Date date = sdf.parse(dateString);
                        if (currentDate.after(date)) {
                            handler.execAction("DELETE FROM ASSIGNMENT WHERE date = '" + dateString + "'");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ResultSet rs = handler.execQuery("SELECT * FROM GUESTS");
            try {
                while (rs.next()) {
                    String dateString = rs.getString("checkOutDate");
                    String roomNum = rs.getString("roomNum");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date currentDate = sdf.parse(todayDate);
                        Date date = sdf.parse(dateString);

                        if (currentDate.after(date)) {
                            // Send the to-be deleted rooms' data to UNCLEANROOMS table
                            String qu = "INSERT INTO UNCLEANROOMS VALUES (" +
                                    "'" + roomNum + "'," +
                                    "'" + dateString + "'," +
                                    "'" + "" + "'" +
                                    ")";
                            if (!handler.execAction(qu)) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText(null);
                                alert.setContentText("Issue with inserting date to UNCLEANROOMS table.");
                                alert.showAndWait();
                            }

                            handler.execAction("DELETE FROM GUESTS WHERE checkOutDate = '" + dateString + "'");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ResultSet rs2 = handler.execQuery("SELECT * FROM HK");
            try {
                while (rs2.next()) {
                    String dateString = rs2.getString("endDate");
                    String id = rs2.getString("name");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date currentDate = sdf.parse(todayDate);
                        Date date = sdf.parse(dateString);

                        if (currentDate.after(date)) {
                            handler.execAction("DELETE FROM ASSIGNMENT WHERE HKID = '" + id + "'");
                            handler.execAction("DELETE FROM HK WHERE endDate = '" + dateString + "'");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Date field is empty.");
            alert.showAndWait();
        }
    }


    @FXML
    private void close (ActionEvent event) {
        Functions.close(event);
    }
}