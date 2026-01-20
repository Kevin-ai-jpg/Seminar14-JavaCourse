package Service;

import Repository.BookingsRepository;
import Repository.ClientRepository;
import Repository.RoomRepository;
import domain.Bookings;
import domain.Client;
import domain.Room;

import java.util.ArrayList;
import java.util.List;

public class HotelService implements Subject {
    private BookingsRepository bookingsRepository;
    private ClientRepository clientRepository;
    private RoomRepository roomRepository;

    private List<Observer> observers = new ArrayList<>();

    public HotelService(BookingsRepository bookingsRepository, ClientRepository clientRepository, RoomRepository roomRepository) {
        this.bookingsRepository = bookingsRepository;
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
    }

    public ArrayList<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    public ArrayList<Room> getAllRooms() {
        return roomRepository.getAllRooms();
    }

    public ArrayList<Bookings> getAllBookings() {
        return bookingsRepository.getAllBookings();
    }

    public ArrayList<Bookings> getBookingsForRoom(int roomNumber) {
        ArrayList<Bookings> bookingsForRoom = new ArrayList<>();
        ArrayList<Bookings> allBookings = bookingsRepository.getAllBookings();
        for (Bookings booking : allBookings) {
            if (booking.getRoomNo() == roomNumber) {
                bookingsForRoom.add(booking);
            }
        }
        return bookingsForRoom;
    }

//    The clients can make bookings by selecting a room type and a period (start and end dates).
//    The application will search if there are any rooms of that type available in the given period
//    and if so, a booking will be added
    public void makeBooking(int clientId, String roomType, String startDate, String endDate) {
        ArrayList<?> allRooms = roomRepository.getAllRooms();
        for (Object roomObj : allRooms) {
            Room room = (Room) roomObj;
            if (room.getType().equals(roomType)) {
                ArrayList<Bookings> bookingsForRoom = getBookingsForRoom(room.getNumber());
                boolean isAvailable = true;
                for (Bookings booking : bookingsForRoom) {
                    String existingStartDate = booking.getCheckInDate();
                    String existingEndDate = booking.getCheckOutDate();
                    if (!(endDate.compareTo(existingStartDate) <= 0 || startDate.compareTo(existingEndDate) >= 0)) {
                        isAvailable = false;
                        break;
                    }
                }
                if (isAvailable) {
                    bookingsRepository.addBooking(clientId, room.getNumber(), startDate, endDate);
                    notifyObservers();
                    return;
                }
            }
        }
    }
//    The staff can select a room and all bookings for that room will be shown, sorted
//    chronologically, by booking period.
    public void getBookingsSortedForRoom(int roomNumber) {
        ArrayList<Bookings> bookingsForRoom = getBookingsForRoom(roomNumber);
        bookingsForRoom.sort((b1, b2) -> b1.getCheckInDate().compareTo(b2.getCheckInDate()));
        for (Bookings booking : bookingsForRoom) {
            System.out.println(booking);
        }
    }
//    When a room is selected in the client’s window, its description will be shown in another
//    control (e.g. text field/area)
    public String getRoomDescription(int roomNumber) {
        ArrayList<Room> allRooms = roomRepository.getAllRooms();
        for (Room room : allRooms) {
            if (room.getNumber() == roomNumber) {
                return room.getDescription();
            }
        }
        return "Room not found";
    }

    //function to get the total price of the bookings starting with the present date
    public int getTotalPriceOfBookingsFromDate(String presentDate) {
        int totalPrice = 0;
        ArrayList<Bookings> allBookings = bookingsRepository.getAllBookings();
        ArrayList<Room> allRooms = roomRepository.getAllRooms();
        for (Bookings booking : allBookings) {
            if (booking.getCheckInDate().compareTo(presentDate) >= 0) {
                for (Room room : allRooms) {
                    if (room.getNumber() == booking.getRoomNo()) {
                        int totalNights = Integer.parseInt(booking.getCheckOutDate().split("-")[2]) - Integer.parseInt(booking.getCheckInDate().split("-")[2]);
                        totalPrice += totalNights * room.getPricePerNight();
                    }
                }
            }
        }
        return totalPrice;
    }


    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers) {
            observer.update();
        }
    }

    public void saveAndClose() {
        roomRepository.saveAndClose();
    }
}
