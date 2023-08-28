package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class VehicleTest {
    @Test
    public void testMoveVehicle() throws IOException, RushHourException {
        // Setup
        RushHour game = new RushHour("data/04_00.csv");
        Move move = new Move('A', Direction.RIGHT);
        int expectedRow = 0;
        int expectedCol = 2;

        // Invoke
        game.moveVehicle(move);
        
        // Analyze
        assertEquals(game.getVehicles().get('A').getFront().getRow(), expectedRow);
        assertEquals(game.getVehicles().get('A').getFront().getCol(), expectedCol);
    }
}
