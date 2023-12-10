package app.klikdungeon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;

    public DatabaseManager() {
        connect();
    }

    public void connect() {
        try {
            // Database connection details
            String DB_URL = "jdbc:mysql://localhost:3306/klikdungeon";
            String DB_USER = "root";
            String DB_PASSWORD = "root";

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Disconnected from the database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String name, String password) {
        boolean isAuthenticated = false;
        String query = "SELECT * FROM player WHERE name = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            isAuthenticated = resultSet.next();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }

    public void addPlayer(String name, String password) {
        String query = "INSERT INTO player (name, password, level, gold) VALUES (?, ?, 1, 0)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Player added successfully: " + name);
            } else {
                System.out.println("Failed to add player: " + name);
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer(String name, String password) {
        Player player = null;
        String query = "SELECT * FROM player WHERE name = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int playerID = resultSet.getInt("idplayer");
                String playerName = resultSet.getString("name");
                int playerLevel = resultSet.getInt("level");
                int playerGold = resultSet.getInt("gold");

                player = new Player(playerID, playerName, playerLevel, playerGold);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return player;
    }

    public void saveGame(Player player) {
        String query = "UPDATE player SET gold = ?, level = ? WHERE idplayer = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, player.getGold());
            preparedStatement.setInt(2, player.getPlayerLevel());
            preparedStatement.setInt(3, player.getPlayerID());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Game saved successfully for player: " + player.getPlayerName());
            } else {
                System.out.println("Failed to save game for player: " + player.getPlayerName());
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // public int getScore(String playerName, String playerPassword) {
    // int score = -1;
    // if (authenticate(playerName, playerPassword)) {
    // String query = "SELECT score FROM player WHERE playername = ?";

    // try {
    // PreparedStatement preparedStatement = connection.prepareStatement(query);
    // preparedStatement.setString(1, playerName);

    // ResultSet resultSet = preparedStatement.executeQuery();
    // if (resultSet.next()) {
    // score = resultSet.getInt("score");
    // }
    // resultSet.close();
    // preparedStatement.close();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }
    // return score;
    // }

    // public void updateScore(String playerName, String playerPassword, int
    // newScore) {
    // if (authenticate(playerName, playerPassword)) {
    // String updateQuery = "UPDATE player SET score = ? WHERE playername = ?";
    // try {
    // PreparedStatement preparedStatement =
    // connection.prepareStatement(updateQuery);
    // preparedStatement.setInt(1, newScore);
    // preparedStatement.setString(2, playerName);

    // int rowsAffected = preparedStatement.executeUpdate();
    // if (rowsAffected > 0) {
    // System.out.println("Score updated successfully for player: " + playerName);
    // } else {
    // System.out.println("Failed to update the score for player: " + playerName);
    // }
    // preparedStatement.close();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }
    // }
}
