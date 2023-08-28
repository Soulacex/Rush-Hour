package rushhour.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class RushHour {
    private static final int BOARD_DIM = 6;
    private static final char RED_SYMBOL = 'R';
    private static final char EMPTY_SYMBOL =  '-';
    private static final Position EXIT_POS = new Position(2, 5);
    private Map<Character, Vehicle> vehicles;
    private int moveCount = 0;
    private Set<Position> positions;
    private RushHourObserver observer;
    private char[][] board = new char[BOARD_DIM][BOARD_DIM];

    public RushHour(String filename) throws IOException {
        vehicles = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                Vehicle car = new Vehicle(tokens[0].charAt(0), new Position(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])), new Position(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4])));
                // car.setSymbol(tokens[0].charAt(0));
                // car.setBack(new Position(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
                // car.setFront(new Position(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4])));
                vehicles.put(tokens[0].charAt(0), car);

            }
            br.close();
        } catch (FileNotFoundException FNFE) {
            System.out.println("File does not exist.");
        }
    }

    public RushHour(RushHour other) {
        vehicles = new HashMap<>();

        for (Map.Entry<Character, Vehicle> entry : other.vehicles.entrySet()) {
            Character key = entry.getKey();
            Vehicle value = entry.getValue();
            vehicles.put(key, new Vehicle(value));
        }

        moveCount = other.moveCount;
        observer = null;

        board = new char[BOARD_DIM][BOARD_DIM];
        board = Arrays.copyOf(other.board, other.board.length);
        updatePostions();
    }

    public void registerObserver(RushHourObserver observer) {
        this.observer = observer;
    }

    public void notifyObserver(Vehicle vehicle) {
        if(this.observer != null) {
            observer.vehicleMoved(vehicle);
        }
    }

    public char[][] getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RushHour) {
            RushHour game = (RushHour)(obj);
            return game.toString().equals(toString());
        } else {
            return false;
        }
    }

    public void moveVehicle(Move move) throws RushHourException {
        char symbol = move.getSymbol();
        Direction dir = move.getDirection();
        Vehicle car = vehicles.get(symbol);
        //previous positon
        car.move(dir); //new position
        moveCount++;
        notifyObserver(car);
        updatePostions();
    }

    public boolean isGameOver() {
        Vehicle redFront = vehicles.get(RED_SYMBOL);
        return redFront.getFront().equals(EXIT_POS);
    }

    public Set<Move> getPossibleMoves() {
        Set<Move> moves = new HashSet<>();
        for(Character symbol : vehicles.keySet()) {
            Vehicle car = vehicles.get(symbol);
            if(car.getFront().getRow() == car.getBack().getRow()) { //if horizontal
                if(car.getBack().getCol() < 5 && car.getFront().getCol() < 5) {
                    Boolean isBlocked = false;
                    Position frontPosition = new Position(car.getFront().getRow(), car.getFront().getCol()+1);
                    for(Position position : positions) {
                        if(position.equals(frontPosition)) {
                            isBlocked = true;
                        }
                    }
                    if(isBlocked == false) {
                        moves.add(new Move(symbol, Direction.RIGHT));
                    }
                }
                if(car.getBack().getCol() > 0 && car.getFront().getCol() > 0) {
                    Boolean isBlocked = false;
                    Position frontPosition = new Position(car.getBack().getRow(), car.getBack().getCol()-1);
                    for(Position position : positions) {
                        if(position.equals(frontPosition)) {
                            isBlocked = true;
                        }
                    }
                    if(isBlocked == false) {
                        moves.add(new Move(symbol, Direction.LEFT));
                    }
                }
            } else { // must be vertical
                if(car.getBack().getRow() < 5 && car.getFront().getRow() < 5) {
                    Boolean isBlocked = false;
                    Position frontPosition = new Position(car.getFront().getRow()+1, car.getFront().getCol());
                    for(Position position : positions) {
                        if(position.equals(frontPosition)) {
                            isBlocked = true;
                        }
                    }
                    if(isBlocked == false) {
                        moves.add(new Move(symbol, Direction.DOWN));
                    }
                }
                if(car.getBack().getRow() > 0 && car.getFront().getRow() > 0) {
                    Boolean isBlocked = false;
                    Position frontPosition = new Position(car.getBack().getRow()-1, car.getBack().getCol());
                    for(Position position : positions) {
                        if(position.equals(frontPosition)) {
                            isBlocked = true;
                        }
                    }
                    if(isBlocked == false) {
                        moves.add(new Move(symbol, Direction.UP));
                    }
                }
            }
        }
        return moves;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void updatePostions() {
        positions = new HashSet<>();
        for(Character symbol : vehicles.keySet()) { //for every vehicle
            Vehicle car = vehicles.get(symbol);
            Position front = car.getFront();
            Position back = car.getBack();
            if (back.getRow() == front.getRow()) { //if horizontal
                if (back.getCol() < front.getCol()) {
                    for(int col = back.getCol(); col <= front.getCol(); col++) {
                        positions.add(new Position(back.getRow(), col));
                    }
                }
                else {
                    for (int col = front.getCol(); col <= back.getCol(); col++) {
                        positions.add(new Position(front.getRow(), col));
                    }
                }
            }
            else {
                if (back.getRow() < front.getRow()) {
                    for (int row = back.getRow(); row <= front.getRow(); row++) {
                        positions.add(new Position(row, back.getCol()));
                    }
                } else {
                for (int row = front.getRow(); row <= back.getRow(); row++) {
                    positions.add(new Position(row, front.getCol()));
                }
            }
            }
                 
        }
    }

    @Override
    public String toString() {

        for (int row = 0; row < BOARD_DIM; row++) {
            for (int col = 0; col < BOARD_DIM; col++) {
                board[row][col] = EMPTY_SYMBOL;
            }
        }

        for(Character symbol : vehicles.keySet()) {
            Vehicle car = vehicles.get(symbol);
            Position front = car.getFront();
            Position back = car.getBack();
            if (back.getRow() == front.getRow()) {
                if (back.getCol() < front.getCol()) {
                    for (int col = back.getCol(); col <= front.getCol(); col++) {
                        board[back.getRow()][col] = symbol;
                    }
                }
                else {
                    for (int col = front.getCol(); col <= back.getCol(); col++) {
                        board[front.getRow()][col] = symbol;
                    }
                }
             }
            else {
                if (back.getRow() < front.getRow()) {
                    for (int row = back.getRow(); row <= front.getRow(); row++) {
                        board[row][back.getCol()] = symbol;
                    }
                } else {
                    for (int row = front.getRow(); row <= back.getRow(); row++) {
                    board[row][front.getCol()] = symbol;
                    }
                }
            }
                 
        }

        String output = "";
        for (int i = 0; i < BOARD_DIM; i++) {
            for(int j = 0; j < BOARD_DIM; j++) {
                output += board[i][j];
            }
            if(i == 2) {
                output += " < EXIT";
            }
            output += "\n";
        }
    
        return output;
    }
    public Map<Character, Vehicle> getVehicles() {
        return vehicles;
    }
    public Set<Position> getPositions() {
        return positions;
    }
    
    public static void main(String[] args) throws IOException {
        RushHour game = new RushHour("data/04_00.csv");
        System.out.println(game);
        Move move = new Move('A', Direction.RIGHT);
        try {
            game.moveVehicle(move);
            game.moveVehicle(new Move('A', Direction.DOWN));
            game.moveVehicle(new Move('B', Direction.LEFT));
            game.moveVehicle(new Move('A', Direction.LEFT));
        } catch (RushHourException rhe){
            System.out.println(rhe.getMessage());
        }
        System.out.println(game);

        RushHour game2 = new RushHour("data/11_00.csv");
        System.out.println(game2);
        game2.updatePostions();
        Collection<Move> moves = game2.getPossibleMoves();
        for(Move move2 : moves) {
            System.out.println(move2);
        }
        // System.out.println(RushHourSolver.solve(game2));
    }
}

