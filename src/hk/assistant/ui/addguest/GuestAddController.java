package hk.assistant.ui.addguest;

import com.jfoenix.controls.JFXComboBox;
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
import java.util.ResourceBundle;

public class GuestAddController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField guestName;
    @FXML
    private JFXTextField roomNum;
    @FXML
    private JFXDatePicker checkInDate;
    @FXML
    private JFXDatePicker checkOutDate;
    @FXML
    private JFXComboBox<String> numOfAdults;
    @FXML
    private JFXComboBox<String> numOfYoung;
    @FXML
    private JFXComboBox<String> numOfChild;
    @FXML
    private JFXComboBox<String> isVip;

    private DatabaseHandler handler = DatabaseHandler.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        numOfAdults.getItems().removeAll(numOfAdults.getItems());
        numOfAdults.getItems().addAll("1", "2", "3", "4", "5", "6");
        numOfAdults.getSelectionModel().select("1");

        numOfYoung.getItems().removeAll(numOfYoung.getItems());
        numOfYoung.getItems().addAll("0", "1", "2", "3", "4", "5", "6");
        numOfYoung.getSelectionModel().select("0");

        numOfChild.getItems().removeAll(numOfChild.getItems());
        numOfChild.getItems().addAll("0", "1", "2", "3", "4", "5", "6");
        numOfChild.getSelectionModel().select("0");

        isVip.getItems().removeAll(isVip.getItems());
        isVip.getItems().addAll("No", "Yes");
        isVip.getSelectionModel().select("No");
    }


    @FXML
    private void addBook(ActionEvent event) {

        // d - data to be input
        String dGuestName = guestName.getText();
        String dRoomNum = roomNum.getText();
        String dCheckInDate = checkInDate.getValue().toString();
        String dCheckOutDate = checkOutDate.getValue().toString();
        String dNumOfAdults = numOfAdults.getSelectionModel().getSelectedItem();
        String dNumOfYoung = numOfYoung.getSelectionModel().getSelectedItem();
        String dNumOfChild = numOfChild.getSelectionModel().getSelectedItem();
        boolean dIsVip = isVip.getValue().toString() == "Yes" ? true : false;

        if (dGuestName.isEmpty() || dRoomNum.isEmpty() || dCheckInDate.isEmpty() || dCheckOutDate.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter in all fields.");
            alert.showAndWait();
            return;
        }

        String qu = "INSERT INTO GUESTS VALUES (" +
                "'" + dGuestName + "'," +
                "'" + dRoomNum + "'," +
                "'" + dCheckInDate + "'," +
                "'" + dCheckOutDate + "'," +
                "'" + dNumOfAdults + "'," +
                "'" + dNumOfYoung + "'," +
                "'" + dNumOfChild + "'," +
                "" + dIsVip + "" +
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
}