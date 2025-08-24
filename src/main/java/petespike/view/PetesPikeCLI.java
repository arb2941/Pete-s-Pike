package petespike.view;

import java.util.List;
import java.util.Scanner;

import backtracker.Backtracker;
import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeSolver;
import petespike.model.Position;

public class PetesPikeCLI {

   public static String filename;
   public static PetesPike game;

   public static void help(){
      System.out.println("help - this menu"
         + "\n\tboard - displays current board"
         + "\n\treset - resets current puzzle to start"
         + "\n\tnew <filename> - starts a new puzzle"
         + "\n\tmove <row> <col> <direction> - moves piece at <row> <col>"
         + "\n\t\twhere <direction> one of u(p), d(own), l(eft), r(ight)"
         + "\n\thint - get a valid move, if one exists"
         + "\n\tsolve - solve the current puzzle"
         + "\n\tquit - quits the game");
   }

   /**
   * @param row
   * @param col
   * @param move
   *@returns updated board
   */
   public static void move(int row, int col, Direction move) {
      try {
         Position start = new Position(row, col);
         Move newMove = new Move(start, move);
         game.makeMove(newMove);
      } catch(PetesPikeException e) {
         System.err.println(e.getMessage());
      }
   }

   /**
   * @return the board
   */
   public static void board(){
      System.out.print(game);
      System.out.println("Moves: " + game.getMoveCount());
   }


   /**
   * changes moveCount to 0
   * calls the filename again to reset board
   * @return toString to display board
   */
   public static void reset() {
      try {
         game = new PetesPike(filename);
      } catch(PetesPikeException e) {
         System.err.println(e.getMessage());
      }
   }

   /**
   * gets new filename
   * makes board
   * @return the board
   */
   public static void newGame() {
      try {
         game = new PetesPike(filename);
      } catch(PetesPikeException e) {
         System.err.println(e.getMessage());
      }
   }

   /**
   * @return cords of a move
   * get possible moves get first in list
   */
   public static void hint() {
      PetesPikeSolver solver = new Backtracker<PetesPikeSolver>(false).solve(new PetesPikeSolver(game));
      //List<Move> list = solver.getMoves();
      //no move
      if(solver == null){
         System.out.println("There are no moves to win this game.");
      }
      //move 
      else{
         List<Move> list = solver.getMoves();
         Move nextMove = list.get(0);
         System.out.println("Move: " + nextMove);
      }
   }

   /**
    * runs solver and moves the peices on the board
    * to fit the solve moves
    * prints message if game can not be solved
    * or is solved
    */
   public static void solve(){
      PetesPikeSolver solver = new Backtracker<PetesPikeSolver>(false).solve(new PetesPikeSolver(game));
      if(solver != null){
         List<Move> list = solver.getMoves();
         for(int i = 0; i < list.size(); i++){
            int row = list.get(i).getPosition().getRow();
           int col = list.get(i).getPosition().getCol();
            // get direction
            Direction dir = null;
            if(list.get(i).getDirection().equals(Direction.UP)) {
               dir = Direction.UP;
            }
            else if(list.get(i).getDirection().equals(Direction.DOWN)) {
               dir = Direction.DOWN;
            }
            else if(list.get(i).getDirection().equals(Direction.LEFT)) {
               dir = Direction.LEFT;
            }
            else if(list.get(i).getDirection().equals(Direction.RIGHT)) {
               dir = Direction.RIGHT;
            }
            move(row, col, dir);
            System.out.println(game);
            if(i == list.size()-1 && game.getGameState() == GameState.WON){
               System.out.println("Congratulations, you have scaled the mountain!");
            }
         }
      }
      else{
         System.out.println("Sorry, there is no solution availble");
      }
   }

   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      String command;
      // make a new game
      // make sure the user enters a valid filename
      while(game == null) {
         System.out.print("Puzzle filename: ");
         filename = input.nextLine();
         newGame();
      }
      // list all possible commands
      help();
      
      // display board and move count
      board();

      // main loop to accept user commands and run helper methods
      while(true) {
         // checks for completed or ended game
         boolean validGame = game.getGameState() != GameState.NO_MOVES && game.getGameState() != GameState.WON;
         
         System.out.print("\nCommand: ");
         // get user input command
         command = input.nextLine();
         // added spacing
         System.out.println();
         // change user input to all lowercase
         command = command.toLowerCase();
         if(command.equals("help")) {
            help();
            // display board and move count
            board();
         }
         else if(command.equals("board")) {
            board();
         }
         else if(command.equals("reset")) {
            reset();
            // display board and move count
            board();
         }
         else if(command.split(" ")[0].equals("new") && command.length() > 3) {
            filename = command.split(" ")[1];
            newGame();
            // display board and move count
            board();
         }
         else if(command.split(" ")[0].equals("move") && validGame) {
            int row = Integer.parseInt(command.split(" ")[1]);
            int col = Integer.parseInt(command.split(" ")[2]);
            // get direction
            Direction dir = null;
            if(command.split(" ")[3].equals("u")) {
               dir = Direction.UP;
            }
            else if(command.split(" ")[3].equals("d")) {
               dir = Direction.DOWN;
            }
            else if(command.split(" ")[3].equals("l")) {
               dir = Direction.LEFT;
            }
            else if(command.split(" ")[3].equals("r")) {
               dir = Direction.RIGHT;
            }
            move(row, col, dir);
            // display board and move count
            board();
         }
         else if(command.equals("hint") && validGame) {
            hint();
         }
         // Not implementing Solve yet
         else if(command.equals("solve")) {
            solve();
         }
         else if(command.equals("quit")) {
            System.out.println("\nGoodbye!");
            break;
         }
         // invalid command
         else {
            System.out.println("Invalid command: " + command);
         }
      }
      input.close();
   }
}
