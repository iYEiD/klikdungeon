package app.klikdungeon;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Weapon> weapons;

    public Inventory() {
        weapons = new ArrayList<>();
    }

    public Inventory(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public void removeWeapon(Weapon weapon) {
        weapons.remove(weapon);
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public Weapon getWeapon(int index) {
        return weapons.get(index);
    }

    public int getWeaponCount() {
        return weapons.size();
    }

}
