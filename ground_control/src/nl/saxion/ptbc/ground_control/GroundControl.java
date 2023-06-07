package nl.saxion.ptbc.ground_control;

import nl.saxion.ptbc.shared.Shared;
import nl.saxion.ptbc.sockets.SasaClient;
import nl.saxion.ptbc.sockets.SasaConnectionException;
import nl.saxion.ptbc.sockets.SasaSocketInformation;

import java.sql.*;

import java.sql.Connection;


/**
 * Ground Control base class
 */
public class GroundControl implements SasaSocketInformation {
    private final SasaClient sasaClient;

    public GroundControl(String host, int port) throws SasaConnectionException {
        // Setup server
        sasaClient = new SasaClient();
        sasaClient.subscribe(this);
        sasaClient.connect(host, port);
        System.out.println("Ground Control is connected to " + host + ":" + port);
    }

    public static void main(String[] args) {
        Connection conn = Shared.setupDatabaseConnection();
        try {
            String username = "test";
            String password = "testpassword";

            String sql = "INSERT INTO users (username, password) VALUES(?, ?)";

            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            int rowsInserted = stm.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User inserted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }


        try {


            // Setup Ground Control as client of The Pilot
            GroundControl thePilot = new GroundControl(Shared.COMMUNICATION_SERVER, Shared.COMMUNICATION_PORT);
            //TODO create the Gound Control dashboard

        } catch (SasaConnectionException exception) {
            System.err.println("Connection error: " + exception.getMessage());
        }
    }

    @Override
    public void receivedData(String message) {
        //TODO what to do when a message is received?
    }

    @Override
    public void statusUpdate(String client, String status) {
        //TODO what to do when the server (The Pilot) accepts (or disconnects)?
    }
}
