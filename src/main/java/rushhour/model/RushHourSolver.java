package rushhour.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import backtracker.Backtracker;
import backtracker.Configuration;

public class RushHourSolver implements Configuration<RushHourSolver>{
    private RushHour rushHour;
    private List<Move> moves;
    private Set<RushHour> games;

    public RushHourSolver(RushHour rushHour, List<Move> moves) {
        this.rushHour = rushHour;
        this.moves = moves;
        this.games = new HashSet<>();
    }

    @Override
    public Collection<RushHourSolver> getSuccessors() {
        List<RushHourSolver> successors = new LinkedList<>();  
        rushHour.updatePostions();
        for(Move move : rushHour.getPossibleMoves()) {
            try{
                RushHour copy = new RushHour(rushHour);
                copy.moveVehicle(move);
                moves.add(move);
                games.add(copy);
                successors.add(new RushHourSolver(copy, moves));
            } catch(RushHourException rhe) {
                System.out.println(rhe.getMessage());
            }
        }
        return successors;
    }

    @Override
    public boolean isValid() {
        for(RushHour game : games) {
            if(Arrays.equals(game.getBoard(), rushHour.getBoard())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isGoal() {
        return rushHour.isGameOver();
    }

    public List<Move> getMoves() {
        return moves;
    }
    
    public RushHourSolver solve(RushHour rushHour) {
        Backtracker<RushHourSolver> backtracker = new Backtracker<>(false);
        RushHourSolver solution = backtracker.solve(new RushHourSolver(rushHour, new ArrayList<>()));
        return solution;
    }

    @Override
    public String toString() {
        String result = "";
        for (Move move : moves) {
            result += move.toString() + "\n";
        }
        result += rushHour.toString();
        return result;
    }
}
