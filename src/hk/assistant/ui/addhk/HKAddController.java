package hk.assistant.ui.addhk;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hk.assistant.database.DatabaseHandler;
import hk.assistant.utility.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HKAddController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXDatePicker dateOfBirth;
    @FXML
    private JFXTextField phoneNum;
    @FXML
    private JFXTextField roomNum;
    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXDatePicker endDate;

    private DatabaseHandler handler = DatabaseHandler.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dateOfBirth.setValue(LOCAL_DATE("01-01-1995")); // sets default value to make it easier to navigate into desired date
        startDate.setValue(LOCAL_DATE("01-05-2018"));
        endDate.setValue(LOCAL_DATE("01-10-2018"));
    }


    @FXML
    private void addBook(ActionEvent event) {

        String dName = name.getText();
        String dId = id.getText();
        String dDateOfBirth = dateOfBirth.getValue().toString();
        String dPhoneNum = phoneNum.getText();
        String dRoomNum = roomNum.getText();
        String dStartDate = startDate.getValue().toString();
        String dEndDate = endDate.getValue().toString();

        if (dPhoneNum.isEmpty()) {
            dPhoneNum = "N/A";
        }

        if (dName.isEmpty() || dId.isEmpty() || dDateOfBirth.isEmpty() || dRoomNum.isEmpty() || dStartDate.isEmpty() || dEndDate.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter all info except phone number.");
            alert.showAndWait();
            return;
        }

        String qu = "INSERT INTO HK VALUES (" +
                "'" + dName + "'," +
                "'" + dId + "'," +
                "'" + dDateOfBirth + "'," +
                "'" + dPhoneNum + "'," +
                "'" + dRoomNum + "'," +
                "'" + dStartDate + "'," +
                "'" + dEndDate + "'" +
                ")";
        if (handler.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed.");
            alert.showAndWait();
        }
    }


    @FXML
    private void cancel(ActionEvent event) {

        Stage stage = (Stage)rootPane.getScene().getWindow();
        stage.close();
    }


    // set default value of Date of Birth
    public static final LocalDate LOCAL_DATE (String dateString){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }
}