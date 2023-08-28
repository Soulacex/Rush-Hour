package rushhour.model;

public class Move {
    private char symbol;
    private Direction dir;

    public Move(char symbol, Direction dir) {
        this.symbol = symbol;
        this.dir = dir;
    }

    public char getSymbol() {
        return symbol;
    }
    public Direction getDirection() {
        return dir;
    }

    @Override
    public String toString() {
        return symbol + " " + dir;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Move) {
            Move move = (Move)(o);
            return move.getSymbol() == symbol && move.getDirection() == dir;
        } else {
            return false;
        }
    }
}
