package domain;

public class Bookings {
    private int clientID;
    private int roomNo;
    private String bookingPeriod;

    public Bookings(int client, int room, String bookingPeriod) {
        this.clientID = client;
        this.roomNo = room;
        this.bookingPeriod = bookingPeriod;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getCheckInDate() {
        return bookingPeriod.split(" to ")[0];
    }


    public String getCheckOutDate() {
        return bookingPeriod.split(" to ")[1];
    }

    @Override
    public String toString() {
        return "Bookings{" +
                "client=" + clientID +
                ", room=" + roomNo +
                ", checkInDate='" + getCheckInDate() + '\'' +
                ", checkOutDate='" + getCheckOutDate() + '\'' +
                '}';
    }
}
