package petespike.view;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import petespike.model.GameState;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.Position;

public class PetesPikeGUI extends Application implements PetesPikeObserver {

    // images
    private static final Map<Character, Image> IMAGES = new HashMap<>();
      static {
        IMAGES.put('-', new Image("file:data/media/blank.png", 40, 40, false, false));
        IMAGES.put('P', new Image("file:data/media/pete.png", 40, 40, false, false));
        IMAGES.put('T', new Image("file:data/media/mountain-range-vector-icon.jpg", 40, 40, false, false));
        IMAGES.put('0', new Image("file:data/media/goat0.png", 40, 40, false, false));
        IMAGES.put('1', new Image("file:data/media/goat1.png", 40, 40, false, false));
        IMAGES.put('2', new Image("file:data/media/goat2.png", 40, 40, false, false));
        IMAGES.put('3', new Image("file:data/media/goat3.png", 40, 40, false, false));
        IMAGES.put('4', new Image("file:data/media/goat4.png", 40, 40, false, false));
        IMAGES.put('5', new Image("file:data/media/goat5.png", 40, 40, false, false));
        IMAGES.put('6', new Image("file:data/media/goat6.png", 40, 40, false, false));
        IMAGES.put('7', new Image("file:data/media/goat7.png", 40, 40, false, false));
        IMAGES.put('8', new Image("file:data/media/goat8.png", 40, 40, false, false));
        IMAGES.put('U', new Image("file:data/media/arrow.png", 40, 40, false, false));
        IMAGES.put('D', new Image("file:data/media/arrowDown.png", 40, 40, false, false));
        IMAGES.put('L', new Image("file:data/media/arrowLeft.png", 40, 40, false, false));
        IMAGES.put('R', new Image("file:data/media/arrowRight.png", 40, 40, false, false));
    }
   

    private PetesPike game;
    private Position selected = null;
    private ImageView statusPieceImage;
    private ImageView statusDirectionImage;
    private Label status;
    private Label moveCount;
    private Label moveHistory;
    private TextField fileTextField;
    private String filename = "data/petes_pike_5_5_2_0.txt";
    private GridPane gamePane;
    private Button[][] buttonGrid;
    private ImageView[][] imageGrid;
    private List<Button> buttonToggleList = new LinkedList<>();

    public PetesPike getGame() {
        return game;
    }

    public Position getSelected() {
        return selected;
    }

    public void updateSelected(Position pos) {
        // revert previously selected node background
        if (selected != null) {
            Button button = buttonGrid[selected.getRow()][selected.getCol()];
            button.setBackground(new Background(
                    new BackgroundFill(
                            Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        // update selected position
        selected = pos;
        // give selected button a yellow background
        Button button = buttonGrid[selected.getRow()][selected.getCol()];
        button.setBackground(new Background(
                new BackgroundFill(
                        Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void updateStatus(String message, String type) {
        // reset status label text
        status.setText("");
        
        if (type.equals("hint")) {
            // is not an error
            if(message.length() == 2) {
                char piece = message.charAt(0);
                char dir = message.charAt(1);
                statusPieceImage.setImage(IMAGES.get(piece));
                statusDirectionImage.setImage(IMAGES.get(dir));
                // dont change status label text
                return;
            }
            // is error
            // exit if/else and set status text to error message
        }
        else {
            // clear status images
            statusPieceImage.setImage(IMAGES.get('-'));
            statusDirectionImage.setImage(IMAGES.get('-'));
        }
        // update special if end of game
        if(game.getGameState() == GameState.WON) {
            message = "Congratulations! You've won!";
            // game is won, disable buttons
            disableButtons();
        }
        else if(game.getGameState() == GameState.NO_MOVES) {
            message = "No more moves available";
            // game is won, disable buttons
            disableButtons();
        }
        // could be error message
        status.setText(message);
        status.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 15));
        status.setTextFill(Color.DARKMAGENTA);
    }

    public void updateMoveHistory(String move) {
        String text = moveHistory.getText();
        moveHistory.setText(text + "\n" + move);
        moveHistory.setBackground(new Background(
            new BackgroundFill(
                Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY
            )
        ));
        moveHistory.setFont(Font.font("Comic Sans MS", 15));
        moveHistory.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), BorderStroke.THIN)));
    }

    public void resetGame() {
        try {
            this.game = new PetesPike(filename);
            // re-add observer
            game.registerObserver(this);
            // reset move count
            moveCount.setText("Moves: " + 0);
            // reset move history
            moveHistory.setText("Move History:");
            // remake GUI board grid
            gamePane.getChildren().clear();
            makeGrid();
            // re-enable disabled option buttons
            for(Button button : buttonToggleList) {
                button.setDisable(false);
            }
            // change status
            updateStatus("New Game", "normal");
        } catch (PetesPikeException e) {
            // only ever occurs with a valid filename, so no errors will be encountered
        }
    }

    public void newGame() {
        String newFilename = fileTextField.getText();
        File file = new File(newFilename);
        if(file.exists()) {
            try {
                // check if valid PetesPike config file
                new PetesPike(newFilename);

                // runs if valid config file
                filename = newFilename;
                resetGame();
            } catch (PetesPikeException e) {
                // invalid config file
                updateStatus("Error in adding new file", "normal");
            }

        } else {
            fileTextField.setText("Invalid file !");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Pete's Pike");
        stage.setMinWidth(500);
        stage.setMinHeight(500);

        // makes a new game given the default filename
        game = new PetesPike(filename);
        // register this gui as an observer
        game.registerObserver(this);

        // create new area fields
        HBox mainHBox = new HBox();
        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();
        HBox statusHBox = new HBox();
        HBox resetHBox = new HBox();
        GridPane arrowsPane = new GridPane();

        // create labels and text fields
        gamePane = new GridPane();
        statusPieceImage = new ImageView(IMAGES.get('-'));
        statusDirectionImage = new ImageView(IMAGES.get('-'));
        status = new Label();
        moveCount = new Label("Moves: " + game.getMoveCount());
        moveHistory = new Label("Move History:");
        fileTextField = new TextField();

        //changes look of moveHistory
        moveHistory.setBackground(new Background(
            new BackgroundFill(
                Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY
            )
        ));
        moveHistory.setFont(Font.font("Comic Sans MS", 15));
        moveHistory.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), BorderStroke.THIN)));

