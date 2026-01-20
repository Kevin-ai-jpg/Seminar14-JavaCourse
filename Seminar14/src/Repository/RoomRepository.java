package Repository;

import domain.Room;

import java.sql.*;
import java.util.ArrayList;

public class RoomRepository {
    private String url;
    private Connection connection;

    public RoomRepository(String url) {
        this.url = url;
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Room> getAllRooms() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Rooms");
            ArrayList<Room> rooms = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rooms.add(new Room(
                        resultSet.getInt("RoomNumber"),
                        resultSet.getString("RoomType"),
                        resultSet.getInt("PricePerNight"),
                        resultSet.getString("Description")
                ));
            }
            return rooms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAndClose() {
        try {
            if (!connection.isClosed()) {
                connection.commit();
                connection.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
