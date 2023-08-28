package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PositionTest {
    @Test
    public void testPosition() {
        //setup
        Position position = new Position(0, 0);
        int expectedRow = 0;
        int expectedCol = 0;

        //invoke
        int actualRow = position.getRow();
        int actualCol = position.getCol();

        //analyze
        assertEquals(expectedRow, actualRow);
        assertEquals(expectedCol, actualCol);
    }
}
