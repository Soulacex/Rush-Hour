package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MoveTest {
    @Test
    public void testMove() {
        //setup
        Move move = new Move('A', Direction.UP);
        char expectedSymbol = 'A';
        Direction expectedDir = Direction.UP;

        //invoke
        char actualSymbol = move.getSymbol();
        Direction actualDir = move.getDirection();

        //analyze
        assertEquals(expectedSymbol, actualSymbol);
        assertEquals(expectedDir, actualDir);
    }
}
