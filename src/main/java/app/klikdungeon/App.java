package app.klikdungeon;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class App extends Application {
    static private DatabaseManager db = new DatabaseManager();
    final String soundTrack = "C:\\Users\\YEiD\\Documents\\Uni3.1\\OOP2\\Project\\KlikDungeon\\src\\main\\java\\app\\klikdungeon\\assets\\soundtrack.wav";

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        startUp(root);

        Scene scene = new Scene(root, 900, 700);

        stage.setScene(scene);
        stage.setTitle("Klik DUNGEON");
        stage.setResizable(false);
        stage.show();
    }

    private void startUp(StackPane root) {
        root.getChildren().clear();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundTrack).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            Pane background = new Pane();
            Image image = new Image(
                    "https://img.freepik.com/premium-vector/cave-with-light-hole-ground-game-background-illustration-vector-forest_597121-571.jpg");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(700);
            imageView.setFitWidth(900);
            background.getChildren().add(imageView);
            VBox loginBox = new VBox();
            loginBox.setPadding(new Insets(25, 50, 50, 50));
            loginBox.setSpacing(10);
            loginBox.setAlignment(Pos.CENTER);
            loginBox.setMaxWidth(400);
            loginBox.setMaxHeight(300);
            Label label = new Label("Welcome to Klik Dungeon!");

            label.setStyle("-fx-font-size: 20px; -fx-text-fill: #CCCC00; -fx-font-style: italic;");

            TextField username = new TextField();
            username.setPromptText("Username");
            username.setStyle(
                    "-fx-background-color: transparent; -fx-border-color: black; -fx-text-fill: yellow; -fx-border-radius: 10;");

            PasswordField password = new PasswordField();
            password.setPromptText("Password");
            password.setStyle(
                    "-fx-background-color: transparent; -fx-border-color: black; -fx-text-fill: black; -fx-border-radius: 10;");

            Button loginButton = new Button("Login");
            loginButton.setStyle("-fx-background-color: #CCCC00;");

            loginButton.setOnAction(e -> {
                if (db.authenticate(username.getText(), password.getText())) {
                    clip.stop();
                    clip.close();

                    System.out.println("Authenticated");
                    Player player = db.getPlayer(username.getText(), password.getText());
                    playGame(root, player);

                } else {
                    System.out.println("Not Authenticated"); // show that wrong password?or no account

                }

            });
            Button registerButton = new Button("Create Account");
            registerButton.setOnAction(e -> {
                clip.stop();
                clip.close();

                register(root);

            });
            registerButton.setStyle("-fx-background-color: #CCCC00;");
            loginBox.getChildren().addAll(label, username, password, loginButton, registerButton);
            root.getChildren().addAll(background, loginBox);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

        }
    }

    private void playGame(StackPane root, Player player) {
        root.getChildren().clear();

        Pane background = new Pane();
        Image image = new Image(
                "https://oldschool.runescape.wiki/images/thumb/Elven_rabbit_cave.png/1200px-Elven_rabbit_cave.png?180f1");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(700);
        imageView.setFitWidth(900);
        background.getChildren().add(imageView);

        BorderPane borderPane = new BorderPane();

        // menu
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: black;");
        Menu leaderboard = new Menu("Leaderboard");
        leaderboard.setStyle("-fx-background-color: #CCCC00;-fx-font-style: italic; -fx-font-weight: bold;");
        MenuItem topKills = new MenuItem("Kills");
        MenuItem topGold = new MenuItem("Gold");
        MenuItem topLevel = new MenuItem("Level");
        leaderboard.getItems().addAll(topKills, topGold, topLevel);
        Menu inventoryMenu = new Menu("Inventory");
        inventoryMenu.setStyle("-fx-background-color: #CCCC00;-fx-font-style: italic; -fx-font-weight: bold;");
        Menu shopMenu = new Menu("Shop");
        shopMenu.setStyle("-fx-background-color: #CCCC00;-fx-font-style: italic; -fx-font-weight: bold;");
        menuBar.getMenus().addAll(leaderboard, inventoryMenu, shopMenu);
        borderPane.setTop(menuBar);
        //
        // Monster Stuff
        final Monster monster = new Monster(player, db);

        VBox monsterBox = new VBox();
        monsterBox.setAlignment(Pos.CENTER);

        ImageView monsterView = new ImageView(new Image(monster.getImagePath()));
        monsterView.setFitHeight(350);
        monsterView.setFitWidth(300);
        monsterView.setTranslateY(50);

        Label monsterName = new Label(
                " '' " + monster.getDialogue() + " '' ");
        monsterName.setStyle("-fx-font-size: 20px; -fx-text-fill: #CCCC00; -fx-font-style: italic;");

        monsterView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            monster.setMonsterHealth(monster.getMonsterHealth() - 10);
            monsterName.setText(
                    monster.getMonsterName() + " : " + monster.getMonsterHealth());
            if (monster.getMonsterHealth() <= 0) {
                System.out.println("Monster has been slain!");
                player.setPlayerLevel(player.getPlayerLevel() + 1);
                int goldEarned = player.getPlayerLevel() * (int) (Math.random() * 3) + 1;
                player.setGold(player.getGold() + goldEarned);
                monster.playDeathSound(monster);
                db.saveGame(player);
                playGame(root, player);
            }
        });
        //
        HBox playerBox = new HBox();
        ImageView gold = new ImageView(new Image(
                "File:C:\\Users\\YEiD\\Documents\\Uni3.1\\OOP2\\Project\\KlikDungeon\\src\\main\\java\\app\\klikdungeon\\assets\\gold.png"));
        gold.setStyle("-fx-font-size: 20px; -fx-text-fill: #CCCC00; -fx-font-weight: bold;");
        gold.setFitHeight(50);
        gold.setFitWidth(50);
        Label goldLabel = new Label(Integer.toString(player.getGold()));
        goldLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #CCCC00; -fx-font-weight: bold;");
        playerBox.getChildren().addAll(gold, goldLabel);

        borderPane.setRight(playerBox);

        monsterBox.getChildren().addAll(monsterName, monsterView);

        // Set the VBox in the center of the BorderPane
        borderPane.setCenter(monsterBox);

        root.getChildren().addAll(background, borderPane);
    }

    private void register(StackPane root) {

        root.getChildren().clear();
        Pane background = new Pane();
        Image image = new Image(
                "https://img.freepik.com/premium-vector/cave-with-light-hole-ground-game-background-illustration-vector-forest_597121-571.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(700);
        imageView.setFitWidth(900);
        background.getChildren().add(imageView);
        VBox loginBox = new VBox();
        loginBox.setPadding(new Insets(25, 50, 50, 50));
        loginBox.setSpacing(10);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setMaxWidth(400);
        loginBox.setMaxHeight(300);
        Label label = new Label("Create A New Account");

        label.setStyle("-fx-font-size: 20px; -fx-text-fill: #CCCC00; -fx-font-style: italic;");

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setStyle(
                "-fx-background-color: transparent; -fx-border-color: black; -fx-text-fill: yellow; -fx-border-radius: 10;");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setStyle(
                "-fx-background-color: transparent; -fx-border-color: black; -fx-text-fill: black; -fx-border-radius: 10;");

        Button registerButton = new Button("Create");
        registerButton.setOnAction(e -> {
            db.addPlayer(username.getText(), password.getText());
            startUp(root);
        });
        registerButton.setStyle("-fx-background-color: #CCCC00;");
        loginBox.getChildren().addAll(label, username, password, registerButton);
        root.getChildren().addAll(background, loginBox);

    }

    public static void main(String[] args) {
        launch();
    }
}