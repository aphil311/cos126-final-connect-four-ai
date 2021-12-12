import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HelloFX extends Application {
    // true = RED, false = YELLOW
    private boolean gameStatus = false;
    private boolean player = true;
    private Board state = new Board();
    private Circle[][] board = new Circle[6][7];
    private Button[] buttons = new Button[7];
    private Text text = new Text();
    // 0 = BLACK, 1 = YELLOW, 2 = RED
    // public HelloFX (int[][] state, boolean player) {
    //     this.state = state;
    //     this.player = player;
    // }

    public boolean changeCircle(int x, int y) {
        if (player) {
            board[y][x].setFill(Color.RED);
            text.setText("Yellow Player's Turn");
        }
        else {
            board[y][x].setFill(Color.YELLOW);
            text.setText("Red Player's Turn");
        }
        return !player;
    }
    public void checkStatus() {
        int progress = state.checkWinner();
        if (progress != -1) {
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.INFORMATION);
            String winnerText = "Player " + ((progress == 2) ? "Red" : "Yellow") + " Wins!";
            a.setContentText(winnerText);
            text.setText(winnerText);
            a.show();
            for (Button btn : buttons) {
                btn.setDisable(true);
            }

        }
    }

    public void handleTwoPlayer(Group root) {
        root.getChildren().clear();
        text.setX(310);
        text.setY(30);
        text.setText("Red Player's Turn");
        root.getChildren().add(text);
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                Circle circ = new Circle(x * 100 + 60, y * 100 + 80, 30);
                int cellVal = state.getCell(x, y);
                root.getChildren().add(circ);
                board[y][x] = circ;
            }
        
            Button btn = new Button();
            btn.setText("Column " + (x + 1));
            btn.setLayoutX(25 + 100 * x);
            btn.setLayoutY(640);
            btn.setId(Integer.toString(x));
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int col = Integer.valueOf(btn.getId());
                    int val = (player ? 1 : 0) + 1;
                    try {
                        int[] c = state.insert(col, val);
                        player = changeCircle(c[0], c[1]);
                        checkStatus();
                    } catch (IllegalArgumentException e) {
                        Alert a = new Alert(AlertType.NONE);
                        a.setAlertType(AlertType.WARNING);
                        a.setContentText("You can't make this move!");
                        a.show();
                    }
                    System.out.println(state);
                }
            });
            root.getChildren().add(btn);
            buttons[x] = btn;
        }
    }

    public void start(Stage stage) {
        Group root = new Group();
        Text startText = new Text("Which mode would you like to play?");
        startText.setX(260);
        startText.setY(200);
        root.getChildren().add(startText);
        Button twoPlayer = new Button("Two-Player Mode");
        Button onePlayer = new Button("Single-Player Mode");
        twoPlayer.setLayoutX(200);
        twoPlayer.setLayoutY(300);
        twoPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleTwoPlayer(root);
            }
        });
        onePlayer.setLayoutX(400);
        onePlayer.setLayoutY(300);
        root.getChildren().addAll(twoPlayer, onePlayer);
        
        Scene startScene = new Scene(root, 720, 680);
        startScene.setFill(Color.BLUE);
        stage.setTitle("Connect 4");
        stage.setScene(startScene);
        stage.show();

        // text.setX(310);
        // text.setY(30);
        // text.setText("Red Player's Turn");
        // root.getChildren().add(text);
        // for (int x = 0; x < 7; x++) {
        //     for (int y = 0; y < 6; y++) {
        //         Circle circ = new Circle(x * 100 + 60, y * 100 + 80, 30);
        //         int cellVal = state.getCell(x, y);
        //         root.getChildren().add(circ);
        //         board[y][x] = circ;
        //     }
        
        //     Button btn = new Button();
        //     btn.setText("Column " + (x + 1));
        //     btn.setLayoutX(25 + 100 * x);
        //     btn.setLayoutY(640);
        //     btn.setId(Integer.toString(x));
        //     btn.setOnAction(new EventHandler<ActionEvent>() {
        //         @Override
        //         public void handle(ActionEvent event) {
        //             int col = Integer.valueOf(btn.getId());
        //             int val = (player ? 1 : 0) + 1;
        //             try {
        //                 int[] c = state.insert(col, val);
        //                 player = changeCircle(c[0], c[1]);
        //                 checkStatus();
        //             } catch (IllegalArgumentException e) {
        //                 Alert a = new Alert(AlertType.NONE);
        //                 a.setAlertType(AlertType.WARNING);
        //                 a.setContentText("You can't make this move!");
        //                 a.show();
        //             }
        //             System.out.println(state);
        //         }
        //     });
        //     root.getChildren().add(btn);
        //     buttons[x] = btn;
        // }
        // Scene scene = new Scene(root, 720, 680);
        // scene.setFill(Color.BLUE);
        // stage.setTitle("Connect 4");
        // stage.setScene(scene);
        // stage.show();
    }
}