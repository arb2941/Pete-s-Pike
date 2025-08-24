package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.Position;

public class MoveTest {
    @Test
    public void TestMoveEquals(){
        Position position1 = new Position(4,5);
        Position position2 = new Position(0, 0);
        Position position3 = new Position(4, 5);
        
        Direction direction1 = Direction.UP;
        Direction direction2 = Direction.DOWN;

        Move move = new Move(position1, direction2);
        Move move2 = new Move(position2, direction1);
        Move move3 = new Move(position3, direction2);

        assertEquals(move.getPosition(), move3.getPosition());
        assertEquals(move.getDirection(), move3.getDirection());
        assertNotEquals(move2, move3);
    
    }
    @Test
    public void TesttoString(){
        Position position1 = new Position(4,5);
        Position position2 = new Position(0, 0);

        
        Direction direction1 = Direction.UP;
        Direction direction2 = Direction.DOWN;

        Move move = new Move(position1, direction2);
        Move move2 = new Move(position2, direction1);

        String expected = move.toString();
        String actual = move.getPosition()+" "+ move.getDirection();

        String expect2 = move2.toString();
        String actual2 = move2.getPosition()+" "+ move2.getDirection();
        assertEquals(expected, actual);
        assertEquals(expect2, actual2);
        
    }
    
}
