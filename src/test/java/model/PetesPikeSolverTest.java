package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import backtracker.Backtracker;
import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeSolver;
import petespike.model.Position;

public class PetesPikeSolverTest {
    @Test
    public void  testSuccessor() throws PetesPikeException{
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        PetesPikeSolver solver = new PetesPikeSolver(game);

        assertTrue(solver.getSuccessors()!=null);

        assertFalse(solver.getSuccessors().isEmpty());
        
    }

    @Test
    public void testValidMove() throws PetesPikeException{
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        PetesPikeSolver solver = new PetesPikeSolver(game);

        solver.getMoves().add(new Move(new Position(0, 4), Direction.LEFT));
        assertTrue(solver.isValid());


    }

    @Test
    public void testGoal() throws PetesPikeException{
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        game.makeMove(new Move(new Position(0, 4), Direction.LEFT));
        game.makeMove(new Move(new Position(0, 2), Direction.DOWN));
        PetesPikeSolver solver = new PetesPikeSolver(game);
        assertTrue(solver.isGoal());

    }

    @Test
    public void testSolving() throws PetesPikeException {
        PetesPike game = new PetesPike("data/petes_pike_5_5_2_0.txt");
        PetesPikeSolver solver = new Backtracker<PetesPikeSolver>(false).solve(new PetesPikeSolver(game));

        assertTrue(solver.isGoal());
    }
    
    
}
