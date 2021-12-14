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
import java.util.concurrent.TimeUnit;

/*
RESOURCES USED:
- https://openjfx.io/openjfx-docs/
- https://docs.oracle.com/javafx/2/get_started/hello_world.htm
- https://docs.oracle.com/javase/8/javafx/api/index.html

Oracle and JavaFX docs were used throughout programming; 
looked at pages for Buttons, Circle, Group, Scene, Text, Alert, etc.
 */

public class GameVisualizer extends Application {
    // MonteCarlo AI that plays against user
    private MonteCarlo mc;
    // boolean value for whether the game is one-player or two-player
    private boolean isOnePlayer = true;
    // boolean value for whose turn it is, true = RED, false = YELLOW
    private boolean player = true;
    // Board object for the state of the game
    // 0 = BLACK, 1 = YELLOW, 2 = RED (set to all 0's at first)
    private Board state;
    // two-dimensional array for circles on the screen
    private Circle[][] board = new Circle[6][7];
    // one-dimensional array for the buttons on the screen
    private Button[] buttons = new Button[7];
    // group initialized that GUI elements are added to
    private Group root = new Group();
    // text element that is accessed by methods to be changed
    private Text text = new Text();
    // creates the scene
    Scene startScene = new Scene(root, 720, 680);

    // method that changes the visual GUI board, takes coordinates and the stage as input
    public boolean changeCircle(int x, int y, Stage stage) {
        // if player is red:
        if (player) {
            // fill the specified slot with red and change to yellow's turn
            board[y][x].setFill(javafx.scene.paint.Color.RED);
            text.setText("Yellow Player's Turn");
        }
        else { // if player is yellow:
            // fill the specified slot with yellow and change to red's turn
            board[y][x].setFill(javafx.scene.paint.Color.YELLOW);
            text.setText("Red Player's Turn");
        }
        // check if game is won or full
        if (checkStatus()) {
            // if so, add a restart button
            Button restart = new Button("Restart Game");
            // clear the player turn text
            text.setText("");
            restart.setLayoutX(310);
            restart.setLayoutY(10);
            // set the restart button to the initial screen
            restart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    start(stage);
                }
            });
            // add button to the screen
            root.getChildren().add(restart);
        }
        // if no player has won yet, return the next player to move
        return !player;
    }

    // checks status of game
    public boolean checkStatus() {
        // if board is full, announce a tie and create an alert for it
        if (state.isFull()) {
            createAlert("Tie!");
            return true;
        }
        // otherwise, check for a winner
        int progress = state.checkWinner();
        // if no one has won, continue on with the game
        if (progress == -1) return false;
        // otherwise, announce the given winner
        String alertText = "Player " + ((progress == 2) ? "Red" : "Yellow") + " Wins!";
        // print the winner, and create an alert
        System.out.println(alertText);  
        createAlert(alertText);
        return true;
    }

    // Method for creating an ending alert given a message
    // disables all buttons as well
    public void createAlert(String s) {
        // create an alert with specified message
        Alert a = new Alert(AlertType.NONE);
        a.setAlertType(AlertType.INFORMATION);
        a.setContentText(s);
        text.setText(s);
        a.show();
        // disable each button
        for (Button btn : buttons) {
            btn.setDisable(true);
        }
    }

    // starts the game after the initial screen
    public void handleGame(Group root, Stage stage) {
        // clears all current elements
        root.getChildren().clear();
        // sets the player text at the top of the game
        text.setX(310);
        text.setY(30);
        // first player should be red at start of game
        text.setText("Red Player's Turn");
        player = true;
        // adds text to the root
        root.getChildren().add(text);
        // for each column on the board
        for (int x = 0; x < 7; x++) {
            // for each row on the board
            for (int y = 0; y < 6; y++) {
                // create a circle that is situated accordingly on the board
                Circle circ = new Circle(x * 100 + 60, y * 100 + 80, 30);
                // add circle to root and to the board object
                root.getChildren().add(circ);
                board[y][x] = circ;
            }
            // creates new button per column
            Button btn = new Button("Column " + (x + 1));
            // situates on the bottom of the screen
            btn.setLayoutX(25 + 100 * x);
            btn.setLayoutY(640);
            // set an id according to the column position
            btn.setId(Integer.toString(x));
            // button handler for onclick
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // get the value of the button, represents the col of the input
                    int col = Integer.valueOf(btn.getId());
                    // value should be 2 if red, 1 if yellow
                    // 0 = BLACK, 1 = YELLOW, 2 = RED
                    int val = (player ? 1 : 0) + 1;
                    // use try in case player tries an invalid move
                    try {
                        // add a chip to the given column and return [row, col]
                        int[] c = state.insert(col, val);
                        // change the circle of the given board space, return who the next player is
                        player = changeCircle(c[0], c[1], stage);
                        // check if the game is filled up or has a winner
                        if (checkStatus()) {
                            // create a restart button
                            Button restart = new Button("Restart Game");
                            text.setText("");
                            restart.setLayoutX(310);
                            restart.setLayoutY(10);
                            // go to the initial screen when clicking the restart button
                            restart.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    start(stage);
                                }
                            });
                            // add restart button to screen
                            root.getChildren().add(restart);
                        }
                        // if move is invalid, create an alert that notifies the player of this
                    } catch (IllegalArgumentException e) {
                        Alert a = new Alert(AlertType.NONE);
                        a.setAlertType(AlertType.WARNING);
                        a.setContentText("You can't make this move!");
                        a.show();
                    }
                    // if there is only one player, have the AI make a move 
                    if (isOnePlayer) {
                        // create a new MonteCarlo with the most recent state
                        mc = new MonteCarlo(1, state, 2, 25000, "");
                        // have the mc make a move and return the coordinates of said move
                        int[] c = mc.move();
                        // change the circle of the given board space, return who the next player is
                        player = changeCircle(c[0], c[1], stage);
                    }
                }
            });
            // add the button to the screen and the button array
            root.getChildren().add(btn);
            buttons[x] = btn;
        }
    }

    // method for the initial selection screen
    public void start(Stage stage) {
        // clears all elements on the screen
        root.getChildren().clear();
        // creates a new board for the game
        state = new Board();
        // creates a text in the center of the screen asking for mode
        Text startText = new Text("Which mode would you like to play?");
        startText.setX(260);
        startText.setY(200);
        // adds startText to the screen
        root.getChildren().add(startText);
        // creates a button for two player mode
        Button twoPlayer = new Button("Two-Player Mode");
        twoPlayer.setLayoutX(200);
        twoPlayer.setLayoutY(300);
        twoPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // method that starts the game
                handleGame(root, stage);
                // set isOnePlayer to false
                isOnePlayer = false;
            }
        });
        // creates a button for one player mode
        Button onePlayer = new Button("Single-Player Mode");
        onePlayer.setLayoutX(400);
        onePlayer.setLayoutY(300);
        onePlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // method that starts the game
                handleGame(root, stage);
                // set isOnePlayer to true
                isOnePlayer = true;

            }
        });
        // add both buttons to the screen
        root.getChildren().addAll(twoPlayer, onePlayer);
        // add a blue background to the scene
        startScene.setFill(javafx.scene.paint.Color.BLUE);
        // set title to "Connect 4"
        stage.setTitle("Connect 4");
        // start the scene and show it on the screen
        stage.setScene(startScene);
        stage.show();
    }
}