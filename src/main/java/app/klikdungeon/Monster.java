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

    public Monster() {
        // random id wquery get lba2ye
        int monsterID = (int) (Math.random() * 3) + 1;
        String monsterName = "";
        String description = "";
        int monsterHealth = 0;
        String imagePath = "";

        switch (monsterID) {
            case 1:
                monsterName = "Allawi";
                description = "FOOL!";
                monsterHealth = 100;
                imagePath = "File:C:\\Users\\YEiD\\Documents\\Uni3.1\\OOP2\\Project\\KlikDungeon\\src\\main\\java\\app\\klikdungeon\\assets\\monster.png";
                break;
            case 2:
                monsterName = "Budi";
                description = "I WILL DESTROY YOU";
                monsterHealth = 50;
                imagePath = "File:C:\\Users\\YEiD\\Documents\\Uni3.1\\OOP2\\Project\\KlikDungeon\\src\\main\\java\\app\\klikdungeon\\assets\\monster2.png";
                break;
            case 3:
                monsterName = "Caca";
                description = "WEAK HUMAN!";
                monsterHealth = 75;
                imagePath = "File:C:\\Users\\YEiD\\Documents\\Uni3.1\\OOP2\\Project\\KlikDungeon\\src\\main\\java\\app\\klikdungeon\\assets\\monster3.png";
                break;
        }
        this.monsterID = monsterID;
        this.monsterName = monsterName;
        this.dialogue = description;
        this.monsterHealth = monsterHealth;
        this.imagePath = imagePath;

    }

    public Monster(int monsterID, String monsterName, String description, int monsterHealth, String imagePath) {
        this.monsterID = monsterID;
        this.monsterName = monsterName;
        this.dialogue = description;
        this.monsterHealth = monsterHealth;
        this.imagePath = imagePath;
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

    public void playDeathSound() {
        Thread soundThread = new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(
                        "C:\\Users\\YEiD\\Documents\\Uni3.1\\OOP2\\Project\\KlikDungeon\\src\\main\\java\\app\\klikdungeon\\assets\\death.wav")
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

    public Monster generateMonster() {
        // random id wquery get lba2ye
        int monsterID = (int) (Math.random() * 3) + 1;
        String monsterName = "";
        String description = "";
        int monsterHealth = 0;
        String imagePath = "";

        switch (monsterID) {
            case 1:
                monsterName = "Allawi";
                description = "Fool !!!!!!!!";
                monsterHealth = 100;
                imagePath = "File:C:\\Users\\YEiD\\Documents\\Uni3.1\\OOP2\\Project\\KlikDungeon\\src\\main\\java\\app\\klikdungeon\\assets\\monster.png";
                break;
            case 2:
                monsterName = "Budi";
                description = "You're back ?!";
                monsterHealth = 50;
                imagePath = "File:C:\\Users\\YEiD\\Documents\\Uni3.1\\OOP2\\Project\\KlikDungeon\\src\\main\\java\\app\\klikdungeon\\assets\\monster.png";
                break;
            case 3:
                monsterName = "Caca";
                description = "I will DESTROY YOU";
                monsterHealth = 75;
                imagePath = "File:C:\\Users\\YEiD\\Documents\\Uni3.1\\OOP2\\Project\\KlikDungeon\\src\\main\\java\\app\\klikdungeon\\assets\\monster.png";
                break;
        }

        return new Monster(monsterID, monsterName, description, monsterHealth, imagePath);
    }
}
