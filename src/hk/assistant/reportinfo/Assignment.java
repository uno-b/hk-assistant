package hk.assistant.reportinfo;

import hk.assistant.database.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Assignment {

    private String name;
    private ArrayList<String> guestList = new ArrayList<>();
    private ArrayList<String> roomList = new ArrayList<>();
    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayList<String> adultsList = new ArrayList<>();
    private ArrayList<String> teensList = new ArrayList<>();
    private ArrayList<String> childrenList = new ArrayList<>();
    private ArrayList<String> checkInDateList = new ArrayList<>();
    private ArrayList<String> checkOutDateList = new ArrayList<>();


    public Assignment (String HKName, String date) {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        name = HKName;
        String HKID = "";
        ResultSet rs = handler.execQuery("SELECT id FROM HK WHERE name = '" + name + "'");
        try {
            while (rs.next()) {
                HKID = rs.getString("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        rs = handler.execQuery("SELECT * FROM ASSIGNMENT WHERE HKID = '" + HKID + "' AND date = '" + date + "'");
        try {
            while (rs.next()) {
                roomList.add(rs.getString("gRoomNum"));
                stateList.add(rs.getString("status"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < roomList.size(); i++) {
            rs = handler.execQuery("SELECT * FROM GUESTS WHERE roomNum = '" + roomList.get(i) + "'");
            try {
                while (rs.next()) {
                    guestList.add(rs.getString("guestName"));
                    adultsList.add(rs.getString("numOfAdults"));
                    teensList.add(rs.getString("numOfYoung"));
                    childrenList.add(rs.getString("numOfChild"));
                    checkInDateList.add(rs.getString("checkInDate"));
                    checkOutDateList.add(rs.getString("checkOutDate"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public int getNumOfRooms() {
        return roomList.size();
    }

    public ArrayList<String> getRoomList() {
        return roomList;
    }

    public ArrayList<String> getStateList() {
        return stateList;
    }

    public ArrayList<String> getAdultsList() {
        return adultsList;
    }

    public ArrayList<String> getTeensList() {
        return teensList;
    }

    public ArrayList<String> getChildrenList() {
        return childrenList;
    }

    public ArrayList<String> getCheckInDateList() {
        return checkInDateList;
    }

    public ArrayList<String> getCheckOutDateList() {
        return checkOutDateList;
    }

    public ArrayList<String> getGuestList() {
        return guestList;
    }

    public String getName() {
        return name;
    }
}