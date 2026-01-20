package Repository;

import domain.Bookings;
import domain.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingsRepository {
    private String url;
    private Connection connection;

    public BookingsRepository(String url) {
        this.url = url;
        try {
            connection = java.sql.DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addBooking(int clientId, int roomNumber, String startDate, String endDate) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Bookings (ClientID, RoomNumber, StartDate, EndDate) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, clientId);
            preparedStatement.setInt(2, roomNumber);
            preparedStatement.setString(3, startDate);
            preparedStatement.setString(4, endDate);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Bookings> getAllBookings() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Bookings");
            ArrayList<Bookings> bookings = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bookings.add(new Bookings(
                        resultSet.getInt("clientID"),
                        resultSet.getInt("RoomNumber"),
                        resultSet.getString("BookingPeriod")
                ));
            }
            return bookings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
