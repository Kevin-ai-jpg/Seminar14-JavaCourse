package ui;

import Service.HotelService;
import Service.Observer;
import Service.Subject;
import domain.Bookings;
import domain.Client;
import domain.Room;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class StaffWindowController implements Observer {
    private HotelService hotelService;

    public StaffWindowController(HotelService hotelService) {
        this.hotelService = hotelService;
        this.hotelService.addObserver(this);
    }

    @FXML
    private ListView<Room> roomsListView;
    @FXML
    private ListView<Bookings> bookingsListView;
    @FXML
    private TextField price;
    @FXML
    private TextArea bookingsForRoomTextArea;

    private ObservableList<Room> roomObservableList;
    private ObservableList<Client> clientObservableList;
    private ObservableList<Bookings> bookingObservableList;

    //make the initialize method to load all rooms in the roomsListView and bookingsListView and total price in the price text field
    @FXML
    public void initialize() {
        // Implementation for initializing the staff window
        try {
            // Load all rooms
            roomObservableList = javafx.collections.FXCollections.observableArrayList(hotelService.getAllRooms());
            roomsListView.setItems(roomObservableList);

            // Load all bookings
            bookingObservableList = javafx.collections.FXCollections.observableArrayList(hotelService.getAllBookings());
            bookingsListView.setItems(bookingObservableList);

            // Calculate total price of all bookings
            int totalPrice = hotelService.getTotalPriceOfBookingsFromDate("2023-01-21");
            price.setText(String.valueOf(totalPrice));

            update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //handle when a room is selected from the roomListview to show all bookingd for that room in the bookingsForRoomTextArea and use the BookingsSortedForRoom function from the hotelService
    @FXML
    public void handleRoomSelection() {
        Room selectedRoom = roomsListView.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            StringBuilder bookingsInfo = new StringBuilder();
            for (Bookings booking : hotelService.getBookingsForRoom(selectedRoom.getNumber())) {
                bookingsInfo.append(booking.toString()).append("\n");
            }
            bookingsForRoomTextArea.setText(bookingsInfo.toString());
        }
    }



    @Override
    public void update() {
        Platform.runLater(() -> {;
            try {
                // Refresh rooms
                roomObservableList.setAll(hotelService.getAllRooms());
                roomsListView.setItems(roomObservableList);

                // Refresh bookings
                bookingObservableList.setAll(hotelService.getAllBookings());
                bookingsListView.setItems(bookingObservableList);

                // Update total price
                int totalPrice = hotelService.getTotalPriceOfBookingsFromDate("2026-01-21");
                price.setText(String.valueOf(totalPrice));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
