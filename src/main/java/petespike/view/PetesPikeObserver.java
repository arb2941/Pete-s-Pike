package petespike.view;

import petespike.model.Position;

public interface PetesPikeObserver {

    /**
     * Abstract method for doing something when a piece is moved
     * from one position to another.
     * @param from (position a piece is moved from)
     * @param to (position a piece is moved to)
     */
    public void pieceMoved(Position from, Position to);
}