package app.klikdungeon;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Monster {
    private int monsterID;
    private String monsterName;
    private String dialogue;
    private int monsterHealth;
    private String imagePath;
    private String deathPath;

    public Monster(Player player, DatabaseManager dbManager) {
        int monsterID = (int) (Math.random() * 5) + 1;
        Monster generatedMonster = dbManager.getMonster(monsterID);
        this.monsterID = monsterID;
        this.monsterName = generatedMonster.getMonsterName();
        this.dialogue = generatedMonster.getDialogue();
        this.monsterHealth = generatedMonster.getMonsterHealth() + player.getPlayerLevel() * 13;
        this.imagePath = generatedMonster.getImagePath();
        this.deathPath = generatedMonster.getDeathPath();

    }

    public Monster(int monsterID, String monsterName, String dialogue, int monsterHealth, String imagePath,
            String deathPath) {
        this.monsterID = monsterID;
        this.monsterName = monsterName;
        this.dialogue = dialogue;
        this.monsterHealth = monsterHealth;
        this.imagePath = imagePath;
        this.deathPath = deathPath;
    }

    public int getMonsterID() {
        return monsterID;
    }

    public void setMonsterID(int monsterID) {
        this.monsterID = monsterID;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    public int getMonsterHealth() {
        return monsterHealth;
    }

    public void setMonsterHealth(int monsterHealth) {
        this.monsterHealth = monsterHealth;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDeathPath() {
        return deathPath;
    }

    public void setDeathPath(String deathPath) {
        this.deathPath = deathPath;
    }

    public void playDeathSound(Monster monster) {
        Thread soundThread = new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(
                        monster.getDeathPath())
                        .getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                // uncoment to play sound
                clip.start();
                // 30k
                Thread.sleep(3000);

                clip.stop();
                clip.close();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        soundThread.start();
    }

}
