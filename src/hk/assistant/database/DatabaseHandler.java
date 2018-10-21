package hk.assistant.database;

import hk.assistant.ui.listguest.GuestListController;
import hk.assistant.ui.listhk.HKListController;

import javax.swing.*;
import java.sql.*;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;


    private DatabaseHandler() {

        createConnection();
        setupGuestTable();
        setupHKTable();
        setupAssignmentTable();
        setupUncleanRoomTable();
    }


    public static DatabaseHandler getInstance() {

        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }


    void createConnection() {

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void setupGuestTable() {

        String TABLE_NAME = "GUESTS";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + " ("
                        + " guestName varchar(200), \n"
                        + " roomNum varchar(100) primary key, \n"
                        + " checkInDate varchar(200), \n"
                        + " checkOutDate varchar(200), \n"
                        + " numOfAdults varchar(10), \n"
                        + " numOfYoung varchar(10), \n"
                        + " numOfChild varchar(10), \n"
                        + " isVip boolean default false"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }


    void setupHKTable() {

        String TABLE_NAME = "HK";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + " ("
                        + " name varchar(200), \n"
                        + " id varchar(100) primary key, \n"
                        + " dateOfBirth varchar(100), \n"
                        + " phoneNum varchar(100), \n"
                        + " roomNum varchar(20), \n"
                        + " startDate varchar(100), \n"
                        + " endDate varchar(100)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }


    void setupAssignmentTable() {

        String TABLE_NAME = "ASSIGNMENT";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                String qu = "CREATE TABLE " + TABLE_NAME + "("
                        + "        gRoomNum varchar(100) primary key, \n"
                        + "         HKID varchar(100), \n"
                        + "         issueTime timestamp default CURRENT_TIMESTAMP, \n"
                        + "         date varchar(100), \n"
                        + "         FOREIGN KEY (gRoomNum) REFERENCES GUESTS(roomNum), \n"
                        + "         FOREIGN KEY (HKID) REFERENCES HK(id), \n"
                        + "         status varchar(100)"
                        + " )";
                stmt.execute(qu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void setupUncleanRoomTable() {

        String TABLE_NAME = "UNCLEANROOMS";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                String qu = "CREATE TABLE " + TABLE_NAME + "("
                        + "        roomNum varchar(100) primary key, \n"
                        + "         checkOutDate varchar(100), \n"
                        + "         note varchar(200)"
                        + " )";
                stmt.execute(qu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet execQuery(String query) {

        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery: dataHandler " + ex.getLocalizedMessage());
            return null;
        }
        return result;
    }


    public boolean execAction(String qu) {

        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(),
                    "Error Occurred", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery: databaseHandler " + ex.getLocalizedMessage());
            return false;
        }
    }


    public boolean deleteGuest(GuestListController.Guest guest) {

        try {
            String deleteStatement = "DELETE FROM GUESTS WHERE roomNum = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, guest.getRoom());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean deleteHK(HKListController.HK hk) {

        try {
            String deleteStatement = "DELETE FROM HK WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, hk.getId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}