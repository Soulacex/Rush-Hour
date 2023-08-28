package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Collection;

import org.junit.jupiter.api.Test;

public class RushHourTest {

    @Test
    public void testMoveVehicle() throws IOException, RushHourException {
        // Setup
        RushHour game = new RushHour("data/03_00.csv");
        Move move = new Move('A', Direction.UP);
        int expectedCount = 1;

        // Invoke
        try {
            game.moveVehicle(move);
 
        } catch (RushHourException RHE) {
            System.out.println(RHE.getMessage());
        }

        // Analyze
        assertEquals(expectedCount, game.getMoveCount());

    }

    @Test
    public void testIsGameOver() throws IOException, RushHourException {
        // Setup
        RushHour game = new RushHour("data/03_00.csv");
        Move move1 = new Move('A', Direction.UP);
        // Move move2 = new Move('A', Direction.UP);
        Move move3 = new Move('O', Direction.DOWN);
        Move move4 = new Move('O', Direction.DOWN);
        Move move5 = new Move('O', Direction.DOWN);
        Move move6 = new Move('R', Direction.RIGHT);
        Move move7 = new Move('R', Direction.RIGHT);
        Move move8 = new Move('R', Direction.RIGHT);
        Move move9 = new Move('R', Direction.RIGHT);

        // Invoke
        try {
        game.moveVehicle(move1);
        // game.moveVehicle(move2);
        game.moveVehicle(move3);
        game.moveVehicle(move4);
        game.moveVehicle(move5);
        game.moveVehicle(move6);
        game.moveVehicle(move7);
        game.moveVehicle(move8);
        game.moveVehicle(move9);
        } catch (RushHourException RHE) {
            System.out.println(RHE.getMessage());
        }

        // Analyze
        assertEquals(true, game.isGameOver());
    }

    @Test
    public void testGetPossibleMoves() throws IOException {
        // Setup
        RushHour game = new RushHour("data/03_00.csv");
        int expectedMoveSize = 3;

        // Invoke
        game.updatePostions();
        Collection<Move> actualMoves = game.getPossibleMoves();

        // Analyze
        assertEquals(expectedMoveSize, actualMoves.size());
    }
    
    @Test
    public void testUpdatePostions() throws IOException {
        //setup
        RushHour game = new RushHour("data/03_00.csv");
        int expectedMoveSize = 3;

        //invoke
        game.updatePostions();
        Collection<Move> actualMoves = game.getPossibleMoves();

        //analyze
        assertEquals(expectedMoveSize, actualMoves.size());
    }
}
