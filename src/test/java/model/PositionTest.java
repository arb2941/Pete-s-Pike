package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Position;

public class PositionTest {
    @Test 
    public void PositionEqualsTest(){
        Position position1 = new Position(1,4);
        Position position2 = new Position(0, 5);
        Position position3 = new Position(2, 0);
        Position position4 = new Position(1, 4);

        assertEquals(position1, position4);
        assertNotEquals(position3, position2);
        
    }
    @Test
    public void PositionStringTest(){
        Position position = new Position(2, 3);
        String expected = position.toString();
        String actual = "(2,3)";

        assertEquals(expected, actual);
    }
    @Test
    public void PositionHasCodeTest(){
        Position position1 = new Position(2, 5);
        Position position2 = new Position(2, 5);
        Position position3 = new Position(1, 4);

        assertEquals(position1.hashCode(), position2.hashCode());
        assertNotEquals(position2.hashCode(), position3.hashCode());
    }
    
}
