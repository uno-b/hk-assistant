package hk.assistant.report;

import com.jfoenix.controls.JFXDatePicker;
import hk.assistant.database.DatabaseHandler;
import hk.assistant.reportinfo.Assignment;
import hk.assistant.reportinfo.Room;
import hk.assistant.utility.Functions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ReportController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField printHKNameField;
    @FXML
    private JFXDatePicker printHKDatePicker;

    private DatabaseHandler handler = DatabaseHandler.getInstance();

    private List<String> names = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ResultSet rs = handler.execQuery("SELECT name FROM HK");
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                names.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TextFields.bindAutoCompletion(printHKNameField, names);
    }


    @FXML
    private void print (ActionEvent event) {

        Boolean flag = false; // name exists
        String name = printHKNameField.getText();
        for (String x : names) {
            if (x.equals(name)) {
                flag = true;
                break;
            }
        }

        if (flag == true) {
            ArrayList<Room> roomList = new ArrayList<>();

            String HKName = printHKNameField.getText();
            String date = printHKDatePicker.getValue().toString();
            Assignment assignment = new Assignment(HKName, date);
            for (int i = 0; i < assignment.getNumOfRooms(); i++) {
                Room room = new Room(assignment, i);
                roomList.add(room);
            }
            try {
                JasperCompileManager.compileReportToFile(
                        "resources//FirstJasper.jrxml",
                        "resources//FirstJasper.jasper");
                String sourceFileName =
                        "resources//FirstJasper.jasper";

                JRBeanCollectionDataSource beanColDataSource = new
                        JRBeanCollectionDataSource(roomList);
                Map parameters = new HashMap();

                JasperFillManager.fillReportToFile(sourceFileName ,parameters, beanColDataSource);
                JasperViewer.viewReport("resources//jasper_report_template.jrprint", false, false);
            } catch (JRException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed.");
            alert.setHeaderText(null);
            alert.setContentText("Entered name doesn't match any housekeeper's name.");
            alert.showAndWait();
        }
    }


    @FXML
    private void close (ActionEvent event) {
        Functions.close(event);
    }
}
