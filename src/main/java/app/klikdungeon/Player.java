package app.klikdungeon;

public class Player {
    private int playerID;
    private String playerName;
    private int playerLevel;
    private int gold;
    public Inventory inventory;

    public Player(int playerID, String playerName, int playerLevel, int gold) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.playerLevel = playerLevel;
        this.gold = gold;
        this.inventory = new Inventory();
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
