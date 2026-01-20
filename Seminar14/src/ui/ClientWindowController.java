package ui;

import Service.HotelService;
import Service.Observer;
import domain.Bookings;
import domain.Client;
import domain.Room;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class ClientWindowController implements Observer {
    private Client client;
    private HotelService hotelService;

    public ClientWindowController(Client client, HotelService hotelService) {
        this.client = client;
        this.hotelService = hotelService;
        this.hotelService.addObserver(this);
    }

    @FXML
    private ListView<Room> roomsListView;
    @FXML
    private TextField dateField;
    @FXML
    private TextArea desciptionsTextArea;
    @FXML
    private Button bookButton;

    private ObservableList<Room> roomObservableList;
    private ObservableList<Client> clientObservableList;
    private ObservableList<Bookings> bookingObservableList;

    @FXML
    public void initialize() {
        try {
            ArrayList<Room> allRooms = hotelService.getAllRooms();
            roomObservableList = FXCollections.observableArrayList(allRooms);
            roomsListView.setItems(roomObservableList);

            update();
        }catch (Exception e) {
            showAlert(e.getMessage());
        }
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleBookButton() {
        Room selectedRoom = roomsListView.getSelectionModel().getSelectedItem();
        String bookingPeriod = dateField.getText();

        if (selectedRoom == null || bookingPeriod.isEmpty()) {
            showAlert("Please select a room and enter a booking period.");
            return;
        }

        String[] dates = bookingPeriod.split(" to ");
        if (dates.length != 2) {
            showAlert("Please enter the booking period in the format: YYYY-MM-DD to YYYY-MM-DD");
            return;
        }

        String startDate = dates[0].trim();
        String endDate = dates[1].trim();

        try {
            hotelService.makeBooking(client.getId(), selectedRoom.getType(), startDate, endDate);
            showAlert("Booking successful!");
        } catch (Exception e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    public void handleRoomSelection() {
        Room selectedRoom = roomsListView.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            try {
                String description = hotelService.getRoomDescription(selectedRoom.getNumber());
                desciptionsTextArea.setText(description);
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        }
    }


    @Override
    public void update() {
        Platform.runLater(() -> {
            try {
                ArrayList<Room> allRooms = hotelService.getAllRooms();
                roomObservableList = FXCollections.observableArrayList(allRooms);
                roomsListView.setItems(roomObservableList);
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        });
    }
}
