package app.klikdungeon;

import java.io.File;
import java.io.IOException;

import javafx.scene.paint.Color;

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
        Menu leaderboardMenu = new Menu("Leaderboard");
        leaderboardMenu.setStyle("-fx-background-color: #CCCC00;-fx-font-style: italic; -fx-font-weight: bold;");
        MenuItem leaderboardItem = new MenuItem("View");
        leaderboardItem.setOnAction(e -> {
            System.out.println("Leaderboard clicked!");
            displayLeaderboard(root, player, menuBar);
        });
        Menu inventoryMenu = new Menu("Inventory");
        MenuItem inventoryItem = new MenuItem("View");
        inventoryMenu.setStyle("-fx-background-color: #CCCC00;-fx-font-style: italic; -fx-font-weight: bold;");
        inventoryItem.setOnAction(e -> {
            System.out.println("Inventory clicked!");
            // displayInventory(root, player);
        });

        Menu shopMenu = new Menu("Shop");
        MenuItem shopItem = new MenuItem("View");
        shopMenu.setStyle("-fx-background-color: #CCCC00;-fx-font-style: italic; -fx-font-weight: bold;");
        shopItem.setOnAction(e -> {
            System.out.println("Shop clicked!");
            displayShop(root, player, menuBar);
        });

        Menu gameMenu = new Menu("Game");
        MenuItem gameItem = new MenuItem("Sign Out");
        MenuItem gameItem2 = new MenuItem("Fight");
        gameMenu.setStyle(
                "-fx-background-color: #8B0000;-fx-font-style: italic; -fx-font-weight: bold; -fx-text-fill: black;");
        gameItem.setOnAction(e -> {
            System.out.println("Sign Out clicked!");
            startUp(root);
        });
        gameItem2.setOnAction(e -> {
            System.out.println("Fight clicked!");
            playGame(root, player);
        });
        gameMenu.getItems().addAll(gameItem, gameItem2);
        leaderboardMenu.getItems().add(leaderboardItem);
        inventoryMenu.getItems().add(inventoryItem);
        shopMenu.getItems().add(shopItem);

        menuBar.getMenus().addAll(leaderboardMenu, inventoryMenu, shopMenu, gameMenu);
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

    public void displayLeaderboard(StackPane root, Player player, MenuBar menuBar) {
        root.getChildren().clear();
        Pane background = new Pane();
        Image image = new Image(
                "https://media.thenerdstash.com/wp-content/uploads/2023/08/Best-Solo-Classes-in-Dark-and-Darker.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(700);
        imageView.setFitWidth(900);
        background.getChildren().add(imageView);
        BorderPane borderPane = new BorderPane();

        borderPane.setTop(menuBar);

        VBox leaderboardBox = new VBox();
        leaderboardBox.setAlignment(Pos.CENTER);
        Label leaderboardLabel = new Label("Top Monster Killers");
        leaderboardLabel.setStyle(
                "-fx-font-size: 25px; -fx-text-fill: #CCCC00; -fx-font-weight: bold;");
        leaderboardBox.getChildren().add(leaderboardLabel);

        TableView<Player> tableView = new TableView<>();
        TableColumn<Player, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("playerID"));
        TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        TableColumn<Player, Integer> totalLevelColumn = new TableColumn<>("Monsters Killed");
        totalLevelColumn.setCellValueFactory(new PropertyValueFactory<>("playerLevel"));
        TableColumn<Player, Integer> goldColumn = new TableColumn<>("Gold");
        goldColumn.setCellValueFactory(new PropertyValueFactory<>("gold"));

        tableView.getColumns().addAll(idColumn, nameColumn, totalLevelColumn, goldColumn);
        // Set the columns to the same size
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width
        nameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width
        totalLevelColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width
        goldColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width

        ObservableList<Player> players = FXCollections.observableArrayList();
        players.addAll(db.getTopPlayers());

        tableView.setItems(players);
        leaderboardBox.getChildren().add(tableView);
        borderPane.setCenter(leaderboardBox);
        root.getChildren().addAll(background, borderPane);

    }

    public void displayShop(StackPane root, Player player, MenuBar menuBar) {
        root.getChildren().clear();
        Pane background = new Pane();
        Image image = new Image("https://i.pinimg.com/736x/cd/01/ca/cd01cafef69c16afe07f05c4a127e776.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(700);
        imageView.setFitWidth(900);
        background.getChildren().add(imageView);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);

        VBox shopBox = new VBox();
        shopBox.setAlignment(Pos.CENTER);
        Label shopLabel = new Label("Dungeon Shop");
        shopLabel.setStyle(
                "-fx-font-size: 25px; -fx-text-fill: #CCCC00; -fx-font-weight: bold;");
        shopBox.getChildren().add(shopLabel);

        TableView<Weapon> tableView = new TableView<>();
        TableColumn<Weapon, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("weaponName"));
        TableColumn<Weapon, Integer> damageColumn = new TableColumn<>("Damage");
        damageColumn.setCellValueFactory(new PropertyValueFactory<>("weaponDamage"));
        TableColumn<Weapon, Integer> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        TableColumn<Weapon, Integer> purchaseColumn = new TableColumn<>("Action");
        purchaseColumn.setCellValueFactory(new PropertyValueFactory<>("gold"));

        tableView.getColumns().addAll(nameColumn, damageColumn, costColumn, purchaseColumn);
        // Set the columns to the same size
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        nameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width
        damageColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width
        costColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width
        purchaseColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width

        ObservableList<Weapon> weapons = FXCollections.observableArrayList();
        weapons.addAll(db.getWeapons());

        tableView.setItems(weapons);
        shopBox.getChildren().add(tableView);
        borderPane.setCenter(shopBox);
        root.getChildren().addAll(background, borderPane);
    }
    //

    // }
    // private void displayShop(StackPane root, Player player, MenuBar menuBar) {
    // root.getChildren().clear();
    // Pane background = new Pane();
    // Image image = new
    // Image("https://www.gamepur.com/wp-content/uploads/2022/12/Dark-and-Darker-loot.jpg?w=1200");
    // ImageView imageView = new ImageView(image);
    // imageView.setFitHeight(700);
    // imageView.setFitWidth(900);
    // background.getChildren().add(imageView);
    // BorderPane borderPane = new BorderPane();
    // borderPane.setTop(menuBar);

    // GridPane gridPane = new GridPane();
    // gridPane.setAlignment(Pos.CENTER);
    // gridPane.setHgap(10);
    // gridPane.setVgap(10);

    // TableView<Weapon> tableView = new TableView<>();
    // TableColumn<Weapon, String> nameColumn = new TableColumn<>("Name");
    // nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    // TableColumn<Weapon, Integer> damageColumn = new TableColumn<>("Damage");
    // damageColumn.setCellValueFactory(new PropertyValueFactory<>("damage"));
    // TableColumn<Weapon, Integer> costColumn = new TableColumn<>("Cost");
    // costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
    // TableColumn<Weapon, Weapon> purchaseColumn = new TableColumn<>("Purchase");
    // purchaseColumn.setCellValueFactory(new PropertyValueFactory<>("this"));

    // // Add event handler to the purchase column
    // purchaseColumn.setCellFactory(column -> {
    // return new TableCell<Weapon, Weapon>() {
    // final Button purchaseButton = new Button("Purchase");

    // {
    // purchaseButton.setOnAction(new EventHandler<ActionEvent>() {
    // @Override
    // public void handle(ActionEvent event) {
    // Weapon weapon = getTableView().getItems().get(getIndex());
    // // Perform action with the details of the selected row
    // // TODO: Add your action here
    // }
    // });
    // }

    // @Override
    // protected void updateItem(Weapon weapon, boolean empty) {
    // super.updateItem(weapon, empty);
    // if (weapon == null || empty) {
    // setGraphic(null);
    // } else {
    // setGraphic(purchaseButton);
    // }
    // }
    // };
    // });

    // tableView.getColumns().addAll(nameColumn, damageColumn, costColumn,
    // purchaseColumn);
    // gridPane.add(tableView, 0, 0);
    // borderPane.setCenter(gridPane);
    // root.getChildren().addAll(background, borderPane);
    // }

    public static void main(String[] args) {
        launch();
    }
}