package hk.assistant.reportinfo;

public class Room {

    private String name;
    private String guestName;
    private String state;
    private String room;
    private String adults;
    private String teens;
    private String children;
    private String checkInDate;
    private String checkOutDate;

    private int numOfRooms;


    public Room (Assignment as, int index) {
        name = as.getName();
        numOfRooms = as.getNumOfRooms();
        guestName = as.getGuestList().get(index);
        state = as.getStateList().get(index);
        room = as.getRoomList().get(index);
        adults = as.getAdultsList().get(index);
        teens = as.getTeensList().get(index);
        children = as.getChildrenList().get(index);
        checkInDate = as.getCheckInDateList().get(index);
        checkOutDate = as.getCheckOutDateList().get(index);
    }


    public String getName() {
        return name;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getState() {
        return state;
    }

    public String getRoom() {
        return room;
    }

    public String getAdults() {
        return adults;
    }

    public String getTeens() {
        return teens;
    }

    public String getChildren() {
        return children;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
}
