package rushhour.model;

public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Position) {
            Position pos = (Position)(o);
            return (pos.getRow() == row) && (pos.getCol() == col);
        } else {
            return false;
        }
    }
}
