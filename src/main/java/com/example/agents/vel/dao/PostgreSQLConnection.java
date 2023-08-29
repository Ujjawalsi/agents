package com.example.agents.vel.dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgreSQLConnection {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bullseye_db";
    private static final String USER = "postgres";
    private static final String PASS = "root";

    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAlert(String jsonAlert) throws SQLException {
        String insertSql = "INSERT INTO ms_thousandeye_alert (alert_data) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
            preparedStatement.setString(1, jsonAlert);
            preparedStatement.executeUpdate();
        }
    }

    public void updateAlert(String newEventId, String dateEnd) throws SQLException {
        String updateSql = "UPDATE ms_thousandeye_alert SET dateEnd = ? WHERE newEventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
            preparedStatement.setString(1, dateEnd);
            preparedStatement.setString(2, newEventId);
            preparedStatement.executeUpdate();
        }
    }
}
