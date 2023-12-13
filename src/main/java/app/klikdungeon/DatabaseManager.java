package app.klikdungeon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        String checkQuery = "SELECT * FROM player WHERE name = ?";
        String insertQuery = "INSERT INTO player (name, password, level, gold) VALUES (?, ?, 1, 0)";

        try {
            // Check if the name is already taken
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, name);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                System.out.println("Name is already taken: " + name);
                return; // Exit the method if the name is already taken
            }

            // Insert the player into the database
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, name);
            insertStatement.setString(2, password);

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Player added successfully: " + name);
            } else {
                System.out.println("Failed to add player: " + name);
            }

            checkResult.close();
            checkStatement.close();
            insertStatement.close();
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
        if (player != null) {
            System.out.println("setting inventory");
            player.setInventory(getInventory(player));
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

    public Monster getMonster(int monsterID) {
        Monster monster = null;
        String query = "SELECT * FROM monster WHERE idmonster = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, monsterID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String dialogue = resultSet.getString("dialogue");
                int health = resultSet.getInt("health");
                String imagePath = resultSet.getString("imagepath");

                String deathPath = resultSet.getString("deathpath");

                monster = new Monster(monsterID, name, dialogue, health, imagePath, deathPath);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return monster;
    }

    public List<Player> getTopPlayers() {
        List<Player> topPlayers = new ArrayList<>();
        String query = "SELECT * FROM player ORDER BY level DESC LIMIT 15";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int playerId = resultSet.getInt("idplayer");
                String playerName = resultSet.getString("name");
                int playerLevel = resultSet.getInt("level");
                int playerGold = resultSet.getInt("gold");

                Player player = new Player(playerId, playerName, playerLevel, playerGold);
                topPlayers.add(player);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topPlayers;
    }

    public List<Weapon> getWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        String query = "SELECT * FROM weapon";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int weaponId = resultSet.getInt("idweapon");
                String weaponName = resultSet.getString("name");
                int weaponDamage = resultSet.getInt("damage");
                int weaponCost = resultSet.getInt("cost");

                Weapon weapon = new Weapon(weaponId, weaponName, weaponDamage, weaponCost);
                weapons.add(weapon);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return weapons;
    }

    public Inventory getInventory(Player player) {
        Inventory inventory = new Inventory();
        List<Weapon> weapons = new ArrayList<>();
        String query = "SELECT weapon.* FROM weapon " +
                "JOIN inventory ON weapon.idweapon = inventory.idweapon " +
                "WHERE inventory.idplayer = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, player.getPlayerID());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int weaponId = resultSet.getInt("idweapon");
                String weaponName = resultSet.getString("name");
                int weaponDamage = resultSet.getInt("damage");
                int weaponCosts = resultSet.getInt("cost");
                System.out.println("Weapon: " + weaponName + " " + weaponDamage + " " + weaponCosts);
                // Assuming Weapon class has a constructor that takes these parameters
                Weapon weapon = new Weapon(weaponId, weaponName, weaponDamage, weaponCosts);
                weapons.add(weapon);
            }

            inventory.setWeapons(weapons);

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventory;
    }

    public void removeWeapon(Player player, Weapon weapon) {
        String query = "DELETE FROM inventory WHERE idplayer = ? AND idweapon = ? LIMIT 1";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, player.getPlayerID());
            preparedStatement.setInt(2, weapon.getWeaponID());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addWeapon(Player player, Weapon weapon) {
        String query = "INSERT INTO inventory (idplayer, idweapon) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, player.getPlayerID());
            preparedStatement.setInt(2, weapon.getWeaponID());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWeapon(Weapon weapon) {
        String deleteWeaponQuery = "DELETE FROM weapon WHERE idweapon = ?";
        String deleteInventoryQuery = "DELETE FROM inventory WHERE idweapon = ?";

        try {
            // Get player IDs from inventory table
            String getPlayerIDsQuery = "SELECT idplayer FROM inventory WHERE idweapon = ?";
            PreparedStatement getPlayerIDsStatement = connection.prepareStatement(getPlayerIDsQuery);
            getPlayerIDsStatement.setInt(1, weapon.getWeaponID());
            ResultSet playerIDsResult = getPlayerIDsStatement.executeQuery();

            List<Integer> playerIDs = new ArrayList<>();
            while (playerIDsResult.next()) {
                playerIDs.add(playerIDsResult.getInt("idplayer"));
            }

            getPlayerIDsStatement.close();
            playerIDsResult.close();

            // Delete weapon from weapon table
            PreparedStatement deleteWeaponStatement = connection.prepareStatement(deleteWeaponQuery);
            deleteWeaponStatement.setInt(1, weapon.getWeaponID());
            deleteWeaponStatement.executeUpdate();
            deleteWeaponStatement.close();

            // Delete weapon from inventory table
            PreparedStatement deleteInventoryStatement = connection.prepareStatement(deleteInventoryQuery);
            deleteInventoryStatement.setInt(1, weapon.getWeaponID());
            deleteInventoryStatement.executeUpdate();
            deleteInventoryStatement.close();

            // Update player's gold
            String updateGoldQuery = "UPDATE player SET gold = gold + ? WHERE playerID = ?";
            PreparedStatement updateGoldStatement = connection.prepareStatement(updateGoldQuery);
            for (int playerID : playerIDs) {
                updateGoldStatement.setInt(1, weapon.getCost());
                updateGoldStatement.setInt(2, playerID);
                updateGoldStatement.executeUpdate();
            }
            updateGoldStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createWeapon(String name, int damage, int cost) {
        String insertWeaponQuery = "INSERT INTO weapon (name, damage, cost) VALUES (?, ?, ?)";

        try {
            PreparedStatement insertWeaponStatement = connection.prepareStatement(insertWeaponQuery);
            insertWeaponStatement.setString(1, name);
            insertWeaponStatement.setInt(2, damage);
            insertWeaponStatement.setInt(3, cost);
            insertWeaponStatement.executeUpdate();
            insertWeaponStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
