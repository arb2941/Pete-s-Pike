package petespike.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import petespike.view.PetesPikeObserver;

public class PetesPike {
    // made things static for more leeway to move
    public static final char MOUNTAINTOP_SYMBOL = 'T';
    public static final char EMPTY_SYMBOL = '-';
    public static final char PETE_SYMBOL = 'P';
    public static final Set<Character> GOAT_SYMBOLS = new TreeSet<>();

    private int moveCount;
    public char [][] board;
    private Position mountainTopPosition;
    private Position petePosition;
    private Map<Character, Position> goatPositions;

    private PetesPikeObserver observer;
   
    // initializes the GOAT_SYMBOLS set
    static {
        GOAT_SYMBOLS.add('0');
        GOAT_SYMBOLS.add('1');
        GOAT_SYMBOLS.add('2');
        GOAT_SYMBOLS.add('3');
        GOAT_SYMBOLS.add('4');
        GOAT_SYMBOLS.add('5');
        GOAT_SYMBOLS.add('6');
        GOAT_SYMBOLS.add('7');
        GOAT_SYMBOLS.add('8');
    }

    /**
     * Primary constructor that reads a data file and creates a new Pete's Pike game.
     * @param filename name of data file to read
     * @throws PetesPikeException if no file found or error reading file
     */
    public PetesPike(String filename) throws PetesPikeException{
        // Parse file, auto-closing readers
        try(FileReader fReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fReader)) {
            // Get number of rows and columns
            String line = bufferedReader.readLine();
            String[] rowcol = line.strip().split(" ");
            int rows = Integer.parseInt(rowcol[0]);
            int cols = Integer.parseInt(rowcol[1]);
            this.board = new char[rows][cols];
            
            // Fill in board with remaining data
            goatPositions = new HashMap<>();
            int row = 0;
            line = bufferedReader.readLine();
            while(line != null) {
                // remove trailing newline
                line = line.strip();
                // for character in string
                for(int i=0; i<line.length(); i++) {
                    char c = line.charAt(i);
                    // take note of mountain top position
                    if(c == MOUNTAINTOP_SYMBOL) {
                        mountainTopPosition = new Position(row, i);
                        // store an empty spot on the board to
                        // allow other pieces to occupy that space
                        board[row][i] = EMPTY_SYMBOL;
                    }
                    else { // not the mountain top
                        // make note of all goats and positions
                        if(GOAT_SYMBOLS.contains(c)) {
                            goatPositions.put(c, new Position(row, i));
                        }
                        // take note of Pete's position on the board
                        if(c == PETE_SYMBOL) {
                            petePosition = new Position(row, i);
                        }
                        // set board character to read character
                        board[row][i] = c;
                    }
                }
                // move to next row
                row++;
                // get next line
                line = bufferedReader.readLine();
            }
            // Default move count
            moveCount = 0;
        } // Error opening file
        catch (FileNotFoundException e) {
            throw new PetesPikeException("File cannot be opened");
        } // Error reading file
        catch (IOException ioe) {
            throw new PetesPikeException("Error reading file");
        }
    }

    /**
     * Secondary constructor to deep copy the game board before checking if a move can be made
     * @param game original game to be deep copied
     */
    protected PetesPike(PetesPike game) {
        // deep copy of previous board
        char[][] copy = new char[game.getRows()][game.getCols()];
        for(int i=0; i<game.getRows(); i++) {
            copy[i] = Arrays.copyOf(game.board[i], game.getCols());
        }
        // set fields with new values
        this.board = copy;
        this.moveCount = game.moveCount;
        this.mountainTopPosition = game.mountainTopPosition;
        this.petePosition = game.petePosition;
        this.goatPositions = game.goatPositions;
    }

    /**
     * Registers the Petes Pike Observer to be used throughout this program.
     * @param observer (the observer to be notified of changes)
     */
    public void registerObserver(PetesPikeObserver observer) {
        this.observer = observer;
    }

    /**
     * Calls to update the game board when a piece is moved.
     * @param from (position a piece is moved from)
     * @param to (position a piece is moved to)
     */
    private void notifyObserver(Position from, Position to) {
        if(observer != null) {
            observer.pieceMoved(from, to);
        }
    }

    /**
     * Converts the board into a readable string for viewing
     * @return String (game board in String format)
     */
    @Override
    public String toString() {
        String game = "  ";
        // top line
        for(int i=0; i<this.getCols(); i++) {
            game += i + " ";
        }
        game += "\n";
        // all other rows
        for(int i=0; i<this.getRows(); i++) {
            game += i + " ";
            for(int j=0; j<this.getCols(); j++) {
                Position pos = new Position(i, j);
                try {
                    if(GOAT_SYMBOLS.contains(getSymbolAt(pos))) {
                        game += "G";
                    }
                    else if(pos.equals(mountainTopPosition)) {
                        game += "+";
                    }
                    else if(pos.equals(petePosition)) {
                        game += "P";
                    }
                    else {
                        game += "-";
                    }
                    game += " ";
                } catch(PetesPikeException e) {
                    System.err.println("Error printing out the game board");
                }
            }
            game += "\n";
        }
        return game;
    }

    /**
     * Gets the number of moves made on the current board
     * @return moveCount (int of moves made on the board)
     */
    public int getMoveCount(){
        return moveCount;
    }

    /**
     * Gets the number of rows on the board
     * @return int of rows
     */
    public int getRows(){
        return board.length;
    }
 
    /**
     * Gets the number of columns on the board
     * @return int of columns
     */
    public  int getCols(){
        // checks for invalid col too
        if(board.length > 0){
            return board[0].length;
        }else{
            return 0;
        }
    }

    /**
     * Checks and returns the current state of the game
     * @return GameState of current game
     */
    public GameState getGameState(){
        if (isGoal()) {
            return GameState.WON;
        } else if (moveCount == 0) {
            return GameState.NEW;
        } else if (getPossibleMoves().isEmpty()) {
            return GameState.NO_MOVES;
        } else {
            return GameState.IN_PROGESS;
        }
    }

    /**
     * Checks to see if Pete is on the Mountain Top
     * @return true if Pete is atop the mountain, false if not
     */
    private boolean isGoal() {
        // checks if Pete is on the Mountain Top
        //System.out.println("Pete is on mountain top");
        return petePosition.equals(mountainTopPosition);
    }


    /**
     * Checks to see if a position is a spot on the board
     * @param position (Position to check on the board)
     * @return true if the Position is on the board, false if not
     */
    public boolean isValidPos(Position position){
        int row = position.getRow();
        int col = position.getCol();
        return row >= 0 && row < getRows() && col >=0 && col < getCols();
    }


    /**
     * Checks to see if a position on the board is an empty space
     * @param position to check on the board
     * @return true if spot on board is empty, false if not
     */
    public boolean isEmpty(Position position){
        int row = position.getRow();
        int col = position.getCol();
        return board[row][col] == EMPTY_SYMBOL;
    }

    public char[][] getBoard(){
        return board;
    }

    /**
     * Makes a move on the board
     * @param move (Move specifications to attempt)
     * @throws PetesPikeException throws for invalid position,
     *      missing piece at position, no collision during move
     */
    public void makeMove(Move move) throws PetesPikeException{
        Position newPosition = move.getPosition();
        Direction newDirection = move.getDirection();
        // makes sure move is within map
        if(!isValidPos(newPosition)){
            throw new PetesPikeException("Position not on map");
        }
        // no piece at position
        if(getSymbolAt(newPosition) == EMPTY_SYMBOL) {
            throw new PetesPikeException("No piece at this position");
        }

        /*
         * -- Pseudo code
         * Move move (position and direction) - unchanged
         * if current position not on the board, throw "invalid position" error
         * if current position doesnt have a piece, throw "no piece at this position" error
         * boolean validMove = false;
         * if statements for each direction:
         * int row/col - store next row/col in direction
         * Position newPosition = empty;
         * while row/col is on the board:
         * - newPosition is position with new row
         * - if symbol at newPosition is not empty, newPosition is previous position, validMove is true, break
         * - else move to next row/col in direction
         * if not validMove, throw "no piece to stop move in given direction" error
         * if newPosition is move.getPosition(), throw "invalid move" error
         * 
         * swap pieces
         * Position pos = move.getPosition();
         * char temp = getSymbolAt(pos);
         * board[pos.getRow()][pos.getCol()] = EMPTY_SYMBOL;
         * board[newPosition.getRow()][newPosition.getCol()] = temp;
         * if(temp == PETE_SYMBOL) petePosition = newPosition;
         * else if(GOAT_SYMBOLS.contains(temp)) goatPositions.put(temp, newPosition);
         */
        
         // takes note if all moves are invalid
        boolean validMove = false;
        // gets next valid position in direction
        if (newDirection == Direction.UP) {
            // get next position up
            int row = newPosition.getRow() - 1;

            // for every position on the board from current position in direction
            // until a non-empty space is reached
            while(row >= 0) {
                newPosition = new Position(row, newPosition.getCol());
                // get symbol at next position up
                if(getSymbolAt(newPosition) != EMPTY_SYMBOL) {
                    // new position is at previous position
                    newPosition = new Position(row+1, newPosition.getCol());
                    // move is valid
                    validMove = true;
                    break;
                }
                // move up another row
                row--;
            }
            // if (newPosition.getRow() - 1 < 0 || isEmpty(new Position(newPosition.getRow() - 1, newPosition.getCol()))) {
            //     throw new PetesPikeException("Nothing in the way, cannot move");
            // }
        } else if (newDirection == Direction.DOWN) {
            // get next position down
            int row = newPosition.getRow() + 1;

            // for every position on the board from current position in direction
            // until a non-empty space is reached
            while(row <= getRows()-1) {
                newPosition = new Position(row, newPosition.getCol());
                // get symbol at next position down
                if(getSymbolAt(newPosition) != EMPTY_SYMBOL) {
                    // new position is at previous position
                    newPosition = new Position(row-1, newPosition.getCol());
                    // move is valid
                    validMove = true;
                    break;
                }
                // move down another row
                row++;
            }
            // if (newPosition.getRow() + 1 >= getRows() || isEmpty(new Position(newPosition.getRow() + 1, newPosition.getCol()))) {
            //     throw new PetesPikeException("Nothing in the way, cannot move");
            // }
        } else if (newDirection == Direction.LEFT) {
            // get next position left
            int col = newPosition.getCol() - 1;

            // for every position on the board from current position in direction
            // until a non-empty space is reached
            while(col >= 0) {
                newPosition = new Position(newPosition.getRow(), col);
                // get symbol at next position left
                if(getSymbolAt(newPosition) != EMPTY_SYMBOL) {
                    // new position is at previous position
                    newPosition = new Position(newPosition.getRow(), col+1);
                    // move is valid
                    validMove = true;
                    break;
                }
                // move left another col
                col--;
            }
            // if (newPosition.getCol() - 1 < 0 || isEmpty(new Position(newPosition.getRow(), newPosition.getCol() - 1))) {
            //     throw new PetesPikeException("Nothing in the way, cannot move");
            // }
        } else if (newDirection == Direction.RIGHT) {
            // get next position right
            int col = newPosition.getCol() + 1;

            // for every position on the board from current position in direction
            // until a non-empty space is reached
            while(col <= getCols()-1) {
                newPosition = new Position(newPosition.getRow(), col);
                // get symbol at next position right
                if(getSymbolAt(newPosition) != EMPTY_SYMBOL) {
                    // new position is at previous position
                    newPosition = new Position(newPosition.getRow(), col-1);
                    // move is valid
                    validMove = true;
                    break;
                }
                // move right another col
                col++;
            }
            // if (newPosition.getCol() + 1 >= getCols() || isEmpty(new Position(newPosition.getRow(), newPosition.getCol() + 1))) {
            //     throw new PetesPikeException("Nothing in the way, cannot move");
            // }
        } else {
            throw new PetesPikeException("Invalid direction");
        }

        // piece tried to move and fell off the map
        if(!validMove) {
            throw new PetesPikeException("No piece to stop move in given direction");
        }
        // tried to move where blocked by another piece
        // System.out.println(newPosition + " - " + move.getPosition());
        if(newPosition.equals(move.getPosition())) {
            throw new PetesPikeException("Invalid move");
        }

        // only for shorter variable name
        Position pos = move.getPosition();
        // store symbol of piece to move
        char temp = getSymbolAt(pos);
        // set original position to empty symbol
        board[pos.getRow()][pos.getCol()] = EMPTY_SYMBOL;
        // change symbol at new position to piece symbol
        board[newPosition.getRow()][newPosition.getCol()] = temp;
        // if piece moved is Pete
        if(temp == PETE_SYMBOL) {
            // update Pete's position
            petePosition = newPosition;
        }
        // if piece moved is a goat
        else if(GOAT_SYMBOLS.contains(temp)) {
            // update the moved goat's position
            goatPositions.put(temp, newPosition);
        }
        // increment move count
        moveCount++;

        // updates the GUI with the moved pieces
        notifyObserver(pos, newPosition);

        // char pieceSym = getSymbolAt(newPosition);
        // char newSymbol;//getSymbolAt(petePosition);
        // Position currentPos;
        // if (pieceSym == 'P') {
        //     newSymbol = getSymbolAt(petePosition);
        //     currentPos = petePosition;
        // } else if (pieceSym == 'G') {
        //     newSymbol = getSymbolAt(goatPosition);
        //     currentPos = goatPosition;
        // } else {
        //     throw new PetesPikeException("Invalid piece symbol");
        // }
        // //take pete off the board
        // board[petePosition.getRow()][petePosition.getCol()] = EMPTY_SYMBOL;
        // // make new position 
        // board[newPosition.getRow()][newPosition.getCol()] = newSymbol;
        // // move pete to new position
        // if (pieceSym == 'P') {
        //     petePosition = newPosition;
        // } else if (pieceSym == 'G') {
        //     goatPosition = newPosition;
        // }
        // moveCount ++;
    }

    /**
     * Gets the symbol in the position on the board
     * @param position on the board to check
     * @return char (symbol on the board)
     * @throws PetesPikeException throws for invalid Position
     */
    public char getSymbolAt(Position position) throws PetesPikeException {
        // throws error if position not on board
        isValidPos(position);
        int row = position.getRow();
        int col = position.getCol();
        return board[row][col];
    }

    /**
     * Gets the position of the Mountain Top
     * @return position of mountain top
     */
    public Position getMountaintop(){
        return mountainTopPosition;
    }

    /**
     * Gets the position of Pete
     * @return position of Pete
     */
    public Position getPetePosition(){
        return petePosition;
    }

    /**
     * Gets all the possible moves on the current board
     * @return list of all possible moves to be made
     */
    public List<Move> getPossibleMoves(){
        List<Move> moves = new LinkedList<>();
        // get Petes possible moves first
        for(Direction direction : Direction.values()) {
            PetesPike copy = new PetesPike(this);
            Move move = new Move(petePosition, direction);
            try {
                copy.makeMove(move);
                moves.add(move);
            }
            catch(PetesPikeException e) {
                // No error feedback necessary
                // errors for invalid move
            }
        }
        // for each goat on board
        for(char goat : goatPositions.keySet()) {
            Position position = goatPositions.get(goat);
            for(Direction direction : Direction.values()) {
                PetesPike copy = new PetesPike(this);
                Move move = new Move(position, direction);
                try {
                    copy.makeMove(move);
                    moves.add(move);
                }
                catch(PetesPikeException e) {
                    // No error feedback necessary
                    // errors for invalid move
                }
            }
        }
        return moves;
    }

    
    /**
     * Main Method for manual testing of code.
     */
    public static void main(String[] args) throws PetesPikeException {
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        System.out.println(game.toString());
        System.out.println(game.isGoal());
        System.out.println(game.getGameState());
        /**game.makeMovePete(new Move(new Position(0, 2),Direction.LEFT));
        System.out.println(game.toString());
        game.makeMovePete(new Move(new Position(2, 2),Direction.DOWN));
        System.out.println(game.toString());
        System.out.println(game.isGoal());
        System.out.println(game.getGameState());
        /* */ // testing pete moving 
        //game.makeMoveGoat(new Move(new Position(0, 3), Direction.RIGHT));
        System.out.println(game.toString());
    }
}
