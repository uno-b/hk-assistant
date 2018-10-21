package hk.assistant.ui.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    private MainController controller = MainController.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private void loadAddGuest() {
        controller.loadAddGuest();
    }


    @FXML
    private void loadAddHK() {
        controller.loadAddHK();
    }


    @FXML
    private void loadGuestTable() {
        controller.loadGuestTable();
    }


    @FXML
    private void loadHKTable() {
        controller.loadHKTable();
    }


    @FXML
    private void loadUncleanRoomTable() {
        controller.loadUncleanRoomTable();
    }


    @FXML
    private void loadPrint() {
        controller.loadPrint();
    }
}