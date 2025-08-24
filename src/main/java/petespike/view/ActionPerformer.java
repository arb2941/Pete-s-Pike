package petespike.view;

import backtracker.Backtracker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeSolver;
import petespike.model.Position;

public class ActionPerformer implements EventHandler<ActionEvent> {

    private String actionType;
    private PetesPikeGUI gui;

    public ActionPerformer(String actionType, PetesPikeGUI gui) {
        this.actionType = actionType;
        this.gui = gui;
    }

    @Override
    public void handle(ActionEvent event) {
        PetesPike game = gui.getGame();
        String message;
        Move move;
        // might be null if not trying to move a piece
        Position selected = gui.getSelected();
        switch (actionType) {
            case "hint":
                PetesPikeSolver solution = new Backtracker<PetesPikeSolver>(false).solve(new PetesPikeSolver(game));
                if(solution != null) {
                    // move hint is the first possible move of a successful solution
                    move = solution.getMoves().get(0);
                    // move = game.getPossibleMoves().get(0);
                    try {
                        message = game.getSymbolAt(move.getPosition()) + "" + move.getDirection().getName().charAt(0);
                    } catch(PetesPikeException e) {
                        message = e.getMessage();
                    }
                }
                // there is no solution
                else {
                    gui.disableButtons();
                    message = "The game cannot be won at this stage.";
                }
                
                gui.updateStatus(message, "hint");
                break;
        
            case "reset":
                gui.resetGame();
                break;
        
            case "new":
                gui.newGame();
                break;
        
            case "up":
                if(selected != null) {
                    try {
                        move = new Move(selected, Direction.UP);
                        game.makeMove(move);
                        message = "Valid Move";
                        gui.updateMoveHistory(move.toString());
                    }
                    catch(PetesPikeException e) {
                        message = e.getMessage();
                    }
                }
                else {
                    message = "A piece must be selected first.";
                }
                gui.updateStatus(message, "normal");
                break;
        
            case "down":
                if(selected != null) {
                    try {
                        move = new Move(selected, Direction.DOWN);
                        game.makeMove(move);
                        message = "Valid Move";
                        gui.updateMoveHistory(move.toString());
                    }
                    catch(PetesPikeException e) {
                        message = e.getMessage();
                    }
                }
                else {
                    message = "A piece must be selected first.";
                }
                gui.updateStatus(message, "normal");
                break;
        
            case "left":
                if(selected != null) {
                    try {
                        move = new Move(selected, Direction.LEFT);
                        game.makeMove(move);
                        message = "Valid Move";
                        gui.updateMoveHistory(move.toString());
                    }
                    catch(PetesPikeException e) {
                        message = e.getMessage();
                    }
                }
                else {
                    message = "A piece must be selected first.";
                }
                gui.updateStatus(message, "normal");
                break;
        
            case "right":
                if(selected != null) {
                    try {
                        move = new Move(selected, Direction.RIGHT);
                        game.makeMove(move);
                        message = "Valid Move";
                        gui.updateMoveHistory(move.toString());
                    }
                    catch(PetesPikeException e) {
                        message = e.getMessage();
                    }
                }
                else {
                    message = "A piece must be selected first.";
                }
                gui.updateStatus(message, "normal");
                break;
            case "solve":
                GameState gameState = gui.getGame().getGameState();
                if(gameState == GameState.IN_PROGESS || gameState == GameState.NEW) {
                    PetesPikeSolver solver = new Backtracker<PetesPikeSolver>(false).solve(new PetesPikeSolver(game));
                    // disable buttons while solving
                    gui.disableButtons();
                    // Game is solvable
                    if(solver != null) {
                        new Thread(() -> {
                                for(Move m : solver.getMoves()) {
                                    Platform.runLater(() -> {
                                        try{
                                            gui.updateSelected(m.getPosition());
                                            Thread.sleep(250);
                                            game.makeMove(m);
                                        }catch(Exception e){}
                                    });
                                    try{Thread.sleep(250);}catch(InterruptedException e){}
                            }
                        }).start();
                    }
                    // Game has no solution
                    else {
                        // set status message
                        gui.updateStatus("The game cannot be solved.", "normal");
                    }
                }
                // Invalid game (shouldn't actually be possible)
                else {
                    gui.updateStatus("Cannot solve an invalid game.", "normal");
                }
        }
    }
}
