package ui;

import Repository.BookingsRepository;
import Repository.ClientRepository;
import Repository.RoomRepository;
import Service.HotelService;
import domain.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainApp extends Application {
    private HotelService hotelService;

    @Override
    public void start(Stage primaryStage) {
        // Initialize database connection and service
        String databaseURL = "jdbc:sqlite:identifier.sqlite";
        ClientRepository clientRepository = new ClientRepository(databaseURL);
        BookingsRepository bookingsRepository = new BookingsRepository(databaseURL);
        RoomRepository roomRepository = new RoomRepository(databaseURL);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        hotelService = new HotelService(bookingsRepository, clientRepository, roomRepository);
        List<Client> allClients = hotelService.getAllClients();
        for(Client client : allClients) {
            openClientWindow(client, hotelService, 20 * client.getId());
        }
        openStaffWindow();
    }

    private void openClientWindow(Client user, HotelService service, double xOffset) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("ClientWindow.fxml"));

            loader.setControllerFactory(param -> new ClientWindowController(user, service));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();

            stage.setTitle("User " + user.getId() + ": " + user.getName());
            stage.setScene(scene);
            stage.setY(0);
            stage.setX(xOffset);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void openStaffWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StaffWindow.fxml"));

            loader.setControllerFactory(param -> new StaffWindowController(hotelService));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Staff Window - Hotel Management System");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading staff window: " + e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        // Clean up resources before closing
        if (hotelService != null) {
            // hotelService.saveAndClose();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
