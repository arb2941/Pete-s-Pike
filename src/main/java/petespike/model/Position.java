package petespike.model;

public class Position {
    private int row;
    private int col;

    /**
     * Constructor for setting a position
     * @param row on board
     * @param col on board
     */
    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * Gets the row the position is set to
     * @return row
     */
    public int getRow(){
        return row;
    }

    /**
     * Gets the column the position is set to
     * @return column
     */
    public int getCol(){
        return col;
    }

    /**
     * Checks if 2 positions are equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Position) {
            Position other = (Position)obj;
            if(this.row == other.row && this.col == other.col) {
                return true;
            }
        }
        return false;
    }

    /**
     * Readable format of a position
     */
    @Override
    public String toString() {
        return "("+ row + "," + col+")";
    }

    /**
     * Hash code of a position
     */
    @Override
    public int hashCode() {
        // Calls the hash on the string form,
        // so hashes are equal if same row and col
        return this.toString().hashCode();
    }
}
