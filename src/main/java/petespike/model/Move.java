package petespike.model;

public class Move {
    private Position position;
    private Direction direction;

    /**
     * Constructor for a move
     * @param position on board for move to start from
     * @param direction to move on the board
     */
    public Move(Position position, Direction direction){
        this.direction = direction;
        this.position = position;
    }

    /**
     * Gets the position to start the move from
     * @return position of move
     */
    public Position getPosition(){
        return position;
    }

    /**
     * Gets the direction the move will go
     * @return direction to move
     */
    public Direction getDirection(){
        return direction;
    }

    /**
     * Checks if two Moves are equal
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if(obj == null || !(obj instanceof Position)){
            return false;
        }
        Move move = (Move) obj;
        return position == move.position && direction == move.direction;
    }

    /**
     * String for a move
     * @return String
     */
    @Override
    public String toString() {
        return position + " "+ direction.getName();
    }

    /**
     * Hashing method for a Move
     * @return int of hash code
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