        // user can hit "Enter" after typing a filename to make a new game with said filename
        fileTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newGame();
            }
        });

        gamePane.setMaxSize(500.0, 500.0);
        // create game board visual
        makeGrid();

        // create non-move buttons
        // Solve puzzle
        Button solver = new Button("Solve");
        solver.setOnAction(new ActionPerformer("solve", this));
        solver.setBackground(new Background(
            new BackgroundFill(
                Color.LIGHTSEAGREEN, new CornerRadii(10), Insets.EMPTY
            )
        ));
        solver.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        solver.minHeight(20);
        solver.minWidth(20);
        solver.setFont(Font.font("Comic Sans MS", 15));
        // Get Hint button
        Button getHint = new Button("Get Hint");
        getHint.setOnAction(new ActionPerformer("hint", this));
        getHint.setBackground(new Background(
            new BackgroundFill(
                Color.LIGHTSEAGREEN, new CornerRadii(10), Insets.EMPTY
            )
        ));
        getHint.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        getHint.minHeight(20);
        getHint.minWidth(20);
        getHint.setFont(Font.font("Comic Sans MS", 15));
        // Reset button
        Button reset = new Button("Reset");
        reset.setOnAction(new ActionPerformer("reset", this));
        reset.setBackground(new Background(
            new BackgroundFill(
                Color.LIGHTSKYBLUE, new CornerRadii(10), Insets.EMPTY
            )
        ));
        reset.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        reset.minHeight(20);
        reset.minWidth(20);
        reset.setFont(Font.font("Comic Sans MS", 15));
        // New Game button
        Button newGame = new Button("New Game");
        newGame.setOnAction(new ActionPerformer("new", this));
        newGame.setBackground(new Background(
            new BackgroundFill(
                Color.LIGHTSKYBLUE, new CornerRadii(10), Insets.EMPTY
            )
        ));
        newGame.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        newGame.minHeight(20);
        newGame.minWidth(20);
        newGame.setFont(Font.font("Comic Sans MS", 15));
        // making movecount label
        moveCount.setAlignment(Pos.TOP_CENTER);
        moveCount.setMinSize(50, 25);
        moveCount.setBackground(new Background(
            new BackgroundFill(
                Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY
            )
        ));
        moveCount.setFont(Font.font("Comic Sans MS", 15));
        moveCount.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), BorderStroke.THIN)));

        // create movement buttons
        arrowsPane.setPadding(new Insets(10));

        Button up = arrowButton(IMAGES.get('U'));
        up.setOnAction(new ActionPerformer("up", this));
        arrowsPane.add(up, 1, 0);

        Button down = arrowButton(IMAGES.get('D'));
        down.setOnAction(new ActionPerformer("down", this));
        arrowsPane.add(down, 1, 2);

        Button left = arrowButton(IMAGES.get('L'));
        left.setOnAction(new ActionPerformer("left", this));
        arrowsPane.add(left, 0, 1);

        Button right = arrowButton(IMAGES.get('R'));
        right.setOnAction(new ActionPerformer("right", this));
        arrowsPane.add(right, 2, 1);

        fileTextField.setAlignment(Pos.CENTER_LEFT);

        fileTextField.setAlignment(Pos.CENTER);
        fileTextField.setText(filename);
        fileTextField.setFont(Font.font(15));

        // take note of which buttons to disable
        // when a game is complete, or solved
        buttonToggleList.add(solver);
        buttonToggleList.add(getHint);
        buttonToggleList.add(up);
        buttonToggleList.add(down);
        buttonToggleList.add(left);
        buttonToggleList.add(right);

        // combine GUI items
        // status parts
        statusHBox.getChildren().add(solver);
        statusHBox.getChildren().add(getHint);
        statusHBox.getChildren().add(statusPieceImage);
        statusHBox.getChildren().add(statusDirectionImage);
        statusHBox.getChildren().add(status);
        // reset/new game parts
        resetHBox.getChildren().add(reset);
        resetHBox.getChildren().add(newGame);
        resetHBox.getChildren().add(fileTextField);
        // left grouped parts
        leftVBox.getChildren().add(gamePane);
        leftVBox.getChildren().add(statusHBox);
        leftVBox.getChildren().add(resetHBox);
        // right grouped parts
        rightVBox.getChildren().add(arrowsPane);
        rightVBox.getChildren().add(moveCount);
        rightVBox.getChildren().add(moveHistory);
        // combine left and right to whole
        mainHBox.getChildren().add(leftVBox);
        mainHBox.getChildren().add(rightVBox);

        stage.setScene(new Scene(mainHBox));
        stage.show();
    }
    private Button arrowButton(Image image) {
        ImageView view = new ImageView(image);
        Button button = new Button();
        button.setGraphic(view);
        button.minHeight(10);
        button.minWidth(10);
        button.setBackground(new Background(
            new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY
            )
        ));
        return button;
    }

    public void makeGrid() {
        buttonGrid = new Button[game.getRows()][game.getCols()];
        imageGrid = new ImageView[game.getRows()][game.getCols()];
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                Position pos = new Position(i, j);
                char c = ' ';
                Image image = IMAGES.get('-');
                try {
                    c = game.getSymbolAt(pos);
                    if(c != '-') {
                        image = IMAGES.get(c);
                    }
                } catch(PetesPikeException e) {
                    // invalid position which can't happen
                }
                ImageView imageView = new ImageView(image);
                Button retVal = new Button("", imageView);
                retVal.setPadding(new Insets(20));
                retVal.setMaxSize(10.0, 10.0);

                // custom background for the mountain top
                if(pos.equals(game.getMountaintop())) {
                    BackgroundImage backgroundImage = new BackgroundImage(IMAGES.get('T'),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, false));
                    Background background = new Background(backgroundImage);
                    retVal.setBackground(background);
                }
                else {
                    retVal.setBackground(new Background(
                        new BackgroundFill(
                            Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));
                }

                retVal.setBorder(new Border(
                    new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN)));
                GridPane.setHgrow(retVal, Priority.ALWAYS);
                GridPane.setVgrow(retVal, Priority.ALWAYS);

                // button action to set itself as selected
                retVal.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        updateSelected(pos);
                    }
                });
                
                // diable button if not moveable piece
                if(c == '-' || c == 'T') {
                    retVal.setDisable(true);
                }

                gamePane.add(retVal, j, i);

                // add button and imageView to grids
                buttonGrid[i][j] = retVal;
                imageGrid[i][j] = imageView;
            }
        }

        GridPane.setHgrow(moveCount, Priority.ALWAYS);
        GridPane.setVgrow(moveCount, Priority.ALWAYS);
        gamePane.add(moveCount, game.getRows() + 1, game.getCols(), 1, 1);
    }

    /**
     * Disables all buttons after
     * the game is complete or solved
     */
    public void disableButtons() {
        // disable all grid buttons
        for(int i=0; i<game.getRows(); i++) {
            for(int j=0; j<game.getCols(); j++) {
                buttonGrid[i][j].setDisable(true);
            }
        }
        // disable other necessary buttons
        for(Button button : buttonToggleList) {
            button.setDisable(true);
        }
    }

    @Override
    public void pieceMoved(Position from, Position to) {
        // changes images after move is made
        Button fromButton = buttonGrid[from.getRow()][from.getCol()];
        Button toButton = buttonGrid[to.getRow()][to.getCol()];
        ImageView fromImage = imageGrid[from.getRow()][from.getCol()];
        ImageView toImage = imageGrid[to.getRow()][to.getCol()];

        // update new position's image
        toButton.setGraphic(fromImage);
        // revert old position's image
        fromButton.setGraphic(toImage);

        // disable now empty board piece
        fromButton.setDisable(true);
        // enable now non-empty board piece
        toButton.setDisable(false);
        // swap images in grid data
        imageGrid[from.getRow()][from.getCol()] = toImage;
        imageGrid[to.getRow()][to.getCol()] = fromImage;

        // update move count
        moveCount.setText("Moves: " + game.getMoveCount());
        // unselect board piece and revert background color
        Button button = buttonGrid[selected.getRow()][selected.getCol()];
        button.setBackground(new Background(
                new BackgroundFill(
                        Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));
        selected = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
