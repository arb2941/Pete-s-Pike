package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.Position;

public class PetesPikeTest {
    @Test
    public void construct() throws PetesPikeException {
        // setup
        String filename = "data/petes_pike_5_5_2_0.txt";
        PetesPike game;
        Position mountainTopPos = new Position(2, 2);

        // invoke
        try {
            game = new PetesPike(filename);
        } catch (Exception e) {
            throw e;
        }

        // analyze
        assertTrue(game != null);
        assertEquals(0, game.getMoveCount());
        assertEquals(5, game.getRows());
        assertEquals(5, game.getCols());
        assertEquals(mountainTopPos, game.getMountaintop());
    }

    // @Test
    // public void constructCopy() throws PetesPikeException {
    //     // setup
    //     String filename = "data/petes_pike_5_5_2_0.txt";
    //     PetesPike game;
    //     Position mountainTopPos = new Position(2, 2);
    //     try {
    //         game = new PetesPike(filename);
    //     } catch (Exception e) {
    //         throw e;
    //     }

    //     // invoke
    //     PetesPike gameCopy = new PetesPike(game);

    //     // analyze
    //     assertTrue(game != null);
    //     assertEquals(0, game.getMoveCount());
    //     assertEquals(5, game.getRows());
    //     assertEquals(5, game.getCols());
    //     assertEquals(mountainTopPos, game.getMountaintop());
    //     assertTrue(gameCopy != null);
    //     assertEquals(gameCopy.getMoveCount(), game.getMoveCount());
    //     assertEquals(gameCopy.getMountaintop(), game.getMountaintop());
    //     assertEquals(gameCopy.getRows(), game.getRows());
    //     assertEquals(gameCopy.getCols(), game.getCols());
    // }

    @Test
    public void gameStateTest() throws PetesPikeException {
        // setup
        PetesPike game1 = new PetesPike("data/petes_pike_5_5_2_0.txt");

        // invoke
        GameState actual = game1.getGameState();

        // analyze
        assertEquals(GameState.NEW, actual);
    }

    @Test
    public void testGetSymbolAt() throws PetesPikeException {
        
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        Position petePosition = new Position(0, 4);
       // Position gPosition = new Position(0, 1);

        char symbol = game.getSymbolAt(petePosition);
        //char symbol2 = game.getSymbolAt(gPosition);
       // assertEquals(0, symbol2);
        assertEquals('P', symbol);
    }

    @Test
    public void testInvalidPosition() throws PetesPikeException{
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        Position validPosition = new Position(0, 1);
        Position invalidPosition = new Position(6, 0);

        assertEquals(game.isValidPos(validPosition), true);
        assertEquals(game.isValidPos(invalidPosition), false); 

    }
    @Test
    public void testEmpty() throws PetesPikeException{
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        Position empty = new Position(0, 0);
        Position notEmpty = new Position(0, 4);

        assertEquals(game.isEmpty(notEmpty), false);
        assertEquals(game.isEmpty(empty), true);

    }
    @Test
    public void testMove() throws PetesPikeException{
        // setup
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        Move moveTest = new Move(new Position(0, 4), Direction.LEFT);

        // invoke
        game.makeMove(moveTest);
        
        // analyze
        try{game.makeMove(new Move(new Position(0, 2), Direction.UP)); assert false;}catch(PetesPikeException e){assert true;}
        try{game.makeMove(new Move(new Position(0, 2), Direction.RIGHT)); assert false;}catch(PetesPikeException e){assert true;}
        try{game.makeMove(new Move(new Position(0, 2), Direction.LEFT)); assert false;}catch(PetesPikeException e){assert true;}
        try{game.makeMove(new Move(new Position(0, 2), Direction.DOWN)); assert true;}catch(PetesPikeException e){assert false;}
        assertEquals(GameState.WON, game.getGameState());
    }

    @Test
    public void testPossibleMoves() throws PetesPikeException{
        // setup
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");

        // invoke
        List<Move> possibles = game.getPossibleMoves();
        
        // analyze
        assertEquals(2, possibles.size());
    }




}
