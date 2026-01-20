package Repository;

import domain.Client;

import java.sql.*;
import java.util.ArrayList;

public class ClientRepository {
    private String url;
    private Connection connection;

    public ClientRepository(String url) {
        this.url = url;
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Client> getAllClients() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Clients");
            ArrayList<Client> clients = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clients.add(new Client(
                        resultSet.getString("ClientName"),
                        resultSet.getString("Email"),
                        resultSet.getInt("ClientID")
                ));
            }
            return clients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
