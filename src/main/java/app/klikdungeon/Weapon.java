package app.klikdungeon;

public class Weapon {
    private int weaponID;
    private String weaponName;
    private int weaponDamage;
    private int cost;

    public Weapon(int weaponID, String weaponName, int weaponDamage, int cost) {
        this.weaponID = weaponID;
        this.weaponName = weaponName;
        this.weaponDamage = weaponDamage;
        this.cost = cost;
    }

    public int getWeaponID() {
        return weaponID;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public int getWeaponDamage() {
        return weaponDamage;
    }

    public int getCost() {
        return cost;
    }

    public void setWeaponID(int weaponID) {
        this.weaponID = weaponID;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }

    public void setWeaponDamage(int weaponDamage) {
        this.weaponDamage = weaponDamage;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}
