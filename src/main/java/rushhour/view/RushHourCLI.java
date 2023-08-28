package rushhour.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;
import rushhour.model.RushHourSolver;

public class RushHourCLI {
    public static void main(String[] args) throws IOException {
        System.out.print("Enter a filename to play from (including the 'data/'!): ");
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();
        RushHour game = new RushHour(filename);
        String input = "";
        System.out.println("Type 'help' for help...");
        while(!input.equals("quit")) {
            game.updatePostions();
            if (game.isGameOver()) {
                System.out.println("Well done!");
                System.out.println("Type 'reset' to play again");
            }
            System.out.println();
            System.out.println(game);
            System.out.println("Move count: " + game.getMoveCount());
            System.out.println();
            System.out.print("> ");
            input = scanner.nextLine();
            if(input.equals("help")) {
                System.out.println("Help Menu:\n\thelp - this menu\n\tquit - quit\n\thint - display a valid move\n\treset - reset the game\n\tsolve - solves the game\n\t<symbol> <UP|DOWN|LEFT|RIGHT> - move the vehicle one space in the given direction");
            } else if (input.equals("reset")) {
                game = new RushHour(filename);
                System.out.println("New Game.");
            } else if (input.equals("hint")) {
                Random random = new Random();
                Object[] possibleMoves = game.getPossibleMoves().toArray();
                Move move = (Move) (possibleMoves[random.nextInt(possibleMoves.length)]);
                System.out.println("Try " + move);
            } else if (input.equals("solve"))  {
                RushHourSolver solver = new RushHourSolver(game, new ArrayList<>());
                try {             
                    RushHourSolver solution = solver.solve(game);
                    for(Move move : solution.getMoves()) {
                        // RushHourSolver.solve(game);
                        game.moveVehicle(move);
                        System.out.println(move);
                        System.out.println(game);
                    }
                    System.out.println("Well Done!");
                    System.out.println("Type 'reset' to play again");
                } catch (RushHourException RHE) {
                    System.out.println(RHE.getMessage());
                }
                System.out.println("Solution: " + solver.solve(game));
            }  else if (input.split(" ").length == 2) {
                String[] tokens = input.split(" ");
                char symbol = tokens[0].charAt(0);
                if(!game.getVehicles().containsKey(symbol)) {
                    System.out.println("Invalid move: Vehicle does not exist");
                    continue;
                }
                String dirString = tokens[1];
                Direction direction;
                if (dirString.equals("UP")) {
                    direction = Direction.UP;
                }
                else if (dirString.equals("DOWN")) {
                    direction = Direction.DOWN;
                }
                else if (dirString.equals("LEFT")) {
                    direction = Direction.LEFT;
                }
                else if (dirString.equals("RIGHT")) {
                    direction = Direction.RIGHT;
                } else {
                    System.out.println("Invalid command.  Type 'help' for menu.");
                    continue;
                }
                try {
                    Move move = new Move(symbol, direction);
                    Boolean shouldMove = false;
                    for(Move possibleMove : game.getPossibleMoves()) {
                        if(move.equals(possibleMove)) {
                            shouldMove = true;
                        }
                    }
                    if(shouldMove == true && game.isGameOver() == false) {
                        game.moveVehicle(move);
                    } else {
                        if(game.isGameOver() == false) {
                            System.out.println("Invalid move: Vehicle is obstructed");
                        }
                    }
                } catch (RushHourException rhe) {
                    System.out.println(rhe.getMessage());
                }
            } else if(!input.equals("quit") && game.isGameOver() == false) {
                System.out.println("Invalid command.  Type 'help' for menu.");
            }
        }
        System.out.println("Goodbye!");
        scanner.close();
    }
}