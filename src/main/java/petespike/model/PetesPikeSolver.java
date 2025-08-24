package petespike.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import backtracker.Backtracker;
import backtracker.Configuration;

public class PetesPikeSolver implements Configuration<PetesPikeSolver> {

    private PetesPike petesPike;
    private List<Move> moves;

    /**
     * Intial constructor called only with inital backtracker creation.
     * @param petesPike Game of Petes Pike
     */
    public PetesPikeSolver(PetesPike petesPike) {
        this(petesPike, new ArrayList<>());
    }

    /**
     * Main constuctor for a game Solver
     * @param petesPike Game of Petes Pike
     * @param moves A list of moves made thus far in the configuration
     */
    public PetesPikeSolver(PetesPike petesPike, List<Move> moves) {
        this.petesPike = petesPike;
        this.moves = moves;
    }

    /**
     * Gets the next moves to the current puzzle.
     * @return List of Solver game configurations
     */
    @Override
    public Collection<PetesPikeSolver> getSuccessors() {
        List<PetesPikeSolver> getNext = new ArrayList<>();
        for (Move move : petesPike.getPossibleMoves()) {
            try {
                // 
                PetesPike newGame = new PetesPike(petesPike);
                newGame.makeMove(move);
                // copy current moves and add new move
                List<Move> newMoves = new ArrayList<>(moves);
                newMoves.add(move);
                PetesPikeSolver successor = new PetesPikeSolver(newGame, newMoves);
                getNext.add(successor);
            } catch (PetesPikeException e) {}
        }
        return getNext;
    }

    /**
     * Checks if a configuration is valid or not.
     * @return boolean, true if valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return !petesPike.getPossibleMoves().isEmpty() || isGoal();
    }

    /**
     * Checks if the configuration is the goal.
     * @return boolean, true if goal, false otherwise
     */
    @Override
    public boolean isGoal() {
        return petesPike.getGameState() == GameState.WON;
    }

    /**
     * Gets the moves made to get to the solved configuration.
     * @return The list of moves it took to get from
     * the original configuration to the solved configuration
     */
    public List<Move> getMoves() {
        return moves;
    }

    /**
     * Prints out the solver in an understandable way.
     * @return String representation of the configuration
     */
    @Override
    public String toString() {
        return "List of moves: " + getMoves() + "and current board " + petesPike.getBoard();
    }

    public static void main(String[] args) throws PetesPikeException {
        // trying to test out config functions
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        // make new backtracker
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(false);
        // use backtracker to solve the PetesPike game
        PetesPikeSolver solution = backtracker.solve(new PetesPikeSolver(game));
        // print the list of moves
        System.out.println("List of moves: " + solution.getMoves());

    }
}
