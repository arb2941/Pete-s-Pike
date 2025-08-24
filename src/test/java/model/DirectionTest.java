package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;

// tests every direction with their given name

public class DirectionTest {
    @Test
    public void TestDirectionUP(){
      String expected = Direction.UP.getName();
      String actual = "UP";

      assertEquals(expected, actual);

    }
    @Test
    public void TestDirectionDOWN(){
      String expected = Direction.DOWN.getName();
      String actual = "DOWN";

      assertEquals(expected, actual);

    }
    @Test
    public void TestDirectionLEFT(){
      String expected = Direction.LEFT.getName();
      String actual = "LEFT";

      assertEquals(expected, actual);

    }
    @Test
    public void TestDirectionRIGHT(){
      String expected = Direction.RIGHT.getName();
      String actual = "RIGHT";

      assertEquals(expected, actual);

    }

    @Test
    public void TestDirectionWrong(){
        String expected = Direction.LEFT.getName();
        String actual = "RIGHT";

        String expected2 = Direction.DOWN.getName();
        String actual2 = "UP";

        assertNotEquals(expected, actual);
        assertNotEquals(expected2, actual2);
    }

    
}
