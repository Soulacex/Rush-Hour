package rushhour.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.scene.paint.Color;

public class Vehicle {
    private char symbol;
    private Position back;
    private Position front;
    private Color color;

    public Vehicle(char symbol, Position back, Position front) {
        this.symbol = symbol;
        this.back = back;
        this.front = front;
        if(symbol == 'R') {
            this.color = Color.RED;
        } else {
            List<Color> colors = new ArrayList<Color>(Arrays.asList(Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE, Color.PINK, Color.LIME, Color.GOLD));
            Collections.shuffle(colors);
            this.color = colors.get(0);
        }
    }

    public Vehicle (Vehicle other) {
        this.symbol = other.symbol;
        this.back = new Position(other.back.getRow(), other.back.getCol());
        this.front = new Position(other.front.getRow(), other.front.getCol());
        if(symbol == 'R') {
            this.color = Color.RED;
        } else {
            List<Color> colors = new ArrayList<Color>(Arrays.asList(Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE, Color.PINK, Color.AZURE, Color.GOLD, Color.SEASHELL));
            Collections.shuffle(colors);
            this.color = colors.get(0);
        }
    }

    public char getSymbol() {
        return symbol;
    }

    public Position getBack() {
        return back;
    }

    public Position getFront() {
        return front;
    }

    public Color getColor() {
        return color;
    }
    
    // public void setSymbol(char symbol) {
    //     this.symbol = symbol;
    // }

    public void setBack(Position back) {
        this.back = back;
    }

    public void setFront(Position front) {
        this.front = front;
    }

    public void move(Direction dir) throws RushHourException {
        if (dir == Direction.UP) {
            if(back.getRow() == front.getRow()) {
                throw new RushHourException("Invalid move: UP is an invalid direction for this vehicle");
            }   

            if(back.getRow() > 0 && front.getRow() > 0) {
                setBack(new Position(back.getRow() - 1, back.getCol()));
                setFront(new Position(front.getRow() - 1, front.getCol()));
            } else {
                throw new RushHourException("Invalid move: Vehicle is at the edge of the board");
            }
        }
        

        if (dir == Direction.RIGHT) {
            if (back.getCol() == front.getCol()) {
                throw new RushHourException("Invalid move: RIGHT is an invalid direction for this vehicle");
            }

            if(back.getCol() < 5 && front.getCol() < 5) {
                setBack(new Position(back.getRow(), back.getCol()+1));
                setFront(new Position(front.getRow(), front.getCol()+1));
            } else {
                throw new RushHourException("Invalid move: Vehicle is at the edge of the board");
            }
        }

        if (dir == Direction.DOWN) {
            if(back.getRow() == front.getRow()) {
                throw new RushHourException("Invalid move: DOWN is an invalid direction for this vehicle");
            } 

            if(back.getRow() < 5 && front.getRow() < 5) {
                setBack(new Position(back.getRow() + 1, back.getCol()));
                setFront(new Position(front.getRow() + 1, front.getCol()));
            } else {
                throw new RushHourException("Invalid move: Vehicle is at the edge of the board");
            }
        }

        if (dir == Direction.LEFT) {
            if (back.getCol() == front.getCol()) {
                throw new RushHourException("Invalid move: LEFT is an invalid direction for this vehicle");
            }

            if(back.getCol() > 0 && front.getCol() > 0) {
                setBack(new Position(back.getRow(), back.getCol() - 1));
                setFront(new Position(front.getRow(), front.getCol() - 1));
            } else {
                throw new RushHourException("Invalid move: Vehicle is at the edge of the board");
            }
        }
    } 
    public static void main(String[] args) throws IOException {
        RushHour game = new RushHour("data/03_00.csv");
        Move move1 = new Move('A', Direction.UP);
        // Move move2 = new Move('A', Direction.UP);
        Move move3 = new Move('O', Direction.DOWN);

        // Invoke
        System.out.println(game);
        try{
            game.moveVehicle(move1);
        }
        catch (RushHourException rhe) {
            System.out.println("Fix");
        }
        System.out.println(game);
        try{
            game.moveVehicle(move3);
        }
        catch (RushHourException rhe){
            System.out.println("Fix2");
        }
        System.out.println(game);
        // game.moveVehicle(move4);
        // System.out.println(game);
        // game.moveVehicle(move5);
        // System.out.println(game);
        // game.moveVehicle(move6);
        // System.out.println(game);
        // game.moveVehicle(move7);
        // System.out.println(game);
        // game.moveVehicle(move8);
        // System.out.println(game);
        // game.moveVehicle(move9);
        // System.out.println(game);
        // game.moveVehicle(move10);
        // System.out.println(game);
    }
}
