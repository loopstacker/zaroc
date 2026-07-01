package be.kdg.integration.gameapplication.model.databaseaccess;

import be.kdg.integration.gameapplication.model.credentials.CredentialsManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection{
    private static final String URL = CredentialsManager.DB_URL;
    private static final String USER = CredentialsManager.DB_USER;
    private static final String PASSWORD = CredentialsManager.DB_PASSWORD;
    private Connection connection;

    public DataBaseConnection() throws SQLException{
        try{
            connect();
        }catch (SQLException e) {
            throw e;
        }
    }

    private void connect() throws SQLException{
        try{
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e) {
            throw new SQLException("Failed to connect to database");
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public void closeConnection() throws SQLException{
        try {
            this.connection.close();
        } catch (SQLException ex) {
            throw new SQLException("Database ran into an error trying to close the connection");
        }
    }
}
