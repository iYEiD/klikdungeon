package app.klikdungeon;

public class Player {
    private int playerID;
    private String playerName;
    private int playerLevel;
    private int gold;
    private Inventory inventory;
    private int damageStat = 10;

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

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getPlayerDamage() {
        return damageStat;
    }

    public void setPlayerDamage(int damageStat) {
        this.damageStat = damageStat;
    }

    public void buffPlayer() {
        int totalDamage = 0;
        for (Weapon weapon : inventory.getWeapons()) {
            totalDamage += weapon.getWeaponDamage();
        }
        this.damageStat = totalDamage + 10; // 10 base damage
    }
}
